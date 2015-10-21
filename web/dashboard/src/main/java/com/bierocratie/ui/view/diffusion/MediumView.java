package com.bierocratie.ui.view.diffusion;

import com.bierocratie.model.catalog.Beer;
import com.bierocratie.model.diffusion.Medium;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.data.util.BeanItem;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.ViewChangeListener;

@SuppressWarnings("serial")
public class MediumView extends AbstractBasicModelView<Medium> {

    private static final Logger LOG = LoggerFactory.getLogger(MediumView.class);

    @Override
    protected Class<Medium> getClazz() {
        return Medium.class;
    }

    @Override
    protected String getTableName() {
        return "Media";
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
    protected void updateForm(Medium item) {
    }

    @Override
    protected boolean isUpdateAuthorized(Medium item) {
        return true;
    }

    @Override
    protected BeanItem<Medium> createNewBeanItem() {
        return new BeanItem<>(new Medium());
    }

    @Override
    protected BeanItem<Medium> createCopiedBeanItem(Medium item) {
        Medium copy = new Medium();
        copy.setName(item.getName());
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"name"});
        table.setColumnHeader("name", "Nom");
    }

    @Override
    protected void addDataToItem(Medium item) throws Exception {
    }

    @Override
    protected void preSaveProcessing(Medium item) {
    }

    @Override
    protected void postSaveProcessing(Medium item) {
    }

    @Override
    protected void createMultiSelectForm() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
