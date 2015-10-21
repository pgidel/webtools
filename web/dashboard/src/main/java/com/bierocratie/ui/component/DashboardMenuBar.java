package com.bierocratie.ui.component;

import com.bierocratie.ui.NavigatorUI;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 08/05/14
 * Time: 14:44
 * To change this template use File | Settings | File Templates.
 */
public class DashboardMenuBar extends AbstractMenuBar {

    public static final String OPERATING_STATEMENT_TITLE = "Compte de résultat";
    public static final String CASH_PLAN_TITLE = "Plan de trésorerie";

    public DashboardMenuBar() {
        setSizeFull();

        addItem("Tableau de bord", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Tableau de bord");
                getUI().getNavigator().navigateTo(NavigatorUI.DASHBOARD_VIEW);
            }
        });
        /*addItem("Commandes", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Commandes");
                getUI().getNavigator().navigateTo(NavigatorUI.ORDER_VIEW);
            }
        });*/
        addItem("Diffusions", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Diffusions");
                getUI().getNavigator().navigateTo(NavigatorUI.DIFFUSION_VIEW);
            }
        });
        /*addItem("Hoppy Hours", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Hoppy Hours");
                getUI().getNavigator().navigateTo(NavigatorUI.HOPPY_HOURS_VIEW);
            }
        });*/

        MenuItem catalogItem = addItem("Catalogue", null);
        catalogItem.addItem("Bières", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Bières");
                getUI().getNavigator().navigateTo(NavigatorUI.BEER_VIEW);
            }
        });
        catalogItem.addItem("Fûts", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Fûts");
                getUI().getNavigator().navigateTo(NavigatorUI.CASK_VIEW);
            }
        });
        catalogItem.addItem("Fournisseurs", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Fournisseurs");
                getUI().getNavigator().navigateTo(NavigatorUI.SUPPLIER_VIEW);
            }
        });

        MenuItem accountingItem = addItem("Comptabilité", null);
        accountingItem.addItem(OPERATING_STATEMENT_TITLE, new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle(OPERATING_STATEMENT_TITLE);
                getUI().getNavigator().navigateTo(NavigatorUI.OPERATING_STATEMENT_VIEW);
            }
        });
        accountingItem.addItem(CASH_PLAN_TITLE, new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle(CASH_PLAN_TITLE);
                getUI().getNavigator().navigateTo(NavigatorUI.CASH_PLAN_VIEW);
            }
        });
        accountingItem.addSeparator();
        accountingItem.addItem("Chiffres d'affaire", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Chiffres d'affaire");
                getUI().getNavigator().navigateTo(NavigatorUI.INCOME_VIEW);
            }
        });
        accountingItem.addItem("Factures", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Factures");
                getUI().getNavigator().navigateTo(NavigatorUI.INVOICE_VIEW);
            }
        });
        accountingItem.addItem("Valeur des stocks", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Valeur des stocks");
                getUI().getNavigator().navigateTo(NavigatorUI.STOCK_VALUE_VIEW);
            }
        });

        MenuItem configItem = addItem("Config.", null);
        configItem.addItem("Media", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Media");
                getUI().getNavigator().navigateTo(NavigatorUI.MEDIUM_VIEW);
            }
        });
        configItem.addSeparator();

        configItem.addItem("Catégories", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Catégories");
                getUI().getNavigator().navigateTo(NavigatorUI.CATEGORY_VIEW);
            }
        });
        configItem.addItem("Exercices comptables", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Exercices comptables");
                getUI().getNavigator().navigateTo(NavigatorUI.BUDGET_YEAR_VIEW);
            }
        });
        configItem.addItem("TVA", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("TVA");
                getUI().getNavigator().navigateTo(NavigatorUI.TVA_VIEW);
            }
        });
        configItem.addSeparator();

        configItem.addItem("Volumes", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Volume");
                getUI().getNavigator().navigateTo(NavigatorUI.CAPACITY_VIEW);
            }
        });
        configItem.addItem("Type de fournisseurs", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Type de fournisseurs");
                getUI().getNavigator().navigateTo(NavigatorUI.SUPPLIER_TYPE_VIEW);
            }
        });
        configItem.addItem("Pays", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Pays");
                getUI().getNavigator().navigateTo(NavigatorUI.COUNTRY_VIEW);
            }
        });

        addItem("Chat", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                getUI().getPage().setTitle("Chat");
                getUI().getNavigator().navigateTo(NavigatorUI.CHAT_VIEW);
            }
        });
    }

}
