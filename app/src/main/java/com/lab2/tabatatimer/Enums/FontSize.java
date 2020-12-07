package com.lab2.tabatatimer.Enums;

public enum FontSize {
    SMALL(new String[]{"малый", "small"}),
    NORMAL(new String[]{"нормальный", "normal"}),
    BIG(new String[]{"большой", "big"});

    private final String [] setting;

    FontSize(String[] sett)
    {
        this.setting = sett;
    }

    public String[] getFontSize()
    {
        return setting;
    }
}
