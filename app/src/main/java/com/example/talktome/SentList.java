package com.example.talktome;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;

public class SentList extends AppCompatActivity {
    private ListView sentEmails;
    private TextToSpeech mTTS;
    private String speechText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_list);

        sentEmails = findViewById(R.id.sentMails);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.UK);

                    //I skipped the log in part.
                    ttsInitialized();

                    if (result == TextToSpeech.LANG_MISSING_DATA|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        /*
        Email readEmail = new Email("","","","");

        ObjectMapper mapper = new ObjectMapper();
        try {
            SharedPreferences mPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
            String json = mPrefs.getString("email", "");
            Toast.makeText(getApplicationContext(), json, Toast.LENGTH_LONG).show();
            readEmail = mapper.readValue(json, Email.class);
            Toast.makeText(getApplicationContext(), readEmail.getContent(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        */

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        String host = "pop.gmail.com";// change accordingly
        String mailStoreType = "pop3";
        String username = "emailclientproject1@gmail.com";// change accordingly
        String password = "computerproject1";// change accordingly

        GetEmails getEmails = new GetEmails();

        Map<String, String> datum = new HashMap<String, String>(2);
        try{
            Message[] messages = getEmails.doit();

            datum.put("First Line", messages[0].getSubject());
            datum.put("Second Line",messages[0].getContent().toString());
            Toast.makeText(getApplicationContext(), messages[0].getSubject(), Toast.LENGTH_LONG).show();
            data.add(datum);
        }
        catch (MessagingException m){
            Toast.makeText(getApplicationContext(), "YAKUP " + m.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (IOException i){
            Toast.makeText(getApplicationContext(), "MEDINA" + i.getMessage(), Toast.LENGTH_LONG).show();
        }

        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[] {"First Line", "Second Line" },
                new int[] {android.R.id.text1, android.R.id.text2 });

        sentEmails.setAdapter(adapter);
    }

    private void ttsInitialized(){
        mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {}
            @Override
            public void onDone(String s) {listen(); }
            @Override
            public void onError(String s) {}
        });

        speechText = "Sent emails list is opened, say email and a number to read the email with that order and say go to go back";

        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID2");

        mTTS.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "messageID2");
    }

    private void listen(){
        //intent to show speech to text dialog
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        //start intent
        try{
            //at this block we do not have an error
            startActivityForResult(intent, 1000);
        }
        catch (Exception e){
            //we get the message error if it was one
            Toast.makeText(this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1000:{
                //get text array from voice intent
                if (resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    switch (speechText) {
                        case "Sent emails list is opened, say email and a number to read the email with that order. Or say back to go back":
                            if(result.get(0).equals("back")){
                                Intent intentToMain = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intentToMain);
                            }
                            else {
                                mTTS.speak("Now reading " + result.get(0), TextToSpeech.QUEUE_FLUSH, null, "messageID2");
                                break;
                            }
                    }
                }
                break;
            }
        }
    }

}
