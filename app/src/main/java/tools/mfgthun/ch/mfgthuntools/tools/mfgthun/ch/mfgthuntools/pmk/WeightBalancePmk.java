package tools.mfgthun.ch.mfgthuntools.tools.mfgthun.ch.mfgthuntools.pmk;


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

import static android.graphics.Color.argb;


public class WeightBalancePmk extends ActionBarActivity {

    TextView basicEmptyWeight;
    TextView basicEmptyMoment;
    TextView pilotMoment;
    EditText pilotWeight;
    TextView pilotWeightText;
    TextView passengerMoment;
    EditText passengerWeight;
    TextView passengerWeightText;
    TextView baggageAMoment;
    EditText baggageAWeight;
    TextView baggageAWeightText;
    EditText baggageBWeight;
    TextView fuelMoment;
    TextView fuelWeight;
    EditText fuelUsg;
    TextView rampWeightText;
    TextView rampMomentText;
    TextView takeOffWeight;
    TextView takeOffMoment;
    TextView savedTime;

    double pilotWeightDouble;
    double passengerWeightDouble;
    double baggageAWeightDouble;
    double baggageBWeightDouble;
    double arm;

    private static final double FUEL_TO_WEIGHT_FACTOR = 6;
    private static final double KG_TO_LBS_FACTOR = 2.204625;
    private static final double MAX_TAKEOFF_WEIGHT = 2550.00;
    private static final double MAX_BAGGAGE_A_WEIGHT = 200.00;
    private static final double MAX_ARM = 93.0;
    private static final int DARK_GREEN = argb(255, 7, 149, 32);
    private static final int RED = argb(255, 255, 0, 30);
    private static final int BLACK = argb(255,0,0,0);
    private static final double FUEL_ARM = 95.0;
    private static final double PILOT_ARM = 80.5;
    private static final double PASSENGER_ARM = 118.1;
    private static final double BAGGAGE_A_ARM = 142.8;

    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wb_pmk);

        // handle input to the fields
        pilotWeight = (EditText) findViewById(R.id.front_kg_pmk);
        pilotWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (pilotWeight.length() == 0) {
                    pilotMoment = (TextView) findViewById(R.id.pilot_moment_pmk);
                    pilotMoment.setText("0");
                    pilotWeight.setText("0");
                } else {
                    calcMoment(R.id.front_kg_pmk, R.id.pilot_weight_pmk, PILOT_ARM, R.id.pilot_moment_pmk);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        passengerWeight = (EditText) findViewById(R.id.rear_kg_pmk);
        passengerWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (passengerWeight.length() == 0) {
                    passengerMoment = (TextView) findViewById(R.id.passenger_moment_pmk);
                    passengerMoment.setText("0");
                } else {
                    calcMoment(R.id.rear_kg_pmk, R.id.passenger_weight_pmk, PASSENGER_ARM, R.id.passenger_moment_pmk);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        baggageAWeight= (EditText) findViewById(R.id.baggageA_kg_pmk);
        baggageAWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (baggageAWeight.length() == 0) {
                    baggageAMoment = (TextView) findViewById(R.id.baggageA_moment_pmk);
                    baggageAMoment.setText("0");
                } else {
                    calcMoment(R.id.baggageA_kg_pmk, R.id.baggageA_weight_pmk, BAGGAGE_A_ARM, R.id.baggageA_moment_pmk);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        fuelUsg = (EditText) findViewById(R.id.fuel_usg_pmk);
        fuelUsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (fuelUsg.length() == 0) {
                    fuelMoment = (TextView) findViewById(R.id.fuel_moment_pmk);
                    fuelMoment.setText("0");
                    fuelWeight = (TextView) findViewById(R.id.fuel_weight_pmk);
                    fuelWeight.setText("0");
                } else {
                    calcMomentFuel(R.id.fuel_usg_pmk, R.id.fuel_weight_pmk, FUEL_ARM, R.id.fuel_moment_pmk);
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
        inflater.inflate(R.menu.menu_wb_pmk, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save_pmk:
                Intent intentSave = new Intent(this, SaveFilePmk.class);
                intentSave.putExtra("pilot_weight", pilotWeight.getText().toString());
                intentSave.putExtra("passenger_weight", passengerWeight.getText().toString());
                intentSave.putExtra("baggageA", baggageAWeight.getText().toString());
                intentSave.putExtra("fuel", fuelUsg.getText().toString());
                intentSave.putExtra("result", 200);
                final int resultSave = 200;
                this.startActivityForResult(intentSave, resultSave);
                return true;
            case R.id.action_open_pmk:
                Intent intentOpen = new Intent(this, OpenFilePmk.class);
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
            String dateTime = data.getStringExtra("dateTime_saved");

            pilotWeight = (EditText) findViewById(R.id.front_kg_pmk);
            passengerWeight = (EditText) findViewById(R.id.rear_kg_pmk);
            baggageAWeight = (EditText) findViewById(R.id.baggageA_kg_pmk);
            fuelUsg = (EditText) findViewById(R.id.fuel_usg_pmk);
            savedTime = (TextView) findViewById(R.id.saved_time);

            pilotWeight.setText(pilot_weight);
            passengerWeight.setText(passenger_weight);
            baggageAWeight.setText(baggageA_weight);
            fuelUsg.setText(fuel);
            savedTime.setText(dateTime);

        } else if (resultCode == 200) {
            String dateTime = data.getStringExtra("dateTime_saved");
            savedTime = (TextView) findViewById(R.id.saved_time);
            savedTime.setText(dateTime);
        }
    }


    public void calcMoment (int weightKgId, int weightLbsId, double arm, int momentId) {

        EditText weightKg;
        TextView moment;
        TextView weight;

        weightKg = (EditText) findViewById(weightKgId);
        if ( weightKg.length() > 0) {

            int weightInt = Integer.parseInt(weightKg.getText().toString());
            double calcWeightLbs  = weightInt * KG_TO_LBS_FACTOR;
            double momentResult = arm * calcWeightLbs;

            BigDecimal bdWeight = new BigDecimal(calcWeightLbs);
            String roundedWeight = bdWeight.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
            weight = (TextView) findViewById(weightLbsId);
            weight.setText(roundedWeight);
            BigDecimal bdMoment = new BigDecimal(momentResult);
            String roundedMoment = bdMoment.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
            moment = (TextView) findViewById(momentId);
            moment.setText(roundedMoment);

        }

    }

    public void calcMomentFuel (int usgId, int weightId, double armDouble, int momentId) {

        EditText usg;
        TextView weight;
        TextView moment;

        usg = (EditText) findViewById(usgId);
        if (usg.length() > 0) {
            int usgInt = Integer.parseInt(usg.getText().toString());
            double weightDouble = usgInt * FUEL_TO_WEIGHT_FACTOR;

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

        double fuelWeightInt;

        basicEmptyWeight = (TextView) findViewById(R.id.bew_weight_pmk);
        pilotWeightText = (TextView) findViewById(R.id.pilot_weight_pmk);
        passengerWeightText = (TextView) findViewById(R.id.passenger_weight_pmk);
        baggageAWeightText = (TextView) findViewById(R.id.baggageA_weight_pmk);
        fuelWeight = (TextView) findViewById(R.id.fuel_weight_pmk);

        if (pilotWeightText.length() > 0) {
            pilotWeightDouble = Double.parseDouble(pilotWeightText.getText().toString());
        } else {
            pilotWeightDouble = 0;
        }

        if (passengerWeightText.length() > 0) {
            passengerWeightDouble = Double.parseDouble(passengerWeightText.getText().toString());
        } else {
            passengerWeightDouble = 0;
        }

        if (baggageAWeightText.length() > 0) {
            baggageAWeightDouble = Double.parseDouble(baggageAWeightText.getText().toString());
        } else {
            baggageAWeightDouble = 0;
        }

        if (fuelWeight.length() > 0) {
            fuelWeightInt = Double.parseDouble(fuelWeight.getText().toString());
        } else {
            fuelWeightInt = 0;
        }

        double rampWeight = Double.parseDouble(basicEmptyWeight.getText().toString()) +
                pilotWeightDouble + passengerWeightDouble + baggageAWeightDouble +
                baggageBWeightDouble + fuelWeightInt;

        // round to two decimal places
        // rampWeight = round(rampWeight * 100);
        // rampWeight = rampWeight / 100;

        basicEmptyMoment = (TextView) findViewById(R.id.bew_moment_pmk);
        pilotMoment = (TextView) findViewById(R.id.pilot_moment_pmk);
        passengerMoment = (TextView) findViewById(R.id.passenger_moment_pmk);
        baggageAMoment = (TextView) findViewById(R.id.baggageA_moment_pmk);
        fuelMoment = (TextView) findViewById(R.id.fuel_moment_pmk);

        double rampMoment = Double.parseDouble(basicEmptyMoment.getText().toString()) +
                Double.parseDouble(pilotMoment.getText().toString()) +
                Double.parseDouble(passengerMoment.getText().toString()) +
                Double.parseDouble(baggageAMoment.getText().toString()) +
                Double.parseDouble(fuelMoment.getText().toString());


        rampWeightText = (TextView) findViewById(R.id.rampWeight_weight_pmk);
        BigDecimal bd = new BigDecimal(rampWeight);
        String rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
        rampWeightText.setText(rounded);

        rampMomentText = (TextView) findViewById(R.id.rampWeight_moment_pmk);
        bd = new BigDecimal(rampMoment);
        rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
        rampMomentText.setText(rounded);

        double takeoffWeight = rampWeight - 8;

        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_pmk);
        if (takeoffWeight <= MAX_TAKEOFF_WEIGHT) {
            takeOffWeight.setTextColor(DARK_GREEN);
        } else {
            takeOffWeight.setTextColor(RED);
        }

        bd = new BigDecimal(takeoffWeight);
        rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
        takeOffWeight.setText(rounded);

        double takeoffMoment = rampMoment - 760;

        takeOffMoment = (TextView) findViewById(R.id.takeOffWeight_moment_pmk);
        bd = new BigDecimal(takeoffMoment);
        rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
        takeOffMoment.setText(rounded);

        double arm = takeoffMoment / takeoffWeight;
        bd = new BigDecimal(arm );
        rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
        TextView calculatedArmPmk = (TextView) findViewById(R.id.calculated_arm_pmk);
        String armOutput = "ARM (inch) = TAKEOFF Moment / TAKEOFF Weight = " + rounded;
        calculatedArmPmk.setText(armOutput);

        drawLinesPlot();

    }

    // start warning Messages

    public void checkWarnings() {

        double baggageA;
        double baggageB;
        double weightTo;
        double momentTo;
        double takeOff;
        boolean isBaggageA = false;
        boolean isBaggageB = false;
        boolean isTakeoff = false;
        boolean isCG = false;
        boolean activeWarning;
        String warningMessage = "";
        TextView warnings;


        baggageAWeightText = (TextView) findViewById(R.id.baggageA_weight_pmk);
        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_pmk);
        takeOffMoment = (TextView) findViewById(R.id.takeOffWeight_moment_pmk);


        if (baggageAWeightText.length() > 0) {
            baggageA = Double.parseDouble(baggageAWeightText.getText().toString());
        } else {
            baggageA = 0;
        }

        if ((takeOffWeight.length() > 0) && (takeOffMoment.length() >0)) {
            weightTo = Double.parseDouble(takeOffWeight.getText().toString());
            momentTo = Double.parseDouble(takeOffMoment.getText().toString());
            arm = momentTo / weightTo;
        }

        if (baggageA > MAX_BAGGAGE_A_WEIGHT) {
            isBaggageA = true;
            baggageAWeight.setTextColor(RED);
        } else {
            baggageAWeight.setTextColor(BLACK);
        }

        if (takeOffWeight.length() > 0) {
            takeOff = Double.parseDouble(takeOffWeight.getText().toString());
        } else {
            takeOff = 0;
        }
        if (takeOff > MAX_TAKEOFF_WEIGHT) {
            isTakeoff = true;
        }

        if (arm > MAX_ARM) {
            isCG = true;
        }

        activeWarning = isCG || isTakeoff || isBaggageA;

        if (! activeWarning) {
            warningMessage = "none";
        }

        while (activeWarning) {

            if (isTakeoff) {
                warningMessage = getResources().getString(R.string.WarningTakeOff_pmk);
                isTakeoff = false;
            }

            if (isBaggageA) {
                warningMessage = warningMessage + getString(R.string.WarningBaggageA_pmk);
                isBaggageA = false;
            }

            if (isCG) {
                warningMessage = warningMessage + getString(R.string.WarningCG_pmk);
                isCG = false;
            }

            warningMessage = warningMessage + "\n";

            activeWarning = isTakeoff ||isBaggageA;

        }

        warnings = (TextView) findViewById(R.id.warnings_pmk);
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
        plot = (XYPlot) findViewById(R.id.plot_pmk);

        // disable legend
        plot.getLayoutManager().remove(plot.getLegendWidget());

        // format x values
        plot.setDomainStep(XYStepMode.INCREMENT_BY_PIXELS.INCREMENT_BY_VAL, 1);
        plot.setDomainValueFormat(new DecimalFormat("#"));
        plot.setDomainBoundaries(81, 94, BoundaryMode.FIXED);
        plot.getGraphWidget().getDomainLabelPaint().setTextSize(20);
        plot.getGraphWidget().getDomainLabelPaint().setColor(Color.WHITE);
        plot.getGraphWidget().setDomainLabelOrientation(-90);
        plot.getGraphWidget().getDomainLabelPaint().setTextAlign(Paint.Align.CENTER);
        plot.setDomainLabel("C.G. LOCATION (INCHES AFT DATUM)");
        plot.getDomainLabelWidget().position(0, XLayoutStyle.ABSOLUTE_FROM_CENTER, 0, YLayoutStyle.RELATIVE_TO_BOTTOM,  AnchorPosition.BOTTOM_MIDDLE);
        plot.getDomainLabelWidget().getLabelPaint().setTextSize(20);
        plot.getDomainLabelWidget().setWidth(300);
        plot.getDomainLabelWidget().setHeight(25);
        plot.getDomainLabelWidget().setPaddingBottom(5);

        // format y values
        plot.setRangeStep(XYStepMode.INCREMENT_BY_PIXELS.INCREMENT_BY_VAL, 100);
        plot.setRangeValueFormat(new DecimalFormat("#"));
        plot.setRangeBoundaries(1200, 2600, BoundaryMode.FIXED);
        plot.getGraphWidget().getRangeLabelPaint().setTextSize(20);
        plot.getGraphWidget().getRangeLabelPaint().setColor(Color.WHITE);
        plot.setRangeLabel("AIRPLANE WEIGHT (lbs)");
        plot.getRangeLabelWidget().getLabelPaint().setTextSize(20);
        plot.getRangeLabelWidget().setPaddingLeft(5);
        plot.getRangeLabelWidget().setWidth(25);
        plot.getRangeLabelWidget().setHeight(500);

    }

    public void drawBasicPlot () {

        // initialize plot
        plot = (XYPlot) findViewById(R.id.plot_pmk);

        // define basic values for normal Category
        Number[] weightValues = {1200, 2080, 2550, 2550, 1200};
        Number[] cgValues = {82, 82, 88.4, 93, 93};

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
        Number[] weightValuesUC = {2120, 2120};
        Number[] cgValuesUC = {82.5, 93};

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
        plot = (XYPlot) findViewById(R.id.plot_pmk);

        // clear and redraw basic plot
        plot.clear();
        drawBasicPlot();

        // define weight line values
        int weight = getWeight();
        Number[] weightValues1 = {weight, weight};
        Number[] cgValues1 = {81,93};

        // define cg line values
        double cg = getCG();
        Number[] weightValues2 = {1200,2600};
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
        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_pmk);
        if (takeOffWeight.length() > 0) {
            intValue = (int) Double.parseDouble(takeOffWeight.getText().toString());
        }
        return intValue;
    }

    public double getCG() {
        double doubleValue = 0.0;
        double intWeightValue = 0;
        double intMomentValue = 0;

        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_pmk);
        if (takeOffWeight.length() > 0) {
            intWeightValue = (double) Double.parseDouble(takeOffWeight.getText().toString());
        }

        takeOffMoment = (TextView) findViewById(R.id.takeOffWeight_moment_pmk);
        if (takeOffMoment.length() > 0) {
            intMomentValue = (double) Double.parseDouble(takeOffMoment.getText().toString());
        }

        doubleValue = intMomentValue / intWeightValue;
        return doubleValue;
    }


}
