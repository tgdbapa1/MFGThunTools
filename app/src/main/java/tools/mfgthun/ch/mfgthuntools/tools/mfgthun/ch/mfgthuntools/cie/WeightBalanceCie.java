package tools.mfgthun.ch.mfgthuntools.tools.mfgthun.ch.mfgthuntools.cie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

import tools.mfgthun.ch.mfgthuntools.R;
import tools.mfgthun.ch.mfgthuntools.tools.mfgthun.ch.mfgthuntools.cie.OpenFileCie;
import tools.mfgthun.ch.mfgthuntools.tools.mfgthun.ch.mfgthuntools.cie.SaveFileCie;

import static android.graphics.Color.argb;
import static java.lang.Math.round;


public class WeightBalanceCie extends ActionBarActivity {

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
    TextView fuelMoment;
    TextView fuelWeight;
    EditText fuelLiter;
    TextView takeOffWeight;
    TextView takeOffArm;
    TextView takeOffMoment;

    Button fileSave;
    String fileName;
    final Context context = this;

    private static final double FUEL_TO_WEIGHT_FACTOR = 0.72;
    private static final double MAX_TAKEOFF_WEIGHT = 1089.00;
    private static final int MAX_BAGGAGE_WEIGHT = 54;
    private static final int MAX_BAGGAGE_B_WEIGHT = 23;
    private static final int DARK_GREEN = argb(255, 7, 149, 32);
    private static final int RED = argb(255, 255, 0, 30);
    private static final int BLACK = argb(255,0,0,0);
    private static final double FUEL_ARM = 1.22;
    private static final double PILOT_ARM = 0.94;
    private static final double PASSENGER_ARM = 1.85;
    private static final double BAGGAGE_A_ARM = 2.41;
    private static final double BAGGAGE_B_ARM = 3.12;

    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wb_cie);

        // handle input to the fields
        pilotWeight = (EditText) findViewById(R.id.pilot_weight_cie);
        pilotWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (pilotWeight.length() == 0) {
                    pilotMoment = (TextView) findViewById(R.id.pilot_moment_cie);
                    pilotMoment.setText("0");
                    pilotWeight.setText("0");
                } else {
                    calcMoment(R.id.pilot_weight_cie, PILOT_ARM, R.id.pilot_moment_cie);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        passengerWeight = (EditText) findViewById(R.id.passenger_weight_cie);
        passengerWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (passengerWeight.length() == 0) {
                    passengerMoment = (TextView) findViewById(R.id.passenger_moment_cie);
                    passengerMoment.setText("0");
                } else {
                    calcMoment(R.id.passenger_weight_cie, PASSENGER_ARM, R.id.passenger_moment_cie);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        baggageAWeight= (EditText) findViewById(R.id.baggageA_weight_cie);
        baggageAWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (baggageAWeight.length() == 0) {
                    baggageAMoment = (TextView) findViewById(R.id.baggageA_moment_cie);
                    baggageAMoment.setText("0");
                } else {
                    calcMoment(R.id.baggageA_weight_cie, BAGGAGE_A_ARM, R.id.baggageA_moment_cie);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        baggageBWeight= (EditText) findViewById(R.id.baggageB_weight_cie);
        baggageBWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (baggageBWeight.length() == 0) {
                    baggageBMoment = (TextView) findViewById(R.id.baggageB_moment_cie);
                    baggageBMoment.setText("0");
                } else {
                    calcMoment(R.id.baggageB_weight_cie, BAGGAGE_B_ARM, R.id.baggageB_moment_cie);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        fuelLiter = (EditText) findViewById(R.id.fuel_liter_cie);
        fuelLiter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (fuelLiter.length() == 0) {
                    fuelMoment = (TextView) findViewById(R.id.fuel_moment_cie);
                    fuelMoment.setText("0");
                    fuelWeight = (TextView) findViewById(R.id.fuel_weight_cie);
                    fuelWeight.setText("0");
                } else {
                    calcMomentFuel(R.id.fuel_liter_cie, R.id.fuel_weight_cie, FUEL_ARM, R.id.fuel_moment_cie);
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
        inflater.inflate(R.menu.menu_wb_cie, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save_cie:
                Intent intentSave = new Intent(this, SaveFileCie.class);
                intentSave.putExtra("pilot_weight", pilotWeight.getText().toString());
                intentSave.putExtra("passenger_weight", passengerWeight.getText().toString());
                intentSave.putExtra("baggageA", baggageAWeight.getText().toString());
                intentSave.putExtra("baggageB", baggageBWeight.getText().toString());
                intentSave.putExtra("fuel", fuelLiter.getText().toString());
                intentSave.putExtra("result", 200);
                final int resultSave = 200;
                this.startActivityForResult(intentSave, resultSave);
                return true;
            case R.id.action_open_cie:
                Intent intentOpen = new Intent(this, OpenFileCie.class);
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
            String fuel = data.getStringExtra("fuel");

            pilotWeight = (EditText) findViewById(R.id.pilot_weight_cie);
            passengerWeight = (EditText) findViewById(R.id.passenger_weight_cie);
            baggageAWeight = (EditText) findViewById(R.id.baggageA_weight_cie);
            baggageBWeight = (EditText) findViewById(R.id.baggageB_weight_cie);
            fuelLiter = (EditText) findViewById(R.id.fuel_liter_cie);

            pilotWeight.setText(pilot_weight);
            passengerWeight.setText(passenger_weight);
            baggageAWeight.setText(baggageA_weight);
            baggageBWeight.setText(baggageB_weight);
            fuelLiter.setText(fuel);

        }
    }


    public void calcMoment (int weightId, double arm, int momentId) {

        EditText weight;
        TextView moment;

        weight = (EditText) findViewById(weightId);
        if ( weight.length() > 0) {

            int weightInt = Integer.parseInt(weight.getText().toString());
            double momentResult = arm * weightInt;

            BigDecimal bd = new BigDecimal(momentResult);
            String rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
            moment = (TextView) findViewById(momentId);
            moment.setText(rounded);

        }

    }

    public void calcMomentFuel (int literId, int weightId, double armDouble, int momentId) {

        EditText liter;
        TextView weight;
        TextView moment;

        liter = (EditText) findViewById(literId);
        if (liter.length() > 0) {
            int literInt = Integer.parseInt(liter.getText().toString());
            double weightDouble = literInt * FUEL_TO_WEIGHT_FACTOR;

            // round to two decimal places
            BigDecimal bd = new BigDecimal(weightDouble);
            String rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();

            weight = (TextView) findViewById(weightId);
            weight.setText(rounded);

            double momentDouble = weightDouble * armDouble;
            int momentInt = (int) momentDouble;

            bd = new BigDecimal(momentDouble);
            rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
            moment = (TextView) findViewById(momentId);
            moment.setText(rounded);
        }

    }

    public void calcTotal (){

        int pilotWeightInt;
        int passengerWeightInt;
        int baggageAWeightInt;
        int baggageBWeightInt;
        double fuelWeightInt;

        basicEmptyWeight = (TextView) findViewById(R.id.bew_weight_cie);
        pilotWeight = (EditText) findViewById(R.id.pilot_weight_cie);
        passengerWeight = (EditText) findViewById(R.id.passenger_weight_cie);
        baggageAWeight = (EditText) findViewById(R.id.baggageA_weight_cie);
        baggageBWeight = (EditText) findViewById(R.id.baggageB_weight_cie);
        fuelWeight = (TextView) findViewById(R.id.fuel_weight_cie);

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


        if (fuelWeight.length() > 0) {
            fuelWeightInt = Double.parseDouble(fuelWeight.getText().toString());
        } else {
            fuelWeightInt = 0;
        }

        double weight = Double.parseDouble(basicEmptyWeight.getText().toString()) +
                pilotWeightInt + passengerWeightInt + baggageAWeightInt +
                baggageBWeightInt + fuelWeightInt;

        // round to two decimal places
        weight = round(weight * 100);
        weight = weight / 100;

        basicEmptyMoment = (TextView) findViewById(R.id.bew_moment_cie);
        pilotMoment = (TextView) findViewById(R.id.pilot_moment_cie);
        passengerMoment = (TextView) findViewById(R.id.passenger_moment_cie);
        baggageAMoment = (TextView) findViewById(R.id.baggageA_moment_cie);
        baggageBMoment = (TextView) findViewById(R.id.baggageB_moment_cie);
        fuelMoment = (TextView) findViewById(R.id.fuel_moment_cie);

        double moment = Double.parseDouble(basicEmptyMoment.getText().toString()) +
                Double.parseDouble(pilotMoment.getText().toString()) +
                Double.parseDouble(passengerMoment.getText().toString()) +
                Double.parseDouble(baggageAMoment.getText().toString()) +
                Double.parseDouble(baggageBMoment.getText().toString()) +
                Double.parseDouble(fuelMoment.getText().toString());

        int arm = (int) moment / (int) weight;

        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_cie);
        if (weight <= MAX_TAKEOFF_WEIGHT) {
            takeOffWeight.setTextColor(DARK_GREEN);
        } else {
            takeOffWeight.setTextColor(RED);
        }

        BigDecimal bd = new BigDecimal(weight);
        String rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
        takeOffWeight.setText(rounded);


        takeOffMoment = (TextView) findViewById(R.id.takeOffWeight_moment_cie);
        bd = new BigDecimal(moment);
        rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
        takeOffMoment.setText(rounded);

        drawLinesPlot();

    }

    // start warning Messages

    public void checkWarnings() {

        int baggageA;
        int baggageB;
        int cg;
        double takeOff;
        boolean isBaggage = false;
        boolean isBaggageA = false;
        boolean isBaggageB = false;
        boolean isTakeoff = false;
        boolean activeWarning;
        String warningMessage = "";
        TextView warnings;


        baggageAWeight = (EditText) findViewById(R.id.baggageA_weight_cie);
        baggageBWeight = (EditText) findViewById(R.id.baggageB_weight_cie);
        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_cie);


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

        if ((baggageA > MAX_BAGGAGE_WEIGHT) && (baggageB == 0)) {
            isBaggageA = true;
            baggageAWeight.setTextColor(RED);
            baggageBWeight.setTextColor(BLACK);
        }

        if ((baggageB > MAX_BAGGAGE_B_WEIGHT) && (baggageA + baggageB) < MAX_BAGGAGE_WEIGHT) {
            isBaggageB = true;
            isBaggage = false;
            baggageBWeight.setTextColor(RED);
        } else if ((baggageB > MAX_BAGGAGE_B_WEIGHT) && (baggageA + baggageB) > MAX_BAGGAGE_WEIGHT){
            isBaggageB = true;
            baggageAWeight.setTextColor(RED);
            baggageBWeight.setTextColor(RED);
        }

        if (takeOffWeight.length() > 0) {
            takeOff = Double.parseDouble(takeOffWeight.getText().toString());
        } else {
            takeOff = 0;
        }
        if (takeOff > MAX_TAKEOFF_WEIGHT) {
            isTakeoff = true;
        }

        activeWarning = isBaggage || isTakeoff || isBaggageA || isBaggageB;

        if (! activeWarning) {
            warningMessage = "none";
        }

        while (activeWarning) {

            if (isTakeoff) {
                warningMessage = getResources().getString(R.string.WarningTakeOff_cie);
                isTakeoff = false;
            }

            if (isBaggageA) {
                warningMessage = warningMessage + getString(R.string.WarningBaggageA_cie);
                isBaggageA = false;
            }

            if (isBaggageB) {
                warningMessage = warningMessage + getString(R.string.WarningBaggageB_cie);
                isBaggageB = false;
            }

            if (isBaggage) {
                warningMessage = warningMessage + getString(R.string.WarningBaggage_cie);
                isBaggage = false;
            }

            warningMessage = warningMessage + "\n";

            activeWarning = isBaggage || isTakeoff ||isBaggageA || isBaggageB;

        }

        warnings = (TextView) findViewById(R.id.warnings_cie);
        if (warningMessage.equals("none")) {
            warnings.setTextColor(DARK_GREEN);
            warnings.setText(warningMessage);
        } else {
            warnings.setTextColor(RED);
            warnings.setText(warningMessage);
        }
    }
    // End Warning Messages


    public void formatPlot() {
        //initialize plot
        plot = (XYPlot) findViewById(R.id.plot_cie);

        // disable legend
        plot.getLayoutManager().remove(plot.getLegendWidget());

        // format x values
        plot.setDomainStep(XYStepMode.INCREMENT_BY_PIXELS.INCREMENT_BY_VAL, 15);
        plot.setDomainValueFormat(new DecimalFormat("#"));
        plot.setDomainBoundaries(885, 1205, BoundaryMode.FIXED);
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
        plot.setRangeBoundaries(600, 1100, BoundaryMode.FIXED);
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
        plot = (XYPlot) findViewById(R.id.plot_cie);

        // define basic values for normal Category
        Number[] weightValues = {600, 880, 1089, 1089, 600};
        Number[] cgValues = {895, 895, 1000, 1200, 1200};

        // turn arrays into XYSeries
        XYSeries basicEnvelope = new SimpleXYSeries(
                Arrays.asList(cgValues),
                Arrays.asList(weightValues),
                "NORMAL CATEGORY");

        // create a formatter to use for drawing (normal Category)
        LineAndPointFormatter basicFormatter = new LineAndPointFormatter(
                Color.BLACK,                    // line color
                null,                           // point color
                Color.rgb(250,250,190),         // fill color
                null);                          // text color

        // define basic values for utility Category
        Number[] weightValuesUC = {950, 950, 600};
        Number[] cgValuesUC = {930, 1025, 1025};

        // turn arrays into XYSeries
        XYSeries ucEnvelope = new SimpleXYSeries(
                Arrays.asList(cgValuesUC),
                Arrays.asList(weightValuesUC),
                "UTILITY CATEGORY");

        // create a formatter to use for drawing (utility Category)
        LineAndPointFormatter ucFormatter = new LineAndPointFormatter(
                Color.BLACK,                    // line color
                null,                           // point color
                Color.rgb(250,250,190),         // fill color
                null);                          // text color

        // add new XYSeries to XYPlot
        plot.addSeries(basicEnvelope, basicFormatter);
        plot.addSeries(ucEnvelope, ucFormatter);
    }

    public void drawLinesPlot () {
        // initialize plot
        plot = (XYPlot) findViewById(R.id.plot_cie);

        // clear and redraw basic plot
        plot.clear();
        drawBasicPlot();

        // define weight line values
        int weight = getWeight();
        Number[] weightValues1 = {weight, weight};
        Number[] cgValues1 = {885,1205};

        // define cg line values
        int cg = getCG();
        Number[] weightValues2 = {600,1100};
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
        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_cie);
        if (takeOffWeight.length() > 0) {
            intValue = (int) Double.parseDouble(takeOffWeight.getText().toString());
        }
        return intValue;
    }

    public int getCG() {
        int intValue = 0;
        double intWeightValue = 0;
        double intMomentValue = 0;

        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_cie);
        if (takeOffWeight.length() > 0) {
            intWeightValue = (double) Double.parseDouble(takeOffWeight.getText().toString());
        }

        takeOffMoment = (TextView) findViewById(R.id.takeOffWeight_moment_cie);
        if (takeOffMoment.length() > 0) {
            intMomentValue = (double) Double.parseDouble(takeOffMoment.getText().toString());
        }

        intValue = (int) ((intMomentValue / intWeightValue) * 1000);
        return intValue;
    }

}
