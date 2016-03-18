package org.bluetooth.bledemo.instrument;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bluetooth.bledemo.R;
import org.bluetooth.bledemo.base.BaseFragment;

public class ActivityInstrument extends BaseFragment implements OnClickListener{


    SharedPreferences sharedPref;
    SettingsToSave settings = SettingsToSave.getSettings();

    View mainView;

    boolean hasUsetTiredToTurnOnBluetooth;

    TextView txtHomebatteryInfo;
    TextView txthomeDateAndTime;

    LinearLayout linearSensorTwoDisplay;
    LinearLayout linearSensorOneMinMax;
    LinearLayout linearSensorOneHighLow;
    LinearLayout linearSensorOneSequenceAlarm;
    LinearLayout linearSensorTwoMaxMin;
    LinearLayout linearSensorTwoHighLow;
    LinearLayout linearSensorTwoSequenceAlarms;
    LinearLayout linearSensorOneName;

    TextView txtSensorOneName;
    TextView txtSensorOneNameArrow;
    TextView txtSensorOneValue;
    TextView txtHomeMaxS1;
    TextView txtHomeMinS1;
    TextView txtHomeHighAlarmS1;
    TextView txtHomeLowAlarmS1;


    TextView txtSensorTwoName;
    TextView txtSensorTwoNameArrow;
    TextView txtSensorTwoValue;
    TextView txtHomeMaxS2;
    TextView txtHomeMinS2;
    TextView txtHomeHighAlarmS2;
    TextView txtHomeLowAlarmS2;

    TextView txtSensorOneHighLimitText;
    TextView txtSensorOneLowLimitText;
    TextView txtSensorTwoHighLimitText;
    TextView txtSensorTwoLowLimitText;

    TextView txtHomeViewSensorOneUnitDisplay;
    TextView txtHomeViewSensorTwoUnitDisplay;

    TextView txtHomeSensorOneNextAlarm;
    TextView txtHomeSensorOneNextAlarmArrow;
    TextView txtHomeSensorTwoNextAlarm;
    TextView txtHomeSensorTwoNextAlarmArrow;

    TextView txtHomeViewNextSensorOneAlarmName;
    TextView txtHomeViewNextSensorTwoAlarmName;

    Dialog oneButtonAlarm;
    Dialog stopAlarm;

    boolean dontShowContainerViewForIntentDialog;


//    public thisHandler mHandler;

    boolean doUILoopOnce;



//    public thisHandler mHandler;
    public NotificationManager notificationManager;


    long countdownReconnectTimer;

    boolean soundSensorOneHighAlarm;
    boolean soundSensorOnLowAlarm;
    boolean soundSensorTwoHighAlarm;
    boolean soundSensorTwoLowAlarm;

