package tools.mfgthun.ch.mfgthuntools.tools.mfgthun.ch.mfgthuntools.pmk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import tools.mfgthun.ch.mfgthuntools.R;


public class OpenFilePmk extends ActionBarActivity {

    Button open;
    Button cancel;
    Button delete;
    String fileName;
    String jsonString;

    ListView listSavedFiles;
    List<String> savedFiles = new ArrayList<String>();
    String[] fileList;
    String targetFile;
    final String searchPattern = "PMK_";

    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_file_pmk);

        getSupportActionBar().hide();

        open = (Button) findViewById(R.id.button_open);
        cancel = (Button) findViewById(R.id.button_cancel);
        delete = (Button) findViewById(R.id.button_delete);
        Intent sender = getIntent();
        final int resultOpen = sender.getExtras().getInt("result");

        listSavedFiles = (ListView) findViewById(R.id.listFiles);
        listSavedFiles.setEmptyView(findViewById(R.id.empty_listFiles));

        fileList = getApplicationContext().fileList();

        for (int i = 0 ; i < fileList.length; i++ ) {
            if (! fileList[i].isEmpty()) {
                targetFile = fileList[i];
                if (targetFile.startsWith(searchPattern)) {
                    savedFiles.add(new String(targetFile.substring(4)));
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                savedFiles
        );
        listSavedFiles.setAdapter(adapter);

        listSavedFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fileName = (String) (listSavedFiles.getItemAtPosition(position));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
                File deleteFile = new File(context.getFilesDir() + "/" + searchPattern + fileName);
                deleteFile.delete();
                finish();
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File openFile = new File(context.getFilesDir() + "/" + searchPattern + fileName);

                int length = (int) openFile.length();
                byte[] bytes = new byte[length];

                try {

                    FileInputStream in = new FileInputStream(openFile);
                    try {
                        in.read(bytes);
                    } finally {
                        in.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                jsonString = new String(bytes);

                try {
                    JSONObject obj = new JSONObject(jsonString);
                    String pilot_weight_open = obj.getString("pilot_weight");
                    String passenger_weight_open = obj.getString("passenger_weight");
                    String baggageA_weight_open = obj.getString("baggageA_weight");
                    String fuel_open = obj.getString("fuel");
                    String dateTime_saved = "Saved at: " + obj.getString("dateTime");

                    Intent responseOpen = new Intent();
                    responseOpen.putExtra("pilot_weight", pilot_weight_open);
                    responseOpen.putExtra("passenger_weight", passenger_weight_open);
                    responseOpen.putExtra("baggageA_weight", baggageA_weight_open);
                    responseOpen.putExtra("fuel", fuel_open);
                    responseOpen.putExtra("dateTime_saved", dateTime_saved);
                    setResult(resultOpen, responseOpen);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
