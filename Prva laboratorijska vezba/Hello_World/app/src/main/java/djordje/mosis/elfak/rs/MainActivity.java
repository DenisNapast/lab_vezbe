package djordje.mosis.elfak.rs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button  = (Button) findViewById(R.id.finish_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                finish();
            }


        });
    }


     @Override
    public void onDestroy()
     {
         Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
         super.onDestroy();
     }

    @Override
    public void onRestart()
    {
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
        super.onRestart();
    }

    @Override
    public void onResume()
    {
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        super.onResume();
    }

    @Override
    public void onPause()
    {
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override
    public void onStop()
    {
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        super.onStop();
    }




}
