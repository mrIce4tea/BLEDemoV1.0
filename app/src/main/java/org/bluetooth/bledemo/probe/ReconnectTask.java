package org.bluetooth.bledemo.probe;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.os.AsyncTask;

import org.bluetooth.bledemo.CoreClass;

/**
 * Created by jamie.meachim on 04/03/2016.
 */
public class ReconnectTask extends AsyncTask <BluetoothDevice, Integer, CoreClass> {

    public ReconnectTask(CoreClass target)
    {
        super();
    }

    @Override
    protected CoreClass doInBackground(BluetoothDevice... params) {
        return null;
    }
}
