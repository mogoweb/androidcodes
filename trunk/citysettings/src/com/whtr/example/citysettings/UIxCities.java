package com.whtr.example.citysettings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class UIxCities extends PreferenceActivity
{
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_default_city_settings);
    }
}
