package com.bierocratie.ui.view.accounting;

import com.bierocratie.model.accounting.BudgetYear;
import com.bierocratie.model.accounting.Income;
import com.bierocratie.model.accounting.Tva;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;

/*public class IncomeView extends VerticalLayout implements View {

    public IncomeView() {
        addComponent(new DashboardMenuBar());

        JPAContainer<Income> entities = JPAContainerFactory.make(Income.class, "dashboard");

        Grid grid = new Grid("Chiffres d'affaire (HT)");

        BeanItemContainer<Income> beanItemContainer = new BeanItemContainer<Income>(Income.class);
        for (Object itemId : entities.getItemIds()) {
            beanItemContainer.addBean(entities.getItem(itemId).getEntity());
        }

        grid.setContainerDataSource(beanItemContainer);
        grid.setEditorEnabled(true);
        grid.setEditorFieldGroup(new BeanFieldGroup<>(Income.class));

        grid.setColumnOrder("year", "month", "amount", "tva", "description");
        grid.getColumn("year").setHeaderCaption("Année");
        grid.getColumn("month").setHeaderCaption("Mois");
        grid.getColumn("amount").setHeaderCaption("Montant HT");
        grid.getColumn("description").setHeaderCaption("Description");
        grid.getColumn("tva").setHeaderCaption("TVA");

        addComponent(grid);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}*/
public class IncomeView extends AbstractBasicModelView<Income> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5386653576461985731L;

	public IncomeView() {
        table.sort(new Object[]{"month"}, new boolean[]{false});
        table.setPageLength(24);
    }

    @Override
    protected Class<Income> getClazz() {
        return Income.class;
    }

    @Override
    protected String getTableName() {
        return "Chiffres d'affaire (HT)";
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

        // TODO SelectBox remplie avec les mois du Plan de trésorerie
        form.addComponent(binder.buildAndBind("Mois", "month"));
        form.addComponent(binder.buildAndBind("Montant HT", "amount"));
        form.addComponent(binder.buildAndBind("Description", "description"));

        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<Tva> tvas = JPAContainerFactory.make(Tva.class, "dashboard");
        ComboBox tvaComboBox = new ComboBox("TVA");
        tvaComboBox.setContainerDataSource(tvas);
        tvaComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tvaComboBox.setItemCaptionPropertyId("rate2String");
        tvaComboBox.setConverter(new SingleSelectConverter<Tva>(tvaComboBox));
        tvaComboBox.setTextInputAllowed(false);
        tvaComboBox.setImmediate(true);
        binder.bind(tvaComboBox, "tva");
        form.addComponent(tvaComboBox);
    }

    @Override
    protected void updateForm(Income item) {
    }

    @Override
    protected boolean isUpdateAuthorized(Income item) {
        return true;
    }

    @Override
    protected BeanItem<Income> createNewBeanItem() {
        Income income;

        final JPAContainer<Tva> tvas = JPAContainerFactory.make(Tva.class, "dashboard");
        tvas.addContainerFilter(new Compare.Equal("rate", 0.2));
        Tva tva = tvas.getItem(tvas.firstItemId()).getEntity();

        final JPAContainer<BudgetYear> years = JPAContainerFactory.make(BudgetYear.class, "dashboard");
        years.addContainerFilter(new Compare.LessOrEqual("firstMonth", BudgetYear.getCurrentMonth()));
        years.addContainerFilter(new Compare.GreaterOrEqual("lastMonth", BudgetYear.getCurrentMonth()));
        BudgetYear year = years.getItem(years.firstItemId()).getEntity();

        income = new Income(tva, year);

        tvas.removeAllContainerFilters();
        years.removeAllContainerFilters();

        return new BeanItem<>(income);
    }

    @Override
    protected BeanItem<Income> createCopiedBeanItem(Income item) {
        Income copy = new Income();
        copy.setYear(item.getYear());
        copy.setMonth(item.getMonth());
        copy.setAmount(item.getAmount());
        copy.setDescription(item.getDescription());
        copy.setTva(item.getTva());
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"year", "month", "amount", "tva", "description"});
        table.setColumnHeader("year", "Année");
        table.setColumnHeader("month", "Mois");
        table.setColumnHeader("amount", "Montant HT");
        table.setColumnHeader("description", "Description");
        table.setColumnHeader("tva", "TVA");
    }

    @Override
    protected void addDataToItem(Income item) throws Exception {
    }

    @Override
    protected void preSaveItemProcessing(Income item) {
    }

    @Override
    protected void postSaveItemProcessing(Income item) {
    }

    @Override
    protected void createMultiSelectForm() {
    }

    @Override
    protected void getMultiFormValues() {
    }

    @Override
    protected void setItemValues(Income item) {
    }

    @Override
    protected void postSaveItemsProcessing() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
