package com.rasikagayan.android.criminalintent;

import android.support.v4.app.Fragment;
import java.util.UUID;

// now we not use this activity

public class CrimeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID)getIntent()
                .getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }

}




