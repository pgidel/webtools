package com.bierocratie.ui.view.order;

import com.bierocratie.model.catalog.Beer;
import com.bierocratie.model.catalog.Supplier;
import com.bierocratie.model.order.Order;
import com.bierocratie.model.order.Stock;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.component.Table;
import com.bierocratie.ui.component.TextArea;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 21/10/14
 * Time: 00:04
 * To change this template use File | Settings | File Templates.
 */
public class OrderView extends AbstractBasicModelView<Order> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderView.class);

    @Override
    protected Class<Order> getClazz() {
        return Order.class;
    }

    @Override
    protected String getTableName() {
        return "Commandes";
    }

    @Override
    protected AbstractMenuBar getMenuBar() {
        // FIXME @Inject
        return new DashboardMenuBar();
    }

    private JPAContainer<Stock> stocks;

    private File tempFile;

    @Override
    protected void buildAndBind() {
        class CsvUploader implements Upload.Receiver {
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                if (!filename.endsWith(".csv")) {
                    new Notification("Le fichier n'est pas un CSV", null, Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
                    return null;
                }

                try {
                    tempFile = new File(System.getProperty("java.io.tmpdir") + filename);
                    return new FileOutputStream(tempFile);
                } catch (FileNotFoundException e) {
                    Notification.show("Le fichier n'a pas pu être ouvert", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    LOG.error(e.getMessage(), e);
                    return null;
                }
            }
        }
        ;

        Upload upload = new Upload("Importer", new CsvUploader());
        upload.addFinishedListener(new Upload.FinishedListener() {
            @Override
            public void uploadFinished(Upload.FinishedEvent event) {
                List<Stock> stockList = new ArrayList<>();
                try (BufferedReader in = new BufferedReader(new FileReader(tempFile));) {
                    // Header
                    String line = in.readLine();
                    while ((line = in.readLine()) != null) {
                        String[] tab = line.split(";");
                        // supplierCode must exist
                        if (tab.length == 5) {
                            Stock stock = new Stock();
                            stock.setCode(tab[0]);
                            stock.setBeer(tab[1]);
                            stock.setQuantity(Integer.parseInt(tab[3]));
                            stock.setSupplierCode(tab[4]);
                            stockList.add(stock);
                        }
                    }
                } catch (IOException e) {
                    Notification.show("Erreur de lecture du fichier", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    LOG.error(e.getMessage(), e);
                }

                if (!stockList.isEmpty()) {
                    stocks.removeAllItems();
                    for (Stock stock : stockList) {
                        stocks.addEntity(stock);
                    }
                }
            }
        });
        upload.addStartedListener(new Upload.StartedListener() {
            @Override
            public void uploadStarted(Upload.StartedEvent startedEvent) {

            }
        });
        upload.addFailedListener(new Upload.FailedListener() {
            @Override
            public void uploadFailed(Upload.FailedEvent failedEvent) {

            }
        });
        addComponent(upload);

        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, "dashboard");
        ComboBox supplierComboBox = new ComboBox("Fournisseur");
        supplierComboBox.setContainerDataSource(suppliers);
        supplierComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        supplierComboBox.setItemCaptionPropertyId("name");
        supplierComboBox.setConverter(new SingleSelectConverter<Supplier>(supplierComboBox));
        supplierComboBox.setFilteringMode(FilteringMode.CONTAINS);
        supplierComboBox.setImmediate(true);
        binder.bind(supplierComboBox, "supplier");
        form.addComponent(supplierComboBox);
        supplierComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object itemId = event.getProperty().getValue();
                if (itemId != null) {
                    EntityItem<Supplier> entityItem = suppliers.getItem(itemId);
                    Supplier supplierFilter = entityItem.getEntity();
                    stocks.removeAllContainerFilters();
                    stocks.addContainerFilter(new Compare.Equal("supplierCode", supplierFilter.getCode()));
                }
            }
        });

        form.addComponent(binder.buildAndBind("Commandée ?", "ordered"));

        TextArea textArea = new TextArea("Commande");
        binder.bind(textArea, "list");
        form.addComponent(textArea);

        // FIXME pb accès persistenceUnitName
        stocks = JPAContainerFactory.make(Stock.class, "dashboard");
        Table stockTable = new Table("Stocks", stocks);
        stockTable.setVisibleColumns(new String[]{"beer", "code", "quantity"});
        stockTable.setColumnHeader("beer", "Bière");
        stockTable.setColumnHeader("code", "Code");
        stockTable.setColumnHeader("quantity", "Quantité");
        stockTable.setBuffered(false);
        form.addComponent(stockTable);
    }

    @Override
    protected void updateForm(Order item) {
    }

    @Override
    protected boolean isUpdateAuthorized(Order item) {
        return true;
    }

    @Override
    protected BeanItem<Order> createNewBeanItem() {
        return new BeanItem<>(new Order());
    }

    @Override
    protected BeanItem<Order> createCopiedBeanItem(Order item) {
        Order copy = new Order();
        copy.setList(item.getList());
        copy.setSupplier(item.getSupplier());
        copy.setOrdered(false);
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"supplier", "ordered"});
        table.setColumnHeader("supplier", "Fournisseur");
        table.setColumnHeader("ordered", "Commandée ?");
    }

    @Override
    protected void addDataToItem(Order item) throws Exception {
    }

    @Override
    protected void preSaveProcessing(Order item) {
    }

    @Override
    protected void postSaveProcessing(Order item) {
    }

    @Override
    protected void createMultiSelectForm() {
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
