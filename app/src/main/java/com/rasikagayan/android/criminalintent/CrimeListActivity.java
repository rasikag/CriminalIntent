package com.rasikagayan.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Rasika Gayan on 12/3/2015.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
