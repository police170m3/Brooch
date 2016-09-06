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

import kr.mint.testbluetoothspp.DBManager;
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
 * Created by MSI on 2016-08-17.
 */
public class StatisticsDay extends FragmentActivity {
    private static int SDcount = 0;
    public static float[] SDdbValue = new float[24];
    public static int hours = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_day);

        Toast.makeText(StatisticsDay.this, "현재 hours는 " + hours + " " + ((hours) / 24) + "일 전입니다.", Toast.LENGTH_LONG).show();

        //DBManger 객체 생성
        final DBManager dbManager = new DBManager(getApplicationContext(), "STRESS.db", null, 1);
        //DB 최근 데이터 24시간 횟수 select
        for (int i = 0; i < 24; i++) {
            SDcount = dbManager.SelectStress24h(i, hours);
            SDdbValue[23 - i] = (float) SDcount;
        }

        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(StatisticsDay.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        //텍스트 뷰 처리////////////////////////////////////////////////////////
        final TextView text = (TextView) findViewById(R.id.StatDayTextView);
        text.setText("최근 24시간");

        if (savedInstanceState == null) {
            //getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }

        ((Button) findViewById(R.id.daybtn_week)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //주별 통계화면으로 이동 처리
                Intent i = new Intent(StatisticsDay.this, StatisticsWeek.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);

            }
        });

        ((Button) findViewById(R.id.daybtn_month)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //월별 통계화면으로 이동 처리
                Intent i = new Intent(StatisticsDay.this, StatisticsMonth.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                //finish();
            }
        });

        //24시간 전 후 < 버튼, > 버튼 처리//////////////////////////////////////////////////////////
        ((Button) findViewById(R.id.daybtn_left)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //24시간 전 24시간 데이터
                if (hours == -144) {
                    //1주일 간의 자료만 제공합니다.
                    Toast.makeText(StatisticsDay.this, "이전 보기는 1주일간 자료를 제공합니다.", Toast.LENGTH_LONG).show();
                } else {
                    hours -= 24;
                    Intent i = new Intent(StatisticsDay.this, StatisticsDay.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        });

        ((Button) findViewById(R.id.daybtn_right)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //24시간 후 24시간 데이터
                if (hours == 0) {
                    //토스트로 가장 마지막 최근 24시간 자료 입니다.
                    Toast.makeText(StatisticsDay.this, "마지막 최근 24시간 그래프입니다.", Toast.LENGTH_LONG).show();
                } else {
                    hours += 24;
                    Intent i = new Intent(StatisticsDay.this, StatisticsDay.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        });
        //24시간 전 후 < 버튼, > 버튼 처리//////////////////////////////////////////////////////////
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("StatDay Activity", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("StatDay Activity", "onResume");
    }

    @Override
    protected void onDestroy() {
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
        private int numberOfPoints = 24;

        float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

        private boolean hasAxes = true;
        private boolean hasAxesNames = true;       //X,Y축 라벨
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
            for (int i = 0; i < 24; i++) {
                randomNumbersTab[0][i] = SDdbValue[i];
            }
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
            v.top = 50;    //y축 최대 크기
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
                line.setColor(ChartUtils.COLORS[1]);    //라인 색상은 상수값 1
                line.setShape(shape);
                line.setCubic(isCubic);
                line.setFilled(isFilled);
                line.setHasLabels(hasLabels);
                line.setHasLabelsOnlyForSelected(hasLabelForSelected);
                line.setHasLines(hasLines);
                line.setHasPoints(hasPoints);
                if (pointsHaveDifferentColor) {
                    line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
                }
                lines.add(line);
            }

            data = new LineChartData(lines);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("시간");
                    axisY.setName("횟수");
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
                //Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }
    }
}
