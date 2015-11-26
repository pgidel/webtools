package com.bierocratie.ui.view.catalog;

import com.vaadin.data.util.filter.*;
import com.vaadin.navigator.ViewChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 21/10/14
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */
public class CaskView extends BeerView {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4976325834772690559L;

	public CaskView() {
        table.sort(new Object[]{"brewery", "name", "capacity"}, new boolean[]{true, true, true});
    }

    @Override
    protected String getTableName() {
        return "Fûts";
    }

    @Override
    protected void setTableColumns() {
        entities.addNestedContainerProperty("brewery.region");

        table.setVisibleColumns(new String[]{"brewery.region", "brewery", "name", "style", "abv", "capacity", "supplier", "costHT", "costingPriceHT", "costingPriceTTC", "priceHT", "priceTTC"});
        table.setColumnHeader("name", "Nom");
        table.setColumnHeader("style", "Style");
        table.setColumnHeader("brewery", "Brasserie");
        table.setColumnHeader("brewery.region", "Origine");
        table.setColumnHeader("abv", "Degré Alcool");
        table.setColumnHeader("capacity", "Volume");
        table.setColumnHeader("supplier", "Fournisseur");
        table.setColumnHeader("costHT", "Achat HT");
        table.setColumnHeader("costingPriceHT", "Tarif coûtant HT");
        table.setColumnHeader("costingPriceTTC", "Tarif coûtant TTC");
        table.setColumnHeader("priceHT", "Tarif HT");
        table.setColumnHeader("priceTTC", "Tarif TTC");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);

        entities.addNestedContainerProperty("capacity.name");
        entities.addContainerFilter(new And(new Not(new IsNull("capacity")), new Or(new Compare.Equal("capacity.name", "20L"), new Compare.Equal("capacity.name", "30L"))));

        // TODO Export PDF
        /*Button pdfExportButton = new Button("Exporter en PDF");
        pdfExportButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(final Button.ClickEvent event) {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("ReportTitle", "Basic JasperReport");
                parameters.put("MaxSalary", new Double(25000.00));

                JasperDesign jasperDesign = JasperFillManager.loadXmlDesign("BasicReport.xml");
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
// Second, create a map of parameters to pass to the report.

// Third, get a database connection
                Connection conn = Database.getConnection();
// Fourth, create JasperPrint using fillReport() method
                JasperPrint jasperPrint = JasperManager.fillReport(jasperReport, parameters, conn);
// You can use JasperPrint to create PDF
                JasperManager.printReportToPdfFile(jasperPrint, "BasicReport.pdf");
                JasperRunManager.(jasperPrint, "BasicReport.pdf");
// Or to view report in the JasperViewer
                JasperViewer.viewReport(jasperPrint);

                String pdfFileName = "futs.pdf";

                String sourceFileName = "demo.jrxml";

                System.out.println("Compiling Report Design ...");
                try {
                    JasperCompileManager.compileReportToFile(sourceFileName);
                } catch (JRException e) {
                    e.printStackTrace();
                }
                System.out.println("Done compiling!!! ...");
            }
        });
        addComponent(pdfExportButton);*/
    }

}