    @Override
    public void onPause(){
        super.onPause();

        settings.showingContainerViewNextPush = true;
    }
    @Override
    public void onResume(){
        super.onResume();
        try{
            String stringFrontSize = sharedPref.getString("prefFrontSize", "1");
            double savedTextSize = Double.parseDouble(stringFrontSize);

            double textSize = 85;  //85 sp
            double newTextSize = textSize * savedTextSize;
            float f = (float) newTextSize;

            txtSensorOneValue.setTextSize(f);
            txtSensorTwoValue.setTextSize(f);

            double textSizeDegrees = 40; // 40sp
            double newTextSizeDegrees = textSizeDegrees * savedTextSize;
            float fDegrees = (float) newTextSizeDegrees;

            txtHomeViewSensorOneUnitDisplay.setTextSize(fDegrees);
            txtHomeViewSensorTwoUnitDisplay.setTextSize(fDegrees);

        }catch (Exception e)
        {
          settings.showingContainerViewNextPush = true;
            doUILoopOnce = false;

            settings.isTheContainerViewShowing = false;

            if (settings.isConnectedToProbe)
            {
//                EnqueuePacket(Bluetherm.bluetherm_refreshreading, null);
            }

//            displayCorrectButtonAndLabelDisplays();
//            displayingMinMaxValues();
//            displayingHighandLowValues();
//
//            boolean showInterdict = checkForBLEEnabledonDevice();
//            if(!(showInterdict) && (hasUserTriedToTurnOnBluetooth == true))
//            {
//                connectToTheProbe();
//                hasUserTriedToTurnOnBluetooth = false;
//            }
        }

    }
    private static ActivityInstrument activityInstrument = null;
    public static ActivityInstrument getHomeView()
    {
        if(activityInstrument == null)
        {
            activityInstrument = new ActivityInstrument();
        }
        return activityInstrument;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityInstrument = new ActivityInstrument();
        sharedPref = mActivity.returnSharedPreferences();

        try {
            hasUsetTiredToTurnOnBluetooth = false;
            BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
            if (ba == null) {
                new AlertDialog.Builder(getActivity()).setMessage("This app requires Bluetooth support on the android device").setTitle("Bluetherm error").show();
                getActivity().finish();
                return null;
            }
        } catch (Exception e) {
//            this.mHandler = new thisHandler;

            if (settings.appFirstLoaded) {
                settings.appFirstLoaded = false;
                try {
                    //settings to null and false for first loading
//                    settings.isConnectedToProbe = false;
//                    settings.duoProbeReading = false;
//                    settings.duoSensorTwoEnabled = false;
//                    settings.probeFirstConnection = false;
//                    settings.connectTask = null;
//                    settings.connectTaskRetries = 4;
//                    settings.currentlyConnectedDevice = null;
//                    settings.reconnectMessage = "";
//                    settings.reconnectTask = null;
//                    settings.shownDialog = null;
//                    settings.socket = null;
//                    settings.imh = new IncomingMessageHandler();
//                    settings.mMessenger = new Messenger(settings.imh);
//
//                    settings.resetMinMaxValues = true;
//
//                    settings.alarmHomeSensorOneShowingSequence = false;
//                    settings.alarmHomeSensorTwoShowingSequence = false;
//
//                    //settings.mConnection = null;
//                    settings.graphAndTableAutoUpdateOn = true;
//                    settings.isTheCurrentTableActivityShowing = false;
//
//                    settings.savedRecordingIntervalSeconds = 60;
//                    settings.savedRecordingIntervalMilliseconds = 60000; //Change this for default recording interval
//
//
//                    settings.emailTableThatHasBeenSent = null;
//
//                    settings.updateTheAlarmDisplayOnHomeView = false;
//
//                    settings.isTheGraphViewShowing = false;


                    String degreeUnits = (sharedPref.getString("displayUnitsPref", "°C"));
                    if (degreeUnits.equals("°C")) {
//                        settings.isDegreesC = true;
                    } else {
//                        settings.isDegreesC = false;
                    }


//                    settings.popAlarmFragmentAsNotSaved = true;
//
//                    //Create a new default profile when the app is first loaded
//                    settings.loadedProfileIDKey = 4;
//
//
//                    //App first loaded so start a new Recording Table
//                    settings.saveOneBlackReadinfIntoTherRecordingTable = false;
//                    settings.startANewRecordingTable = true;
//
//                    RecordingClass recordingClass = new RecordingClass();
//                    recordingClass.startTheRecordingThread();
//
//
//                    AlarmSoundingClass.getThis().setAlarmCountsToZero();
//                    AlarmSoundingClass.getThis().getAllAlarmsForSensorOne();
//                    AlarmSoundingClass.getThis().getAllAlarmsForSensorTwo();
//
//
//                    soundSensorOneHighAlarm = true;
//                    soundSensorOnLowAlarm = true;
//                    soundSensorTwoHighAlarm = true;
//                    soundSensorTwoLowAlarm = true;
//
//                    settings.alarmsSensorOneLimbo = false;
//                    settings.alarmsSensorTwoLimbo = false;
//                    settings.alarmsSensorOneEditing = false;
//                    settings.alarmsSensorTwoEditing = false;
//
//
//                    //All dialogs and alerts
//                    settings.mMediaPlayer = null;
//                    //all alert dialogs
//                    settings.s1HighAlarm = null;
//                    settings.s1LowAlarm = null;
//                    settings.s2HighAlarm = null;
//                    settings.s2LowAlarm = null;
//                    settings.sequenceAlarmsOneDialog = null;
//                    settings.sequenceAlarmsTwoDialog = null;
//                    settings.notificationManager = null;

                } catch (Exception z) {
                    z.getMessage();
//                    LogErrors.Log(e);
                }
//                DefaultProfileClass defaultProfile = new DefaultProfileClass();
//                defaultProfile.createDefaultProfile();

//                connectTotheProbe();

            }
//            settings.imh.UpdateHandler(this.mHandler);

            //load the view
            mainView = inflater.inflate(R.layout.activity_instrument_home, container, false);

            linearSensorTwoDisplay = (LinearLayout) mainView.findViewById(R.id.linearSensorTwoDisplay);
            linearSensorOneMinMax = (LinearLayout) mainView.findViewById(R.id.linearSensorOneMinMax);
            linearSensorOneHighLow = (LinearLayout) mainView.findViewById(R.id.linearSensorOneHighLow);
            linearSensorOneSequenceAlarm = (LinearLayout) mainView.findViewById(R.id.linearSensorOneSequenceAlarm);
            linearSensorTwoMaxMin = (LinearLayout) mainView.findViewById(R.id.linearSensorTwoMaxMin);
            linearSensorTwoHighLow = (LinearLayout) mainView.findViewById(R.id.linearSensorTwoHighLow);
            linearSensorTwoSequenceAlarms = (LinearLayout) mainView.findViewById(R.id.linearSensorTwoSequenceAlarms);
            linearSensorOneName = (LinearLayout) mainView.findViewById(R.id.linearSensorOneName);

            txtSensorOneName = (TextView) mainView.findViewById(R.id.txtSensorOneName);
            txtSensorOneName.setOnClickListener(this);
            txtSensorOneNameArrow = (TextView) mainView.findViewById(R.id.txtSensorOneNameArrow);
            txtSensorOneNameArrow.setOnClickListener(this);

            txtSensorOneValue = (TextView) mainView.findViewById(R.id.txtSensorOneValue);
            txtHomeMaxS1 = (TextView) mainView.findViewById(R.id.txtHomeMaxS1);
            txtHomeMinS1 = (TextView) mainView.findViewById(R.id.txtHomeMinS1);
            txtHomeHighAlarmS1 = (TextView) mainView.findViewById(R.id.txtHomeHighAlarmS1);

            txtHomeLowAlarmS1 = (TextView) mainView.findViewById(R.id.txtHomeLowAlarmS1);

            txtSensorTwoName = (TextView) mainView.findViewById(R.id.txtSensorTwoName);
            txtSensorTwoName.setOnClickListener(this);
            txtSensorTwoNameArrow = (TextView) mainView.findViewById(R.id.txtSensorTwoNameArrow);
            txtSensorTwoNameArrow.setOnClickListener(this);

            txtSensorTwoValue = (TextView) mainView.findViewById(R.id.txtSensorTwoValue);
            txtHomeMaxS2 = (TextView) mainView.findViewById(R.id.txtHomeMaxS2);
            txtHomeMinS2 = (TextView) mainView.findViewById(R.id.txtHomeMinS2);
            txtHomeHighAlarmS2 = (TextView) mainView.findViewById(R.id.txtHomeHighAlarmS2);
            txtHomeLowAlarmS2 = (TextView) mainView.findViewById(R.id.txtHomeLowAlarmS2);

            txtHomeViewSensorOneUnitDisplay = (TextView) mainView.findViewById(R.id.txtHomeViewSensorOneUnitDisplay);
            txtHomeViewSensorTwoUnitDisplay = (TextView) mainView.findViewById(R.id.txtHomeViewSensorTwoUnitDisplay);

            txtSensorOneHighLimitText = (TextView) mainView.findViewById(R.id.txtSensorOneHighLimitText);
            txtSensorOneLowLimitText = (TextView) mainView.findViewById(R.id.txtSensorOneLowLimitText);
            txtSensorTwoHighLimitText = (TextView) mainView.findViewById(R.id.txtSensorTwoHighLimitText);
            txtSensorTwoLowLimitText = (TextView) mainView.findViewById(R.id.txtSensorTwoLowLimitText);


            txtHomeSensorOneNextAlarm = (TextView) mainView.findViewById(R.id.txtHomeSensorOneNextAlarm);
            txtHomeSensorOneNextAlarm.setOnClickListener(this);
            txtHomeSensorOneNextAlarmArrow = (TextView) mainView.findViewById(R.id.txtHomeSensorOneNextAlarmArrow);
            txtHomeSensorOneNextAlarmArrow.setOnClickListener(this);
            txtHomeSensorTwoNextAlarm = (TextView) mainView.findViewById(R.id.txtHomeSensorTwoNextAlarm);
            txtHomeSensorTwoNextAlarm.setOnClickListener(this);
            txtHomeSensorTwoNextAlarmArrow = (TextView) mainView.findViewById(R.id.txtHomeSensorTwoNextAlarmArrow);
            txtHomeSensorTwoNextAlarmArrow.setOnClickListener(this);


            txtHomeViewNextSensorOneAlarmName = (TextView) mainView.findViewById(R.id.txtHomeViewNextSensorOneAlarmName);
            txtHomeViewNextSensorOneAlarmName.setOnClickListener(this);

            txtHomeViewNextSensorTwoAlarmName = (TextView) mainView.findViewById(R.id.txtHomeViewNextSensorTwoAlarmName);
            txtHomeViewNextSensorTwoAlarmName.setOnClickListener(this);

//            txtHomeBatteryInfo = (TextView) mainView.findViewById(R.id.txtHomeBatteryInfo);
//            txtHomeDateAndTime = (TextView) mainView.findViewById(R.id.txtHomeDateAndTime);
        }

            return mainView;
    }

    @Override
    public void onClick(View v) {

    }
}
