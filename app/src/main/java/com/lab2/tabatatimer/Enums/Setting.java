package com.lab2.tabatatimer.Enums;

public enum Setting {
    FONTSIZE("fontSize"),
    THEME ("theme"),
    LANG ("test_lang"),
    DELETE_ALL("DeleteAll");

   private final String setting;

   Setting(String sett)
   {
       this.setting = sett;
   }

   public String getSetting()
   {
       return setting;
   }
}
