package com.example.myplaces;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewMyPlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_place);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        int position = -1;

        try{
            Intent listIntent = getIntent();
            Bundle positionBundle = listIntent.getExtras();
            position = positionBundle.getInt("position");
        }catch(Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            finish();
        }

        if(position>=0)
        {
            MyPlaces places = MyPlacesData.getInstance().getIndex(position);
            TextView twName = (TextView)findViewById(R.id.viewmyplace_name_text);
            twName.setText(places.getName());
            TextView twDesc = (TextView)findViewById(R.id.viewmyplace_desc_text);
            twDesc.setText(places.getDesc());

        }

        final Button finishedButton = (Button)findViewById(R.id.viewmyplace_finish_button);
        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_my_place, menu);
        return true;
    }



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

        else if(id == R.id.my_place_list_item)
        {
            Intent i = new Intent(this, MyPlaceList.class);
            startActivity(i);

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

}
