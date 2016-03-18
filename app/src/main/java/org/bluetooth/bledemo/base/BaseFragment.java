package org.bluetooth.bledemo.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.bluetooth.bledemo.CoreClass;

/**
 * Created by jamie.meachim on 24/02/2016.
 */
public class BaseFragment extends Fragment {

    private static BaseFragment baseFragment = null;
    public CoreClass mActivity;

    public static BaseFragment getBase()
    {
        if(baseFragment == null)
        {
            baseFragment = new BaseFragment();
        }
        return baseFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        baseFragment = this;
    }

    public boolean onBackPressed(){return false;}

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        mActivity.onActivityResult(requestCode, resultCode, data);
    }



}
