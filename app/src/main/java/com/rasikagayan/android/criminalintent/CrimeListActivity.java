package com.rasikagayan.android.criminalintent;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by Rasika Gayan on 12/3/2015.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
