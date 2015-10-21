package com.bierocratie.ui.view.catalog;

import com.bierocratie.model.catalog.Beer;
import com.bierocratie.model.catalog.Capacity;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.data.util.BeanItem;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.ComboBox;

@SuppressWarnings("serial")
public class CapacityView extends AbstractBasicModelView<Capacity> {

    private static final Logger LOG = LoggerFactory.getLogger(CapacityView.class);

    @Override
    protected Class<Capacity> getClazz() {
        return Capacity.class;
    }

    @Override
    protected String getTableName() {
        return "Formats";
    }

    @Override
    protected AbstractMenuBar getMenuBar() {
        // FIXME @Inject
        return new DashboardMenuBar();
    }

    private ComboBox comboBox;

    @Override
    protected void buildAndBind() {
        form.addComponent(binder.buildAndBind("Nom", "name"));
        form.addComponent(binder.buildAndBind("Capacité", "size"));
        form.addComponent(binder.buildAndBind("Poids", "weight"));
    }

    @Override
    protected void updateForm(Capacity item) {
    }

    @Override
    protected boolean isUpdateAuthorized(Capacity item) {
        return true;
    }

    @Override
    protected BeanItem<Capacity> createNewBeanItem() {
        return new BeanItem<>(new Capacity());
    }

    @Override
    protected BeanItem<Capacity> createCopiedBeanItem(Capacity item) {
        Capacity copy = new Capacity();
        copy.setName(item.getName());
        copy.setSize(item.getSize());
        copy.setWeight(item.getWeight());
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"name", "size", "weight"});
        table.setColumnHeader("name", "Nom");
        table.setColumnHeader("size", "Capacité");
        table.setColumnHeader("weight", "Poids");
    }

    @Override
    protected void addDataToItem(Capacity item) throws Exception {
    }

    @Override
    protected void preSaveProcessing(Capacity item) {
    }

    @Override
    protected void postSaveProcessing(Capacity item) {
    }

    @Override
    protected void createMultiSelectForm() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
