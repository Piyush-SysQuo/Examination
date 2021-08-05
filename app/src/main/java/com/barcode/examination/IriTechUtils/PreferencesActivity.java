package com.barcode.examination.IriTechUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.barcode.examination.R;

import java.util.regex.Pattern;

public class PreferencesActivity extends PreferenceActivity implements OnPreferenceChangeListener, OnPreferenceClickListener
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

		SharedPreferences sharedPref;
		int index = 0;
		EditTextPreference countPref = (EditTextPreference) findPreference("count_interval_pref");
		countPref.setOnPreferenceChangeListener(this);
		sharedPref = countPref.getSharedPreferences();
		countPref.setSummary(sharedPref.getString("count_interval_pref", ""));
		countPref.setOnPreferenceClickListener(this);

		EditTextPreference captureTimeoutPref = (EditTextPreference) findPreference("capture_timeout_pref");
		captureTimeoutPref.setOnPreferenceChangeListener(this);
		sharedPref = captureTimeoutPref.getSharedPreferences();
		captureTimeoutPref.setSummary(sharedPref.getString("capture_timeout_pref", ""));
		captureTimeoutPref.setOnPreferenceClickListener(this);

		EditTextPreference totalScorePref = (EditTextPreference) findPreference("threshold_total_score");
		totalScorePref.setOnPreferenceChangeListener(this);
		sharedPref = totalScorePref.getSharedPreferences();
		totalScorePref.setSummary(sharedPref.getString("threshold_total_score", ""));
		totalScorePref.setOnPreferenceClickListener(this);

		EditTextPreference usableAreaPref = (EditTextPreference) findPreference("threshold_usable_area");
		usableAreaPref.setOnPreferenceChangeListener(this);
		sharedPref = usableAreaPref.getSharedPreferences();
		usableAreaPref.setSummary(sharedPref.getString("threshold_usable_area", ""));
		usableAreaPref.setOnPreferenceClickListener(this);

		EditTextPreference outputPref = (EditTextPreference) findPreference("output_dir_pref");
		outputPref.setOnPreferenceChangeListener(this);
		outputPref.setSummary(sharedPref.getString("output_dir_pref", ""));
		sharedPref = outputPref.getSharedPreferences();

		outputPref.setOnPreferenceClickListener(this);

		EditTextPreference preNamePref = (EditTextPreference) findPreference("prefix_name_pref");
		preNamePref.setOnPreferenceChangeListener(this);
		sharedPref = preNamePref.getSharedPreferences();
		preNamePref.setSummary(sharedPref.getString("prefix_name_pref", ""));
		preNamePref.setOnPreferenceClickListener(this);

		ListPreference listPref = (ListPreference) findPreference("capture_mode_pref");
		listPref.setOnPreferenceChangeListener(this);
		sharedPref = listPref.getSharedPreferences();
		index = listPref.findIndexOfValue(sharedPref.getString("capture_mode_pref", "0"));
		listPref.setSummary(listPref.getEntries()[index].toString());

		listPref = (ListPreference) findPreference("quality_mode_pref");
		listPref.setOnPreferenceChangeListener(this);
		sharedPref = listPref.getSharedPreferences();
		index = listPref.findIndexOfValue(sharedPref.getString("quality_mode_pref", "0"));
		listPref.setSummary(listPref.getEntries()[index].toString());

		listPref = (ListPreference) findPreference("operation_mode_pref");
		listPref.setOnPreferenceChangeListener(this);
		sharedPref = listPref.getSharedPreferences();
		index = listPref.findIndexOfValue(sharedPref.getString("operation_mode_pref", "0"));
		listPref.setSummary(listPref.getEntries()[index].toString());
		listPref.setEnabled(false);

		listPref = (ListPreference) findPreference("stream_image_scale_pref");
		listPref.setOnPreferenceChangeListener(this);
		sharedPref = listPref.getSharedPreferences();
		index = listPref.findIndexOfValue(sharedPref.getString("stream_image_scale_pref", "1"));
		listPref.setSummary(listPref.getEntries()[index].toString());

		 //DO not used
		PreferenceScreen pPreferenceScreen = (PreferenceScreen) findPreference("preferenceScreen");

		PreferenceCategory mCategory = (PreferenceCategory) findPreference("category_threshold");
		pPreferenceScreen.removePreference(mCategory);

		mCategory = (PreferenceCategory) findPreference("category_save_image");
		pPreferenceScreen.removePreference(mCategory);

		ListPreference streamImageScalePref = (ListPreference) findPreference("stream_image_scale_pref");
		mCategory = (PreferenceCategory) findPreference("category_capture");
		mCategory.removePreference(streamImageScalePref);

		CheckBoxPreference checkDuplicatePref = (CheckBoxPreference) findPreference("check_dedup_pref");
		mCategory = (PreferenceCategory) findPreference("category_check_dedup");
		mCategory.removePreference(checkDuplicatePref);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setTitle(getString(R.string.settings_title));
	}

	public static void setOutputDirectory(Context context, String value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefs.edit().putString("output_dir_pref", value).apply();
	}

	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if(preference.getKey().equals("count_interval_pref"))
		{
			EditTextPreference countPref = (EditTextPreference) preference;
			
			try
			{
				int count = Integer.parseInt(countPref.getEditText().getText().toString());
				if(count < 3 || count > 65535)
				{
					throw new NumberFormatException();
				}
				countPref.setSummary(countPref.getEditText().getText());
			}
			catch(NumberFormatException ex)
			{
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle(getString(R.string.error));  
				alertDialog.setMessage("Please enter a number between 3 and 65535 !");
				alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//Do nothing
				    } 
				}); 
				alertDialog.show();
				countPref.getEditText().setText("3");
				countPref.setSummary(countPref.getEditText().getText());
				return false;
			}
		} else if(preference.getKey().equals("capture_timeout_pref"))
		{
			EditTextPreference timeoutPref = (EditTextPreference) preference;

			try
			{
				int count = Integer.parseInt(timeoutPref.getEditText().getText().toString());
				if(count < 1 || count > 65535)
				{
					throw new NumberFormatException();
				}
				timeoutPref.setSummary(timeoutPref.getEditText().getText());
			}
			catch(NumberFormatException ex)
			{
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle(getString(R.string.error));
				alertDialog.setMessage("Please enter a number between 1 and 65535!");
				alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//Do nothing
					}
				});
				alertDialog.show();
				timeoutPref.getEditText().setText("15");
				timeoutPref.setSummary(timeoutPref.getEditText().getText());
				return false;
			}
		} else if(preference.getKey().equals("threshold_total_score"))
		{
			EditTextPreference ePref = (EditTextPreference) preference;

			try
			{
				int count = Integer.parseInt(ePref.getEditText().getText().toString());
				if(count < 1 || count > 100)
				{
					throw new NumberFormatException();
				}
				ePref.setSummary(ePref.getEditText().getText());
			}
			catch(NumberFormatException ex)
			{
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle(getString(R.string.error));
				alertDialog.setMessage("Please enter a number between 1 and 100!");
				alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//Do nothing
					}
				});
				alertDialog.show();
				ePref.getEditText().setText("50");
				ePref.setSummary(ePref.getEditText().getText());
				return false;
			}
		} else if(preference.getKey().equals("threshold_usable_area"))
		{
			EditTextPreference ePref = (EditTextPreference) preference;

			try
			{
				int count = Integer.parseInt(ePref.getEditText().getText().toString());
				if(count < 1 || count > 100)
				{
					throw new NumberFormatException();
				}
				ePref.setSummary(ePref.getEditText().getText());
			}
			catch(NumberFormatException ex)
			{
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle(getString(R.string.error));
				alertDialog.setMessage("Please enter a number between 1 and 100!");
				alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//Do nothing
					}
				});
				alertDialog.show();
				ePref.getEditText().setText("50");
				ePref.setSummary(ePref.getEditText().getText());
				return false;
			}
		}
		else if(preference.getKey().equals("exposure_pref"))
		{
			EditTextPreference exposurePref = (EditTextPreference) preference;
			
			try
			{
				int count = Integer.parseInt(exposurePref.getEditText().getText().toString());
				if(count < -4 || count > 4)
				{
					throw new NumberFormatException();
				}
				exposurePref.setSummary(exposurePref.getEditText().getText());
			}
			catch(NumberFormatException ex)
			{
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle(getString(R.string.error));  
				alertDialog.setMessage("Please enter a number between -4 and 4 !");
				alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing
				    } 
				}); 
				alertDialog.show();
				exposurePref.getEditText().setText("0");
				exposurePref.setSummary(exposurePref.getEditText().getText());
				return false;
			}
		}
		else if(preference.getKey().equals("prefix_name_pref"))
		{
			EditTextPreference preNamePref = (EditTextPreference) preference;
			Pattern p = Pattern.compile("^[A-Za-z0-9_]*$");
			String txtPrefix = preNamePref.getEditText().getText().toString();
			boolean isValidCharacters = p.matcher(txtPrefix).matches();
			if(false == isValidCharacters)
			{
				AlertDialog dlg = new AlertDialog.Builder(this).create();
				dlg.setMessage("Invalid prefix name!");
				dlg.setTitle(getString(R.string.error));
				dlg.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				dlg.show();
				return false;
			}
			
			preNamePref.setSummary(preNamePref.getEditText().getText());
		}
		else if(preference.getKey().equals("output_dir_pref"))
		{
			EditTextPreference outputPref = (EditTextPreference) preference;
			outputPref.setSummary(outputPref.getEditText().getText());
		}
		else
		{
			ListPreference listPref;
			if(preference instanceof ListPreference)
			{
				listPref = (ListPreference) preference;
				int selectedIndex = 0;
				if(preference.getKey().equals("capture_mode_pref"))
				{
					selectedIndex = listPref.findIndexOfValue(newValue.toString());
					listPref.setSummary(listPref.getEntries()[selectedIndex].toString());
				}
				else if(preference.getKey().equals("quality_mode_pref"))
				{
					selectedIndex = listPref.findIndexOfValue(newValue.toString());
					listPref.setSummary(listPref.getEntries()[selectedIndex].toString());
				}
				else if(preference.getKey().equals("operation_mode_pref"))
				{
					selectedIndex = listPref.findIndexOfValue(newValue.toString());
					listPref.setSummary(listPref.getEntries()[selectedIndex].toString());
				}
				else if(preference.getKey().equals("stream_image_scale_pref"))
				{
					selectedIndex = listPref.findIndexOfValue(newValue.toString());
					listPref.setSummary(listPref.getEntries()[selectedIndex].toString());
				}
			}
		}
		return true;
	}

	public boolean onPreferenceClick(Preference preference)
	{
	    EditTextPreference countPref = (EditTextPreference) findPreference("count_interval_pref");
	    countPref.getEditText().setText(countPref.getSummary());

		EditTextPreference timeoutPref = (EditTextPreference) findPreference("capture_timeout_pref");
		timeoutPref.getEditText().setText(timeoutPref.getSummary());

		EditTextPreference totalScorePref = (EditTextPreference) findPreference("threshod_total_score");
		if (totalScorePref != null) {
			totalScorePref.getEditText().setText(totalScorePref.getSummary());
		}

		EditTextPreference usableAreaPref = (EditTextPreference) findPreference("threshold_usable_area");
		if (usableAreaPref != null) {
			usableAreaPref.getEditText().setText(usableAreaPref.getSummary());
		}

	    EditTextPreference outputPref = (EditTextPreference) findPreference("output_dir_pref");
	    if (outputPref != null) {
			outputPref.getEditText().setText(outputPref.getSummary());
		}
	    
	    EditTextPreference preNamePref = (EditTextPreference) findPreference("prefix_name_pref");
		if (preNamePref != null) {
			preNamePref.getEditText().setText(preNamePref.getSummary());
		}
		return true;
	}
}
