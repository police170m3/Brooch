package com.tathink.brooch.brooch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by MSI on 2016-08-18.
 */
public class RageStatisticsMonth extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_month);

        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(RageStatisticsMonth.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        //텍스트 뷰 처리////////////////////////////////////////////////////////
        final TextView text = (TextView)findViewById(R.id.StatMonthTextView);
        text.setText("최근 한달");

        if (savedInstanceState == null) {
            //getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }

        ((Button)findViewById(R.id.monthbtn_day)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //주별 통계화면으로 이동 처리
                Intent i = new Intent(RageStatisticsMonth.this, RageStatisticsDay.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.monthbtn_week)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //월별 통계화면으로 이동 처리
                Intent i = new Intent(RageStatisticsMonth.this, RageStatisticsWeek.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                //finish();
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("StatMonth Activity", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("StatMonth Activity", "onResume");
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        Log.d("StatWeek Activity", "onDestroy");
    }

    /**
     * A fragment containing a line chart.
     */
    public static class PlaceholderFragment extends android.app.Fragment {

        private LineChartView chart;
        private LineChartData data;
        private int numberOfLines = 1;
        private int maxNumberOfLines = 4;
        private int numberOfPoints = 12;

        float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

        private boolean hasAxes = true;
        private boolean hasAxesNames = true;
        private boolean hasLines = true;
        private boolean hasPoints = true;
        private ValueShape shape = ValueShape.CIRCLE;
        private boolean isFilled = false;
        private boolean hasLabels = false;
        private boolean isCubic = false;
        private boolean hasLabelForSelected = false;
        private boolean pointsHaveDifferentColor;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);

            chart = (LineChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());

            // Generate some random values.
            generateValues();

            generateData();

            // Disable viewport recalculations, see toggleCubic() method for more info.
            chart.setViewportCalculationEnabled(false);

            resetViewport();
            toggleLabels();

            return rootView;
        }



        private void generateValues() {
            /*for (int i = 0; i < maxNumberOfLines; ++i) {
                for (int j = 0; j < numberOfPoints; ++j) {
                    randomNumbersTab[i][j] = (float) Math.random() * 100f;
                }
            }*/
            randomNumbersTab[0][0] = 80f;
            randomNumbersTab[0][1] = 80f;
            randomNumbersTab[0][2] = 80f;
            randomNumbersTab[0][3] = 80f;
            randomNumbersTab[0][4] = 80f;
            randomNumbersTab[0][5] = 80f;
            randomNumbersTab[0][6] = 80f;
            randomNumbersTab[0][7] = 80f;
            randomNumbersTab[0][8] = 80f;
            randomNumbersTab[0][9] = 80f;
            randomNumbersTab[0][10] = 80f;
            randomNumbersTab[0][11] = 80f;

        }

        private void reset() {
            numberOfLines = 1;

            hasAxes = true;
            hasAxesNames = true;
            hasLines = true;
            hasPoints = true;
            shape = ValueShape.CIRCLE;
            isFilled = false;
            hasLabels = false;
            isCubic = false;
            hasLabelForSelected = false;
            pointsHaveDifferentColor = false;

            //chart.setValueSelectionEnabled(hasLabelForSelected);
            resetViewport();
        }

        private void resetViewport() {
            // Reset viewport height range to (0,100)
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.bottom = 0;
            v.top = 100;    //y축 최대 크기
            v.left = 0;
            v.right = numberOfPoints - 1;
            chart.setMaximumViewport(v);
            chart.setCurrentViewport(v);
        }

        private void generateData() {

            List<Line> lines = new ArrayList<Line>();
            for (int i = 0; i < numberOfLines; ++i) {

                List<PointValue> values = new ArrayList<PointValue>();
                for (int j = 0; j < numberOfPoints; ++j) {
                    values.add(new PointValue(j, randomNumbersTab[i][j]));
                }

                Line line = new Line(values);
                line.setColor(ChartUtils.COLORS[2]);    //라인 색상은 상수값 2
                line.setShape(shape);
                line.setCubic(isCubic);
                line.setFilled(isFilled);
                line.setHasLabels(hasLabels);
                line.setHasLabelsOnlyForSelected(hasLabelForSelected);
                line.setHasLines(hasLines);
                line.setHasPoints(hasPoints);
                if (pointsHaveDifferentColor){
                    line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
                }
                lines.add(line);
            }

            data = new LineChartData(lines);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("Days");
                    axisY.setName("a number of Count about High dB");
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            data.setBaseValue(Float.NEGATIVE_INFINITY);
            chart.setLineChartData(data);

        }

        private void toggleFilled() {
            isFilled = !isFilled;

            generateData();
        }

        private void togglePointColor() {
            pointsHaveDifferentColor = !pointsHaveDifferentColor;

            generateData();
        }

        private void setCircles() {
            shape = ValueShape.CIRCLE;

            generateData();
        }

        private void toggleLabels() {
            hasLabels = !hasLabels;

            if (hasLabels) {
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);
            }

            generateData();
        }

        private void toggleLabelForSelected() {
            hasLabelForSelected = !hasLabelForSelected;

            chart.setValueSelectionEnabled(hasLabelForSelected);

            if (hasLabelForSelected) {
                hasLabels = false;
            }

            generateData();
        }

        private void toggleAxes() {
            hasAxes = !hasAxes;

            generateData();
        }

        private void toggleAxesNames() {
            hasAxesNames = !hasAxesNames;

            generateData();
        }


        private class ValueTouchListener implements LineChartOnValueSelectListener {

            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }
    }
}
