package org.bluetooth.bledemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bluetooth.bledemo.base.TrackingApplicationVisibility;
import org.bluetooth.bledemo.instrument.ActivityInstrument;
import org.bluetooth.bledemo.instrument.BlueFragment;
import org.bluetooth.bledemo.instrument.OrangeFragment;
import org.bluetooth.bledemo.instrument.PurpleFragment;
import org.bluetooth.bledemo.instrument.RedFragment;
import org.bluetooth.bledemo.instrument.SettingsToSave;
import org.bluetooth.bledemo.instrument.YellowFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class CoreClass extends FragmentActivity implements OnClickListener, BlueFragment.OnFragmentSelectedActivity{

    private FragmentManager fm;
    private PeripheralActivity.ListType mListType;
    private CharacteristicsListAdapter mCharacteristicsListAdapter = null;
    private CharacteristicDetailsAdapter mCharDetailsAdapter = null;
    private BlueFragment blueFragment = null;
    SettingsToSave settings = SettingsToSave.getSettings();
    BlueFragment bf = new BlueFragment();
    Timer timer;
    TimerTask timerTask;
    public static long scanTIME = 200;
    public static final String EXTRAS_DEVICE_NAME    = "BLE_DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "BLE_DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_RSSI    = "BLE_DEVICE_RSSI";
    public static final String EXTRAS_DEVICE_TEMP    = "BLE_DEVICE_TEMPERATURE";
    TextView textView, rssiTextView;
    private FragmentTransaction ft;
    public String mDeviceAddress;
    public static Double temperature;
    public static final String MY_PREF = "PreferenceSettingsBlueThermPro";
    ActivityInstrument ai = new ActivityInstrument();
    private ArrayList<BluetoothGattService> mBTServices;
    private LayoutInflater mInflater;
    private String txtTempView;
    private TextView txtTemperature;
    private HashMap<String, Stack<Fragment>> mStacks;
    private String mCurrentTab;
    private FragmentManager fragmentManager;
    private LinearLayout linlay;
    Button tab_instrument, tab_alarms, tab_tables, tab_graph, tab_profiles, tab_settings;

    @Override
    protected void onPause()
    {
        super.onPause();
        mCharacteristicsListAdapter.clearList();
        Log.d("Bluetherm", "Main Paused");
        TrackingApplicationVisibility.activityPaused();
    }

    @Override
    protected void onResume()
    {
        Log.d("Bluetherm", "Main Resumed");
        super.onResume();
        if(mCharacteristicsListAdapter == null) mCharacteristicsListAdapter = new CharacteristicsListAdapter(this);
        startTimer();
    }
    public void startTimer() {
        timer = new Timer();
        intitializeTimerTask();
        timer.schedule(timerTask, 300, 600);
    }
    public void intitializeTimerTask(){
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getTemperature();
                BlueFragment.CardFrontFragment cf = new BlueFragment.CardFrontFragment();
                cf.getTemperature();
            }
        };
    }
    public void stopTimerTask(View v)
    {
     if(timer != null)
     {
         timer.cancel();
         timer = null;
     }
    }
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Bluetherm", "Main Created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        textView = (TextView) findViewById(R.id.core_temp_text);

        rssiTextView = (TextView) findViewById(R.id.liveRssiUpdate);

        fm = getFragmentManager();

        linlay = (LinearLayout) findViewById(R.id.container);
        tab_instrument = (Button) findViewById(R.id.button_a);
        tab_alarms = (Button) findViewById(R.id.button_b);
        tab_graph = (Button) findViewById(R.id.button_c);
        tab_profiles = (Button) findViewById(R.id.button_d);
        tab_tables = (Button) findViewById(R.id.button_e);
        tab_settings = (Button) findViewById(R.id.button_f);

        tab_instrument.setOnClickListener(this);
        tab_alarms.setOnClickListener(this);
        tab_graph.setOnClickListener(this);
        tab_profiles.setOnClickListener(this);
        tab_tables.setOnClickListener(this);
        tab_settings.setOnClickListener(this);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    @Override
    public void onClick(final View v) {
        ft = fm.beginTransaction();
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                switch (v.getId()) {
                    case R.id.button_a:
                        loadInstFrag(v);
                        BlueFragment.CardFrontFragment cf= new BlueFragment.CardFrontFragment();
                        cf.getTemperature();
                        getTemperature();
                        break;
                    case R.id.button_b:
                        loadAalarmFrag(v);
                        break;
                    case R.id.button_c:
                        loadGraphFrag(v);
                        break;
                    case R.id.button_d:
                        loadProifilesFrag(v);
                        break;
                    case R.id.button_e:
                        Log.d("btn e Pressed", "E btn Pressed");
                        loadChartFrag(v);
                        break;
                    case R.id.button_f:
                        Log.d("btn f Pressed", "F btn Pressed");
                        break;
                }
                ft.commit();
            }
        }).start();

    }

    void getTemperature() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                double s = Double.valueOf(BleWrapper.SensorOneValue);
                showResult(String.valueOf(Double.valueOf(s)));
                int k = Integer.valueOf(BleWrapper.rssiReading);
                showRssiResult(k);
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
    void showRssiResult(final int k)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rssiTextView.setText("RSSI : " + BleWrapper.rssiReading);
            }
        });
    }
    void showResult(final String s)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(s);
            }
        });
    }
    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
            if (mStacks.get(mCurrentTab).size() == 0) {
                return;
            }

            // mStacks.get(mCurrentTab).lastElement();
        /*Now current fragment on screen gets onActivityResult callback..*/
            mStacks.get(mCurrentTab).lastElement().onActivityResult(requestCode, resultCode, data);
    }
    public SharedPreferences returnSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences(MY_PREF, 0);
        return sharedPref;
    }
    public void loadInstFrag(View view) {
        ft = fm.beginTransaction();
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                LinearLayout linlay1 = (LinearLayout) findViewById(R.id.container);
                linlay1.removeAllViews();
                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BlueFragment.CardFrontFragment cf = new BlueFragment.CardFrontFragment();
                                Log.d("btn a Pressed", "A btn Pressed");
                                ft.replace(R.id.container, cf);
                                ft.setTransition(ft.TRANSIT_FRAGMENT_OPEN);
                                textView.setText("");
                                rssiTextView.setText("");
                                showRssiResult(BIND_ABOVE_CLIENT);
                                cf.getView();
                            }
                        });
                    }
                };
                tt.scheduledExecutionTime();
                tt.run();
            }
        });
    }
    public void loadAalarmFrag(View view) {
        ft = fm.beginTransaction();
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                Log.d("btn b Pressed", "B btn Pressed");
                LinearLayout linlay1 = (LinearLayout) findViewById(R.id.container);
                linlay1.removeAllViews();
                RedFragment redF = new RedFragment();
                ft.replace(R.id.container, redF);
                ft.setTransition(ft.TRANSIT_FRAGMENT_CLOSE);
            }
        });
    }
    public void loadGraphFrag(View view) {
        ft = fm.beginTransaction();
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                LinearLayout linlay2 = (LinearLayout) findViewById(R.id.container);
                linlay2.removeAllViews();
                YellowFragment yellowF = new YellowFragment();
                ft.add(R.id.container, yellowF);
                ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);
            }
        });
    }
    public void loadProifilesFrag(View view) {
        ft = fm.beginTransaction();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("btn d Pressed", "D btn Pressed");
                LinearLayout linlay9 = (LinearLayout) findViewById(R.id.container);
                linlay9.removeAllViews();
                PurpleFragment purpleF = new PurpleFragment();
                ft.replace(R.id.container, purpleF);
                ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);
            }
        });
    }
    public void loadChartFrag(View view) {
        ft = fm.beginTransaction();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("btn d Pressed", "D btn Pressed");
                LinearLayout linlay10 = (LinearLayout) findViewById(R.id.container);
                linlay10.removeAllViews();
                OrangeFragment orangeF = new OrangeFragment();
                ft.replace(R.id.container, orangeF);
                ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);
            }
        });
    }

    @Override
    public void onFragmentSelected(int position) {BlueFragment blueFragment = new BlueFragment();
        if(blueFragment != null)
        {
            new BlueFragment.OnFragmentSelectedActivity() {
                @Override
                public void onFragmentSelected(int position) {
                    BlueFragment.CardFrontFragment cc = new BlueFragment.CardFrontFragment();
                    cc.getTemperature();
                }
            };
        }
    }
}

