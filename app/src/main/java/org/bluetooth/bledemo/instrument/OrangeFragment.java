package org.bluetooth.bledemo.instrument;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.bluetooth.bledemo.R;

public class OrangeFragment extends Fragment {

    View view;

    boolean enabled;

    private BubbleChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;

    private Typeface tf;
    private AssetManager assets;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.orange_frag, container, false);

        tvX = (TextView) view.findViewById(R.id.tvXMax);
        tvY = (TextView) view.findViewById(R.id.tvYMax);

        mSeekBarX = (SeekBar) view.findViewById(R.id.seekBar1);
        mSeekBarX.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener) this);

        mSeekBarY = (SeekBar) view.findViewById(R.id.seekBar2);
        mSeekBarY.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener) this);

        mChart = (BubbleChart) view.findViewById(R.id.chart);
        mChart.setDescription("");

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setOnChartValueSelectedListener((OnChartValueSelectedListener) this);
        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(true);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        mChart.setMaxVisibleValueCount(200);
        mChart.setPinchZoom(true);

        mSeekBarX.setProgress(5);
        mSeekBarY.setProgress(50);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setTypeface(tf);

        YAxis y1 = mChart.getAxisLeft();
        y1.setTypeface(tf);
        y1.setSpaceTop(30f);
        y1.setSpaceBottom(30f);
//        y1.setDrawZeroLine(false);

        mChart.getAxisRight().setEnabled(false);

        XAxis x1 = mChart.getXAxis();
        x1.setPosition(XAxis.XAxisPosition.BOTTOM);
        x1.setTypeface(tf);

        return view;

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

//        Toast.makeText(getActivity(), "YellowFragment.onAttach()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Purple", "onAttach");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Toast.makeText(getActivity(), "YellowFragment.onActivityCreated()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Purple", "onActivityCreated");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Toast.makeText(getActivity(), "YellowFragment.onDestroyView()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Purple", "onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
//        Toast.makeText(getActivity(), "YellowFragment.onDetach()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Purple", "onDetach");
    }

    public AssetManager getAssets() {

        return assets;
    }
}
