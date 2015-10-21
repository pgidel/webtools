package com.bierocratie.ui.component;

import com.bierocratie.ui.NavigatorUI;
import com.vaadin.ui.MenuBar;
import org.apache.shiro.SecurityUtils;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 08/05/14
 * Time: 14:44
 * To change this template use File | Settings | File Templates.
 */
public class OrderMenuBar extends AbstractMenuBar {

    public OrderMenuBar() {
        addItem("En cours", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                getUI().getNavigator().navigateTo(NavigatorUI.IN_PROGRESS_VIEW);
            }
        });
        addItem("Commandes", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                getUI().getNavigator().navigateTo(NavigatorUI.ORDER_VIEW);
            }
        });
        addItem("Caves & bars", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                getUI().getNavigator().navigateTo(NavigatorUI.CUSTOMER_VIEW);
            }
        });
        addItem("Brasseries & fournisseurs", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                getUI().getNavigator().navigateTo(NavigatorUI.SUPPLIER_VIEW);
            }
        });
        addItem("Bières", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                getUI().getNavigator().navigateTo(NavigatorUI.BEER_VIEW);
            }
        });
        addItem("Formats", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                getUI().getNavigator().navigateTo(NavigatorUI.CAPACITY_VIEW);
            }
        });
        addItem("Se déconnecter [" + SecurityUtils.getSubject().getPrincipal() + "]", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                SecurityUtils.getSubject().logout();
                getUI().getPage().setLocation("/");
                getSession().close();
            }
        });
    }

}
