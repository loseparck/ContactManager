package com.esisa.contactmanager;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import com.esisa.contactmanager.business.DataHandler;

import java.util.HashMap;
import java.util.List;


public class MainActivity extends ListActivity{
    private ListView lv;
    private List<HashMap<String, String>> fillMaps;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lv = getListView();
        DataHandler handler = new DataHandler(getApplicationContext());
        fillMaps = handler.loadFromPhone();
        String[] from = new String[] { "Image", "Nom", "Emails", "Numéros" };
        int[] to = new int[] { R.id.imageView, R.id.textView, R.id.textView2, R.id.textView3 };
        adapter = new SimpleAdapter(this,fillMaps,R.layout.items, from, to);
        lv.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("Filtre", newText);
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // search action
                return true;
            case R.id.action_export:
                // export action
                return true;
            case R.id.action_import:
                // import action

                return true;
            case R.id.action_add:
                // add contact action
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
