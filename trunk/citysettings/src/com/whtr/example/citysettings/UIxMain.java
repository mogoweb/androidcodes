package com.whtr.example.citysettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class UIxMain extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.menu_main, menu);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        if (item.getItemId() == R.id.menu_configure)
        {
            Intent intent = new Intent(this, UIxConfigure.class);
            startActivity(intent);
        }
        
        return true;
    }
}