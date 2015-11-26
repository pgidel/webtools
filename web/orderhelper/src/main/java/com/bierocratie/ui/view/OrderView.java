package com.bierocratie.ui.view;

import com.bierocratie.model.Order;
import com.bierocratie.model.catalog.Supplier;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.OrderMenuBar;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import org.apache.shiro.SecurityUtils;

public class OrderView extends AbstractBasicModelView<Order> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderView.class);
    private Long supplierId;

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
        return new OrderMenuBar();
    }

    @Override
    protected void createMultiSelectForm() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected BeanItem<Order> createCopiedBeanItem(Order item) {
        Order copy = new Order();
        copy.setCustomer(item.getCustomer());
        copy.setSupplier(item.getSupplier());
        copy.setComment(item.getComment());
        copy.setDate(item.getDate());
        return new BeanItem<>(copy);
    }

    @Override
    protected void buildAndBind() {
        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, "orderhelper");
        ComboBox comboBox = new ComboBox("Fournisseur");
        comboBox.setContainerDataSource(suppliers);
        comboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        comboBox.setItemCaptionPropertyId("name");
        comboBox.setConverter(new SingleSelectConverter<>(comboBox));
        comboBox.setFilteringMode(FilteringMode.CONTAINS);
        if (supplierId == null) {
            comboBox.setEnabled(true);
        } else {
            comboBox.setValue(supplierId);
            comboBox.setEnabled(false);
        }
        binder.bind(comboBox, "supplier");
        form.addComponent(comboBox);

        form.addComponent(binder.buildAndBind("Date", "date"));
        form.addComponent(binder.buildAndBind("Commentaire", "comment"));
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
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"supplier", "date", "comment"});
        table.setColumnHeader("supplier", "Fournisseur");
        table.setColumnHeader("date", "Date");
        table.setColumnHeader("comment", "Commentaire");
    }

    @Override
    protected void addDataToItem(Order item) throws Exception {
    }

    @Override
    protected void preSaveItemProcessing(Order item) {
    }

    @Override
    protected void postSaveItemProcessing(Order item) {
    }

    @Override
    protected void getMultiFormValues() {
    }

    @Override
    protected void setItemValues(Order item) {
    }

    @Override
    protected void postSaveItemsProcessing() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() == null || event.getParameters().isEmpty()) {
            // TODO Lister les commandes en cours de la personne connectée + bouton Nouvelle commande
        } else {
            if (!SecurityUtils.getSubject().getPrincipal().equals(event.getParameters())) {
                Notification.show("Erreur d'authentification", Notification.Type.WARNING_MESSAGE);
            } else {
                try {
                    // TODO Lister les commandes en cours de la personne connectée + formulaire de nouvelle commande
                    prepareFormToAddNewItem();
                } catch (NumberFormatException e) {
                    Notification.show("Erreur de conversion de données", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

}
