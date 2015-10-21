package com.bierocratie.ui.view.accounting;

import com.bierocratie.model.accounting.BudgetYear;
import com.bierocratie.model.catalog.Capacity;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.data.util.BeanItem;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.ViewChangeListener;

@SuppressWarnings("serial")
public class BudgetYearView extends AbstractBasicModelView<BudgetYear> {

    private static final Logger LOG = LoggerFactory.getLogger(BudgetYearView.class);

    @Override
    protected Class<BudgetYear> getClazz() {
        return BudgetYear.class;
    }

    @Override
    protected String getTableName() {
        return "Exercices comptables";
    }

    @Override
    protected AbstractMenuBar getMenuBar() {
        // FIXME @Inject
        return new DashboardMenuBar();
    }

    @Override
    protected void buildAndBind() {
        form.addComponent(binder.buildAndBind("Année", "year"));
        form.addComponent(binder.buildAndBind("Début", "firstMonth"));
        form.addComponent(binder.buildAndBind("Fin", "lastMonth"));
    }

    @Override
    protected void updateForm(BudgetYear item) {
    }

    @Override
    protected boolean isUpdateAuthorized(BudgetYear item) {
        return true;
    }

    @Override
    protected BeanItem<BudgetYear> createNewBeanItem() {
        return new BeanItem<>(new BudgetYear());
    }

    @Override
    protected BeanItem<BudgetYear> createCopiedBeanItem(BudgetYear item) {
        BudgetYear copy = new BudgetYear();
        copy.setFirstMonth(item.getFirstMonth());
        copy.setLastMonth(item.getLastMonth());
        copy.setYear(item.getYear());
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"year", "firstMonth", "lastMonth"});
        table.setColumnHeader("year", "Exercice");
        table.setColumnHeader("firstMonth", "Début");
        table.setColumnHeader("lastMonth", "Fin");
    }

    @Override
    protected void addDataToItem(BudgetYear item) throws Exception {
    }

    @Override
    protected void preSaveProcessing(BudgetYear item) {
    }

    @Override
    protected void postSaveProcessing(BudgetYear item) {
    }

    @Override
    protected void createMultiSelectForm() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
