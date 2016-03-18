package org.bluetooth.bledemo.instrument;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.bluetooth.bledemo.R;

/**
 * Created by jamie.meachim on 25/02/2016.
 */
public class RedFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View view = inflater.inflate(R.layout.my_fragment1, container, false);
//        Context context = getActivity().getApplicationContext();
//        LinearLayout layout = new LinearLayout(context);
//        layout.setBackgroundColor(Color.RED);
//        TextView tet = new TextView(context);
//        tet.setText("RED Fragment");
//        layout.addView(tet);
        return view;

//        mainView = inflater.inflate(R.layout.my_fragment1, container, false);
//        return mainView;

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

//        Toast.makeText(getActivity(), "RedFragment.onAttach()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Red", "onAttach");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Toast.makeText(getActivity(), "RedFragment.onActivityCreated()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Red", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
//        Toast.makeText(getActivity(), "RedFragment.onStart()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Red", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getActivity(), "RedFragment.onResume()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Red", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
//        Toast.makeText(getActivity(), "RedFragment.onPause()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Red", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
//        Toast.makeText(getActivity(), "RedFragment.onStop()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Red", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Toast.makeText(getActivity(), "RedFragment.onDestroyView()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Red", "onDestroyView");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(getActivity(), "RedFragment.onDestroy()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Red", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
//        Toast.makeText(getActivity(), "RedFragment.onDetach()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Red", "onDetach");
    }


}
