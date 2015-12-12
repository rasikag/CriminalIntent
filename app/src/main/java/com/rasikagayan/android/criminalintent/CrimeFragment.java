package com.rasikagayan.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Date;
import java.util.UUID;


public class CrimeFragment extends Fragment {

    private static final String TAG = "CrimeFragment";

    private static final String DIALOG_IMAGE = "image";
    public static final String EXTRA_CRIME_ID = "criminalintent.CRIME_ID";
    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;

    // this is for the result get from the crime camera fragment for the start activity for the result
    // understand the pattern
    // 1. key in activity of fragment that getting the result
    private static final int REQUEST_PHOTO = 1;

    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    Crime mCrime;
    EditText mTitleField;
    Button mDateButton;
    CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        setHasOptionsMenu(true);

    }

    //@TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, parent, false);

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
//           if (NavUtils.getParentActivityName(getActivity()) != null) {
//            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
//           }
//        }
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mCrime.setTitle(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        //mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // here we can show the date

                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                //DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(fm, DIALOG_DATE);


            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.crime_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo p = mCrime.getPhoto();
                if(p==null){
                    return;
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(p.getFileName()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fm,DIALOG_IMAGE);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // set the crime's solved property
                mCrime.setSolved(isChecked);
            }
        });

        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CrimeCameraActivity.class);
                // 2. call the startActivityForResult
                // pass the intent and code in the fragment
                // this is request code
                startActivityForResult(intent,REQUEST_PHOTO);
                //startActivity(intent);
            }
        });

        PackageManager pm = getActivity().getPackageManager();
        boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)||
                                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                (Build.VERSION.SDK_INT>= Build.VERSION_CODES.GINGERBREAD && Camera.getNumberOfCameras()>0);
        if(!hasACamera){
            mPhotoButton.setEnabled(false);
        }
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }else if(requestCode == REQUEST_PHOTO){
            // when we getting data we have to submit with putExtra method's code also
            String fileName = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if(fileName != null){
                //Log.d(TAG, "filename: " + fileName);
                Photo p = new Photo(fileName);
                mCrime.setPhoto(p);
                showPhoto();
                //Log.i(TAG, "Crime: " + mCrime.getTitle() + " has a photo");
            }

        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                if(NavUtils.getParentActivityName(getActivity())!= null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

    private void showPhoto(){
        Photo p = mCrime.getPhoto();
        BitmapDrawable b = null;
        if(p!=null){
            String path = getActivity().getFileStreamPath(p.getFileName()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(),path);
        }
        mPhotoView.setImageDrawable(b);
    }


    // why this is in onStart
    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }
}
