package com.example.criminalintentsolution;
import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
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


public class CrimeFragment extends Fragment {
 private Crime mCrime;
 private EditText mTitleField;
 private Button mDateButton;
 private CheckBox mSolvedCheckBox;
 private ImageButton mPhotoButton;
 public static final String EXTRA_CRIME_ID = "com.example.criminalintentsolution.crime_id";
 private static final String DIALOG_DATE = "date";
 private static final int REQUEST_DATE = 0;
 
 public static CrimeFragment newInstance(UUID crimeId) {
	 Bundle args = new Bundle();
	 args.putSerializable(EXTRA_CRIME_ID, crimeId);
	 CrimeFragment fragment = new CrimeFragment();
	 fragment.setArguments(args);
	 return fragment;
 }
 public void updateDate() {
	 mDateButton.setText(mCrime.getDate().toString());

 }
 
 @Override
 public boolean onOptionsItemSelected(MenuItem item) {
	 switch (item.getItemId()) {
	 case android.R.id.home:
		 //to be implemented next
		 if(NavUtils.getParentActivityName(getActivity()) != null) {
			 NavUtils.navigateUpFromSameTask(getActivity());
		 }
		 return true;
		 default:
			 return super.onOptionsItemSelected(item);
			 
	 }
 }
 
 @Override public void onPause() {
	 super.onPause();
	 CrimeLab.get(getActivity()).saveCrimes();
	 
 }
 @Override
 public void onActivityResult(int requestCode, int resultCode, Intent data) {
	 if(resultCode != Activity.RESULT_OK) return;
	 if(requestCode == REQUEST_DATE) {
		 Date date = (Date)data
				 .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
		 mCrime.setDate(date);
		 updateDate();
		 //mDateButton.setText(mCrime.getDate().toString());
	 }
 }
 @Override
 public void onCreate(Bundle savedInstanceState)
 {
	 super.onCreate(savedInstanceState);
	 mCrime = new Crime();
	 //UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
	 UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
	 mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
	 setHasOptionsMenu(true);
 }
 @TargetApi(11)
 @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
	 View v = inflater.inflate(R.layout.fragment_crime, parent, false);
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		if(NavUtils.getParentActivityName(getActivity()) !=null) {
			

		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		}
	 
	 mTitleField = (EditText)v.findViewById(R.id.crime_title);
	 mTitleField.setText(mCrime.getTitle());
	 mTitleField.addTextChangedListener(new TextWatcher() {
		 public void onTextChanged(CharSequence c, int start, int before, int count) {
			 mCrime.setTitle(c.toString());
		 }
		 public void beforeTextChanged(CharSequence c, int start, int count, int after) {
			 //This space intentionally left blank
		 }
		 public void afterTextChanged(Editable c) {
			 //This one too
		 }
		 
	 });
	 mDateButton = (Button)v.findViewById(R.id.crime_date);
	 updateDate();
	 //mDateButton.setText(mCrime.getDate().toString());
	 //mDateButton.setEnabled(false);
	 mDateButton.setOnClickListener(new View.OnClickListener() {
		 public void onClick(View v) {
			 FragmentManager fm = getActivity().getSupportFragmentManager();
			 //DatePickerFragment dialog = new DatePickerFragment();
			 DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
			 dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
			 dialog.show(fm, DIALOG_DATE);
		 }
	 });
	 mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
	 mSolvedCheckBox.setChecked(mCrime.isSolved());
	 mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			 //Set the crime's solved property
			 mCrime.setSolved(isChecked);
		 
		 }
	 });
	 
	 mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
     mPhotoButton.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
             // launch the camera activity
             Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
             startActivity(i);
         }
     });
     
     // if camera is not available, disable camera functionality
     PackageManager pm = getActivity().getPackageManager();
     if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
             !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
         mPhotoButton.setEnabled(false);
     }
	 return v;
 }

}
