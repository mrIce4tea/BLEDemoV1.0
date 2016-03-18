package org.bluetooth.bledemo;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import android.util.Log;
import android.widget.TextView;

import org.bluetooth.bledemo.instrument.BlueFragment;
import org.bluetooth.bledemo.instrument.SettingsToSave;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.UUID;

public class BleWrapper extends Service {
    /* defines (in milliseconds) how often RSSI should be updated */
    private static final int RSSI_UPDATE_TIME_INTERVAL = 1500; // 1.5 seconds
    private final static String TAG = BleWrapper.class.getSimpleName();

    public static final String CHANNEL = BleWrapper.class.getSimpleName()+".broadcast";

    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public  static String rssiReading = "";
    public static String deviceName1 = "";
    public static String deviceSerialNumber = "";


    BlueFragment bf;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";

    private static String deviceAddress ;

    private float mTemperature;
    boolean isConnected = false;

    private boolean mScanning;
    BluetoothDevice leDevice;

    /* callback object through which we are returning results to the caller */
    private BleWrapperUiCallbacks mUiCallback = null;
    SettingsToSave settings = SettingsToSave.getSettings();

    private final static int UPDATE_DEVICE = 0;
    private final static int UPDATE_VALUE = 1;


    public static String commonStringName;

    PeripheralActivity pa;

    /* define NULL object for UI callbacks */
    private static final BleWrapperUiCallbacks NULL_CALLBACK = new BleWrapperUiCallbacks.Null();


    private static final Handler uiHandler = new Handler() {
        public void handleMessage(Message msg) {
            final int what = msg.what;
            final String value = (String) msg.obj;
            switch(what) {
                case UPDATE_VALUE: updateValue(value); break;
            }
        }
    };

    public BleWrapper(BleWrapperUiCallbacks mUiCallback) {
        this.mUiCallback = mUiCallback;
    }

    public BleWrapper() {
    }
    @Override
    public int onStartCommand(Intent intent , int flags, int startId){
        scanResult();
        return Service.START_NOT_STICKY;
    }
    private void scanResult(){
        Intent intent = new Intent(CHANNEL);
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public BleWrapper(Class<BlueFragment> blueFragmentClass, BlueFragment callback) {

    }

    public static String updateValue(String value){
        return (value);
    }

    /* creates BleWrapper object, set its parent activity and callback object */
    public BleWrapper(Activity parent, BleWrapperUiCallbacks callback) {

//        mDevicesListAdapter = new DeviceListAdapter();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            System.exit(-1);
        } else {
//            deviceAddress= mDevicesListAdapter.address;
            Log.v(TAG, "Device does not support BLuetooth LE TECHNOLOGY");
//            mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(deviceAddress);
//            mBluetoothGatt = mBluetoothDevice.connectGatt(getApplicationContext(), false, mBleCallback);
//            mBluetoothGatt.connect();

        }
        this.mParent = parent;
        mUiCallback = callback;
        if(mUiCallback == null) mUiCallback = NULL_CALLBACK;
    }

    public BluetoothManager           getManager() { return mBluetoothManager; }
    public BluetoothAdapter           getAdapter() { return mBluetoothAdapter; }
    public BluetoothDevice            getDevice()  { return mBluetoothDevice; }
    public BluetoothGatt              getGatt()    { return mBluetoothGatt; }
    public BluetoothGattService       getCachedService() { return mBluetoothSelectedService; }
    public List<BluetoothGattService> getCachedServices() { return mBluetoothGattServices; }
    public boolean                    isConnected() { return mConnected; }

    /* run test and check if this device has BT and BLE hardware available */
    public boolean checkBleHardwareAvailable() {
        // First check general Bluetooth Hardware:
        // get BluetoothManager...
        final BluetoothManager manager = (BluetoothManager) mParent.getSystemService(Context.BLUETOOTH_SERVICE);
        if(manager == null) return false;
        // .. and then get adapter from manager
        final BluetoothAdapter adapter = manager.getAdapter();
        if(adapter == null) return false;

        // and then check if BT LE is also available
        boolean hasBle = mParent.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        return hasBle;
    }


