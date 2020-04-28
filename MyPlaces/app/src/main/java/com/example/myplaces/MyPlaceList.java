package com.example.myplaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyPlaceList extends AppCompatActivity {

    ArrayList<String> places;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyPlaceList.this,EditMyPlaceActivity.class);
                startActivityForResult(i, NEW_PLACE);
            }
        });



        ListView myList = (ListView) findViewById(R.id.my_places_list);
        myList.setAdapter(new ArrayAdapter<MyPlaces>(this,android.R.layout.simple_list_item_1, MyPlacesData.getInstance().getMyPlaces()));
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle positionBundle = new Bundle();
                positionBundle.putInt("position", position);
                Intent intent = new Intent ( MyPlaceList.this , ViewMyPlaceActivity.class);
                intent.putExtras(positionBundle);
                startActivity(intent);
            }
        });

        myList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
                MyPlaces place = MyPlacesData.getInstance().getIndex(info.position);
                menu.setHeaderTitle(place.getName());
                menu.add(0,1,1,"View place");
                menu.add(0,2,2,"Edit place");

            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Bundle positionBundle = new Bundle();
        positionBundle.putInt("position", info.position);
        Intent i = null;
        if(item.getItemId() == 1)
        {
            i = new Intent(this, ViewMyPlaceActivity.class);
            i.putExtras(positionBundle);
            startActivity(i);
        }
        else if(item.getItemId() == 2)
        {
            i = new Intent(this, EditMyPlaceActivity.class);
            i.putExtras(positionBundle);
            startActivityForResult(i,1);
        }

        return super.onContextItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_my_places_list, menu);
        return true;
    }


    static int NEW_PLACE = 1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.show_map_item)
        {
            Toast.makeText(this, "Show Map!!!" , Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.new_place_item) {
            Intent i = new Intent(this,EditMyPlaceActivity.class);
            startActivityForResult(i, NEW_PLACE);
        }
        else if (id == R.id.about_item)
        {
            Intent i = new Intent(this, About.class);
            startActivity(i);

        }else if (id == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            ListView myList = (ListView) findViewById(R.id.my_places_list);
            myList.setAdapter(new ArrayAdapter<MyPlaces>(this,android.R.layout.simple_list_item_1, MyPlacesData.getInstance().getMyPlaces()));
        }


    }


}
