package com.lab2.tabatatimer.Enums;

public enum Language {
    RUSSIAN_r("русский"),
    RUSSIAN_e("russian"),
    ENGLISH_r("английский"),
    ENGLISH_e("english"),
    EN("en"),
    RUS("ru");

    private final String setting;

    Language(String sett)
    {
        this.setting = sett;
    }

    public String getLanguage()
    {
        return setting;
    }
}