    /* before any action check if BT is turned ON and enabled for us
     * call this in onResume to be always sure that BT is ON when Your
     * application is put into the foreground */
    public boolean isBtEnabled() {
        final BluetoothManager manager = (BluetoothManager) mParent.getSystemService(Context.BLUETOOTH_SERVICE);
        if(manager == null) return false;

        final BluetoothAdapter adapter = manager.getAdapter();
        if(adapter == null) return false;
        return adapter.isEnabled();
    }

    /* start scanning for BT LE devices around */
    public void startScanning() {
        mScanning = true;

        mBluetoothAdapter.startLeScan(mDeviceFoundCallback);
    }

    /* stops current scanning */
    public void stopScanning() {
        mScanning = false;
        mBluetoothAdapter.stopLeScan(mDeviceFoundCallback);
    }

    /* initialize BLE and get BT Manager & Adapter */
    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mParent.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                return false;
            }
        }

        if(mBluetoothAdapter == null) mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            return false;
        }
        return true;
    }

    private Queue<BluetoothDevice> connectionQueue = new LinkedList<BluetoothDevice>();

    public void initConnection(){
        Thread connectionThread = new Thread();
        if(connectionThread == null){
            final Thread finalConnectionThread = connectionThread;
            connectionThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    connectionLoop();
                    finalConnectionThread.interrupt();

                }
            });
            connectionThread.start();
        }
    }
    public void connectionLoop(){
        while(!connectionQueue.isEmpty()){
            connectionQueue.poll().connectGatt(getApplicationContext(), false, mBleCallback);
            try{
                Thread.sleep(250);
            }catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    /* connect to the device with specified address */

    public boolean connect(String deviceAddress) {

        if (mBluetoothAdapter == null || deviceAddress == null) return false;

        Handler handler = new Handler();
        handler.getLooper();
        // check if we need to connect from scratch or just reconnect to previous device
        if(mBluetoothGatt != null && mBluetoothGatt.getDevice().getAddress().equals(deviceAddress)) {
            // just reconnect
            Log.d(TAG, "Trying to use existing mBluetoothGatt for connection");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                initConnection();
                return true;
            } else {

                return false;

            }
        }
        return true;
    }

    /* disconnect the device. It is still possible to reconnect to it later with this Gatt client */
    public void diconnect() {
        if(mBluetoothGatt != null) mBluetoothGatt.disconnect();
        mUiCallback.uiDeviceDisconnected(mBluetoothGatt, mBluetoothDevice);
    }

    /* close GATT client completely */
    public void close() {
        if(mBluetoothGatt != null) mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /* request new RSSi value for the connection*/
    public void readPeriodicalyRssiValue(final boolean repeat) {
        mTimerEnabled = repeat;
        // check if we should stop checking RSSI value
        if(mConnected == false || mBluetoothGatt == null || mTimerEnabled == false) {
            mTimerEnabled = false;
            return;
        }

        mTimerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBluetoothGatt == null ||
                        mBluetoothAdapter == null ||
                        mConnected == false) {
                    mTimerEnabled = false;
                    return;
                }

                // request RSSI value
                mBluetoothGatt.readRemoteRssi();
                // add call it once more in the future
                readPeriodicalyRssiValue(mTimerEnabled);
            }
        }, RSSI_UPDATE_TIME_INTERVAL);
    }

    /* starts monitoring RSSI value */
    public void startMonitoringRssiValue() {
        readPeriodicalyRssiValue(true);
    }

    /* stops monitoring of RSSI value */
    public void stopMonitoringRssiValue() {
        readPeriodicalyRssiValue(false);
    }

    /* request to discover all services available on the remote devices
     * results are delivered through callback object */
    public void startServicesDiscovery() {
        if(mBluetoothGatt != null) mBluetoothGatt.discoverServices();
    }

    /* gets services and calls UI callback to handle them
     * before calling getServices() make sure service discovery is finished! */
    public void getSupportedServices() {
        if(mBluetoothGattServices != null && mBluetoothGattServices.size() > 0) mBluetoothGattServices.clear();
        // keep reference to all services in local array:
        if(mBluetoothGatt != null) mBluetoothGattServices = mBluetoothGatt.getServices();

        mUiCallback.uiAvailableServices(mBluetoothGatt, mBluetoothDevice, mBluetoothGattServices);
    }

    /* get all characteristic for particular service and pass them to the UI callback */
    public void getCharacteristicsForService(final BluetoothGattService service) {
        if(service == null) return;
        List<BluetoothGattCharacteristic> chars = null;

        chars = service.getCharacteristics();
        mUiCallback.uiCharacteristicForService(mBluetoothGatt, mBluetoothDevice, service, chars);
        // keep reference to the last selected service
        mBluetoothSelectedService = service;
    }

    /* request to fetch newest value stored on the remote device for particular characteristic */
    public void requestCharacteristicValue(BluetoothGattCharacteristic ch) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) return;

        mBluetoothGatt.readCharacteristic(ch);
        // new value available will be notified in Callback Object
    }

    /* get characteristic's value (and parse it for some types of characteristics) 
     * before calling this You should always update the value by calling requestCharacteristicValue() */
    public void getCharacteristicValue(BluetoothGattCharacteristic ch) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null || ch == null) return;

        byte[] rawValue = ch.getValue();
        String strValue = null;
        int intValue = 0;

        // lets read and do real parsing of some characteristic to get meaningful value from it 
        UUID uuid = ch.getUuid();

        if(uuid.equals(BleDefinedUUIDs.Characteristic.TEMPERATURE_MEASUREMENT)) { // heart rate
            // follow https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
            // first check format used by the device - it is specified in bit 0 and tells us if we should ask for index 1 (and uint8) or index 2 (and uint16)
            int index = ((rawValue[0] & 0x01) >0) ? 2 : 1;
            // also we need to define format
            int format = (index == 1) ? BluetoothGattCharacteristic.FORMAT_FLOAT : BluetoothGattCharacteristic.FORMAT_FLOAT;
            // now we have everything, get the value
            intValue = ch.getIntValue(format, index);
            strValue = intValue + " bpm"; // it is always in bpm units
        }
        else if (uuid.equals(BleDefinedUUIDs.Characteristic.HEART_RATE_MEASUREMENT) || // manufacturer name string
                uuid.equals(BleDefinedUUIDs.Characteristic.MODEL_NUMBER_STRING) || // model number string)
                uuid.equals(BleDefinedUUIDs.Characteristic.FIRMWARE_REVISION_STRING)) // firmware revision string
        {
            // follow https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.manufacturer_name_string.xml etc.
            // string value are usually simple utf8s string at index 0
            strValue = ch.getStringValue(0);
        }
        else if(uuid.equals(BleDefinedUUIDs.Characteristic.APPEARANCE)) { // appearance
            // follow: https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.gap.appearance.xml
            intValue  = ((int)rawValue[1]) << 8;
            intValue += rawValue[0];
            strValue = BleNamesResolver.resolveAppearance(intValue);
        }
        else if(uuid.equals(BleDefinedUUIDs.Characteristic.BODY_SENSOR_LOCATION)) { // body sensor location
            // follow: https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.body_sensor_location.xml
            intValue = rawValue[0];
            strValue = BleNamesResolver.resolveHeartRateSensorLocation(intValue);
        }
        else if(uuid.equals(BleDefinedUUIDs.Characteristic.BATTERY_LEVEL)) { // battery level
            // follow: https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.battery_level.xml
            intValue = rawValue[0];
            strValue = "" + intValue + "% battery level";
        }
        else {
            // not known type of characteristic, so we need to handle this in "general" way
            // get first four bytes and transform it to integer
            intValue = 0;
            if(rawValue.length > 0) intValue = (int)rawValue[0];
            if(rawValue.length > 1) intValue = intValue + ((int)rawValue[1] << 8);
            if(rawValue.length > 2) intValue = intValue + ((int)rawValue[2] << 8);
            if(rawValue.length > 3) intValue = intValue + ((int)rawValue[3] << 8);

            if (rawValue.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(rawValue.length);
                for(byte byteChar : rawValue) {
                    stringBuilder.append(String.format("%c", byteChar));
                }
                strValue = stringBuilder.toString();
            }
        }
        String timestamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS").format(new Date());
        mUiCallback.uiNewValueForCharacteristic(mBluetoothGatt,
                mBluetoothDevice,
                mBluetoothSelectedService,
                ch,
                strValue,
                intValue,
                rawValue,
                timestamp);
    }

    /* reads and return what what FORMAT is indicated by characteristic's properties
     * seems that value makes no sense in most cases */
    public int getValueFormat(BluetoothGattCharacteristic ch) {
        int properties = ch.getProperties();

        if((BluetoothGattCharacteristic.FORMAT_FLOAT & properties) != 0) return BluetoothGattCharacteristic.FORMAT_FLOAT;
        if((BluetoothGattCharacteristic.FORMAT_SFLOAT & properties) != 0) return BluetoothGattCharacteristic.FORMAT_SFLOAT;
        if((BluetoothGattCharacteristic.FORMAT_SINT16 & properties) != 0) return BluetoothGattCharacteristic.FORMAT_SINT16;
        if((BluetoothGattCharacteristic.FORMAT_SINT32 & properties) != 0) return BluetoothGattCharacteristic.FORMAT_SINT32;
        if((BluetoothGattCharacteristic.FORMAT_SINT8 & properties) != 0) return BluetoothGattCharacteristic.FORMAT_SINT8;
        if((BluetoothGattCharacteristic.FORMAT_UINT16 & properties) != 0) return BluetoothGattCharacteristic.FORMAT_UINT16;
        if((BluetoothGattCharacteristic.FORMAT_UINT32 & properties) != 0) return BluetoothGattCharacteristic.FORMAT_UINT32;
        if((BluetoothGattCharacteristic.FORMAT_UINT8 & properties) != 0) return BluetoothGattCharacteristic.FORMAT_UINT8;

        return 0;
    }

    /* set new value for particular characteristic */
    public void writeDataToCharacteristic(final BluetoothGattCharacteristic ch, final byte[] dataToWrite) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null || ch == null) return;

        // first set it locally....
        ch.setValue(dataToWrite);
        // ... and then "commit" changes to the peripheral
        mBluetoothGatt.writeCharacteristic(ch);
    }

    /* enables/disables notification for characteristic */
    public void setNotificationForCharacteristic(BluetoothGattCharacteristic ch, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) return;

        boolean success = mBluetoothGatt.setCharacteristicNotification(ch, enabled);
        if(!success) {
            Log.e("------", "Seting proper notification status for characteristic failed!");
        }
        mBluetoothGatt.setCharacteristicNotification(ch, enabled);
        if (UUID.fromString(String.valueOf(BleDefinedUUIDs.Characteristic.HEART_RATE_MEASUREMENT)).equals(ch.getUuid())) {
            BluetoothGattDescriptor descriptor = ch.getDescriptor(
                    UUID.fromString(String.valueOf(BleDefinedUUIDs.Descriptor.CHAR_CLIENT_CONFIG)));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
        // This is also sometimes required (e.g. for heart rate monitors) to enable notifications/indications
        // see: https://developer.bluetooth.org/gatt/descriptors/Pages/DescriptorViewer.aspx?u=org.bluetooth.descriptor.gatt.client_characteristic_configuration.xml
        BluetoothGattDescriptor descriptor = ch.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
        if(descriptor != null) {
            byte[] val = enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
            descriptor.setValue(val);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }
    public void setIndicationForCharacteristic(BluetoothGattCharacteristic ch, boolean enabled) {
        Log.d(TAG, "setIndicationForCharacteristic" + "\r" + ch);
        if (mBluetoothAdapter == null || mBluetoothGatt == null) return;

        boolean success = mBluetoothGatt.setCharacteristicNotification(ch, enabled);
        if (!success) {
            Log.e("------", "Seting proper notification status for characteristic failed!");
        }

        // This is also sometimes required (e.g. for heart rate monitors) to enable notifications/indications
        // see: https://developer.bluetooth.org/gatt/descriptors/Pages/DescriptorViewer.aspx?u=org.bluetooth.descriptor.gatt.client_characteristic_configuration.xml
        BluetoothGattDescriptor descriptor = ch.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
        if (descriptor != null) {
            byte[] val = enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
            descriptor.setValue(val);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /* defines callback for scanning results */
    public BluetoothAdapter.LeScanCallback mDeviceFoundCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {


                    mBluetoothGatt = device.connectGatt(mParent, false, mBleCallback);
                    mUiCallback.uiDeviceFound(device, rssi, scanRecord);

        }
    };


    /* callbacks called for any action on particular Ble Device */
    public final BluetoothGattCallback mBleCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mConnected = true;
                mUiCallback.uiDeviceConnected(mBluetoothGatt, mBluetoothDevice);
                mConnectionState = STATE_CONNECTED;
                intentAction = ACTION_GATT_CONNECTED;
                // now we can start talking with the device, e.g.
                mBluetoothGatt.readRemoteRssi();
                // response will be delivered to callback object!
                // in our case we would also like automatically to call for services discovery
                startServicesDiscovery();
                // and we also want to get RSSI value to be updated periodically
                startMonitoringRssiValue();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mConnected = false;
                intentAction = ACTION_GATT_DISCONNECTED;
                mUiCallback.uiDeviceDisconnected(mBluetoothGatt, mBluetoothDevice);

            }
        }


        public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";


        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
             if (status == BluetoothGatt.GATT_SUCCESS) {
                getSupportedServices();
                // now, when services discovery is finished, we can call getServices() for Gatt

                BluetoothGattService gs = gatt.getService(UUID.fromString(String.valueOf(BleDefinedUUIDs.Service.HEALTH_THERMOMETER)));
                if (gs != null) {
                    BluetoothGattCharacteristic chr = gs.getCharacteristic(BleDefinedUUIDs.Characteristic.TEMPERATURE_MEASUREMENT);
                    if (chr != null) {

                        gatt.setCharacteristicNotification(chr, true);
                        BluetoothGattDescriptor descriptor = chr.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));

                        if (descriptor != null) {
                            byte[] val = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE;
                            descriptor.setValue(val);
                            gatt.writeDescriptor(descriptor);
                        }
                    }

                    BluetoothGattService deviceService = gatt.getService(BleDefinedUUIDs.Service.DEVICE_INFORMATION);
                    if (deviceService != null) {
                        BluetoothGattCharacteristic serialCH = deviceService.getCharacteristic(BleDefinedUUIDs.Characteristic.SERIAL_NUMBER_STRING);
                        if (serialCH != null) {
                            gatt.readCharacteristic(serialCH);
                            serialCH.getValue();

                        }
                        BluetoothGattCharacteristic firmW = deviceService.getCharacteristic(BleDefinedUUIDs.Characteristic.FIRMWARE_REVISION_STRING);
                        if (firmW != null) {
                            gatt.readCharacteristic(firmW);
                            firmW.getValue();
                        }
                        BluetoothGattCharacteristic calib = deviceService.getCharacteristic(BleDefinedUUIDs.Characteristic.CALIBRATION_INFO);
                        if (calib != null) {
                            gatt.readCharacteristic(calib);
                            calib.getValue();
                        }
                    }
                }
                BluetoothGattService truConnectService = gatt.getService(BleDefinedUUIDs.Service.SERVICE_TRUCONNECT_UUID);
                if (truConnectService != null) {
                    BluetoothGattCharacteristic truModeCh = truConnectService.getCharacteristic(BleDefinedUUIDs.Characteristic.CHARACTERISTIC_TRUCONNECT_MODE_UUID);
                    if (truModeCh != null) {
                        gatt.readCharacteristic(truModeCh);
                        gatt.setCharacteristicNotification(truModeCh, true);
                        truModeCh.getValue();

                        BluetoothGattDescriptor descriptor = truModeCh.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));

                        if (descriptor != null) {
                            byte[] val = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE;
                            descriptor.setValue(val);
                            gatt.writeDescriptor(descriptor);
                        }
                    }

                    BluetoothGattCharacteristic truRxCh = truConnectService.getCharacteristic(BleDefinedUUIDs.Characteristic.CHARACTERISTIC_TRUCONNECT_PERIPHERAL_RX_UUID);
                    if (truRxCh != null) {
                        gatt.readCharacteristic(truRxCh);
                        gatt.setCharacteristicNotification(truRxCh, true);
                        truRxCh.getValue();

                        BluetoothGattDescriptor descriptor = truModeCh.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                        if (descriptor != null) {
                            byte[] val = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE;
                            descriptor.setValue(val);
                            gatt.writeDescriptor(descriptor);
                        }
                    }

                    BluetoothGattCharacteristic truTxCh = truConnectService.getCharacteristic(BleDefinedUUIDs.Characteristic.CHARACTERISTIC_TRUCONNECT_PERIPHERAL_TX_UUID);
                    if (truTxCh != null) {
                        gatt.readCharacteristic(truTxCh);
                        gatt.setCharacteristicNotification(truTxCh, true);
                        truTxCh.getValue();

                        BluetoothGattDescriptor descriptor = truTxCh.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                        if (descriptor != null) {
                            byte[] val = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE;
                            descriptor.setValue(val);
                            gatt.writeDescriptor(descriptor);
                        }
                    }


                }

            }
        }


        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            // we got response regarding our request to fetch characteristic value
            if (status == BluetoothGatt.GATT_SUCCESS) {

                // and it success, so we can get the value
                getCharacteristicValue(characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            final BluetoothGattCharacteristic characteristic) {

            if (characteristic.getUuid().equals(BleDefinedUUIDs.Characteristic.TEMPERATURE_MEASUREMENT)) {
                byte[] r = characteristic.getValue();
                if (r != null) if ((r[0] & 0x01) > 0) {
                    SensorOneValue = characteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_FLOAT, 1);
                    bluetherm_NewReading = (double) SensorOneValue;
                    SensorOneUnits = Units.DegreesC;

                } else {
                    SensorOneValue = characteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_FLOAT, 1);
                    bluetherm_NewReading = (int) SensorOneValue;
                    SensorOneUnits = Units.DegreesF;
                }
                else {
                    SensorOneValue = -5555.5f;
                    SensorOneUnits = Units.DegreesC;
                }
                return;
            }


            if (characteristic.getUuid().equals(BleDefinedUUIDs.Characteristic.BATTERY_LEVEL)) {
                batteryPercentage = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
//				batteryPercentage = Integer.parseInt(Integer.valueOf(batteryPercentage).toString());
            }
            // characteristic's value was updated due to enabled notification, lets get this value
            // the value itself will be reported to the UI inside getCharacteristicValue
            getCharacteristicValue(characteristic);
            // also, notify UI that notification are enabled for particular characteristic
            mUiCallback.uiGotNotification(mBluetoothGatt, mBluetoothDevice, mBluetoothSelectedService, characteristic);
            Message msg = Message.obtain();
            msg.what = 1;
            msg.setTarget(uiHandler);
            msg.sendToTarget();


        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

            String deviceName = gatt.getDevice().getName();
            commonStringName = String.valueOf(deviceName);

            String serviceName = BleNamesResolver.resolveServiceName(characteristic.getService().getUuid().toString().toLowerCase(Locale.getDefault()));
            String charName = BleNamesResolver.resolveCharacteristicName(characteristic.getUuid().toString().toLowerCase(Locale.getDefault()));
            String description = "Device: " + deviceName + " Service: " + serviceName + " Characteristic: " + charName;

            // we got response regarding our request to write new value to the characteristic
            // let see if it failed or not
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mUiCallback.uiSuccessfulWrite(mBluetoothGatt, mBluetoothDevice, mBluetoothSelectedService, characteristic, description);
            } else {
                mUiCallback.uiFailedWrite(mBluetoothGatt, mBluetoothDevice, mBluetoothSelectedService, characteristic, description + " STATUS = " + status);
            }

        }

        ;

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.d(TAG, "onDescriptorWrite" + descriptor + "\r" + status);

