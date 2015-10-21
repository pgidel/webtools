package com.bierocratie.ui.view.catalog;

import com.bierocratie.model.catalog.Beer;
import com.bierocratie.model.catalog.Country;
import com.bierocratie.model.catalog.Supplier;
import com.bierocratie.model.catalog.SupplierType;
import com.bierocratie.ui.NavigatorUI;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.component.TextArea;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 21/10/14
 * Time: 00:04
 * To change this template use File | Settings | File Templates.
 */
public class SupplierView extends AbstractBasicModelView<Supplier> {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierView.class);

    @Override
    protected Class<Supplier> getClazz() {
        return Supplier.class;
    }

    @Override
    protected String getTableName() {
        return "Fournisseurs";
    }

    @Override
    protected AbstractMenuBar getMenuBar() {
        // FIXME @Inject
        return new DashboardMenuBar();
    }

    private JPAContainer<Beer> beers;

    private Button addBeerButton;

    @Override
    protected void buildAndBind() {
        form.addComponent(binder.buildAndBind("Nom", "name"));
        form.addComponent(binder.buildAndBind("Code", "code"));
        form.addComponent(binder.buildAndBind("Adresse", "address"));
        form.addComponent(binder.buildAndBind("Email", "email"));
        form.addComponent(binder.buildAndBind("Téléphone", "telephone"));

        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<SupplierType> types = JPAContainerFactory.make(SupplierType.class, "dashboard");
        ComboBox typeComboBox = new ComboBox("Type");
        typeComboBox.setContainerDataSource(types);
        typeComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        typeComboBox.setItemCaptionPropertyId("name");
        typeComboBox.setConverter(new SingleSelectConverter<SupplierType>(typeComboBox));
        typeComboBox.setFilteringMode(FilteringMode.CONTAINS);
        typeComboBox.setImmediate(true);
        binder.bind(typeComboBox, "type");
        form.addComponent(typeComboBox);

        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<Country> countries = JPAContainerFactory.make(Country.class, "dashboard");
        ComboBox countryComboBox = new ComboBox("Pays");
        countryComboBox.setContainerDataSource(countries);
        countryComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        countryComboBox.setItemCaptionPropertyId("name");
        countryComboBox.setConverter(new SingleSelectConverter<Country>(countryComboBox));
        countryComboBox.setFilteringMode(FilteringMode.CONTAINS);
        countryComboBox.setImmediate(true);
        binder.bind(countryComboBox, "country");
        form.addComponent(countryComboBox);

        TextArea textArea = new TextArea("Dégustation");
        binder.bind(textArea, "tastingNotes");
        form.addComponent(textArea);

        beers = JPAContainerFactory.make(Beer.class, "dashboard");
        Table beerTable = new Table("Bières", beers);
        // TODO Ajouter une colonne pour chaque format
        beerTable.setVisibleColumns(new String[]{"name", "brewery", "supplier"});
        beerTable.setColumnHeader("name", "Nom");
        beerTable.setColumnHeader("brewery", "Brasserie");
        beerTable.setColumnHeader("supplier", "Fournisseur");
        beerTable.setEnabled(false);
        beerTable.setPageLength(0);
        binder.bind(beerTable, "beers");
        form.addComponent(beerTable);

        addBeerButton = new Button("Nouvelle bière");
        addBeerButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getUI().getNavigator().navigateTo(NavigatorUI.BEER_VIEW + "/" + addBeerButton.getData());
            }
        });
        form.addComponent(addBeerButton);
    }

    @Override
    protected void updateForm(Supplier item) {
        if ((boolean) addButton.getData()) {
            addBeerButton.setEnabled(false);
        } else {
            addBeerButton.setEnabled(true);
            addBeerButton.setData(item.getId());

            refreshContainerFilters(item);
        }
    }

    private void refreshContainerFilters(Supplier item) {
        beers.removeAllContainerFilters();
        beers.addContainerFilter(new Or(new Compare.Equal("supplier", item), new Compare.Equal("brewery", item)));
    }

    @Override
    protected boolean isUpdateAuthorized(Supplier item) {
        return true;
    }

    @Override
    protected BeanItem<Supplier> createNewBeanItem() {
        Supplier newItem = new Supplier();
        refreshContainerFilters(newItem);
        return new BeanItem<>(newItem);
    }

    @Override
    protected BeanItem<Supplier> createCopiedBeanItem(Supplier item) {
        Supplier copy = new Supplier();
        copy.setName(item.getName());
        copy.setCode(item.getCode());
        copy.setAddress(item.getAddress());
        copy.setCountry(item.getCountry());
        copy.setTelephone(item.getTelephone());
        copy.setEmail(item.getEmail());
        copy.setTastingNotes(item.getTastingNotes());
        return new BeanItem<>(copy);
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"name", "code", "address", "email", "telephone", "type", "country"});
        table.setColumnHeader("name", "Nom");
        table.setColumnHeader("code", "Code");
        table.setColumnHeader("address", "Adresse");
        table.setColumnHeader("email", "Email");
        table.setColumnHeader("telephone", "Téléphone");
        table.setColumnHeader("type", "Type");
        table.setColumnHeader("country", "Pays");
    }

    @Override
    protected void addDataToItem(Supplier item) throws Exception {
    }

    @Override
    protected void preSaveProcessing(Supplier item) {
    }

    @Override
    protected void postSaveProcessing(Supplier item) {
    }

    @Override
    protected void createMultiSelectForm() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}

