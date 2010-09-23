package com.whtr.example.skiademo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity {
    
    private String[] mSamplesTitle = {"Hello Skia", "Draw Text"};
    private int[] mSamplesId = {0, 1};
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setListAdapter(new SimpleAdapter(this, getData(), 
                android.R.layout.simple_list_item_1, new String[] {"title"},
                new int[] {android.R.id.text1}));
    }
    
    protected List<Map<String, String>> getData()
    {
        List<Map<String, String>> myData = new ArrayList<Map<String, String>>();
        
        for (int i = 0; i < mSamplesTitle.length; i++)
        {
            Map<String, String> sample = new HashMap<String, String>();
            sample.put("title", mSamplesTitle[i]);
            sample.put("id", String.valueOf(mSamplesId[i]));
            myData.add(sample);
        }
        return myData;
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        Map<String, String> data = (Map<String, String>)l.getItemAtPosition(position);
        
        Intent intent = new Intent(this, SkiaDemoActivity.class);
        intent.putExtra("id", data.get("id"));
        startActivity(intent);
    }
}