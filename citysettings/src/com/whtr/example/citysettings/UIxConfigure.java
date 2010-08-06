package com.whtr.example.citysettings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class UIxConfigure extends PreferenceActivity
{
    private PreferenceScreen mCitySettingsScreen;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_app_settings);
        mCitySettingsScreen = (PreferenceScreen)findPreference("config_city_default");
        assert(mCitySettingsScreen != null);
    }
    
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)
    {
        if (preference == mCitySettingsScreen)
        {
            Intent intent = new Intent(this, UIxCities.class);
            startActivityForResult(intent, 0);
        }
        
        return true;
    }
}
