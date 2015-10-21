package com.bierocratie.ui.view.accounting;

import com.bierocratie.model.accounting.Tva;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.data.util.BeanItem;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.ViewChangeListener;

@SuppressWarnings("serial")
public class TvaView extends AbstractBasicModelView<Tva> {

    private static final Logger LOG = LoggerFactory.getLogger(TvaView.class);

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
    protected void preSaveProcessing(Tva item) {
    }

    @Override
    protected void postSaveProcessing(Tva item) {
    }

    @Override
    protected void createMultiSelectForm() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
