package com.bierocratie.ui.view.catalog;

import com.bierocratie.model.catalog.Beer;
import com.bierocratie.model.catalog.SupplierType;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.data.util.BeanItem;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.ViewChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 21/10/14
 * Time: 00:04
 * To change this template use File | Settings | File Templates.
 */
public class SupplierTypeView extends AbstractBasicModelView<SupplierType> {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierTypeView.class);

    @Override
    protected Class<SupplierType> getClazz() {
        return SupplierType.class;
    }

    @Override
    protected String getTableName() {
        return "Type de fournisseur";
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
    protected void updateForm(SupplierType item) {
    }

    @Override
    protected boolean isUpdateAuthorized(SupplierType item) {
        return true;
    }

    @Override
    protected BeanItem<SupplierType> createNewBeanItem() {
        return new BeanItem<>(new SupplierType());
    }

    @Override
    protected BeanItem<SupplierType> createCopiedBeanItem(SupplierType item) {
        SupplierType copy = new SupplierType();
        copy.setName(item.getName());
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"name"});
        table.setColumnHeader("name", "Nom");
    }

    @Override
    protected void addDataToItem(SupplierType item) throws Exception {
    }

    @Override
    protected void preSaveProcessing(SupplierType item) {
    }

    @Override
    protected void postSaveProcessing(SupplierType item) {
    }

    @Override
    protected void createMultiSelectForm() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
