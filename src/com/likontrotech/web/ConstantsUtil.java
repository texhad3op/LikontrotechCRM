package com.likontrotech.web;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ConstantsUtil
{

    private static Locale locale = new Locale("LT","lt");
    private static Locale localeEn = new Locale("En","en");
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat fullsdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private static DecimalFormatSymbols symb = new DecimalFormatSymbols(localeEn);
    private static DecimalFormat Currency = new DecimalFormat("#0.00",symb);


    public static SimpleDateFormat getSDF()
    {
        return sdf;
    }
    public static SimpleDateFormat getFSDF()
    {
        return fullsdf;
    }
    //private static NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
    private static NumberFormat nf = NumberFormat.getInstance(localeEn);
    static
    {
        nf.setGroupingUsed(false);
        nf.setMinimumFractionDigits(2);

    }
    public static String format(Float val)
    {
        return Currency.format(val);
    }
    public static Locale getLocaleLt()
    {
        return locale;
    }

}

