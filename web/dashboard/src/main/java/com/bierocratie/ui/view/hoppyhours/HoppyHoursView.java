package com.bierocratie.ui.view.hoppyhours;

import com.bierocratie.model.catalog.Beer;
import com.bierocratie.model.hoppyhours.Selection;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 21/10/14
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */
public class HoppyHoursView extends AbstractBasicModelView<Selection> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 9108623179370033847L;
	
    @Override
    protected Class<Selection> getClazz() {
        return Selection.class;
    }

    @Override
    protected String getTableName() {
        return "Hoppy Hours";
    }

    @Override
    protected AbstractMenuBar getMenuBar() {
        // FIXME @Inject
        return new DashboardMenuBar();
    }

    @Override
    protected void buildAndBind() {
        form.addComponent(binder.buildAndBind("Date", "date"));

        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<Beer> beers = JPAContainerFactory.make(Beer.class, "dashboard");
        ComboBox beerComboBox = new ComboBox("Bière");
        beerComboBox.setContainerDataSource(beers);
        beerComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        beerComboBox.setItemCaptionPropertyId("label");
        beerComboBox.setConverter(new SingleSelectConverter<Beer>(beerComboBox));
        beerComboBox.setFilteringMode(FilteringMode.CONTAINS);
        beerComboBox.setImmediate(true);
        binder.bind(beerComboBox, "beer");
        form.addComponent(beerComboBox);

        form.addComponent(binder.buildAndBind("Quantité", "quantity"));
        form.addComponent(binder.buildAndBind("Réglées", "billingDate"));
    }

    @Override
    protected void updateForm(Selection item) {
    }

    @Override
    protected boolean isUpdateAuthorized(Selection item) {
        return true;
    }

    @Override
    protected BeanItem<Selection> createNewBeanItem() {
        return new BeanItem<>(new Selection());
    }

    @Override
    protected BeanItem<Selection> createCopiedBeanItem(Selection item) {
        Selection copy = new Selection();
        copy.setBeer(item.getBeer());
        copy.setDate(item.getDate());
        copy.setQuantity(item.getQuantity());
        copy.setBillingDate(item.getBillingDate());
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        entities.addNestedContainerProperty("beer.brewery");

        table.setVisibleColumns(new String[]{"date", "beer", "beer.brewery", "quantity", "billingDate"});
        table.setColumnHeader("date", "Date");
        table.setColumnHeader("beer", "Bière");
        table.setColumnHeader("beer.brewery", "Brasserie");
        table.setColumnHeader("quantity", "Quantité");
        table.setColumnHeader("billingDate", "Reglées");
    }

    @Override
    protected void addDataToItem(Selection item) throws Exception {
    }

    @Override
    protected void preSaveItemProcessing(Selection item) {
    }

    @Override
    protected void postSaveItemProcessing(Selection item) {
    }

    @Override
    protected void createMultiSelectForm() {
    }

    @Override
    protected void getMultiFormValues() {
    }

    @Override
    protected void setItemValues(Selection item) {
    }

    @Override
    protected void postSaveItemsProcessing() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
