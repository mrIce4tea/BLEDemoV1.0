package org.bluetooth.bledemo.instrument;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.bluetooth.bledemo.BleWrapper;
import org.bluetooth.bledemo.DeviceListAdapter;
import org.bluetooth.bledemo.R;
import org.bluetooth.bledemo.probe.ProbeProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BlueFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {


    OnFragmentSelectedActivity mFragCallback;

    private Handler mHandler = new Handler();
    private boolean mShowingBack = false;
    Button flipBtn;
    static CardFrontFragment cd = new CardFrontFragment();
    View view;

    public interface OnFragmentSelectedActivity{
        public void onFragmentSelected(int position);
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        flipBtn = (Button) view.findViewById(R.id.btnFlip);

        cd.getTemperature();
        flipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });

        getFragmentManager().addOnBackStackChangedListener(this);


    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

//        Toast.makeText(getActivity(), "YellowFragment.onAttach()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Purple", "onAttach");
        try{
             mFragCallback = (OnFragmentSelectedActivity) activity;
        }catch (ClassCastException e)
        {
            cd.getTemperature();
            throw new ClassCastException(activity.toString()+ " must implement OnHeadlineSelectedListener");
        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cd.getTemperature();
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

    private void flipCard() {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }
        // Flip to the back.
        mShowingBack = true;
        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        getFragmentManager()
                .beginTransaction()
                        // Replace the default fragment animations with animator resources representing
                        // rotations when switching to the back of the card, as well as animator
                        // resources representing rotations when flipping back to the front (e.g. when
                        // the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)

                        // Replace any fragments currently in the container view with a fragment
                        // representing the next page (indicated by the just-incremented currentPage
                        // variable).
                .replace(R.id.container, new CardBackFragment())

                        // Add this transaction to the back stack, allowing users to press Back
                        // to get to the front of the card.
                .addToBackStack(null)

                        // Commit the transaction.
                .commit();

        mHandler.post(new Runnable() {
            @Override
            public void run() {

            }
        });
        // Defer an invalidation of the options menu (on modern devices, the action bar). This
        // can't be done immediately because the transaction may not yet be committed. Commits
        // are asynchronous in that they are posted to the main thread's message loop.
    }
    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        cd.getTemperature();
        // When the back stack changes, invalidate the options menu (action bar).

    }

    public static String batteryLevel;
    public static class CardFrontFragment extends Fragment {

        DeviceListAdapter mDevicesListAdapter = null;

        TextView txtConSensorOneName;
        TextView txtConSensorOneValue;

        TextView txtContainerDateAndTime;
        TextView txtContainerBatteryLevel;

        SettingsToSave settings = SettingsToSave.getSettings();
        View view;

        String temp;

        public CardFrontFragment() {
        }
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            view = inflater.inflate(R.layout.my_fragment2, container, false);
            ProbeProperties probeProperties = new ProbeProperties();
            batteryLevel = String.valueOf(ProbeProperties.batteryPercentage());
            txtConSensorOneName = (TextView) view.findViewById(R.id.txtConSensorOneName);
            txtConSensorOneValue = (TextView) view.findViewById(R.id.txtConSensorOneValue);
            txtContainerBatteryLevel = (TextView) view.findViewById(R.id.txtContainerBatteryLevel);
            txtContainerDateAndTime = (TextView) view.findViewById(R.id.txtContainerDateAndTime);
            settings.activityContainer = this;
            String dateMSG;
            if (settings.isConnectedToProbe) {
                dateMSG = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a", Locale.ENGLISH).format(new Date());
            } else {

                dateMSG = "not connected";
            }
            mDevicesListAdapter = new DeviceListAdapter();

            txtContainerDateAndTime.setText(dateMSG);
            settings.nowDateAndTime = dateMSG;

            temp = getTemperature();
            txtConSensorOneValue.setText(String.valueOf(getTemperature()));
            txtContainerBatteryLevel.setText(String.valueOf("Battery " + BleWrapper.batteryPercentage + "%"));

            settings.isConnectedToProbe = true;
            settings.activityContainer = this;
            return view;
        }

        public String getTemperature() {
            temp = String.valueOf(BleWrapper.SensorOneValue);
            return temp;
        }
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            Log.d("Fragment Blue", "onAttach");
        }


        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

//        Toast.makeText(getActivity(), "BlueFragment.onActivityCreated()",
//                Toast.LENGTH_LONG).show();
            Log.d("Fragment Blue", "onActivityCreated");
        }

        @Override
        public void onStart() {
            super.onStart();
//        Toast.makeText(getActivity(), "BlueFragment.onStart()",
//                Toast.LENGTH_LONG).show();
            Log.d("Fragment Blue", "onStart");
        }


        @Override
        public void onResume() {
            super.onResume();
//        Toast.makeText(getActivity(), "BlueFragment.onResume()",
//                Toast.LENGTH_LONG).show();
            Log.d("Fragment Blue", "onResume");
            txtConSensorOneName.setText(DeviceListAdapter.name);
            txtConSensorOneValue.setText(String.valueOf(BleWrapper.SensorOneValue));
            batteryLevel = String.valueOf("Battery " + BleWrapper.batteryPercentage + "%");
        }

//        @Override
//        public void onPause() {
//            super.onPause();
//            Log.d("Fragment Blue", "onPause");
//        }

        @Override
        public void onStop() {
            super.onStop();
            Log.d("Fragment Blue", "onStop");
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            Log.d("Fragment Blue", "onDestroyView");
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.d("Fragment Blue", "onDestroy");
        }

        @Override
        public void onDetach() {
            super.onDetach();
            Log.d("Fragment Blue", "onDetach");
        }
    }
        public static class CardBackFragment extends Fragment {
            public CardBackFragment() {
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                return inflater.inflate(R.layout.my_fragment1, container, false);
            }
        }

}
