package org.bluetooth.bledemo;

import java.util.HashMap;
import java.util.UUID;

public class BleDefinedUUIDs {
	
	public static class Service {
		//Truconnect Service
		final static public UUID SERVICE_TRUCONNECT_UUID = UUID.fromString("175f8f23-a570-49bd-9627-815a6a27de2a");

		final static public UUID HEART_RATE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
		final static public UUID ETI_SERVICE = UUID.fromString("45544942-4C55-4554-4845-524DB87AD700");
		final static public UUID HEALTH_THERMOMETER = UUID.fromString("00001809-0000-1000-8000-00805f9b34fb");
		final static public UUID BATTERY_SERVICE = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
		final static public UUID DEVICE_INFORMATION = UUID.fromString("00000180a-0000-1000-8000-00805f9b34fb");
	};
	
	public static class Characteristic {

		//String sent to TruConnect serial Interface
		final static public UUID CHARACTERISTIC_TRUCONNECT_PERIPHERAL_RX_UUID = UUID.fromString("1cce1ea8-bd34-4813-a00a-c76e028fadcb");
		//String received from TruConnect serial Interface
		final static public UUID CHARACTERISTIC_TRUCONNECT_PERIPHERAL_TX_UUID = UUID.fromString("cacc07ff-ffff-4c48-8fae-a9ef71b75e26");


		final static public UUID TEMPERATURE_MEASUREMENT = UUID.fromString("00002a1c-0000-1000-8000-00805f9b34fb");
		final static public UUID HEART_RATE_MEASUREMENT = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
		final static public UUID MANUFACTURER_NAME_STRING = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");
		final static public UUID MODEL_NUMBER_STRING = UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb");
		final static public UUID FIRMWARE_REVISION_STRING = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
		final static public UUID APPEARANCE = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb");
		final static public UUID BODY_SENSOR_LOCATION = UUID.fromString("00002a38-0000-1000-8000-00805f9b34fb");
		final static public UUID SERIAL_NUMBER_STRING = UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb");
		final static public UUID BATTERY_LEVEL = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
		final static public UUID SENSOR_ONE = UUID.fromString("45544942-4C55-4554-4845-524DB87AD703");
		final static public UUID SENSOR_TWO = UUID.fromString("45544942-4C55-4554-4845-524DB87AD704");
		final static public UUID SENSOR_THREE = UUID.fromString("45544942-4C55-4554-4845-524DB87AD705");
		final static public UUID CALIBRATION_INFO = UUID.fromString("45544942-4C55-4554-4845-524DB87AD706");
		final static public UUID ETI_MANUFACTURING = UUID.fromString("45544942-4C55-4554-4845-524DB87AD707");
		final static public UUID BUTTON_LOW_POWER_NOTIFY = UUID.fromString("45544942-4C55-4554-4845-524DB87AD708");

		final static public UUID CHARACTERISTIC_TRUCONNECT_MODE_UUID = UUID.fromString("20b9794f-da1a-4d14-8014-a0fb9cefb2f7");

	}
	
	public static class Descriptor {
		final static public UUID CHAR_CLIENT_CONFIG       = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
	}

	private static HashMap<String, String> attributes = new HashMap();

	public static String lookup(String uuid, String defaultName) {
		String name = attributes.get(uuid);
		return name == null ? defaultName : name;
	}

}
