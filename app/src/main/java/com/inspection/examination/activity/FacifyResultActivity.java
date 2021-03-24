package com.inspection.examination.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.reflect.TypeToken;
import com.inspection.examination.R;
import com.inspection.examination.database.db.CandidatesDBClient;
import com.inspection.examination.model.LoginResponse.LoginAPIResponse;
import com.inspection.examination.model.ScanWithFaceResponse.CandidateData;
import com.inspection.examination.model.SyncDataResponse.CandidatesList;
import com.inspection.examination.util.Constants;
import com.inspection.examination.util.ProgressDialog;
import com.inspection.examination.util.Util;
import com.inspection.examination.util.Utility;
import com.inspection.examination.viewmodel.UpdateOccurrenceViewModel;

import org.parceler.Parcels;

import java.io.File;

public class FacifyResultActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_accept,btn_bypass;
    private ProgressDialog appDialog;

    private LoginAPIResponse resp;
    private String token,rollNumber,faceStatus="Not Verified";
    private double confidence;

    private CandidateData candData;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facify_result);
        // Hide the status bar.
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        appDialog = new ProgressDialog(FacifyResultActivity.this);
        // find id's
        // find id's
        ImageView back = findViewById(R.id.img_back);
        TextView txt_name_val = findViewById(R.id.txt_name_val);
        TextView txt_roll_key = findViewById(R.id.txt_roll_key);
        TextView txt_roll_val = findViewById(R.id.txt_roll_val);
        TextView txt_lab_key = findViewById(R.id.txt_lab_key);
        TextView txt_lab_val = findViewById(R.id.txt_lab_val);
        TextView txt_time_val = findViewById(R.id.txt_time_val);
        TextView txt_occ_key = findViewById(R.id.txt_occ_key);
        TextView txt_occ_val = findViewById(R.id.txt_occ_val);
        TextView txt_seat_key = findViewById(R.id.txt_seat_key);
        TextView txt_seat_val = findViewById(R.id.txt_seat_val);
        ImageView profile = findViewById(R.id.profile_image);
        TextView txt_dob_key = findViewById(R.id.txt_dob_key);
        TextView txt_dob_val = findViewById(R.id.txt_dob_val);
        TextView txt_cat_key = findViewById(R.id.txt_cat_key);
        TextView txt_cat_val = findViewById(R.id.txt_cat_val);
        TextView txt_reportt_key = findViewById(R.id.txt_reportt_key);
        TextView txt_reportt_val = findViewById(R.id.txt_reportt_val);
        TextView txt_center_val = findViewById(R.id.txt_center_val);
        TextView txt_app_key = findViewById(R.id.txt_app_key);
        TextView txt_app_val = findViewById(R.id.txt_app_val);
        TextView txt_face_key = findViewById(R.id.txt_face_status);
        TextView txt_face_val = findViewById(R.id.txt_face_status_val);
        TextView txt_confidence_key = findViewById(R.id.txt_confidence);
        TextView txt_confidence_val = findViewById(R.id.txt_confidence_val);

        ImageView faceImage1 = findViewById(R.id.faceImage1);
        ImageView faceImage2 = findViewById(R.id.faceImage2);
        Button btn_accept = findViewById(R.id.btn_accept);
        Button btn_bypass = findViewById(R.id.btn_bypass);
        LinearLayout faceStatusLayout = findViewById(R.id.faceStatusLayout);
        LinearLayout faceImageLayout = findViewById(R.id.faceImagesLayout);

        back.setOnClickListener(this);
        btn_accept.setOnClickListener(this);
        btn_bypass.setOnClickListener(this);
        // get Token
        resp = (LoginAPIResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginAPIResponse>() {
                });

        if (resp != null){
            token = resp.getData().getToken();
        }

        // get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            candData = Parcels.unwrap(intent.getParcelableExtra(Constants.KEY_RESULT));
            faceStatus = intent.getStringExtra(Constants.KEY_FACE_STATUS);
            confidence = intent.getDoubleExtra(Constants.KEY_CONFIDENCE,0.0);
        }

        // set custom font
        Typeface typeface_bold = Typeface.createFromAsset(getAssets(), "Metropolis_SemiBold.ttf");
        Typeface typeface_reg = Typeface.createFromAsset(getAssets(), "Metropolis_Regular.ttf");
        txt_roll_key.setTypeface(typeface_bold);
        txt_lab_key.setTypeface(typeface_bold);
        txt_occ_key.setTypeface(typeface_bold);
        txt_seat_key.setTypeface(typeface_bold);
        txt_dob_key.setTypeface(typeface_bold);
        txt_cat_key.setTypeface(typeface_bold);
        txt_reportt_key.setTypeface(typeface_bold);
        txt_app_key.setTypeface(typeface_bold);
        txt_name_val.setTypeface(typeface_reg);
        txt_roll_val.setTypeface(typeface_reg);
        txt_lab_val.setTypeface(typeface_reg);
        txt_time_val.setTypeface(typeface_reg);
        txt_occ_val.setTypeface(typeface_reg);
        txt_seat_val.setTypeface(typeface_reg);
        txt_dob_val.setTypeface(typeface_reg);
        txt_cat_val.setTypeface(typeface_reg);
        txt_reportt_val.setTypeface(typeface_reg);
        txt_center_val.setTypeface(typeface_reg);
        txt_app_val.setTypeface(typeface_reg);
        txt_face_key.setTypeface(typeface_bold);
        txt_face_val.setTypeface(typeface_reg);
        txt_confidence_key.setTypeface(typeface_bold);
        txt_confidence_val.setTypeface(typeface_reg);


        // set data into view's
        if(candData != null) {
            txt_name_val.setText(candData.getName());
            txt_roll_val.setText(candData.getRollNumber());
            txt_lab_val.setText(candData.getLabAllotted());
            txt_time_val.setText(candData.getExamStartTime()+" - "+candData.getExamEndTime());
            txt_occ_val.setText(String.valueOf(candData.getOccurrance()));
            txt_seat_val.setText(candData.getSeatAllotted());
            txt_dob_val.setText(candData.getDob());
            txt_cat_val.setText(candData.getCategory());
            txt_reportt_val.setText(candData.getReportingTime());
            txt_center_val.setText(candData.getCentreName());
            txt_app_val.setText(candData.getApplicationNumber());
            txt_face_val.setText(faceStatus);
            txt_confidence_val.setText(String.valueOf(confidence));
            rollNumber = candData.getRollNumber();
            if (!faceStatus.trim().matches("Verified")){
                txt_face_val.setTextColor(this.getColor(R.color.red));
                btn_accept.setVisibility(View.GONE);
                btn_bypass.setVisibility(View.VISIBLE);
            }else {
                txt_face_val.setTextColor(this.getColor(R.color.green));
                btn_accept.setVisibility(View.VISIBLE);
                btn_bypass.setVisibility(View.GONE);
            }

            // set image url to profile image view
            Glide.with(FacifyResultActivity.this).load(candData.getImageURL())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(profile);

            // set image url to face image view
            Glide.with(FacifyResultActivity.this).load(candData.getImageURL())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(faceImage1);
            Glide.with(FacifyResultActivity.this).load(candData.getFaceImageURL())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(faceImage2);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_back :
                finish();
                break;
            case R.id.btn_accept :
                callUpdateOccurranceAPI(token,rollNumber);
                break;
            case R.id.btn_bypass :
                Toast.makeText(this, "Allowing candidate to sit in exam.", Toast.LENGTH_SHORT).show();
                callUpdateOccurranceAPI(token,rollNumber);
                break;
        }
    }


    private void callUpdateOccurranceAPI(String token, String rollNumber) {
        /* check internet connected or not*/
        boolean isConnected = new Utility().getInternetConnection(this);

        if(isConnected){
            if (appDialog != null) {
                appDialog.showLoader();
            }

            new Thread(() -> {
                // Update Occurrance to DB
                int occ =  CandidatesDBClient.getInstance(FacifyResultActivity.this).getAppDatabase().candidatesDAO()
                        .getOccurrenceForStudent(rollNumber);

                int occurrance = occ+1;

                CandidatesDBClient.getInstance(FacifyResultActivity.this).getAppDatabase().candidatesDAO()
                        .updateOccurranceForStudent(rollNumber, occurrance);

            }).start();

            // call update occurrence Api
            UpdateOccurrenceViewModel viewModel = ViewModelProviders.of(this).get(UpdateOccurrenceViewModel.class);
            viewModel.init();
            viewModel.updateOccurrence(token, rollNumber);
            viewModel.getVolumesResponseLiveData().observe(this, updateOccurrenceAPIResponse -> {
                if (appDialog != null) {
                    appDialog.dismiss();
                }
                if(updateOccurrenceAPIResponse != null) {
//                    Boolean status = updateOccurrenceAPIResponse.getStatus();
                    Toast.makeText(FacifyResultActivity.this, updateOccurrenceAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(FacifyResultActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            new Thread(() -> {
                CandidatesList list =  CandidatesDBClient.getInstance(FacifyResultActivity.this).getAppDatabase().candidatesDAO()
                        .getSingleStudentData(rollNumber);

                int occ =  list.getOccurrance();

                int occurrance = occ+1;

                CandidatesDBClient.getInstance(FacifyResultActivity.this).getAppDatabase().candidatesDAO()
                        .updateOccurranceForStudent(rollNumber, occurrance);
            }).start();
            Toast.makeText(this, "Occurrence Updated Successfully. Candidate attendance marked!", Toast.LENGTH_SHORT).show();
        }
        this.finish();
    }

}