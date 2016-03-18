package org.bluetooth.bledemo.instrument;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.bluetooth.bledemo.BleWrapper;
import org.bluetooth.bledemo.CoreClass;
import org.bluetooth.bledemo.R;
import org.bluetooth.bledemo.ScanningActivity;
import org.bluetooth.bledemo.probe.ProbeProperties;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by jamie.meachim on 04/03/2016.
 */
public class PurpleFragment extends Fragment {

    private String deviceName;
    private String deviceSerialNumber;
    private String deviceFirmware;
    private String deviceCalibrated;
    private String deviceModel;
    private String batteryLevel;
    private String deviceLastUpdate;

    TextView firmWDat;
    TextView serialNrDat;
    TextView calibDat;
    TextView modelDat;
    TextView batteryLvDat;
    TextView lastUpdateDat;
    TextView deviceNameDat;
    static TextView rssiDat;





    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.device_info, container, false);


        final ProbeProperties probeProperties = new ProbeProperties();


        batteryLevel = String.valueOf(probeProperties.batteryPercentage());
        firmWDat = (TextView) view.findViewById(R.id.firmWdat);
        serialNrDat = (TextView) view.findViewById(R.id.snDat);
        calibDat = (TextView) view.findViewById(R.id.calibDat);
        modelDat = (TextView) view.findViewById(R.id.modelDat);
        batteryLvDat = (TextView) view.findViewById(R.id.batLvlDat);
        lastUpdateDat = (TextView) view.findViewById(R.id.lastUpdateDat);
        deviceNameDat = (TextView) view.findViewById(R.id.tempNameDat);
        rssiDat = (TextView) view.findViewById(R.id.rssiDat);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                Thread t  = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        rssiDat.setText(BleWrapper.rssiReading);
                        deviceNameDat.setText(ScanningActivity.deviceName1);
                        serialNrDat.setText(ScanningActivity.deviceAddress);
                        batteryLvDat.setText(batteryLevel + "%");
                    }
                });
                t.run();
                t.start();
            }
        };
        r.run();
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
    public void onStart() {
        super.onStart();
//        Toast.makeText(getActivity(), "YellowFragment.onStart()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Purple", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getActivity(), "YellowFragment.onResume()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Purple", "onResume");

        rssiDat.setText(BleWrapper.rssiReading);
        deviceNameDat.setText(ScanningActivity.deviceName1);
        serialNrDat.setText(ScanningActivity.deviceAddress);
        batteryLvDat.setText(batteryLevel + "%");
    }

    @Override
    public void onPause() {
        super.onPause();
//        Toast.makeText(getActivity(), "YellowFragment.onPause()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Purple", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
//        Toast.makeText(getActivity(), "YellowFragment.onStop()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Purple", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Toast.makeText(getActivity(), "YellowFragment.onDestroyView()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Purple", "onDestroyView");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(getActivity(), "YellowFragment.onDestroy()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Purple", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
//        Toast.makeText(getActivity(), "YellowFragment.onDetach()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Purple", "onDetach");
    }

    public static class MyFragActivity extends Activity{

        public static String rssiMy;
        protected MyFragActivity(){
        }

        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Thread r = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            rssiDat.setText(BleWrapper.rssiReading);
                            rssiMy = String.valueOf(rssiDat);
                        }
                    });
                    r.start();
                }
            });
        }
    }
}
