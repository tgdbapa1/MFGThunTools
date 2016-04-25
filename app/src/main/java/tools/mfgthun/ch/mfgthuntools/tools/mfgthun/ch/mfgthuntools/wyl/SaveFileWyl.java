package tools.mfgthun.ch.mfgthuntools.tools.mfgthun.ch.mfgthuntools.wyl;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import tools.mfgthun.ch.mfgthuntools.R;


public class SaveFileWyl extends ActionBarActivity {

    Button save, cancel;
    EditText fileName;
    String fileNameText;

    final String searchPattern = "WYL_";
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_file_wyl);

        getSupportActionBar().hide();

        cancel = (Button)findViewById(R.id.button_cancel);
        save = (Button) findViewById(R.id.button_save);

        Intent sender = getIntent();
        final String pilot_weight = sender.getExtras().getString("pilot_weight");
        final String passenger_weight = sender.getExtras().getString("passenger_weight");
        final String baggageA_weight = sender.getExtras().getString("baggageA");
        final String baggageB_weight = sender.getExtras().getString("baggageB");
        final String wingLockers_weight = sender.getExtras().getString("wing_lockers");
        final String fuel = sender.getExtras().getString("fuel");
        final int resultSave = sender.getExtras().getInt("result");
        final int resultOpen = sender.getExtras().getInt("result");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {

                fileName = (EditText) findViewById(R.id.fileName);
                fileNameText = searchPattern + fileName.getText().toString();
                Intent sender = getIntent();

                if (fileName.getText().length() != 0) {

                    try {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                        String currentDateAndTime = sdf.format(new Date());

                        JSONObject obj = new JSONObject();
                        obj.put("pilot_weight", pilot_weight);
                        obj.put("passenger_weight", passenger_weight);
                        obj.put("baggageA_weight", baggageA_weight);
                        obj.put("baggageB_weight", baggageB_weight);
                        obj.put("wingLockers_weight", wingLockers_weight);
                        obj.put("fuel", fuel);
                        obj.put("dateTime", currentDateAndTime);


                        try {

                            File saveFile = new File(context.getFilesDir() + "/" + fileNameText);

                            if (!saveFile.exists()) {
                                saveFile.createNewFile();
                            }

                            String jsonString = obj.toString();

                            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile, true));
                            writer.write(jsonString);
                            writer.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {

                            String dateTime_saved = "Saved at: " + currentDateAndTime;
                            Intent responseOpen = new Intent();
                            responseOpen.putExtra("dateTime_saved", dateTime_saved);
                            setResult(resultOpen, responseOpen);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    setResult(resultSave);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter a filename!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}

