package com.bierocratie.ui.component;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 17/11/14
 * Time: 00:56
 * To change this template use File | Settings | File Templates.
 */
public class TextArea extends com.vaadin.ui.TextArea {

    public TextArea(String s) {
        super(s);
        init();
    }

    private void init() {
        setRows(10);
        setColumns(35);
    }

}
