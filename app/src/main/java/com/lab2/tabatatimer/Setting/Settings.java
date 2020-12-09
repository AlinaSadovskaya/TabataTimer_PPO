package com.lab2.tabatatimer.Setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatDelegate;

import com.lab2.tabatatimer.App;
import com.lab2.tabatatimer.DataBase.DataBaseHelper;
import com.lab2.tabatatimer.Enums.FontSize;
import com.lab2.tabatatimer.Enums.Language;
import com.lab2.tabatatimer.Enums.Setting;
import com.lab2.tabatatimer.R;

import java.util.Arrays;
import java.util.Locale;

public class Settings extends PreferenceActivity {

    SharedPreferences sp;
    int language_def;
    int font_def;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean(Setting.THEME.getSetting(), true)) {
            setTheme(R.style.Theme_AppCompat);
        }

        String font = sp.getString(Setting.FONTSIZE.getSetting(), FontSize.SMALL.getFontSize()[0]);
        String listValue = sp.getString(Setting.LANG.getSetting(), Language.ENGLISH_r.getLanguage());
        Configuration configuration = new Configuration();

        Locale locale;
        assert listValue != null;
        if (listValue.toLowerCase().equals(Language.ENGLISH_e.getLanguage()) || listValue.toLowerCase().equals(Language.ENGLISH_r.getLanguage())) {
            font_def = 1;
            locale = new Locale(Language.EN.getLanguage());
        } else {
            font_def = 0;
            locale = new Locale(Language.RUS.getLanguage());
        }
        Locale.setDefault(locale);
        configuration.locale = locale;

        assert font != null;
        if (Arrays.asList(FontSize.SMALL.getFontSize()).contains(font.toLowerCase())) {
            language_def = 0;
            configuration.fontScale = (float) 0.85;
        } else if (Arrays.asList(FontSize.NORMAL.getFontSize()).contains(font.toLowerCase())) {
            language_def = 1;
            configuration.fontScale = (float) 1;
        } else {
            language_def = 2;
            configuration.fontScale = (float) 1.15;
        }

        getBaseContext().getResources().updateConfiguration(configuration, null);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new ChangeSettingsFragment()).commit();
        super.onCreate(savedInstanceState);
    }


    public static class ChangeSettingsFragment extends PreferenceFragment {

        private DataBaseHelper db;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            db = App.getInstance().getDatabase();
            addPreferencesFromResource(R.xml.settings);
            Preference button = findPreference(Setting.DELETE_ALL.getSetting());
            ListPreference language = (ListPreference) findPreference(Setting.LANG.getSetting());
            Preference theme = findPreference(Setting.THEME.getSetting());
            ListPreference font = (ListPreference) findPreference(Setting.FONTSIZE.getSetting());
            font.setValueIndex(((Settings) getActivity()).language_def);
            language.setValueIndex(((Settings) getActivity()).font_def);
            theme.setOnPreferenceChangeListener(this::onThemeChange);
            language.setOnPreferenceChangeListener(this::onLanguageChange);
            button.setOnPreferenceClickListener(this::onDeleteClick);
            font.setOnPreferenceChangeListener(this::onFontChange);
        }


        private boolean onLanguageChange(Preference preference, Object newValue) {
            Locale locale;
            if (newValue.toString().toLowerCase().equals(Language.ENGLISH_e.getLanguage()) || newValue.toString().toLowerCase().equals(Language.ENGLISH_r.getLanguage())) {
                locale = new Locale(Language.EN.getLanguage());
            } else {
                locale = new Locale(Language.RUS.getLanguage());
            }
            Locale.setDefault(locale);
            Configuration configuration = new Configuration();
            configuration.locale = locale;
            getActivity().getResources().updateConfiguration(configuration, null);
            getActivity().recreate();
            return true;
        }


        private boolean onFontChange(Preference preference, Object newValue) {
            Configuration configuration = getResources().getConfiguration();
            if (Arrays.asList(FontSize.SMALL.getFontSize()).contains(newValue.toString().toLowerCase())) {
                configuration.fontScale = (float) 0.85;
            } else if (Arrays.asList(FontSize.SMALL.getFontSize()).contains(newValue.toString().toLowerCase())) {
                configuration.fontScale = (float) 1;
            } else {
                configuration.fontScale = (float) 1.15;
            }
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            getActivity().getBaseContext().getResources().updateConfiguration(configuration, metrics);
            getActivity().recreate();
            return true;
        }

        private boolean onThemeChange(Preference preference, Object newValue) {
            if ((boolean) newValue) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            getActivity().recreate();
            return true;
        }


        private boolean onDeleteClick(Preference preference) {
            db.timerDao().DeleteAll();
            Intent intent = new Intent();
            getActivity().setResult(RESULT_OK, intent);
            getActivity().finish();
            return true;
        }
    }
}