package com.bierocratie.ui.view.catalog;

import com.bierocratie.model.accounting.BudgetYear;
import com.bierocratie.model.accounting.Income;
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
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;

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

    /**
	 * 
	 */
	private static final long serialVersionUID = 3674580658357199906L;

	private static final Logger LOG = LoggerFactory.getLogger(BeerView.class);

    private BigDecimal sellingTva;

    final JPAContainer<Income> incomes = JPAContainerFactory.make(Income.class, "dashboard");

    public BeerView() {
        incomes.addContainerFilter(new Compare.Equal("month", BudgetYear.getCurrentMonth()));
        while (incomes.size() == 0) {
            incomes.removeAllContainerFilters();

            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.add(java.util.Calendar.MONTH, -1);

            incomes.addContainerFilter(new Compare.Equal("month", BudgetYear.getMonth(cal.getTime())));
        }
        Tva tva = incomes.getItem(incomes.firstItemId()).getEntity().getTva();

        sellingTva = BigDecimal.ONE.add(tva.getRate());

        table.setMultiSelect(true);
        table.setMultiSelectMode(MultiSelectMode.SIMPLE);
    }

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

    private CheckBox availableForMultiBeersCheckBox;

    @Override
    // TODO Choisir le fournisseur si différent du brasseur
    protected void buildAndBind() {
        form.addComponent(binder.buildAndBind("Nom", "name"));
        form.addComponent(binder.buildAndBind("Style", "style"));
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

        Field costingPriceHTField = binder.buildAndBind("Coûtant HT", "costingPriceHT");
        costingPriceHTField.setEnabled(false);
        form.addComponent(costingPriceHTField);

        Field costingPriceTTCField = binder.buildAndBind("Coûtant TTC", "costingPriceTTC");
        costingPriceTTCField.setEnabled(false);
        form.addComponent(costingPriceTTCField);

        Field priceHTField = binder.buildAndBind("Prix HT", "priceHT");
        priceHTField.setEnabled(false);
        form.addComponent(priceHTField);

        Field priceTTCField = binder.buildAndBind("Prix TTC", "priceTTC");
        priceTTCField.setEnabled(false);
        form.addComponent(priceTTCField);
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
        copy.setStyle(item.getStyle());
        copy.setDescription(item.getDescription());
        copy.setBrewery(item.getBrewery());
        copy.setAvailable(item.getAvailable());
        copy.setSupplier(item.getSupplier());
        copy.setCostHT(item.getCostHT());
        copy.setAbv(item.getAbv());
        copy.setCapacity(item.getCapacity());
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"brewery", "name", "style", "abv", "capacity", "available"});
        table.setColumnHeader("brewery", "Brasserie");
        table.setColumnHeader("name", "Nom");
        table.setColumnHeader("style", "Style");
        table.setColumnHeader("abv", "Degré Alcool");
        table.setColumnHeader("capacity", "Volume");
        table.setColumnHeader("available", "Disponible");
    }

    @Override
    protected void addDataToItem(Beer item) throws Exception {
    }

    @Override
    protected void preSaveItemProcessing(Beer item) {
        if (item.getCapacity() == null) {
            return;
        }

        Supplier supplier = item.getSupplier() != null ? item.getSupplier() : item.getBrewery();

        if ("20L".equals(item.getCapacity().getName()) || "30L".equals(item.getCapacity().getName())) {
            BigDecimal costingHT = item.getCostHT().add(costingMarginPerLiterCask.multiply(new BigDecimal(item.getCapacity().getSize())));
            item.setCostingPriceTTC(costingHT.multiply(sellingTva).divide(costingIncrementCask, 0, RoundingMode.CEILING).multiply(costingIncrementCask));

            BigDecimal priceHT = item.getCostHT().add(marginPerLiterCask.multiply(new BigDecimal(item.getCapacity().getSize())));
            item.setPriceTTC(priceHT.multiply(sellingTva).divide(incrementCask, 0, RoundingMode.CEILING).multiply(incrementCask));
        } else {
            item.setCostingPriceTTC(item.getCostHT().multiply(sellingTva).setScale(1, RoundingMode.CEILING));
            //item.setCostingPriceTTC(item.getCostHT().multiply(sellingTva).divide(incrementBottle, 0, RoundingMode.CEILING).multiply(incrementBottle));
            BigDecimal priceHT = item.getCostHT().add(marginPerLiterBeer.multiply(new BigDecimal(item.getCapacity().getSize())));
            item.setPriceTTC(priceHT.multiply(sellingTva).setScale(1, RoundingMode.CEILING));
        }

        item.setCostingPriceHT(item.getCostingPriceTTC().divide(sellingTva, 2, RoundingMode.HALF_UP));
        item.setPriceHT(item.getPriceTTC().divide(sellingTva, 2, RoundingMode.HALF_UP));
    }

    // TODO
    private BigDecimal costingIncrementCask = new BigDecimal(10);
    private BigDecimal costingMarginPerLiterCask = BigDecimal.ZERO;
    private BigDecimal marginPerLiterCask = new BigDecimal(2.5);
    private BigDecimal marginPerLiterBeer = new BigDecimal(3);
    private BigDecimal incrementCask = new BigDecimal(5);
    private BigDecimal incrementBottle = new BigDecimal(0.1);

    @Override
    protected void postSaveItemProcessing(Beer item) {
    }

    @Override
    protected void createMultiSelectForm() {
        availableForMultiBeersCheckBox = new CheckBox("Disponibles");
        multiSelectForm.addComponent(availableForMultiBeersCheckBox);
    }

    private boolean availability;

    @Override
    protected void getMultiFormValues() {
        availability = availableForMultiBeersCheckBox.getValue();
    }

    @Override
    protected void setItemValues(Beer item) {
        item.setAvailable(availability);
    }

    @Override
    protected void postSaveItemsProcessing() {
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
