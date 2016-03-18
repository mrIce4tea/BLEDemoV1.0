package org.bluetooth.bledemo.probe;

import android.os.AsyncTask;
import android.os.Message;

import org.bluetooth.bledemo.BleWrapper;
import org.bluetooth.bledemo.instrument.BlueFragment;

/**
 * Created by jamie.meachim on 01/03/2016.
 */
public class ProbeProperties extends Thread{



    public static int batteryPercentage()
    {
        int batteryPercentage = BleWrapper.batteryPercentage;

        return batteryPercentage;

    }


}



