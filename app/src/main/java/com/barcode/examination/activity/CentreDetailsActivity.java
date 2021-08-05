package com.barcode.examination.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.barcode.examination.R;
import com.barcode.examination.database.db.CandidatesDBClient;
import com.barcode.examination.model.LoginResponse.LoginAPIResponse;
import com.barcode.examination.model.SyncDataResponse.CandidatesList;
import com.barcode.examination.model.SyncDataResponse.SyncDataAPIResponse;
import com.barcode.examination.util.Constants;
import com.barcode.examination.util.DialogCaller;
import com.barcode.examination.util.ProgressDialog;
import com.barcode.examination.util.Util;
import com.barcode.examination.util.Utility;
import com.barcode.examination.viewmodel.LogoutViewModel;
import com.barcode.examination.viewmodel.SyncDataViewModel;

import java.util.List;

public class CentreDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView centreCodeTV,centreNameTV,examNameTV,examDateTV,examTimeTV,reportingTV,modulesTV;
    private Button acceptBtn,logoutBtn;
    private ProgressDialog appDialog;
    private LogoutViewModel viewModel;
    private LoginAPIResponse resp;
    private List<CandidatesList> list;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre_details);

        // Hide the status bar.
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        appDialog = new ProgressDialog(CentreDetailsActivity.this);

        centreCodeTV = findViewById(R.id.txt_centre_code_val);
        centreNameTV = findViewById(R.id.txt_center_val);
        examNameTV = findViewById(R.id.txt_exam_name_val);
        examDateTV = findViewById(R.id.txt_date_val);
        examTimeTV = findViewById(R.id.txt_time_val);
        reportingTV = findViewById(R.id.txt_reportt_val);
        modulesTV = findViewById(R.id.txt_modules_val);
        acceptBtn = findViewById(R.id.btn_accept);
        logoutBtn = findViewById(R.id.btn_logout);

        // set custom font
        Typeface typeface_reg = Typeface.createFromAsset(getAssets(), "Metropolis_Regular.ttf");
        Typeface typeface_bold = Typeface.createFromAsset(getAssets(), "Metropolis_SemiBold.ttf");
        centreCodeTV.setTypeface(typeface_bold);
        examNameTV.setTypeface(typeface_bold);
        reportingTV.setTypeface(typeface_bold);
        modulesTV.setTypeface(typeface_bold);
        acceptBtn.setTypeface(typeface_bold);
        logoutBtn.setTypeface(typeface_bold);
        centreNameTV.setTypeface(typeface_reg);
        examDateTV.setTypeface(typeface_reg);
        examTimeTV.setTypeface(typeface_reg);


        // get Token
        resp = (LoginAPIResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginAPIResponse>() {
                });

        if (resp != null){
            token = resp.getData().getToken();

            centreCodeTV.setText(resp.getData().getCentreInfo().getCenterCode());
            centreNameTV.setText(resp.getData().getCentreInfo().getCenterName());
            examNameTV.setText(resp.getData().getCentreInfo().getExamName());

//            Piyush Comment below code after Date Issue solved
            String examDate = Utility.getFormatedDateTime(resp.getData().getCentreInfo().getCurrentExamDate(),"yyyy-MM-dd","EEE, dd MMM, yyyy");
            examDateTV.setText(examDate);
            String examStartTime = Utility.getFormatedDateTime(resp.getData().getCentreInfo().getExamStartTime(),"h:mm:ss","h:mm a");
            String examEndTime = Utility.getFormatedDateTime(resp.getData().getCentreInfo().getExamEndTime(),"h:mm:ss","h:mm a");
            examTimeTV.setText(resp.getData().getCentreInfo().getCurrentExamShift() + ", " + examStartTime + " - " + examEndTime);
            String reportingTime = Utility.getFormatedDateTime(resp.getData().getCentreInfo().getReportingTime(),"h:mm:ss","h:mm a");
            reportingTV.setText(reportingTime);
            String module = "";
            for (int i= 0; i<resp.getData().getCentreInfo().getModules().size();i++){
                module += "- " + resp.getData().getCentreInfo().getModules().get(i) + "\n";
            }
            modulesTV.setText(module);
        }

        acceptBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);




    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.btn_logout :
                showDialog("Logout",getString(R.string.exit));
                break;
            case R.id.btn_accept :
                sync();
                Util.getInstance(CentreDetailsActivity.this).setString(Constants.DATA_DOWNLOADED,"true");
                break;
        }
    }

    // show dialog
    private void showDialog(String title, String msg) {

        DialogCaller.showDialog(CentreDetailsActivity.this, title,msg , (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // Add your code if it positive button
                    if (title.matches("Logout")){
                        logout();
                    }
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // Add your code if it negative button
                    dialog.dismiss();
                    break;
            }
        });

    }

    // logout from application
    private void logout() {

        callLogoutAPI();
    }

    private void callLogoutAPI() {
        /* check internet connected or not*/
        boolean isConnected = new Utility().getInternetConnection(this);

        if(isConnected){
            if(appDialog != null){
                appDialog.showLoader();
            }
            // call login Api
            viewModel = ViewModelProviders.of(this).get(LogoutViewModel.class);
            viewModel.init();
            viewModel.userLogout(token);
            viewModel.getVolumesResponseLiveData().observe(this, logoutResponse -> {
                if(appDialog != null){
                    appDialog.dismiss();
                }
                if(logoutResponse != null) {
                    Boolean status = logoutResponse.getStatus();
                    // Clear Session
                    Util.logout(this);
                    // Go to login page
                    Util.getInstance(CentreDetailsActivity.this).setString(Constants.DATA_DOWNLOADED,"false");
                    finish();
                    startActivity(new Intent(this, LogInActivity.class));
                    Toast.makeText(CentreDetailsActivity.this, logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CentreDetailsActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(this, getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    //get data from api and insert into local database
    private void sync() {

        boolean isConnected = new Utility().getInternetConnection(this);

        if(isConnected) {

            if (appDialog != null) {
                appDialog.showLoader();
            }

            SyncDataViewModel viewModel = ViewModelProviders.of(this).get(SyncDataViewModel.class);
            viewModel.init();
            viewModel.syncData(token);
            viewModel.getVolumesResponseLiveData().observe(this, new Observer<SyncDataAPIResponse>() {
                @Override
                public void onChanged(SyncDataAPIResponse syncDataAPIResponse) {
                    if (appDialog != null) {
                        appDialog.dismiss();
                    }
                    if (syncDataAPIResponse != null) {
                        list = syncDataAPIResponse.getData().getCandidatesList();
                        // save data into local
                        if (list != null) {
                            Toast.makeText(CentreDetailsActivity.this, "Data saved!", Toast.LENGTH_SHORT).show();
                            saveDataIntoLocal(list);
                        } else {
                            showDialog("Information",getString(R.string.data_not_found));
                        }
                    } else {
                        showDialog("Error",getString(R.string.error_msg));
                    }
                }
            });
        }else {
            showDialog("Error",getString(R.string.internet_connection));
        }

    }

    // save data into local db
    protected void saveDataIntoLocal(List<CandidatesList> list){

        new Thread(() -> {

            CandidatesDBClient.getInstance(CentreDetailsActivity.this).getAppDatabase()
                    .candidatesDAO().insertAllCandidatesData(list);


        }).start();

        //  card_sync.setVisibility(View.GONE);
        //  card_save.setVisibility(View.VISIBLE);

        showMessageDialog(getString(R.string.data_sync_msg));
    }

    // show popup
    private void showMessageDialog(String msg){
        DialogCaller.showDialog(CentreDetailsActivity.this, "",msg , (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // Add your code if it positive button
                    startActivity(new Intent(this,OptionActivity.class));
                    finish();
                    dialog.dismiss();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // Add your code if it negative button
                    break;
            }
        });
    }

}