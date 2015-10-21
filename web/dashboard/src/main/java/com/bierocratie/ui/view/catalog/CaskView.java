package com.bierocratie.ui.view.catalog;

import com.bierocratie.model.accounting.Tva;
import com.bierocratie.model.catalog.Beer;
import com.bierocratie.model.catalog.Supplier;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.*;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 21/10/14
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */
public class CaskView extends BeerView {

    private static final Logger LOG = LoggerFactory.getLogger(CaskView.class);

    @Override
    protected String getTableName() {
        return "Fûts";
    }

    @Override
    protected void setTableColumns() {
        entities.addNestedContainerProperty("capacity.name");

        table.setVisibleColumns(new String[]{"region", "brewery", "name", "style", "abv", "capacity.name", "supplier", "costHT", "available", "costingPriceTTC", "priceHT", "priceTTC"});
        table.setColumnHeader("name", "Nom");
        table.setColumnHeader("style", "Style");
        table.setColumnHeader("brewery", "Brasserie");
        table.setColumnHeader("region", "Origine");
        table.setColumnHeader("abv", "Degré Alcool");
        table.setColumnHeader("capacity.name", "Volume");
        table.setColumnHeader("supplier", "Fournisseur");
        table.setColumnHeader("costHT", "Achat HT");
        table.setColumnHeader("available", "Disponible");
        table.setColumnHeader("costingPriceTTC", "Tarif coûtant TTC");
        table.setColumnHeader("priceHT", "Tarif HT");
        table.setColumnHeader("priceTTC", "Tarif TTC");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);

        entities.addNestedContainerProperty("capacity.name");
        entities.addContainerFilter(new And(new Not(new IsNull("capacity")), new Or(new Compare.Equal("capacity.name", "20L"),new Compare.Equal("capacity.name", "30L"))));

        Button excelExportButton = new Button("Exporter sous Excel");
        excelExportButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(final Button.ClickEvent event) {
                ExcelExport excelExport = new ExcelExport(table);
                String title = "Biérocratie - Fûts";
                excelExport.setReportTitle(title);
                // FIXME Pouvoir changer le nom
                //excelExport.setExportFileName(title);
                excelExport.export();
            }
        });
        addComponent(excelExportButton);
    }

}
