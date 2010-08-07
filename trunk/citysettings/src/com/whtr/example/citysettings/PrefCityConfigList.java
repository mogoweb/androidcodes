package com.whtr.example.citysettings;

import android.preference.ListPreference;
import android.util.AttributeSet;
import android.content.Context;

public class PrefCityConfigList extends ListPreference
{
    private UIxCities mCitiesPreference;
    
    public PrefCityConfigList(Context context)
    {
        this(context, null);
    }
    
    public PrefCityConfigList(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        
        mCitiesPreference = (UIxCities)context;
    }
    
    public void onDialogClosed(boolean positiveResult)
    {
        super.onDialogClosed(positiveResult);
        
        if (positiveResult)
        {    
            mCitiesPreference.setResult(1);
            mCitiesPreference.finish();
        }
        else
        {
            mCitiesPreference.setResult(0);
        }
    }
}
