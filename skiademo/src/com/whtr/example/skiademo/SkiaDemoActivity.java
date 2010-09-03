package com.whtr.example.skiademo;

import android.app.Activity;
import android.os.Bundle;

public class SkiaDemoActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        SkiaView view = new SkiaView(this, Integer.parseInt(getIntent().getStringExtra("id")));
        
        setContentView(view);
    }
}
