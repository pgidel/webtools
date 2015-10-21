package com.bierocratie.ui.view.accounting;

import com.bierocratie.model.accounting.BudgetYear;
import com.bierocratie.model.accounting.StockValue;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;

@SuppressWarnings("serial")
public class StockValueView extends AbstractBasicModelView<StockValue> {

    private static final Logger LOG = LoggerFactory.getLogger(StockValueView.class);

    public StockValueView() {
        table.sort(new Object[]{"year"}, new boolean[]{false});
        table.setPageLength(5);
    }

    @Override
    protected Class<StockValue> getClazz() {
        return StockValue.class;
    }

    @Override
    protected String getTableName() {
        return "Stocks (HT)";
    }

    @Override
    protected AbstractMenuBar getMenuBar() {
        // FIXME @Inject
        return new DashboardMenuBar();
    }

    @Override
    protected void buildAndBind() {
        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<BudgetYear> years = JPAContainerFactory.make(BudgetYear.class, "dashboard");
        ComboBox yearComboBox = new ComboBox("Année");
        yearComboBox.setContainerDataSource(years);
        yearComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        yearComboBox.setItemCaptionPropertyId("year");
        yearComboBox.setConverter(new SingleSelectConverter<BudgetYear>(yearComboBox));
        yearComboBox.setFilteringMode(FilteringMode.CONTAINS);
        yearComboBox.setImmediate(true);
        binder.bind(yearComboBox, "year");
        form.addComponent(yearComboBox);

        form.addComponent(binder.buildAndBind("Montant", "amount"));
    }

    @Override
    protected void updateForm(StockValue item) {
    }

    @Override
    protected boolean isUpdateAuthorized(StockValue item) {
        return true;
    }

    @Override
    protected BeanItem<StockValue> createNewBeanItem() {
        final JPAContainer<BudgetYear> years = JPAContainerFactory.make(BudgetYear.class, "dashboard");
        years.addContainerFilter(new Compare.LessOrEqual("firstMonth", BudgetYear.getCurrentMonth()));
        years.addContainerFilter(new Compare.GreaterOrEqual("lastMonth", BudgetYear.getCurrentMonth()));
        Object idYear = years.firstItemId();

        StockValue stockValue = new StockValue(years.getItem(idYear).getEntity());

        years.removeAllContainerFilters();

        return new BeanItem<>(stockValue);
    }

    @Override
    protected BeanItem<StockValue> createCopiedBeanItem(StockValue item) {
        StockValue copy = new StockValue();
        copy.setYear(item.getYear());
        copy.setAmount(item.getAmount());
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"year", "amount"});
        table.setColumnHeader("year", "Année");
        table.setColumnHeader("amount", "Montant");
    }

    @Override
    protected void addDataToItem(StockValue item) throws Exception {
    }

    @Override
    protected void preSaveProcessing(StockValue item) {
    }

    @Override
    protected void postSaveProcessing(StockValue item) {
    }

    @Override
    protected void createMultiSelectForm() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
