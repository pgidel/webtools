package com.bierocratie.data.util;

import com.vaadin.data.util.converter.StringToBigIntegerConverter;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 24/04/15
 * Time: 17:45
 * To change this template use File | Settings | File Templates.
 */
public class CurrencyConverter extends StringToBigIntegerConverter {

    @Override
    protected NumberFormat getFormat(Locale locale) {
        NumberFormat format = super.getFormat(locale);
        format.setGroupingUsed(true);
        return format;
    }

}
