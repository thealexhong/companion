package ece1778.voicetotext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;


public class Voice extends Activity implements View.OnClickListener {
    ListView lv;
    static final int check=2;
    private String LOG_TAG = "Voice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        lv=(ListView)findViewById(R.id.lvVoiceReturn);
        Button b=(Button)findViewById(R.id.bVoice);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Alarm!?");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        startActivityForResult(intent, check);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==check && resultCode==RESULT_OK){
            ArrayList<String> results;
            results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.i(LOG_TAG, String.valueOf(results));
            if(results.get(0).equals("Help")||results.get(0).equals("help me")||results.get(0).equals("help")){
                startActivity(new Intent(getApplicationContext(), Alm.class));
            }
            lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