//			descriptor.getValue();
            if (descriptor.getCharacteristic().getUuid().equals(BleDefinedUUIDs.Characteristic.TEMPERATURE_MEASUREMENT)) {

                BluetoothGattService batS = gatt.getService(BleDefinedUUIDs.Service.BATTERY_SERVICE);
                if (batS != null) {
                    BluetoothGattCharacteristic batLvl = batS.getCharacteristic(BleDefinedUUIDs.Characteristic.BATTERY_LEVEL);
                    if (batLvl != null) {
                        gatt.setCharacteristicNotification(batLvl, true);
                        BluetoothGattDescriptor descriptor1 = batLvl.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                        if (descriptor1 != null) {
                            descriptor1.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

                            Boolean rv = gatt.writeDescriptor(descriptor1);
//
                            if (!rv) {
                                while (true) {

                                }
                            }
                        }
                    }
                }
            }
        }


        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // we got new value of RSSI of the connection, pass it to the UI
                mUiCallback.uiNewRssiAvailable(mBluetoothGatt, mBluetoothDevice, rssi);
                rssiReading = String.valueOf(rssi);
            }
        }
    };


    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }
    private void DisconnectProbe() {
        if (isConnected == true) {
            Log.e("runningDebug", "DISCONNECED FROM PROBE");
            isConnected = false;
        } else {
            Log.e("runningDebug", "Already disconnected");
        }
    }




    ArrayList<Messenger> mClients = new ArrayList<Messenger>();
    private Activity mParent = null;
    private boolean mConnected = false;
    private String mDeviceAddress = "";

    TextView t;

    public static int batteryPercentage;

    private BluetoothManager mBluetoothManager = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    public BluetoothDevice  mBluetoothDevice = null;
    private BluetoothGatt    mBluetoothGatt = null;
    private BluetoothGattService mBluetoothSelectedService = null;
    private List<BluetoothGattService> mBluetoothGattServices = null;

    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA_RAW = "com.example.bluetooth.le.EXTRA_DATA_RAW";


    public static final int bluetherm_connect = 1;
    public static final int bluetherm_setMessenger = 2;
    public static final int bluetherm_removeMessenger = 3;
    public static final int bluetherm_disconnect = 4;
    public static final int bluetherm_Set_1_Name = 5;
    public static final int bluetherm_Set_1_High_Limit = 6;
    public static final int bluetherm_Set_1_Low_Limit = 7;
    public static final int bluetherm_Set_1_Trim = 8;
    public static final int bluetherm_Set_2_Name = 9;
    public static final int bluetherm_Set_2_High_Limit = 10;
    public static final int bluetherm_Set_2_Low_Limit = 11;
    public static final int bluetherm_Set_2_Trim = 12;
    public static final int bluetherm_Set_Update_Interval = 13;
    public static final int bluetherm_isConnected = 14;

    public static final int bluetherm_Set_Input2State = 20;
    public static final int bluetherm_setUnits = 21;
    public static final int bluetherm_setInput2State_and_setUnits = 22;

    // messages from this service to the client
    public static final int bluetherm_ConnectedToProbe = 50;
    public static final int bluetherm_ProbeButtonPressed = 51;
    public static final int bluetherm_ProbeShuttingDown = 52;
    public static double bluetherm_NewReading = 53;
    public static final int bluetherm_disconnected = 54;
    public static final int bluetherm_theDateAndTime = 55;

    public static final int bluetherm_refreshReadings = 60;

    public static float SensorOneValue;
    static Units SensorOneUnits;
    public static  String sensorName;

    private Handler mTimerHandler = new Handler();
    private boolean mTimerEnabled = false;

    public enum Units {
        DegreesC,
        DegreesF,
        RelativeHumidity
    }

}
