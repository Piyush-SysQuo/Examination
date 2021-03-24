package com.inspection.examination.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.inspection.examination.database.db.DataBaseClient;
import com.inspection.examination.model.GetCandidateDataResponse.CandidateData;
import com.inspection.examination.model.ListStudent;
import com.inspection.examination.model.LoginResponse.LoginAPIResponse;
import com.inspection.examination.model.RESULT;
import com.inspection.examination.model.SyncDataResponse.CandidatesList;
import com.inspection.examination.util.Constants;
import com.inspection.examination.util.ProgressDialog;
import com.inspection.examination.util.Util;
import com.inspection.examination.util.Utility;
import com.inspection.examination.viewmodel.LogoutViewModel;
import com.inspection.examination.viewmodel.UpdateOccurrenceViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private CandidateData result;
    private CandidatesList dataList;
    private String from;
    private LoginAPIResponse resp;
    private String token, rollNumber, useCase;
    private ProgressDialog appDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        appDialog = new ProgressDialog(DetailsActivity.this);

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
        TextView txt_iris_key = findViewById(R.id.txt_iris_status);
        TextView txt_iris_val = findViewById(R.id.txt_iris_status_val);
        TextView txt_iris_img_key = findViewById(R.id.txt_iris_img_key);
        ImageView irisImageView = findViewById(R.id.irisImageView);
        Button btn_accept = findViewById(R.id.btn_accept);
        Button btn_deny = findViewById(R.id.btn_deny);
        LinearLayout faceStatusLayout = findViewById(R.id.faceStatusLayout);
        LinearLayout irisStatusLayout = findViewById(R.id.irisStatusLayout);
        LinearLayout irisImageLayout = findViewById(R.id.irisImageLayout);


        back.setOnClickListener(this);
        btn_accept.setOnClickListener(this);
        btn_deny.setOnClickListener(this);
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
             from = (String) intent.getExtras().get(Constants.KEY_FROM);

            if(from.equals(Constants.VAL_FROM_SERVER)) {
                result = Parcels.unwrap(intent.getParcelableExtra(Constants.KEY_RESULT));

            }
            else{
                dataList = Parcels.unwrap(intent.getParcelableExtra(Constants.KEY_RESULT));
            }

            if (intent.hasExtra(Constants.USE_CASE)){
                useCase = (String) intent.getExtras().get(Constants.USE_CASE);
            }else{
                useCase = Constants.REGISTER_CAND;
            }
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
        txt_iris_key.setTypeface(typeface_bold);
        txt_iris_val.setTypeface(typeface_reg);
        txt_iris_img_key.setTypeface(typeface_bold);


        // set data into view's
        if(result != null) {
            if (from.equals(Constants.VAL_FROM_SERVER)) {

//                new Thread(() -> {
////                    // Update Occurrance to DB
////                    int occ =  CandidatesDBClient.getInstance(DetailsActivity.this).getAppDatabase().candidatesDAO()
////                            .getOccurrenceForStudent(String.valueOf(result.getRollNumber()));
////
////                    int occurrance = occ+1;
////
////                    CandidatesDBClient.getInstance(DetailsActivity.this).getAppDatabase().candidatesDAO()
////                                .updateOccurranceForStudent(String.valueOf(result.getRollNumber()), occurrance);
////
////
//////                    long occData =  DataBaseClient.getInstance(DetailsActivity.this).getAppDatabase().examinationDao()
//////                            .getOccurrence(String.valueOf(result.getRollNo()));
//
//
//                    runOnUiThread(() -> {
//
////                        callUpdateOccurranceAPI(token, String.valueOf(result.getRollNumber()));
//
//                        txt_name_val.setText(result.getName());
//                        txt_roll_val.setText(result.getRollNumber());
//                        txt_lab_val.setText(result.getLabAllotted());
//                        txt_time_val.setText(result.getExamStartTime()+" - "+result.getExamEndTime());
//                        txt_occ_val.setText(String.valueOf(result.getOccurrance()));
//                        txt_seat_val.setText(result.getSeatAllotted());
//                        txt_dob_val.setText(result.getDob());
//                        txt_cat_val.setText(result.getCategory());
//                        txt_reportt_val.setText(result.getReportingTime());
//                        txt_center_val.setText(result.getCentreName());
//                        txt_app_val.setText(result.getApplicationNumber());
//                        txt_face_val.setText(result.getFaceStatus());
//                        txt_iris_val.setText(result.getIrisStatus());
//
//                        if (result.getFaceStatus().matches("Not Registered")){
//                            txt_face_val.setTextColor(this.getColor(R.color.red));
//                        }else if (result.getFaceStatus().matches("Registration Bypassed")){
//                            txt_face_val.setTextColor(this.getColor(R.color.light_orange));
//                        }else if (result.getFaceStatus().matches("Not Verified")){
//                            txt_face_val.setTextColor(this.getColor(R.color.red));
//                        }else if (result.getFaceStatus().matches("Verification Bypassed")){
//                            txt_face_val.setTextColor(this.getColor(R.color.light_orange));
//                        }else {
//                            txt_face_val.setTextColor(this.getColor(R.color.green));
//                        }
//
//                        if (result.getIrisStatus().matches("Not Registered")){
//                            txt_iris_val.setTextColor(this.getColor(R.color.red));
//                        }else if (result.getIrisStatus().matches("Registration Bypassed")){
//                            txt_iris_val.setTextColor(this.getColor(R.color.light_orange));
//                        }else if (result.getIrisStatus().matches("Not Verified")){
//                            txt_iris_val.setTextColor(this.getColor(R.color.red));
//                        }else if (result.getIrisStatus().matches("Verification Bypassed")){
//                            txt_iris_val.setTextColor(this.getColor(R.color.light_orange));
//                        }else {
//                            txt_iris_val.setTextColor(this.getColor(R.color.green));
//                        }
//
//                        // set image url to profile image view
//                        Glide.with(DetailsActivity.this).load(result.getImageURL()).into(profile);
//
//                        // set image url to iris image view
//                        Glide.with(DetailsActivity.this).load(result.getIrisImageURL()).into(irisImageView);
//
//                    });
//                }).start();
                txt_name_val.setText(result.getName());
                txt_roll_val.setText(result.getRollNumber());
                txt_lab_val.setText(result.getLabAllotted());
                txt_time_val.setText(result.getExamStartTime()+" - "+result.getExamEndTime());
                txt_occ_val.setText(String.valueOf(result.getOccurrance()));
                txt_seat_val.setText(result.getSeatAllotted());
                txt_dob_val.setText(result.getDob());
                txt_cat_val.setText(result.getCategory());
                txt_reportt_val.setText(result.getReportingTime());
                txt_center_val.setText(result.getCentreName());
                txt_app_val.setText(result.getApplicationNumber());
                txt_face_val.setText(result.getFaceStatus());
                txt_iris_val.setText(result.getIrisStatus());
                rollNumber = result.getRollNumber();
                if (result.getFaceStatus().matches("Not Registered")){
                    txt_face_val.setTextColor(this.getColor(R.color.red));
                }else if (result.getFaceStatus().matches("Registration Bypassed")){
                    txt_face_val.setTextColor(this.getColor(R.color.light_orange));
                }else if (result.getFaceStatus().matches("Not Verified")){
                    txt_face_val.setTextColor(this.getColor(R.color.red));
                }else if (result.getFaceStatus().matches("Verification Bypassed")){
                    txt_face_val.setTextColor(this.getColor(R.color.light_orange));
                }else {
                    txt_face_val.setTextColor(this.getColor(R.color.green));
                }

                if (result.getIrisStatus().matches("Not Registered")){
                    txt_iris_val.setTextColor(this.getColor(R.color.red));
                }else if (result.getIrisStatus().matches("Registration Bypassed")){
                    txt_iris_val.setTextColor(this.getColor(R.color.light_orange));
                }else if (result.getIrisStatus().matches("Not Verified")){
                    txt_iris_val.setTextColor(this.getColor(R.color.red));
                }else if (result.getIrisStatus().matches("Verification Bypassed")){
                    txt_iris_val.setTextColor(this.getColor(R.color.light_orange));
                }else {
                    txt_iris_val.setTextColor(this.getColor(R.color.green));
                }

                // set image url to profile image view
                Glide.with(DetailsActivity.this).load(result.getImageURL())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).into(profile);

                // set image url to iris image view
                Glide.with(DetailsActivity.this).load(result.getIrisImageURL())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).into(irisImageView);

            }
        }
        else {

//                new Thread(() -> {
////                    CandidatesList list =  CandidatesDBClient.getInstance(DetailsActivity.this).getAppDatabase().candidatesDAO()
////                            .getSingleStudentData(String.valueOf(dataList.getRollNumber()));
////
////                    int occ =  list.getOccurrance();
////
////                    int occurrance = occ+1;
////
////                    CandidatesDBClient.getInstance(DetailsActivity.this).getAppDatabase().candidatesDAO()
////                            .updateOccurranceForStudent(String.valueOf(list.getRollNumber()), occurrance);
//
//                    runOnUiThread(() -> {
//
//                        txt_name_val.setText(list.getName());
//                        txt_roll_val.setText(String.valueOf(list.getRollNumber()));
//                        txt_lab_val.setText(list.getLabAllotted());
//                        txt_time_val.setText(list.getExamStartTime()+" - "+list.getExamEndTime());
//                        txt_occ_val.setText(String.valueOf(occurrance));
//                        txt_seat_val.setText(list.getSeatAllotted());
//                        txt_dob_val.setText(list.getDob());
//                        txt_cat_val.setText(list.getCategory());
//                        txt_reportt_val.setText(list.getReportingTime());
//                        txt_center_val.setText(list.getCentreName());
//                        txt_app_val.setText(list.getApplicationNumber());
//
//                        // set image url to circle image view
//                        byte[] decodedString = Base64.decode(list.getImageString(), Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        profile.setImageBitmap(decodedByte);
//                    });
//                }).start();
            txt_name_val.setText(dataList.getName());
            txt_roll_val.setText(String.valueOf(dataList.getRollNumber()));
            txt_lab_val.setText(dataList.getLabAllotted());
            txt_time_val.setText(dataList.getExamStartTime()+" - "+dataList.getExamEndTime());
            txt_occ_val.setText(String.valueOf(dataList.getOccurrance()));
            txt_seat_val.setText(dataList.getSeatAllotted());
            txt_dob_val.setText(dataList.getDob());
            txt_cat_val.setText(dataList.getCategory());
            txt_reportt_val.setText(dataList.getReportingTime());
            txt_center_val.setText(dataList.getCentreName());
            txt_app_val.setText(dataList.getApplicationNumber());
            txt_face_val.setText(dataList.getFaceStatus());
            txt_iris_val.setText(dataList.getIrisStatus());
            rollNumber = dataList.getRollNumber();

            if (dataList.getFaceStatus().matches("Not Registered")){
                txt_face_val.setTextColor(this.getColor(R.color.red));
            }else if (dataList.getFaceStatus().matches("Registration Bypassed")){
                txt_face_val.setTextColor(this.getColor(R.color.light_orange));
            }else if (dataList.getFaceStatus().matches("Not Verified")){
                txt_face_val.setTextColor(this.getColor(R.color.red));
            }else if (dataList.getFaceStatus().matches("Verification Bypassed")){
                txt_face_val.setTextColor(this.getColor(R.color.light_orange));
            }else {
                txt_face_val.setTextColor(this.getColor(R.color.green));
            }

            if (dataList.getIrisStatus().matches("Not Registered")){
                txt_iris_val.setTextColor(this.getColor(R.color.red));
            }else if (dataList.getIrisStatus().matches("Registration Bypassed")){
                txt_iris_val.setTextColor(this.getColor(R.color.light_orange));
            }else if (dataList.getIrisStatus().matches("Not Verified")){
                txt_iris_val.setTextColor(this.getColor(R.color.red));
            }else if (dataList.getIrisStatus().matches("Verification Bypassed")){
                txt_iris_val.setTextColor(this.getColor(R.color.light_orange));
            }else {
                txt_iris_val.setTextColor(this.getColor(R.color.green));
            }


            // set image url to profile image view
            byte[] decodedString = Base64.decode(dataList.getImageString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profile.setImageBitmap(decodedByte);

            // set image url to iris image view
            byte[] decodedString2 = Base64.decode(dataList.getIrisImageString(), Base64.DEFAULT);
            Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
            irisImageView.setImageBitmap(decodedByte2);
        }

        // Hiding the unnecessary views on the basis of authentication modules allowed
        if (!resp.getData().getCentreInfo().getFaceAuth()){
            // Face Auth Disabled
            // Hide the face layouts
            faceStatusLayout.setVisibility(View.GONE);
        }

//        if (!resp.getData().getCentreInfo().getIrisAuth()){
//            // Iris Auth Disabled
//            // Hide the iris layouts
//            irisStatusLayout.setVisibility(View.GONE);
//            irisImageLayout.setVisibility(View.GONE);
//        }


        /**
         *  Uncomment above code and comment below code for enabling iris auth
         * */
        if (true){
            // Iris Auth Disabled
            // Hide the iris layouts
            irisStatusLayout.setVisibility(View.GONE);
            irisImageLayout.setVisibility(View.GONE);
        }


        if(txt_iris_val.getText().toString().matches("Registration Bypassed")){
            irisImageLayout.setVisibility(View.GONE);
        }

        if(txt_iris_val.getText().toString().matches("Verification Bypassed")){
            irisImageLayout.setVisibility(View.GONE);
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
                    int occ =  CandidatesDBClient.getInstance(DetailsActivity.this).getAppDatabase().candidatesDAO()
                            .getOccurrenceForStudent(rollNumber);

                    int occurrance = occ+1;

                    CandidatesDBClient.getInstance(DetailsActivity.this).getAppDatabase().candidatesDAO()
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
                    Toast.makeText(DetailsActivity.this, updateOccurrenceAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DetailsActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            new Thread(() -> {
                    CandidatesList list =  CandidatesDBClient.getInstance(DetailsActivity.this).getAppDatabase().candidatesDAO()
                            .getSingleStudentData(rollNumber);

                    int occ =  list.getOccurrance();

                    int occurrance = occ+1;

                    CandidatesDBClient.getInstance(DetailsActivity.this).getAppDatabase().candidatesDAO()
                            .updateOccurranceForStudent(rollNumber, occurrance);

//                    runOnUiThread(() -> {
//
//                        txt_name_val.setText(list.getName());
//                        txt_roll_val.setText(String.valueOf(list.getRollNumber()));
//                        txt_lab_val.setText(list.getLabAllotted());
//                        txt_time_val.setText(list.getExamStartTime()+" - "+list.getExamEndTime());
//                        txt_occ_val.setText(String.valueOf(occurrance));
//                        txt_seat_val.setText(list.getSeatAllotted());
//                        txt_dob_val.setText(list.getDob());
//                        txt_cat_val.setText(list.getCategory());
//                        txt_reportt_val.setText(list.getReportingTime());
//                        txt_center_val.setText(list.getCentreName());
//                        txt_app_val.setText(list.getApplicationNumber());
//
//                        // set image url to circle image view
//                        byte[] decodedString = Base64.decode(list.getImageString(), Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        profile.setImageBitmap(decodedByte);
//                    });
            }).start();
            Toast.makeText(this, "Occurrence Updated Successfully. Candidate attendance marked!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.img_back :
                finish();
                break;
            case R.id.btn_accept :
                callUpdateOccurranceAPI(token,rollNumber);
                checkRunTimePermission();
                break;
            case R.id.btn_deny :
                Toast.makeText(this, "Attendance won't be marked for this candidate.", Toast.LENGTH_SHORT).show();
                checkRunTimePermission();
                break;

        }
    }

   // check phone state run time permission
   protected void checkRunTimePermission(){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent = new Intent(DetailsActivity.this,ScannerActivity.class);
                        intent.putExtra(Constants.USE_CASE,useCase);
                        startActivity(intent);
                        DetailsActivity.this.finish();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
   }

}