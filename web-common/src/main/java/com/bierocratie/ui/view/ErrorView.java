package com.bierocratie.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 01/05/14
 * Time: 16:08
 * To change this template use File | Settings | File Templates.
 */
public class ErrorView extends VerticalLayout implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3092686219277653351L;

	public ErrorView() {
        addComponent(new Label("Oops. The view you tried to navigate to doesn't exist."));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
