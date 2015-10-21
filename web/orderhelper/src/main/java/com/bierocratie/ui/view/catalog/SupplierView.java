package com.bierocratie.ui.view.catalog;

import com.bierocratie.model.catalog.Beer;
import com.bierocratie.model.catalog.Supplier;
import com.bierocratie.ui.NavigatorUI;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.OrderMenuBar;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class SupplierView extends AbstractBasicModelView<Supplier> {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierView.class);

    @Override
    protected Class<Supplier> getClazz() {
        return Supplier.class;
    }

    @Override
    protected String getTableName() {
        return "Brasseries & fournisseurs";
    }

    @Override
    protected AbstractMenuBar getMenuBar() {
        // FIXME @Inject
        return new OrderMenuBar();
    }

    private JPAContainer<Beer> beers;

    private Button addBeerButton;
    private Table beerTable;

    @Override
    protected void buildAndBind() {
        form.addComponent(binder.buildAndBind("Nom", "name"));
        form.addComponent(binder.buildAndBind("Adresse", "address"));
        form.addComponent(binder.buildAndBind("Email", "email"));
        form.addComponent(binder.buildAndBind("Téléphone", "telephone"));

        beers = JPAContainerFactory.make(Beer.class, "orderhelper");
        beerTable = new Table("Bières", beers);
        // TODO Ajouter une colonne pour chaque format
        beerTable.setVisibleColumns(new String[]{"name", "description", "supplier"});
        beerTable.setColumnHeader("name", "Nom");
        beerTable.setColumnHeader("description", "Description");
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

    @Override
    protected boolean isUpdateAuthorized(Supplier item) {
        return true;
    }

    private void refreshContainerFilters(Supplier item) {
        Container.Filter supplierFilter = new Compare.Equal("supplier", item);
        beers.removeAllContainerFilters();
        beers.addContainerFilter(supplierFilter);
    }

    @Override
    protected void postSaveProcessing(Supplier item) {
    }

    @Override
    protected BeanItem<Supplier> createNewBeanItem() {
        Supplier newItem = new Supplier();
        refreshContainerFilters(newItem);
        return new BeanItem<Supplier>(newItem);
    }

    @Override
    protected void createMultiSelectForm() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected BeanItem<Supplier> createCopiedBeanItem(Supplier item) {
        Supplier copy = new Supplier();
        copy.setName(item.getName());
        copy.setCountry(item.getCountry());
        copy.setAddress(item.getAddress());
        copy.setTelephone(item.getTelephone());
        copy.setEmail(item.getEmail());
        copy.setType(item.getType());
        return new BeanItem<>(copy);
    }

    @Override
    protected void addDataToItem(Supplier item) throws Exception {
    }


    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"name", "address", "email", "telephone"});
        table.setColumnHeader("name", "Nom");
        table.setColumnHeader("address", "Adresse");
        table.setColumnHeader("email", "Email");
        table.setColumnHeader("telephone", "Téléphone");
    }

}
