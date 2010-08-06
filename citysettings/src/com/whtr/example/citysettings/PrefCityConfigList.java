package com.whtr.example.citysettings;

import android.preference.ListPreference;
import android.util.AttributeSet;
import android.content.Context;

public class PrefCityConfigList extends ListPreference
{
    public PrefCityConfigList(Context context)
    {
        super(context);
    }
    
    public PrefCityConfigList(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public void onDialogClosed(boolean positiveResult)
    {
        super.onDialogClosed(positiveResult);
        
        if (positiveResult)
        {    
            
        }
    }
}
