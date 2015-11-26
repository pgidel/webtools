package com.bierocratie.ui.view.accounting;

import com.bierocratie.model.accounting.Tva;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.ViewChangeListener;

@SuppressWarnings("serial")
public class TvaView extends AbstractBasicModelView<Tva> {

    @Override
    protected Class<Tva> getClazz() {
        return Tva.class;
    }

    @Override
    protected String getTableName() {
        return "TVA";
    }

    @Override
    protected AbstractMenuBar getMenuBar() {
        // FIXME @Inject
        return new DashboardMenuBar();
    }

    @Override
    protected void buildAndBind() {
        form.addComponent(binder.buildAndBind("Taux", "rate"));
    }

    @Override
    protected void updateForm(Tva item) {
    }

    @Override
    protected boolean isUpdateAuthorized(Tva item) {
        return true;
    }

    @Override
    protected BeanItem<Tva> createNewBeanItem() {
        return new BeanItem<>(new Tva());
    }

    @Override
    protected BeanItem<Tva> createCopiedBeanItem(Tva item) {
        Tva copy = new Tva();
        copy.setRate(item.getRate());
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"rate"});
        table.setColumnHeader("rate", "Taux");
    }

    @Override
    protected void addDataToItem(Tva item) throws Exception {
    }

    @Override
    protected void preSaveItemProcessing(Tva item) {
    }

    @Override
    protected void postSaveItemProcessing(Tva item) {
    }

    @Override
    protected void createMultiSelectForm() {
    }

    @Override
    protected void getMultiFormValues() {
    }

    @Override
    protected void setItemValues(Tva item) {
    }

    @Override
    protected void postSaveItemsProcessing() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
