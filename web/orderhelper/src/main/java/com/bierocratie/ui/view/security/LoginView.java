package com.bierocratie.ui.view.security;

import com.bierocratie.ui.NavigatorUI;
import com.bierocratie.ui.view.CustomerView;
import com.bierocratie.ui.view.InProgressView;
import com.bierocratie.ui.view.OrderView;
import com.bierocratie.ui.view.catalog.BeerView;
import com.bierocratie.ui.view.catalog.CapacityView;
import com.bierocratie.ui.view.catalog.SupplierView;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 12/04/14
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
public class LoginView extends AbstractLoginView {

    @Override
    protected void addAuthorizedViewsToNavigator() {
        getUI().getNavigator().addView(NavigatorUI.IN_PROGRESS_VIEW, InProgressView.class);
        getUI().getNavigator().addView(NavigatorUI.ORDER_VIEW, OrderView.class);
        getUI().getNavigator().addView(NavigatorUI.CUSTOMER_VIEW, CustomerView.class);
        getUI().getNavigator().addView(NavigatorUI.SUPPLIER_VIEW, SupplierView.class);
        getUI().getNavigator().addView(NavigatorUI.BEER_VIEW, BeerView.class);
        getUI().getNavigator().addView(NavigatorUI.CAPACITY_VIEW, CapacityView.class);
    }

    @Override
    protected void navigate() {
        getUI().getNavigator().navigateTo(NavigatorUI.IN_PROGRESS_VIEW);
    }

}
