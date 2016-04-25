package tools.mfgthun.ch.mfgthuntools.tools.mfgthun.ch.mfgthuntools.wyl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import java.text.DecimalFormat;
import java.util.Arrays;

import tools.mfgthun.ch.mfgthuntools.R;

import static android.graphics.Color.argb;


public class WeightBalanceWyl extends ActionBarActivity {

    TextView basicEmptyWeight;
    TextView basicEmptyMoment;
    TextView pilotMoment;
    EditText pilotWeight;
    TextView passengerMoment;
    EditText passengerWeight;
    TextView baggageAMoment;
    EditText baggageAWeight;
    TextView baggageBMoment;
    EditText baggageBWeight;
    TextView wingLockersMoment;
    EditText wingLockersWeight;
    TextView fuelMoment;
    TextView fuelWeight;
    EditText fuelLiter;
    TextView takeOffWeight;
    TextView takeOffArm;
    TextView takeOffMoment;
    TextView savedTime;

    Button fileSave;
    String fileName;
    final Context context = this;

    private static final double FUEL_TO_WEIGHT_FACTOR = 0.72;
    private static final double MAX_TAKEOFF_WEIGHT = 600.00;
    private static final int MAX_WING_LOCKERS_WEIGHT = 40;
    private static final int MAX_BAGGAGE_WEIGHT = 18;
    private static final int MAX_CG = 540;
    private static final int MIN_CG = 405;
    private static final int DARK_GREEN = argb(255, 7, 149, 32);
    private static final int RED = argb(255, 255, 0, 30);
    private static final int BLACK = argb(255,0,0,0);

    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wb_wyl);

        // handle input to the fields
        pilotWeight = (EditText) findViewById(R.id.pilot_weight_wyl);
        pilotWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (pilotWeight.length() == 0) {
                    pilotMoment = (TextView) findViewById(R.id.pilot_moment_wyl);
                    pilotMoment.setText("0");
                    pilotWeight.setText("0");
                } else {
                    calcMoment(R.id.pilot_weight_wyl, R.id.pilot_arm_wyl, R.id.pilot_moment_wyl);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        passengerWeight = (EditText) findViewById(R.id.passenger_weight_wyl);
        passengerWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (passengerWeight.length() == 0) {
                    passengerMoment = (TextView) findViewById(R.id.passenger_moment_wyl);
                    passengerMoment.setText("0");
                } else {
                    calcMoment(R.id.passenger_weight_wyl, R.id.passenger_arm_wyl, R.id.passenger_moment_wyl);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        baggageAWeight= (EditText) findViewById(R.id.baggageA_weight_wyl);
        baggageAWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (baggageAWeight.length() == 0) {
                    baggageAMoment = (TextView) findViewById(R.id.baggageA_moment_wyl);
                    baggageAMoment.setText("0");
                } else {
                    calcMoment(R.id.baggageA_weight_wyl, R.id.baggageA_arm_wyl, R.id.baggageA_moment_wyl);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        baggageBWeight= (EditText) findViewById(R.id.baggageB_weight_wyl);
        baggageBWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (baggageBWeight.length() == 0) {
                    baggageBMoment = (TextView) findViewById(R.id.baggageB_moment_wyl);
                    baggageBMoment.setText("0");
                } else {
                    calcMoment(R.id.baggageB_weight_wyl, R.id.baggageB_arm_wyl, R.id.baggageB_moment_wyl);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        wingLockersWeight= (EditText) findViewById(R.id.wingLockers_weight_wyl);
        wingLockersWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (wingLockersWeight.length() == 0) {
                    wingLockersMoment = (TextView) findViewById(R.id.wingLockers_moment_wyl);
                    wingLockersMoment.setText("0");
                } else {
                    calcMoment(R.id.wingLockers_weight_wyl, R.id.wingLockers_arm_wyl, R.id.wingLockers_moment_wyl);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        fuelLiter= (EditText) findViewById(R.id.fuel_liter_wyl);
        fuelLiter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (fuelLiter.length() == 0) {
                    fuelMoment = (TextView) findViewById(R.id.fuel_moment_wyl);
                    fuelMoment.setText("0");
                    fuelWeight = (TextView) findViewById(R.id.fuel_weight_wyl);
                    fuelWeight.setText("0");
                } else {
                    calcMomentFuel(R.id.fuel_liter_wyl, R.id.fuel_weight_wyl, R.id.fuel_arm_wyl, R.id.fuel_moment_wyl);
                    calcTotal();
                    checkWarnings();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // initialize values for plot
        double weight = 0;
        int cg = 0;

        formatPlot();
        drawBasicPlot();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_wb_wyl, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save_wyl:
                Intent intentSave = new Intent(this, SaveFileWyl.class);
                intentSave.putExtra("pilot_weight", pilotWeight.getText().toString());
                intentSave.putExtra("passenger_weight", passengerWeight.getText().toString());
                intentSave.putExtra("baggageA", baggageAWeight.getText().toString());
                intentSave.putExtra("baggageB", baggageBWeight.getText().toString());
                intentSave.putExtra("wing_lockers", wingLockersWeight.getText().toString());
                intentSave.putExtra("fuel", fuelLiter.getText().toString());
                intentSave.putExtra("result", 200);
                final int resultSave = 200;
                this.startActivityForResult(intentSave, resultSave);
                return true;
            case R.id.action_open_wyl:
                Intent intentOpen = new Intent(this, OpenFileWyl.class);
                intentOpen.putExtra("result", 100);
                final int resultOpen = 100;
                this.startActivityForResult(intentOpen, resultOpen);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            String pilot_weight = data.getStringExtra("pilot_weight");
            String passenger_weight = data.getStringExtra("passenger_weight");
            String baggageA_weight = data.getStringExtra("baggageA_weight");
            String baggageB_weight = data.getStringExtra("baggageB_weight");
            String wing_lockers_weight = data.getStringExtra("wing_lockers_weight");
            String fuel = data.getStringExtra("fuel");
            String dateTime = data.getStringExtra("dateTime_saved");

            pilotWeight = (EditText) findViewById(R.id.pilot_weight_wyl);
            passengerWeight = (EditText) findViewById(R.id.passenger_weight_wyl);
            baggageAWeight = (EditText) findViewById(R.id.baggageA_weight_wyl);
            baggageBWeight = (EditText) findViewById(R.id.baggageB_weight_wyl);
            wingLockersWeight = (EditText) findViewById(R.id.wingLockers_weight_wyl);
            fuelLiter = (EditText) findViewById(R.id.fuel_liter_wyl);
            savedTime = (TextView) findViewById(R.id.saved_time);

            pilotWeight.setText(pilot_weight);
            passengerWeight.setText(passenger_weight);
            baggageAWeight.setText(baggageA_weight);
            baggageBWeight.setText(baggageB_weight);
            wingLockersWeight.setText(wing_lockers_weight);
            fuelLiter.setText(fuel);
            savedTime.setText(dateTime);

        } else if (resultCode == 200) {
            String dateTime = data.getStringExtra("dateTime_saved");
            savedTime = (TextView) findViewById(R.id.saved_time);
            savedTime.setText(dateTime);
        }
    }


    public void calcMoment (int weightId, int armId, int momentId) {

        EditText weight;
        TextView arm;
        TextView moment;

        weight = (EditText) findViewById(weightId);
        if ( weight.length() > 0) {
            arm = (TextView) findViewById(armId);
            int armInt = Integer.parseInt(arm.getText().toString());
            int weightInt = Integer.parseInt(weight.getText().toString());

            int momentInt = armInt * weightInt;

            moment = (TextView) findViewById(momentId);
            moment.setText(new Integer(momentInt).toString());

        }

    }

    public void calcMomentFuel (int literId, int weightId, int armId, int momentId) {

        EditText liter;
        TextView weight;
        TextView arm;
        TextView moment;

        liter = (EditText) findViewById(literId);
        if (liter.length() > 0) {
            int literInt = Integer.parseInt(liter.getText().toString());
            double weightDouble = literInt * FUEL_TO_WEIGHT_FACTOR;

            // round to two decimal places
            weightDouble = Math.round(weightDouble * 100) ;
            weightDouble = weightDouble / 100;

            weight = (TextView) findViewById(weightId);
            weight.setText(Double.toString(weightDouble));

            arm = (TextView) findViewById(armId);
            int armInt = Integer.parseInt(arm.getText().toString());
            double momentDouble = weightDouble * armInt;
            double momentFinal = Math.round(momentDouble * 100.0)/100.0;
            //int momentInt = (int) momentDouble;

            moment = (TextView) findViewById(momentId);
            moment.setText(Double.toString(momentFinal));
        }

    }

    public void calcTotal (){

        int pilotWeightInt;
        int passengerWeightInt;
        int baggageAWeightInt;
        int baggageBWeightInt;
        int wingLockersWeightInt;
        double fuelWeightInt;

        basicEmptyWeight = (TextView) findViewById(R.id.bew_weight_wyl);
        pilotWeight = (EditText) findViewById(R.id.pilot_weight_wyl);
        passengerWeight = (EditText) findViewById(R.id.passenger_weight_wyl);
        baggageAWeight = (EditText) findViewById(R.id.baggageA_weight_wyl);
        baggageBWeight = (EditText) findViewById(R.id.baggageB_weight_wyl);
        wingLockersWeight = (EditText) findViewById(R.id.wingLockers_weight_wyl);
        fuelWeight = (TextView) findViewById(R.id.fuel_weight_wyl);

        if (pilotWeight.length() > 0) {
            pilotWeightInt = Integer.parseInt(pilotWeight.getText().toString());
        } else {
            pilotWeightInt = 0;
        }

        if (passengerWeight.length() > 0) {
            passengerWeightInt = Integer.parseInt(passengerWeight.getText().toString());
        } else {
            passengerWeightInt = 0;
        }

        if (baggageAWeight.length() > 0) {
            baggageAWeightInt = Integer.parseInt(baggageAWeight.getText().toString());
        } else {
            baggageAWeightInt = 0;
        }

        if (baggageBWeight.length() > 0) {
            baggageBWeightInt = Integer.parseInt(baggageBWeight.getText().toString());
        } else {
            baggageBWeightInt = 0;
        }

        if (wingLockersWeight.length() > 0) {
            wingLockersWeightInt = Integer.parseInt(wingLockersWeight.getText().toString());
        } else {
            wingLockersWeightInt = 0;
        }

        if (fuelWeight.length() > 0) {
            fuelWeightInt = Double.parseDouble(fuelWeight.getText().toString());
        } else {
            fuelWeightInt = 0;
        }

        double weight = Double.parseDouble(basicEmptyWeight.getText().toString()) +
                pilotWeightInt + passengerWeightInt + baggageAWeightInt +
                baggageBWeightInt + wingLockersWeightInt + fuelWeightInt;

        // round to two decimal places
        weight = Math.round(weight * 100);
        weight = weight / 100;

        basicEmptyMoment = (TextView) findViewById(R.id.bew_moment_wyl);
        pilotMoment = (TextView) findViewById(R.id.pilot_moment_wyl);
        passengerMoment = (TextView) findViewById(R.id.passenger_moment_wyl);
        baggageAMoment = (TextView) findViewById(R.id.baggageA_moment_wyl);
        baggageBMoment = (TextView) findViewById(R.id.baggageB_moment_wyl);
        wingLockersMoment = (TextView) findViewById(R.id.wingLockers_moment_wyl);
        fuelMoment = (TextView) findViewById(R.id.fuel_moment_wyl);

        double moment = Double.parseDouble(basicEmptyMoment.getText().toString()) +
                Double.parseDouble(pilotMoment.getText().toString()) +
                Double.parseDouble(passengerMoment.getText().toString()) +
                Double.parseDouble(baggageAMoment.getText().toString()) +
                Double.parseDouble(baggageBMoment.getText().toString()) +
                Double.parseDouble(wingLockersMoment.getText().toString()) +
                Double.parseDouble(fuelMoment.getText().toString());

        int arm = (int) moment / (int) weight;

        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_wyl);
        if (weight <= MAX_TAKEOFF_WEIGHT) {
            takeOffWeight.setTextColor(DARK_GREEN);
        } else {
            takeOffWeight.setTextColor(RED);
        }
        takeOffWeight.setText(Double.toString(weight));


        takeOffArm = (TextView) findViewById(R.id.takeOffWeight_arm_wyl);
        takeOffArm.setText(Integer.toString(arm));
        if (arm <= MAX_CG && arm >= MIN_CG) {
            takeOffArm.setTextColor(DARK_GREEN);
        } else {
            takeOffArm.setTextColor(RED);
        }

        takeOffMoment = (TextView) findViewById(R.id.takeOffWeight_moment_wyl);
        takeOffMoment.setText(Double.toString(moment));

        drawLinesPlot();

    }

    public void checkWarnings() {

        int baggageA;
        int baggageB;
        int wingLockers;
        int cg;
        double takeOff;
        boolean isBaggage = false;
        boolean isWingLockers = false;
        boolean isTakeoff = false;
        boolean isCG = false;
        boolean activeWarning;
        String warningMessage = "";
        TextView warnings;


        baggageAWeight = (EditText) findViewById(R.id.baggageA_weight_wyl);
        baggageBWeight = (EditText) findViewById(R.id.baggageB_weight_wyl);
        wingLockersWeight = (EditText) findViewById(R.id.wingLockers_weight_wyl);
        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_wyl);
        takeOffArm = (TextView) findViewById(R.id.takeOffWeight_arm_wyl);

        if (baggageAWeight.length() > 0) {
            baggageA = Integer.parseInt(baggageAWeight.getText().toString());
        } else {
            baggageA = 0;
        }

        if (baggageBWeight.length() > 0) {
            baggageB = Integer.parseInt(baggageBWeight.getText().toString());
        } else {
            baggageB = 0;
        }
        if ((baggageA + baggageB) > MAX_BAGGAGE_WEIGHT) {
            isBaggage = true;
            baggageAWeight.setTextColor(RED);
            baggageBWeight.setTextColor(RED);
        } else {
            baggageAWeight.setTextColor(BLACK);
            baggageBWeight.setTextColor(BLACK);
        }

        if (wingLockersWeight.length() > 0) {
            wingLockers = Integer.parseInt(wingLockersWeight.getText().toString());
        } else {
            wingLockers = 0;
        }
        if (wingLockers > MAX_WING_LOCKERS_WEIGHT) {
            isWingLockers = true;
            wingLockersWeight.setTextColor(RED);
        } else {
            wingLockersWeight.setTextColor(BLACK);
        }

        if (takeOffWeight.length() > 0) {
            takeOff = Double.parseDouble(takeOffWeight.getText().toString());
        } else {
            takeOff = 0;
        }
        if (takeOff > MAX_TAKEOFF_WEIGHT) {
            isTakeoff = true;
        }

        if (takeOffArm.length() > 0) {
            cg = Integer.parseInt(takeOffArm.getText().toString());
        } else {
            cg = 0;
        }

        if (cg > MAX_CG || cg < MIN_CG) {
            isCG = true;
        }

        activeWarning = isBaggage || isWingLockers || isTakeoff || isCG;

        if (! activeWarning) {
            warningMessage = "none";
        }

        while (activeWarning) {

            if (isTakeoff) {
                warningMessage = getResources().getString(R.string.WarningTakeOff_wyl);
                isTakeoff = false;
            }
            if (isBaggage) {
                warningMessage = warningMessage + getString(R.string.WarningBaggage_wyl);
                isBaggage = false;
            }
            if (isWingLockers) {
                warningMessage = warningMessage + getString(R.string.WarningWingLockers_wyl);
                isWingLockers = false;
            }
            if (isCG) {
                warningMessage = warningMessage + getString(R.string.WarningCG_wyl);
                isCG = false;
            }

            //warningMessage = warningMessage + "\n";

            activeWarning = isBaggage || isWingLockers || isTakeoff || isCG;

        }

        warnings = (TextView) findViewById(R.id.warnings_wyl);
        if (warningMessage.equals("none")) {
            warnings.setTextColor(DARK_GREEN);
            warnings.setText(warningMessage);
        } else {
            warnings.setTextColor(RED);
            warnings.setText(warningMessage);
        }
    }

    public void formatPlot() {
        //initialize plot
        plot = (XYPlot) findViewById(R.id.plot_wyl);

        // disable legend
        plot.getLayoutManager().remove(plot.getLegendWidget());

        // format x values
        plot.setDomainStep(XYStepMode.INCREMENT_BY_PIXELS.INCREMENT_BY_VAL, 15);
        plot.setDomainValueFormat(new DecimalFormat("#"));
        plot.setDomainBoundaries(390, 555, BoundaryMode.FIXED);
        plot.getGraphWidget().getDomainLabelPaint().setTextSize(20);
        plot.getGraphWidget().getDomainLabelPaint().setColor(Color.WHITE);
        plot.getGraphWidget().setDomainLabelOrientation(-90);
        plot.getGraphWidget().getDomainLabelPaint().setTextAlign(Paint.Align.CENTER);
        plot.setDomainLabel("C.G. LOCATION (mm)");
        plot.getDomainLabelWidget().position(0, XLayoutStyle.ABSOLUTE_FROM_CENTER, 0, YLayoutStyle.RELATIVE_TO_BOTTOM,  AnchorPosition.BOTTOM_MIDDLE);
        plot.getDomainLabelWidget().getLabelPaint().setTextSize(20);
        plot.getDomainLabelWidget().setWidth(300);
        plot.getDomainLabelWidget().setHeight(25);
        plot.getDomainLabelWidget().setPaddingBottom(5);

        // format y values
        plot.setRangeStep(XYStepMode.INCREMENT_BY_PIXELS.INCREMENT_BY_VAL, 50);
        plot.setRangeValueFormat(new DecimalFormat("#"));
        plot.setRangeBoundaries(350, 650, BoundaryMode.FIXED);
        plot.getGraphWidget().getRangeLabelPaint().setTextSize(20);
        plot.getGraphWidget().getRangeLabelPaint().setColor(Color.WHITE);
        plot.setRangeLabel("AIRPLANE WEIGHT (kg)");
        plot.getRangeLabelWidget().getLabelPaint().setTextSize(20);
        plot.getRangeLabelWidget().setPaddingLeft(5);
        plot.getRangeLabelWidget().setWidth(25);
        plot.getRangeLabelWidget().setHeight(500);

    }

    public void drawBasicPlot () {

        // initialize plot
        plot = (XYPlot) findViewById(R.id.plot_wyl);

        // define basic values for envelope
        Number[] weightValues = {350, 350, 600, 600, 350,350};
        Number[] cgValues = {395, 405, 405, 540, 540, 555};

        // turn arrays into XYSeries
        XYSeries basicEnvelope = new SimpleXYSeries(
                Arrays.asList(cgValues),
                Arrays.asList(weightValues),
                "Envelope");

        // create a formatter to use for drawing
        LineAndPointFormatter basicFormatter = new LineAndPointFormatter(
                Color.BLACK,                    // line color
                null,                           // point color
                Color.rgb(250,250,190),         // fill color
                null);                          // text color


        // add new XYSeries to XYPlot
        plot.addSeries(basicEnvelope, basicFormatter);
    }

    public void drawLinesPlot () {
        // initialize plot
        plot = (XYPlot) findViewById(R.id.plot_wyl);

        // clear and redraw basic plot
        plot.clear();
        drawBasicPlot();

        // define weight line values
        int weight = getWeight();
        Number[] weightValues1 = {weight, weight};
        Number[] cgValues1 = {390,555};

        // define cg line values
        int cg = getCG();
        Number[] weightValues2 = {350,650};
        Number[] cgValues2 = {cg, cg};

        // define point value
        Number[] pointWeightValue = {weight};
        Number[] pointCGValue = {cg};

        // turn arrays into XYSeries
        XYSeries weightLine = new SimpleXYSeries(
                Arrays.asList(cgValues1),
                Arrays.asList(weightValues1),
                "Weight");

        XYSeries cgLine = new SimpleXYSeries(
                Arrays.asList(cgValues2),
                Arrays.asList(weightValues2),
                "CG");

        XYSeries point = new SimpleXYSeries(
                Arrays.asList(pointCGValue),
                Arrays.asList(pointWeightValue),
                "Point");

        // create a formatter to use for drawing
        LineAndPointFormatter LineFormatter = new LineAndPointFormatter(
                Color.RED,                      // line color
                null,                           // point color
                null,                           // fill color
                null);                          // text color

        LineAndPointFormatter PointFormatter = new LineAndPointFormatter(
                null,
                Color.BLACK,
                null,
                null);
        PointFormatter.getVertexPaint().setStrokeWidth(PixelUtils.dpToPix(10));

        // add new XYSeries to XYPlot
        plot.addSeries(weightLine, LineFormatter);
        plot.addSeries(cgLine, LineFormatter);
        plot.addSeries(point, PointFormatter);

        plot.redraw();

    }

    public int getWeight(){
        int intValue = 0;
        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_wyl);
        if (takeOffWeight.length() > 0) {
            intValue = (int) Double.parseDouble(takeOffWeight.getText().toString());
        }
        return intValue;
    }

    public int getCG() {
        int intValue = 0;
        takeOffArm = (TextView) findViewById(R.id.takeOffWeight_arm_wyl);
        if (takeOffArm.length() > 0) {
            intValue = Integer.parseInt(takeOffArm.getText().toString());
        }
        return intValue;
    }

}
