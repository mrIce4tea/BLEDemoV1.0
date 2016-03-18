package org.bluetooth.bledemo.instrument;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.bluetooth.bledemo.R;

/**
 * Created by jamie.meachim on 25/02/2016.
 */
public class YellowFragment extends Fragment {

    PackageManager pm;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Button cameraBtn;
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.sharing_with_others_lay, container, false);

        cameraBtn  = (Button) view.findViewById(R.id.cameraBtn);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.id.imageView, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;


        return view;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

//        Toast.makeText(getActivity(), "YellowFragment.onAttach()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Yellow", "onAttach");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Toast.makeText(getActivity(), "YellowFragment.onActivityCreated()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Yellow", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
//        Toast.makeText(getActivity(), "YellowFragment.onStart()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Yellow", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getActivity(), "YellowFragment.onResume()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Yellow", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
//        Toast.makeText(getActivity(), "YellowFragment.onPause()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Yellow", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
//        Toast.makeText(getActivity(), "YellowFragment.onStop()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Yellow", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Toast.makeText(getActivity(), "YellowFragment.onDestroyView()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Yellow", "onDestroyView");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(getActivity(), "YellowFragment.onDestroy()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Yellow", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
//        Toast.makeText(getActivity(), "YellowFragment.onDetach()",
//                Toast.LENGTH_LONG).show();
        Log.d("Fragment Yellow", "onDetach");
    }
}
