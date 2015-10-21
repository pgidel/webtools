package com.bierocratie.ui.view;

import com.bierocratie.db.OrderDAO;
import com.bierocratie.model.Order;
import com.bierocratie.ui.NavigatorUI;
import com.bierocratie.ui.component.OrderMenuBar;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.apache.shiro.SecurityUtils;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 11/05/14
 * Time: 12:07
 * To change this template use File | Settings | File Templates.
 */
public class InProgressView extends VerticalLayout implements View {

    private JPAContainer<Order> orders;

    private Button newOrderbutton;
    private Table orderTable;

    // FIXME @Inject
    private OrderDAO orderDAO = new OrderDAO("orderhelper");

    public InProgressView() {
        addComponent(new OrderMenuBar());

        newOrderbutton = new Button("Nouvelle commande", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getUI().getNavigator().navigateTo(NavigatorUI.ORDER_VIEW + "/" + SecurityUtils.getSubject().getPrincipal());
            }
        });
        addComponent(newOrderbutton);

        orders = JPAContainerFactory.make(Order.class, orderDAO.getPersistenceUnitName());
        orderTable = new Table("Commandes en cours", orders);
        orderTable.setVisibleColumns(new String[]{"supplier", "date", "comment"});
        orderTable.setColumnHeader("supplier", "Fournisseur");
        orderTable.setColumnHeader("date", "Date");
        orderTable.setColumnHeader("comment", "Commentaire");
        orderTable.setPageLength(5);
        orderTable.setSelectable(false);

        addComponent(orderTable);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        orders.removeAllContainerFilters();
        orders.addContainerFilter(new Compare.GreaterOrEqual("date", new Date()));
    }

    public void setOrderDAO(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

}
