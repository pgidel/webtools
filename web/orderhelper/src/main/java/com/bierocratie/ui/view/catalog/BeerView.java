package com.bierocratie.ui.view.catalog;

import com.bierocratie.model.catalog.Beer;
import com.bierocratie.model.catalog.Capacity;
import com.bierocratie.model.catalog.Supplier;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.OrderMenuBar;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.MultiSelectConverter;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;

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
        return new OrderMenuBar();
    }

    @Override
    protected void createMultiSelectForm() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private ComboBox suppliersComboBox;
    private OptionGroup capacitiesOptionGroup;

    @Override
    protected void buildAndBind() {
        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, "orderhelper");
        suppliersComboBox = new ComboBox("Fournisseur");
        suppliersComboBox.setContainerDataSource(suppliers);
        suppliersComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        suppliersComboBox.setItemCaptionPropertyId("name");
        suppliersComboBox.setConverter(new SingleSelectConverter<Supplier>(suppliersComboBox));
        suppliersComboBox.setFilteringMode(FilteringMode.CONTAINS);
        suppliersComboBox.setImmediate(true);
        binder.bind(suppliersComboBox, "supplier");
        form.addComponent(suppliersComboBox);

        form.addComponent(binder.buildAndBind("Nom", "name"));
        form.addComponent(binder.buildAndBind("Description", "description"));

        // FIXME setConverter
        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Capacity> capacities = JPAContainerFactory.make(Capacity.class, persistenceUnitName);
        final JPAContainer<Capacity> capacities = JPAContainerFactory.make(Capacity.class, "orderhelper");
        capacitiesOptionGroup = new OptionGroup("Formats");
        capacitiesOptionGroup.setContainerDataSource(capacities);
        capacitiesOptionGroup.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        capacitiesOptionGroup.setItemCaptionPropertyId("title");
        capacitiesOptionGroup.setConverter(new MultiSelectConverter(capacitiesOptionGroup));
        capacitiesOptionGroup.setMultiSelect(true);
        capacitiesOptionGroup.setImmediate(true);
        binder.bind(capacitiesOptionGroup, "availableCapacities");
        form.addComponent(capacitiesOptionGroup);
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
        return new BeanItem<>(new Beer());
    }

    @Override
    protected BeanItem<Beer> createCopiedBeanItem(Beer item) {
        Beer copy = new Beer();
        copy.setName(item.getName());
        copy.setAbv(item.getAbv());
        copy.setStyle(item.getStyle());
        copy.setDescription(item.getDescription());
        copy.setBrewery(item.getBrewery());
        copy.setSupplier(item.getSupplier());
        copy.setAvailable(item.getAvailable());
        copy.setCostHT(item.getCostHT());
        copy.setCapacity(item.getCapacity());
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"name", "description", "supplier"});
        table.setColumnHeader("name", "Nom");
        table.setColumnHeader("description", "Description");
        table.setColumnHeader("supplier", "Fournisseur");
    }

    @Override
    protected void addDataToItem(Beer item) throws Exception {
    }

    @Override
    protected void preSaveItemProcessing(Beer item) {
    }

    @Override
    protected void postSaveItemProcessing(Beer item) {
    }

    @Override
    protected void getMultiFormValues() {
    }

    @Override
    protected void setItemValues(Beer item) {
    }

    @Override
    protected void postSaveItemsProcessing() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() == null || event.getParameters().isEmpty()) {
            suppliersComboBox.setEnabled(true);
        } else {
            try {
                prepareFormToAddNewItem();
                suppliersComboBox.setValue(Long.parseLong(event.getParameters()));
                suppliersComboBox.setEnabled(false);
            } catch (NumberFormatException e) {
                Notification.show("Erreur de conversion de données", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                LOG.error(e.getMessage(), e);
            }
        }
    }

}
