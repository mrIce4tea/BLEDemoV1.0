package org.bluetooth.bledemo.instrument;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ServiceConnection;
import android.os.Messenger;

/**
 * Created by jamie.meachim on 24/02/2016.
 */
public class SettingsToSave {
    public static SettingsToSave raw = null;

    public static SettingsToSave getSettings()
    {
        if(raw == null)
        {
            raw = new SettingsToSave();
            raw.appFirstLoaded = true;
        }
        return raw;
    }
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }

    // Service connection
    public ServiceConnection mConnection;
    public boolean isDegreesC;
    public boolean isConnectedToProbe = false;
    public boolean leFirstConnection = false;
    public boolean appFirstLoaded = false;

    public BlueFragment.CardFrontFragment activityContainer = null;

    //Device Connection
    public int conectionTaskRetries;
    public String reconnectMessage;
    public Messenger mMessenger = null;
    public Messenger mService = null;
    public double sensorOneValueC;
    public double sensorTwoValueC;
    public double sensorOneValueF;
    public double sensorTwoValueF;
    public int loadedProfileIDKey;

    // Bluetooth LE
    public BluetoothDevice mLeDevice;
    public BluetoothGattCharacteristic mCharacteristics;
    public BluetoothGatt mGatt;
    public BluetoothAdapter mLeAdapater;



    public String nowSensorOneTextColour;
    public String nowSensorTwoTextColour;
    public Double nowSensorOneValue;
    public String nowSensorTwoValue;

    public boolean showingContainerViewNextPush;
    public boolean isTheContainerViewShowing;

    //Date container
    public String nowDateAndTime;
    public String nowBatteryLevel;


    public boolean duoLeProbeReading = false;  //is LE device Duo
    public boolean duoSensorTwoEnabled = false; //is sensor enabled for Duo


    public String sensorOneName;
    public String sensorTwoName;




}
