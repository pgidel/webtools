package com.bierocratie.ui.view.accounting;

import com.bierocratie.model.accounting.Category;
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
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;

public class CategoryView extends AbstractBasicModelView<Category> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1005243066270817798L;

	@Override
    protected Class<Category> getClazz() {
        return Category.class;
    }

    @Override
    protected String getTableName() {
        return "Catégories";
    }

    @Override
    protected AbstractMenuBar getMenuBar() {
        // FIXME @Inject
        return new DashboardMenuBar();
    }

    @Override
    protected void buildAndBind() {
        form.addComponent(binder.buildAndBind("Nom", "name"));
        form.addComponent(binder.buildAndBind("Pourcentage payé", "percentageDueByTheCompany"));

        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<Tva> tvas = JPAContainerFactory.make(Tva.class, "dashboard");
        ComboBox tvaComboBox = new ComboBox("TVA par défaut");
        tvaComboBox.setContainerDataSource(tvas);
        tvaComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tvaComboBox.setItemCaptionPropertyId("rate2String");
        tvaComboBox.setConverter(new SingleSelectConverter<Tva>(tvaComboBox));
        tvaComboBox.setTextInputAllowed(false);
        tvaComboBox.setImmediate(true);
        binder.bind(tvaComboBox, "defaultTva");
        form.addComponent(tvaComboBox);
    }

    @Override
    protected void updateForm(Category item) {
    }

    @Override
    protected boolean isUpdateAuthorized(Category item) {
        return true;
    }

    @Override
    protected BeanItem<Category> createNewBeanItem() {
        Category category;
        final JPAContainer<Tva> tvas = JPAContainerFactory.make(Tva.class, "dashboard");
        tvas.addContainerFilter(new Compare.Equal("rate", 0.2));
        Object itemId = tvas.firstItemId();
        if (itemId != null) {
            Tva defaultTva = tvas.getItem(itemId).getEntity();
            category = new Category(defaultTva);
        } else {
            category = new Category();
        }
        tvas.removeAllContainerFilters();
        return new BeanItem<>(category);
    }

    @Override
    protected BeanItem<Category> createCopiedBeanItem(Category item) {
        Category copy = new Category();
        copy.setName(item.getName());
        copy.setDefaultTva(item.getDefaultTva());
        copy.setPercentageDueByTheCompany(item.getPercentageDueByTheCompany());
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"name", "percentageDueByTheCompany", "defaultTva"});
        table.setColumnHeader("name", "Nom");
        table.setColumnHeader("percentageDueByTheCompany", "Pourcentage payé");
        table.setColumnHeader("defaultTva", "TVA par défaut");
    }

    @Override
    protected void addDataToItem(Category item) throws Exception {
    }

    @Override
    protected void preSaveItemProcessing(Category item) {
    }

    @Override
    protected void postSaveItemProcessing(Category item) {
    }

    @Override
    protected void createMultiSelectForm() {
    }

    @Override
    protected void getMultiFormValues() {
    }

    @Override
    protected void setItemValues(Category item) {
    }

    @Override
    protected void postSaveItemsProcessing() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
