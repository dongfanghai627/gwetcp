package dh.gw.httpserver.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import dh.gw.httpserver.R;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "DH";
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.lunchHttpServer);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"click B1");
                HttpServerActivity.lunchActivity(MainActivity.this);
            }
        });
    }
}
