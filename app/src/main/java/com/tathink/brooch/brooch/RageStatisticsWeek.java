package com.tathink.brooch.brooch;

import android.content.Intent;
import android.content.SharedPreferences;
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

import kr.mint.testbluetoothspp.BTService;
import kr.mint.testbluetoothspp.DBManager;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
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
public class RageStatisticsWeek extends FragmentActivity {
    private static int SWcount = 0, top = 100;
    public static float[] RWdbValue = new float[7];
    public static  int days = 0;
    private int min = 0, max = 0, avg = 0, avgSub = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_week);

        BTService.config_check = true;  //sejin 2016.10.24

        getPreferences();
        avg = (min + max) / 2;
        avgSub = (min + avg) / 2;     //소리쳤을 때 min, max의 평균

        //Toast.makeText(RageStatisticsWeek.this, "현재 days는 "+days+" "+((days)/7)+"주일 전입니다.", Toast.LENGTH_LONG).show();

        //DBManger 객체 생성
        final DBManager dbManager = new DBManager(getApplicationContext(), "STRESS.db", null, 1);
        //DB 최근 데이터 24시간 횟수 select
        for(int i = 0; i < 7; i++) {
            SWcount = dbManager.SelectStress1week(i, days, avgSub, max);
            RWdbValue[6 - i] = (float) SWcount;
        }

        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(RageStatisticsWeek.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        //텍스트 뷰 처리////////////////////////////////////////////////////////
        final TextView text = (TextView)findViewById(R.id.StatWeekTextView);
        text.setText("최근 일주일");

        if (savedInstanceState == null) {
            //getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }

        ((Button)findViewById(R.id.weekbtn_day)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //주별 통계화면으로 이동 처리
                Intent i = new Intent(RageStatisticsWeek.this, RageStatisticsDay.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.weekbtn_month)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //월별 통계화면으로 이동 처리
                Intent i = new Intent(RageStatisticsWeek.this, RageStatisticsMonth.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                //finish();
            }
        });

        //1주일 전 후 < 버튼, > 버튼 처리//////////////////////////////////////////////////////////
        ((Button)findViewById(R.id.weekbtn_left)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1주일 전 데이터
                if(days == -21) {
                    //1주일 간의 자료만 제공합니다.
                    Toast.makeText(RageStatisticsWeek.this, "이전 보기는 한달간 자료를 제공합니다.", Toast.LENGTH_LONG).show();
                } else {
                    days -= 7;
                    Intent i = new Intent(RageStatisticsWeek.this, RageStatisticsWeek.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        });

        ((Button)findViewById(R.id.weekbtn_right)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1주일 후 데이터
                if(days == 0){
                    //토스트로 가장 마지막 최근 24시간 자료 입니다.
                    Toast.makeText(RageStatisticsWeek.this, "현재기준 최근 일주일 그래프입니다.", Toast.LENGTH_LONG).show();
                } else {
                    days += 7;
                    Intent i = new Intent(RageStatisticsWeek.this, RageStatisticsWeek.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        });
        //1주일 전 후 < 버튼, > 버튼 처리//////////////////////////////////////////////////////////
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("StatWeek Activity", "onResume");
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
        public final static String[] weeks = new String[]{"-6일", "-5일", "-4일", "-3일", "-2일", "-1일", "오늘"};

        public final static String[] weeks1weekago = new String[]{"-6일", "-5일", "-4일", "-3일", "-2일", "-1일", "0일"};

        private LineChartView chart;
        private LineChartData data;
        private int numberOfLines = 1;
        private int maxNumberOfLines = 4;
        private int numberOfPoints = 7;

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
            for(int i = 0; i < 7; i++){
                randomNumbersTab[0][i] = RWdbValue[i];
                if (RWdbValue[i] > top) {
                    top = (int)RWdbValue[i];
                }
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
            v.top = top;    //y축 최대 크기
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
                line.setColor(ChartUtils.COLORS[3]);    //라인 색상은 상수값 3
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

            //x축 단위 표시
            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            for(int i = 0; i < 7; i++){
                if(days == 0) {
                    axisValues.add(new AxisValue(i, weeks[i].toCharArray()));
                } else {
                    axisValues.add(new AxisValue(i, weeks1weekago[i].toCharArray()));
                }
            }

            if (hasAxes) {
                Axis axisX = new Axis(axisValues);
                data.setAxisXBottom(axisX);
                //Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    if(days == 0) {
                        axisX.setName("최근 일주일");
                    } else {
                        axisX.setName((days)/-7+"주일전");
                    }
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
                Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }
    }

    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        min = pref.getInt("rvMin", 0);
        max = pref.getInt("rvMax", 0);
    }

    @Override
    protected void onPause() {     //sejin 2016.10.24
        super.onPause();
        BTService.config_check = false;  //sejin 2016.10.24
    }
}
