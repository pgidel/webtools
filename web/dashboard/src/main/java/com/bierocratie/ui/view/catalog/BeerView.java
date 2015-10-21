package com.bierocratie.ui.view.catalog;

import com.bierocratie.model.accounting.Tva;
import com.bierocratie.model.catalog.Beer;
import com.bierocratie.model.catalog.Capacity;
import com.bierocratie.model.catalog.Supplier;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.component.TextArea;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.data.util.filter.Or;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 21/10/14
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */
public class BeerView extends AbstractBasicModelView<Beer> {

    private static final Logger LOG = LoggerFactory.getLogger(BeerView.class);

    @Override
    protected Class<Beer> getClazz() {
        return Beer.class;
    }

    @Override
    protected String getTableName() {
        return "Bières";
    }

    @Override
    protected AbstractMenuBar getMenuBar() {
        // FIXME @Inject
        return new DashboardMenuBar();
    }

    private ComboBox supplierComboBox;
    private ComboBox breweryComboBox;
    private ComboBox capacityComboBox;
    private ComboBox tvaComboBox;

    @Override
    // TODO Choisir le fournisseur si différent du brasseur
    protected void buildAndBind() {
        form.addComponent(binder.buildAndBind("Nom", "name"));
        form.addComponent(binder.buildAndBind("Style", "style"));
        form.addComponent(binder.buildAndBind("Origine", "region"));
        form.addComponent(binder.buildAndBind("Degré alcool", "abv"));

        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<Capacity> capacities = JPAContainerFactory.make(Capacity.class, "dashboard");
        capacityComboBox = new ComboBox("Volume");
        capacityComboBox.setContainerDataSource(capacities);
        capacityComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        capacityComboBox.setItemCaptionPropertyId("name");
        capacityComboBox.setConverter(new SingleSelectConverter<Capacity>(capacityComboBox));
        capacityComboBox.setFilteringMode(FilteringMode.CONTAINS);
        capacityComboBox.setImmediate(true);
        binder.bind(capacityComboBox, "capacity");
        form.addComponent(capacityComboBox);

        TextArea textArea = new TextArea("Description");
        binder.bind(textArea, "description");
        form.addComponent(textArea);

        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<Supplier> breweries = JPAContainerFactory.make(Supplier.class, "dashboard");
        breweryComboBox = new ComboBox("Brasserie");
        breweryComboBox.setContainerDataSource(breweries);
        breweryComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        breweryComboBox.setItemCaptionPropertyId("name");
        breweryComboBox.setConverter(new SingleSelectConverter<Supplier>(breweryComboBox));
        breweryComboBox.setFilteringMode(FilteringMode.CONTAINS);
        breweryComboBox.setImmediate(true);
        binder.bind(breweryComboBox, "brewery");
        form.addComponent(breweryComboBox);
        breweries.addNestedContainerProperty("type.name");
        breweries.addContainerFilter(new Or(new IsNull("type"), new Compare.Equal("type.name", "Brasserie")));

        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, "dashboard");
        supplierComboBox = new ComboBox("Fournisseur");
        supplierComboBox.setContainerDataSource(suppliers);
        supplierComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        supplierComboBox.setItemCaptionPropertyId("name");
        supplierComboBox.setConverter(new SingleSelectConverter<Supplier>(supplierComboBox));
        supplierComboBox.setFilteringMode(FilteringMode.CONTAINS);
        supplierComboBox.setImmediate(true);
        binder.bind(supplierComboBox, "supplier");
        form.addComponent(supplierComboBox);
        suppliers.addNestedContainerProperty("type.name");
        suppliers.addContainerFilter(new Compare.Equal("type.name", "Grossiste"));

        form.addComponent(binder.buildAndBind("Disponible", "available"));

        form.addComponent(binder.buildAndBind("Achat HT", "costHT"));

        Field costingPriceTTCField = binder.buildAndBind("Coûtant TTC", "costingPriceTTC");
        costingPriceTTCField.setEnabled(false);
        form.addComponent(costingPriceTTCField);

        Field priceHTField = binder.buildAndBind("Prix HT", "priceHT");
        priceHTField.setEnabled(false);
        form.addComponent(priceHTField);

