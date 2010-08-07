package com.whtr.example.citysettings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.content.SharedPreferences;
import android.util.Log;

public class UIxConfigure extends PreferenceActivity
{
    private static final String TAG = "citysettings";
    
    private PreferenceScreen mCitySettingsScreen;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_app_settings);
        mCitySettingsScreen = (PreferenceScreen)findPreference("config_city_default");
        assert(mCitySettingsScreen != null);
        
        SharedPreferences prefs = getPreferenceManager().getDefaultSharedPreferences(this);
        mCitySettingsScreen.setSummary(prefs.getString("city_config_name_temp", "请选择城市"));
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
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d(TAG, "UIxConfigure.onActivityResult. requestCode: " + requestCode + ", resultCode:" + resultCode);
        if (requestCode == 0)
        {
            if (resultCode == 1)
            {
                SharedPreferences prefs = getPreferenceManager().getDefaultSharedPreferences(this);
                mCitySettingsScreen.setSummary(prefs.getString("city_config_name_temp", "请选择城市"));
            }
        }
    }
}
