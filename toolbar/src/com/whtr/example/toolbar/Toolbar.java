package com.whtr.example.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Toolbar extends LinearLayout
{
    public Toolbar(Context context)
    {
        this(context, null);
    }
    
    public Toolbar(final Context context, AttributeSet attrs)
    {
        super(context, attrs);
        
        setOrientation(HORIZONTAL);
        setBackgroundColor(getResources().getColor(android.R.color.transparent));
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.navigator, this);
        TypedArray a = context.obtainStyledAttributes(attrs, 
                R.styleable.Toolbar);
        String option = a.getString(R.styleable.Toolbar_textViewId);

        String resourceId = "com.whtr.example.toolbar:id/" + option;
        int optionId = getResources().getIdentifier(resourceId, null, null);                      
        TextView currentOption = (TextView) findViewById(optionId);
        currentOption.setBackgroundColor(getResources().
                getColor(android.R.color.white));
        currentOption.setTextColor(getResources().
                getColor(android.R.color.black));
        currentOption.requestFocus(optionId);
        currentOption.setFocusable(false);
        currentOption.setClickable(false);

        TextView option1 = (TextView) findViewById(R.id.option1);
        option1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CharSequence txt = "Hello!";
                int len = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, txt, len);
                toast.show();
            }
        });
    }
}