        Field priceTTCField = binder.buildAndBind("Prix TTC", "priceTTC");
        priceTTCField.setEnabled(false);
        form.addComponent(priceTTCField);

        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<Tva> tvas = JPAContainerFactory.make(Tva.class, "dashboard");
        tvaComboBox = new ComboBox("TVA");
        tvaComboBox.setContainerDataSource(tvas);
        tvaComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tvaComboBox.setItemCaptionPropertyId("rate2String");
        tvaComboBox.setConverter(new SingleSelectConverter<Tva>(tvaComboBox));
        tvaComboBox.setFilteringMode(FilteringMode.CONTAINS);
        tvaComboBox.setImmediate(true);
        tvaComboBox.setRequired(true);
        binder.bind(tvaComboBox, "tva");
        form.addComponent(tvaComboBox);
    }

    @Override
    protected void updateForm(Beer item) {
    }

    @Override
    protected boolean isUpdateAuthorized(Beer item) {
        return true;
    }

    @Override
    protected BeanItem<Beer> createNewBeanItem() {
        Beer beer = new Beer();

        final JPAContainer<Tva> tvas = JPAContainerFactory.make(Tva.class, "dashboard");
        tvas.addContainerFilter(new Compare.Equal("rate", new BigDecimal(0.2)));
        beer.setTva(tvas.getItem(tvas.firstItemId()).getEntity());

        return new BeanItem<>(beer);
    }

    @Override
    protected BeanItem<Beer> createCopiedBeanItem(Beer item) {
        Beer copy = new Beer();
        copy.setName(item.getName());
        copy.setStyle(item.getStyle());
        copy.setDescription(item.getDescription());
        copy.setBrewery(item.getBrewery());
        copy.setAvailable(item.getAvailable());
        copy.setSupplier(item.getSupplier());
        copy.setCostHT(item.getCostHT());
        copy.setAbv(item.getAbv());
        copy.setTva(item.getTva());
        copy.setCapacity(item.getCapacity());
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        entities.addNestedContainerProperty("capacity.name");

        table.setVisibleColumns(new String[]{"brewery", "name", "style", "abv", "capacity.name"});
        table.setColumnHeader("brewery", "Brasserie");
        table.setColumnHeader("name", "Nom");
        table.setColumnHeader("style", "Style");
        table.setColumnHeader("abv", "Degré Alcool");
        table.setColumnHeader("capacity.name", "Volume");
    }

    @Override
    protected void addDataToItem(Beer item) throws Exception {
    }

    // FIXME
    private double marginPerLiterCask = 2.5;
    private double marginPerLiterBeer = 3;
    private BigDecimal incrementCask = new BigDecimal(5);
    private BigDecimal invoiceTva = new BigDecimal(1.2);

    @Override
    protected void preSaveProcessing(Beer item) {
        if (item.getCapacity() == null) {
            return;
        }

        BigDecimal itemTva = BigDecimal.ONE;
        if (item.getTva() != null) {
            itemTva = itemTva.add(item.getTva().getRate());
        }

        if ("20L".equals(item.getCapacity().getName()) || "30L".equals(item.getCapacity().getName())) {
            item.setCostingPriceTTC(item.getCostHT().multiply(itemTva).setScale(-1, RoundingMode.CEILING));
            BigDecimal priceHT = item.getCostHT().add(new BigDecimal(marginPerLiterCask * item.getCapacity().getSize()));
            item.setPriceTTC(priceHT.multiply(invoiceTva).divide(incrementCask, 0, RoundingMode.HALF_UP).multiply(incrementCask));
        } else {
            item.setCostingPriceTTC(item.getCostHT().multiply(itemTva).setScale(1, RoundingMode.CEILING));
            BigDecimal priceHT = item.getCostHT().add(new BigDecimal(marginPerLiterBeer * item.getCapacity().getSize()));
            item.setPriceTTC(priceHT.multiply(invoiceTva).setScale(1, RoundingMode.CEILING));
        }

        item.setPriceHT(item.getPriceTTC().divide(invoiceTva, 2, RoundingMode.HALF_UP));
    }

    @Override
    protected void postSaveProcessing(Beer item) {
    }

    @Override
    protected void createMultiSelectForm() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() == null || event.getParameters().isEmpty()) {
            supplierComboBox.setEnabled(true);
            breweryComboBox.setEnabled(true);
        } else {
            try {
                Long itemId = Long.parseLong(event.getParameters());

                prepareFormToAddNewItem();

                final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, "dashboard");
                EntityItem<Supplier> item = suppliers.getItem(itemId);

                if (item != null) {
                    if ("Grossiste".equals(item.getEntity().getType().getName())) {
                        supplierComboBox.setValue(itemId);
                        supplierComboBox.setEnabled(false);
                    } else {
                        breweryComboBox.setValue(itemId);
                        breweryComboBox.setEnabled(false);
                    }
                }
            } catch (NumberFormatException e) {
                Notification.show("Erreur de conversion de données", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                LOG.error(e.getMessage(), e);
            }
        }
    }

}
