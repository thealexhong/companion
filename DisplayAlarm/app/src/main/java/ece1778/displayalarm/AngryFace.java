package ece1778.displayalarm;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class AngryFace extends ActionBarActivity {
    ImageView face_a,face_h,face_s;
    ImageView btn_angry, btn_happy, btn_surprised;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angry_face);
        face_a =  (ImageView) findViewById(R.id.im_angry);
        face_h =  (ImageView) findViewById(R.id.im_happy);
        face_s =  (ImageView) findViewById(R.id.im_surprised);

        btn_angry=(ImageView) findViewById(R.id.btn_angry);
        btn_happy=(ImageView) findViewById(R.id.btn_happy);
        btn_surprised=(ImageView) findViewById(R.id.btn_surprise);
        face_a.setVisibility(View.GONE);
        face_h.setVisibility(View.GONE);
        face_s.setVisibility(View.GONE);

        btn_angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                face_a.setVisibility(View.VISIBLE);
                face_h.setVisibility(View.GONE);
                face_s.setVisibility(View.GONE);
               }
        });
        btn_happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                face_a.setVisibility(View.GONE);
                face_s.setVisibility(View.GONE);
                face_h.setVisibility(View.VISIBLE);
            }
        });
        btn_surprised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                face_a.setVisibility(View.GONE);
                face_h.setVisibility(View.GONE);
                face_s.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_angry_face, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
