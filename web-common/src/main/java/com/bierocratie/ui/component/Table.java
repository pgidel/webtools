package com.bierocratie.ui.component;

import com.vaadin.data.Container;
import com.vaadin.data.Property;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 13/11/14
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
public class Table extends com.vaadin.ui.Table {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4340997217320528325L;
	public static final SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    public static final NumberFormat doubleFormatter = NumberFormat.getNumberInstance();
    public static final NumberFormat integerFormatter = NumberFormat.getNumberInstance();

    static {
        doubleFormatter.setMinimumFractionDigits(2);
        doubleFormatter.setMaximumFractionDigits(2);
    }

    public Table(String tableName, Container dataSource) {
        super(tableName, dataSource);
        for (Object propertyId : this.getContainerPropertyIds()) {
            setColumnAlignment(propertyId, getColumnAlignment(getType(propertyId)));
        }
    }

    public Table(String tableName) {
        super(tableName);
    }

    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException {
        setColumnAlignment(propertyId, getColumnAlignment(type));
        return super.addContainerProperty(propertyId, type, defaultValue);
    }

    private Align getColumnAlignment(Class<?> type) {
        if (type == Date.class) {
            return Align.CENTER;
        }
        if (type == Boolean.class) {
            return Align.CENTER;
        }
        if (type == Double.class) {
            return Align.RIGHT;
        }
        if (type == BigInteger.class) {
            return Align.RIGHT;
        }
        if (type == BigDecimal.class) {
            return Align.RIGHT;
        }
        return Align.LEFT;
    }

    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property property) {
        if (property.getValue() == null) {
            return "";
        }
        if (property.getType() == Date.class) {
            return simpleDateFormatter.format((Date) property.getValue());
        }
        if (property.getType() == Boolean.class) {
            return (Boolean) property.getValue() ? "X" : "";
        }
        if (property.getType() == Double.class) {
            return doubleFormatter.format((Double) property.getValue());
        }
        if (property.getType() == BigInteger.class) {
            return integerFormatter.format(((BigInteger) property.getValue()).longValue());
        }
        if (property.getType() == BigDecimal.class) {
            return doubleFormatter.format(((BigDecimal) property.getValue()).doubleValue());
        }
        return super.formatPropertyValue(rowId, colId, property);
    }

}
