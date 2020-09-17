package com.example.movevehicle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

//I added a line here by Github
//I added this line by Android Studio

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) by style.xml.
 */
public class FullscreenActivity extends AppCompatActivity {

    private static final String TAG = "FullscreenActivity";
    private ImageButton imageButton_forward,imageButton_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        imageButton_forward=findViewById(R.id.forward);
        imageButton_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Forward");
            }
        });

        imageButton_backward=findViewById(R.id.backward);
        imageButton_backward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        Log.d(TAG, "onTouch: backward");
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "onTouch: backwarding");
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "onTouch: Up");
                }
                return true;
            }
        });



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }
}
