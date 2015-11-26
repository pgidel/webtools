package com.bierocratie.ui.view.catalog;

import com.bierocratie.model.catalog.Country;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.ViewChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 21/10/14
 * Time: 00:04
 * To change this template use File | Settings | File Templates.
 */
public class CountryView extends AbstractBasicModelView<Country> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1047107696250695717L;

    @Override
    protected Class<Country> getClazz() {
        return Country.class;
    }

    @Override
    protected String getTableName() {
        return "Pays";
    }

    @Override
    protected AbstractMenuBar getMenuBar() {
        // FIXME @Inject
        return new DashboardMenuBar();
    }

    @Override
    protected void buildAndBind() {
        form.addComponent(binder.buildAndBind("Nom", "name"));
    }

    @Override
    protected void updateForm(Country item) {
    }

    @Override
    protected boolean isUpdateAuthorized(Country item) {
        return true;
    }

    @Override
    protected BeanItem<Country> createNewBeanItem() {
        return new BeanItem<>(new Country());
    }

    @Override
    protected BeanItem<Country> createCopiedBeanItem(Country item) {
        Country copy = new Country();
        copy.setName(item.getName());
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"name"});
        table.setColumnHeader("name", "Nom");
    }

    @Override
    protected void addDataToItem(Country item) throws Exception {
    }

    @Override
    protected void preSaveItemProcessing(Country item) {
    }

    @Override
    protected void postSaveItemProcessing(Country item) {
    }

    @Override
    protected void createMultiSelectForm() {
    }

    @Override
    protected void getMultiFormValues() {
    }

    @Override
    protected void setItemValues(Country item) {
    }

    @Override
    protected void postSaveItemsProcessing() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
