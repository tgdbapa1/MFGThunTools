package tools.mfgthun.ch.mfgthuntools.tools.mfgthun.ch.mfgthuntools.cit;



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

import static android.graphics.Color.argb;
import static java.lang.Math.round;


public class WeightBalanceCit extends ActionBarActivity {

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
    TextView baggageBMoment;
    EditText baggageBWeight;
    TextView baggageBWeightText;
    TextView fuelMoment;
    TextView fuelWeight;
    EditText fuelUsg;
    TextView rampWeightText;
    TextView rampMomentText;
    TextView takeOffWeight;
    TextView takeOffMoment;

    double pilotWeightDouble;
    double passengerWeightDouble;
    double baggageAWeightDouble;
    double baggageBWeightDouble;
    double arm;

    private static final double FUEL_TO_WEIGHT_FACTOR = 6;
    private static final double KG_TO_LBS_FACTOR = 2.204625;
    private static final double MAX_TAKEOFF_WEIGHT = 2400.00;
    private static final double MAX_BAGGAGE_A_WEIGHT = 120.00;
    private static final double MAX_BAGGAGE_B_WEIGHT = 50.00;
    private static final double MAX_ARM = 47.50;
    private static final int DARK_GREEN = argb(255, 7, 149, 32);
    private static final int RED = argb(255, 255, 0, 30);
    private static final int BLACK = argb(255,0,0,0);
    private static final double FUEL_ARM = 48;
    private static final double PILOT_ARM = 37;
    private static final double PASSENGER_ARM = 73;
    private static final double BAGGAGE_A_ARM = 95;
    private static final double BAGGAGE_B_ARM = 123;

    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wb_cit);

        // handle input to the fields
        pilotWeight = (EditText) findViewById(R.id.front_kg_cit);
        pilotWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (pilotWeight.length() == 0) {
                    pilotMoment = (TextView) findViewById(R.id.pilot_moment_cit);
                    pilotMoment.setText("0");
                    pilotWeight.setText("0");
                } else {
                    calcMoment(R.id.front_kg_cit, R.id.pilot_weight_cit, PILOT_ARM, R.id.pilot_moment_cit);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        passengerWeight = (EditText) findViewById(R.id.rear_kg_cit);
        passengerWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (passengerWeight.length() == 0) {
                    passengerMoment = (TextView) findViewById(R.id.passenger_moment_cit);
                    passengerMoment.setText("0");
                } else {
                    calcMoment(R.id.rear_kg_cit, R.id.passenger_weight_cit, PASSENGER_ARM, R.id.passenger_moment_cit);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        baggageAWeight= (EditText) findViewById(R.id.baggageA_kg_cit);
        baggageAWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (baggageAWeight.length() == 0) {
                    baggageAMoment = (TextView) findViewById(R.id.baggageA_moment_cit);
                    baggageAMoment.setText("0");
                } else {
                    calcMoment(R.id.baggageA_kg_cit, R.id.baggageA_weight_cit, BAGGAGE_A_ARM, R.id.baggageA_moment_cit);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        baggageBWeight= (EditText) findViewById(R.id.baggageB_kg_cit);
        baggageBWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (baggageBWeight.length() == 0) {
                    baggageBMoment = (TextView) findViewById(R.id.baggageB_moment_cit);
                    baggageBMoment.setText("0");
                } else {
                    calcMoment(R.id.baggageB_kg_cit, R.id.baggageB_weight_cit, BAGGAGE_B_ARM, R.id.baggageB_moment_cit);
                    calcTotal();
                    checkWarnings();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        fuelUsg = (EditText) findViewById(R.id.fuel_usg_cit);
        fuelUsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (fuelUsg.length() == 0) {
                    fuelMoment = (TextView) findViewById(R.id.fuel_moment_cit);
                    fuelMoment.setText("0");
                    fuelWeight = (TextView) findViewById(R.id.fuel_weight_cit);
                    fuelWeight.setText("0");
                } else {
                    calcMomentFuel(R.id.fuel_usg_cit, R.id.fuel_weight_cit, FUEL_ARM, R.id.fuel_moment_cit);
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
        inflater.inflate(R.menu.menu_wb_cit, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save_cit:
                Intent intentSave = new Intent(this, SaveFileCit.class);
                intentSave.putExtra("pilot_weight", pilotWeight.getText().toString());
                intentSave.putExtra("passenger_weight", passengerWeight.getText().toString());
                intentSave.putExtra("baggageA", baggageAWeight.getText().toString());
                intentSave.putExtra("baggageB", baggageBWeight.getText().toString());
                intentSave.putExtra("fuel", fuelUsg.getText().toString());
                intentSave.putExtra("result", 200);
                final int resultSave = 200;
                this.startActivityForResult(intentSave, resultSave);
                return true;
            case R.id.action_open_cit:
                Intent intentOpen = new Intent(this, OpenFileCit.class);
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

            pilotWeight = (EditText) findViewById(R.id.front_kg_cit);
            passengerWeight = (EditText) findViewById(R.id.rear_kg_cit);
            baggageAWeight = (EditText) findViewById(R.id.baggageA_kg_cit);
            baggageBWeight = (EditText) findViewById(R.id.baggageB_kg_cit);
            fuelUsg = (EditText) findViewById(R.id.fuel_usg_cit);

            pilotWeight.setText(pilot_weight);
            passengerWeight.setText(passenger_weight);
            baggageAWeight.setText(baggageA_weight);
            baggageBWeight.setText(baggageB_weight);
            fuelUsg.setText(fuel);

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

        basicEmptyWeight = (TextView) findViewById(R.id.bew_weight_cit);
        pilotWeightText = (TextView) findViewById(R.id.pilot_weight_cit);
        passengerWeightText = (TextView) findViewById(R.id.passenger_weight_cit);
        baggageAWeightText = (TextView) findViewById(R.id.baggageA_weight_cit);
        baggageBWeightText = (TextView) findViewById(R.id.baggageB_weight_cit);
        fuelWeight = (TextView) findViewById(R.id.fuel_weight_cit);

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

        if (baggageBWeightText.length() > 0) {
            baggageBWeightDouble = Double.parseDouble(baggageBWeightText.getText().toString());
        } else {
            baggageBWeightDouble = 0;
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

        basicEmptyMoment = (TextView) findViewById(R.id.bew_moment_cit);
        pilotMoment = (TextView) findViewById(R.id.pilot_moment_cit);
        passengerMoment = (TextView) findViewById(R.id.passenger_moment_cit);
        baggageAMoment = (TextView) findViewById(R.id.baggageA_moment_cit);
        baggageBMoment = (TextView) findViewById(R.id.baggageB_moment_cit);
        fuelMoment = (TextView) findViewById(R.id.fuel_moment_cit);

        double rampMoment = Double.parseDouble(basicEmptyMoment.getText().toString()) +
                Double.parseDouble(pilotMoment.getText().toString()) +
                Double.parseDouble(passengerMoment.getText().toString()) +
                Double.parseDouble(baggageAMoment.getText().toString()) +
                Double.parseDouble(baggageBMoment.getText().toString()) +
                Double.parseDouble(fuelMoment.getText().toString());


        rampWeightText = (TextView) findViewById(R.id.rampWeight_weight_cit);
        BigDecimal bd = new BigDecimal(rampWeight);
        String rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
        rampWeightText.setText(rounded);

        rampMomentText = (TextView) findViewById(R.id.rampWeight_moment_cit);
        bd = new BigDecimal(rampMoment);
        rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
        rampMomentText.setText(rounded);

        double takeoffWeight = rampWeight - 7;

        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_cit);
        if (takeoffWeight <= MAX_TAKEOFF_WEIGHT) {
            takeOffWeight.setTextColor(DARK_GREEN);
        } else {
            takeOffWeight.setTextColor(RED);
        }

        bd = new BigDecimal(takeoffWeight);
        rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
        takeOffWeight.setText(rounded);

        double takeoffMoment = rampMoment - 300;

        takeOffMoment = (TextView) findViewById(R.id.takeOffWeight_moment_cit);
        bd = new BigDecimal(takeoffMoment);
        rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
        takeOffMoment.setText(rounded);

        double arm = takeoffMoment / takeoffWeight;
        bd = new BigDecimal(arm );
        rounded = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN).toPlainString();
        TextView calculatedArmCit = (TextView) findViewById(R.id.calculated_arm_cit);
        String armOutput = "ARM (inch) = TAKEOFF Moment / TAKEOFF Weight = " + rounded;
        calculatedArmCit.setText(armOutput);

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


        baggageAWeightText = (TextView) findViewById(R.id.baggageA_weight_cit);
        baggageBWeightText = (TextView) findViewById(R.id.baggageB_weight_cit);
        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_cit);
        takeOffMoment = (TextView) findViewById(R.id.takeOffWeight_moment_cit);


        if (baggageAWeightText.length() > 0) {
            baggageA = Double.parseDouble(baggageAWeightText.getText().toString());
        } else {
            baggageA = 0;
        }

        if (baggageBWeightText.length() > 0) {
            baggageB = Double.parseDouble(baggageBWeightText.getText().toString());
        } else {
            baggageB = 0;
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

        if (baggageB > MAX_BAGGAGE_B_WEIGHT) {
            isBaggageB = true;
            baggageBWeight.setTextColor(RED);
        } else {
            baggageBWeight.setTextColor(BLACK);
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

        activeWarning = isCG || isTakeoff || isBaggageA || isBaggageB;

        if (! activeWarning) {
            warningMessage = "none";
        }

        while (activeWarning) {

            if (isTakeoff) {
                warningMessage = getResources().getString(R.string.WarningTakeOff_cit);
                isTakeoff = false;
            }

            if (isBaggageA) {
                warningMessage = warningMessage + getString(R.string.WarningBaggageA_cit);
                isBaggageA = false;
            }

            if (isBaggageB) {
                warningMessage = warningMessage + getString(R.string.WarningBaggageB_cit);
                isBaggageB = false;
            }

            if (isCG) {
                warningMessage = warningMessage + getString(R.string.WarningCG_cit);
                isCG = false;
            }

            warningMessage = warningMessage + "\n";

            activeWarning = isTakeoff ||isBaggageA || isBaggageB;

        }

        warnings = (TextView) findViewById(R.id.warnings_cit);
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
        plot = (XYPlot) findViewById(R.id.plot_cit);

        // disable legend
        plot.getLayoutManager().remove(plot.getLegendWidget());

        // format x values
        plot.setDomainStep(XYStepMode.INCREMENT_BY_PIXELS.INCREMENT_BY_VAL, 1);
        plot.setDomainValueFormat(new DecimalFormat("#"));
        plot.setDomainBoundaries(34, 48, BoundaryMode.FIXED);
        plot.getGraphWidget().getDomainLabelPaint().setTextSize(20);
        plot.getGraphWidget().getDomainLabelPaint().setColor(Color.WHITE);
        plot.getGraphWidget().setDomainLabelOrientation(-90);
        plot.getGraphWidget().getDomainLabelPaint().setTextAlign(Paint.Align.CENTER);
        plot.setDomainLabel("C.G. LOCATION (ARM)");
        plot.getDomainLabelWidget().position(0, XLayoutStyle.ABSOLUTE_FROM_CENTER, 0, YLayoutStyle.RELATIVE_TO_BOTTOM,  AnchorPosition.BOTTOM_MIDDLE);
        plot.getDomainLabelWidget().getLabelPaint().setTextSize(20);
        plot.getDomainLabelWidget().setWidth(300);
        plot.getDomainLabelWidget().setHeight(25);
        plot.getDomainLabelWidget().setPaddingBottom(5);

        // format y values
        plot.setRangeStep(XYStepMode.INCREMENT_BY_PIXELS.INCREMENT_BY_VAL, 100);
        plot.setRangeValueFormat(new DecimalFormat("#"));
        plot.setRangeBoundaries(1500, 2450, BoundaryMode.FIXED);
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
        plot = (XYPlot) findViewById(R.id.plot_cit);

        // define basic values for normal Category
        Number[] weightValues = {1500, 1960, 2400, 2400, 1500};
        Number[] cgValues = {35, 35, 39.5, 47.3, 47.3};

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
        Number[] weightValuesUC = {2100, 2100, 1500};
        Number[] cgValuesUC = {36.4, 40.5, 40.5};

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
        plot = (XYPlot) findViewById(R.id.plot_cit);

        // clear and redraw basic plot
        plot.clear();
        drawBasicPlot();

        // define weight line values
        int weight = getWeight();
        Number[] weightValues1 = {weight, weight};
        Number[] cgValues1 = {34,48};

        // define cg line values
        double cg = getCG();
        Number[] weightValues2 = {1500,2450};
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
        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_cit);
        if (takeOffWeight.length() > 0) {
            intValue = (int) Double.parseDouble(takeOffWeight.getText().toString());
        }
        return intValue;
    }

    public double getCG() {
        double doubleValue = 0.0;
        double intWeightValue = 0;
        double intMomentValue = 0;

        takeOffWeight = (TextView) findViewById(R.id.takeOffWeight_weight_cit);
        if (takeOffWeight.length() > 0) {
            intWeightValue = (double) Double.parseDouble(takeOffWeight.getText().toString());
        }

        takeOffMoment = (TextView) findViewById(R.id.takeOffWeight_moment_cit);
        if (takeOffMoment.length() > 0) {
            intMomentValue = (double) Double.parseDouble(takeOffMoment.getText().toString());
        }

        doubleValue = intMomentValue / intWeightValue;
        return doubleValue;
    }


}
