package tools.mfgthun.ch.mfgthuntools;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tools.mfgthun.ch.mfgthuntools.tools.mfgthun.ch.mfgthuntools.cie.WeightBalanceCie;
import tools.mfgthun.ch.mfgthuntools.tools.mfgthun.ch.mfgthuntools.cit.WeightBalanceCit;
import tools.mfgthun.ch.mfgthuntools.tools.mfgthun.ch.mfgthuntools.pmk.WeightBalancePmk;
import tools.mfgthun.ch.mfgthuntools.tools.mfgthun.ch.mfgthuntools.wyl.WeightBalanceWyl;


public class MainMenu extends ActionBarActivity {


    Button wbWYL;
    Button wbCIE;
    Button wbCIT;
    Button wbPMK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        getSupportActionBar().hide();

        wbWYL = (Button) findViewById(R.id.wb_wyl);

        wbWYL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, WeightBalanceWyl.class));
            }
        });

        wbCIE = (Button) findViewById(R.id.wb_cie);

        wbCIE.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, WeightBalanceCie.class));
            }
        });

        wbCIT = (Button) findViewById(R.id.wb_cit);

        wbCIT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, WeightBalanceCit.class));
            }
        });

        wbPMK = (Button) findViewById(R.id.wb_pmk);

        wbPMK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, WeightBalancePmk.class));
            }
        });
    }

}
