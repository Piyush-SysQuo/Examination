package com.barcode.examination.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.barcode.examination.R;
import com.barcode.examination.database.db.CandidatesDBClient;
import com.barcode.examination.model.DataToServer;
import com.barcode.examination.model.LoginResponse.LoginAPIResponse;
import com.barcode.examination.model.SyncDataResponse.CandidatesList;
import com.barcode.examination.model.SyncDataResponse.SyncDataAPIResponse;
import com.barcode.examination.util.Constants;
import com.barcode.examination.util.DialogCaller;
import com.barcode.examination.util.ProgressDialog;
import com.barcode.examination.util.Util;
import com.barcode.examination.util.Utility;
import com.barcode.examination.viewmodel.SaveDataToServerViewModel;
import com.barcode.examination.viewmodel.SyncDataViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoreOptions extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog appDialog;
    private List<CandidatesList> list;
    private CardView card_save, card_sync, card_addToCentre, card_verifyCandidate;
    private LoginAPIResponse resp;
    private String token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_options);

        // Hide the status bar.
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        // find id's
        LinearLayout btn_sv = findViewById(R.id.btn_sv);
        LinearLayout save = findViewById(R.id.btn_savetoserver);
        LinearLayout sync = findViewById(R.id.btn_sync);
        LinearLayout addToCentre = findViewById(R.id.btn_add_to_centre);
        LinearLayout verifyCandidate = findViewById(R.id.btn_verify_candidate);
        ImageView back = findViewById(R.id.img_back);
        TextView txt_arrow = findViewById(R.id.txt_arrow);
        TextView txt_btn_sv = findViewById(R.id.txt_btn_sv);
        TextView txt_btn_savetoserver = findViewById(R.id.txt_btn_savetoserver);
        TextView txt_btn_sync = findViewById(R.id.txt_btn_sync);
        TextView txt_btn_addToCentre = findViewById(R.id.txt_btn_add_to_centre);
        TextView txt_btn_verifyCandidate = findViewById(R.id.txt_btn_verify_candidate);
        card_save = findViewById(R.id.card_save);
        card_sync = findViewById(R.id.card_sync);
        card_addToCentre = findViewById(R.id.card_add_to_centre);
        card_verifyCandidate = findViewById(R.id.card_verify_candidate);

        btn_sv.setOnClickListener(this);
        save.setOnClickListener(this);
        sync.setOnClickListener(this);
        addToCentre.setOnClickListener(this);
        verifyCandidate.setOnClickListener(this);
        back.setOnClickListener(this);

        Typeface typeface_reg = Typeface.createFromAsset(getAssets(), "Metropolis_Regular.ttf");
        Typeface typeface_bold = Typeface.createFromAsset(getAssets(), "Metropolis_SemiBold.ttf");
        txt_btn_sv.setTypeface(typeface_bold);
        txt_btn_savetoserver.setTypeface(typeface_bold);
        txt_btn_sync.setTypeface(typeface_bold);
        txt_btn_addToCentre.setTypeface(typeface_bold);
        txt_btn_verifyCandidate.setTypeface(typeface_bold);
        txt_arrow.setTypeface(typeface_bold);

        // get Token
        resp = (LoginAPIResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginAPIResponse>() {
                });

        if (resp != null){
            token = resp.getData().getToken();
        }


        appDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_sv :
                startActivity(new Intent(this,PresentActivity.class));
                break;
            case R.id.btn_savetoserver :
                saveDataToServer();
                break;
            case R.id.btn_sync :
                sync();
                break;
            case R.id.btn_add_to_centre :
                startActivity(new Intent(this,AddCandidateScannerActivity.class));
                finish();
                break;
            case R.id.btn_verify_candidate :
                Intent intent = new Intent(this,ScannerActivity.class);
                intent.putExtra(Constants.USE_CASE,Constants.VERIFY_CAND);
                startActivity(intent);
                finish();
                break;
            case R.id.img_back:
                finish();
                break;
        }

    }

    /*data save To server*/
    private void saveDataToServer() {

        boolean isConnected = new Utility().getInternetConnection(this);

        if(isConnected) {
            new Thread(() -> {

                CandidatesList[] list = CandidatesDBClient.getInstance(MoreOptions.this).getAppDatabase().candidatesDAO()
                        .getAllCandidatesData();

                List<CandidatesList> listStudents = new ArrayList<>(Arrays.asList(list));

                List<DataToServer> listOccData = new ArrayList<>();

                for (int i = 0; i < listStudents.size(); i++) {
                    DataToServer data = new DataToServer();
                    data.setRollNumber(listStudents.get(i).getRollNumber());
                    data.setOccurrance(listStudents.get(i).getOccurrance());
                    data.setCenterCode(listStudents.get(i).getCentreCode());
                    listOccData.add(data);
                }

                runOnUiThread(() -> {

                    if (appDialog != null) {
                        appDialog.showLoader();
                    }
                    SaveDataToServerViewModel viewModel = ViewModelProviders.of(this).get(SaveDataToServerViewModel.class);
                    viewModel.init();
                    viewModel.saveDataToServer(token,listOccData);
                    viewModel.getVolumesResponseLiveData().observe(this, saveDataToServerAPIResponse -> {
                        if (appDialog != null) {
                            appDialog.dismiss();
                        }
                        if (saveDataToServerAPIResponse != null) {
                            String msg = saveDataToServerAPIResponse.getMessage();
                            if (saveDataToServerAPIResponse.getStatus()) {
                               // card_sync.setVisibility(View.VISIBLE);
                              //  card_save.setVisibility(View.GONE);
                                showDialog(getString(R.string.data_sc_msg));
                            } else {
                                showDialog(msg);
                            }
                            Toast.makeText(this, saveDataToServerAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            showDialog(getString(R.string.error_msg));
                        }
                    });
                });
            }).start();

//            new Thread(() -> {
//                DataBaseClient.getInstance(MoreOptions.this).getAppDatabase().examinationDao()
//                        .updateCount(0);
//            }).start();
        }else {
            showDialog(getString(R.string.internet_connection));
        }
    }


    //get data from api and insert into local database
    private void sync() {

        boolean isConnected = new Utility().getInternetConnection(this);

        if(isConnected) {

            if (appDialog != null) {
                appDialog.showLoader();
            }
//            GetAllStudentViewModel viewModel = ViewModelProviders.of(this).get(GetAllStudentViewModel.class);
//            viewModel.init();
//            viewModel.getStudentList();
//            viewModel.getVolumesResponseLiveData().observe(this, new Observer<AllStudentDataResp>() {
//                @Override
//                public void onChanged(AllStudentDataResp allStudentDataResp) {
//                    if (appDialog != null) {
//                        appDialog.dismiss();
//                    }
//                    if (allStudentDataResp != null) {
//                        list = allStudentDataResp.getListStudent();
//                        // save data into local
//                        if (list != null) {
//                            saveDataIntoLocal(list);
//                        } else {
//                            showDialog(getString(R.string.data_not_found));
//                        }
//                    } else {
//                        showDialog(getString(R.string.error_msg));
//                    }
//                }
//            });

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
                            Toast.makeText(MoreOptions.this, "Data saved!", Toast.LENGTH_SHORT).show();
                            saveDataIntoLocal(list);
                        } else {
                            showDialog(getString(R.string.data_not_found));
                        }
                    } else {
                        showDialog(getString(R.string.error_msg));
                    }
                }
            });
        }else {
            showDialog(getString(R.string.internet_connection));
        }

    }


    // save data into local db
    protected void saveDataIntoLocal(List<CandidatesList> list){

        new Thread(() -> {

            CandidatesDBClient.getInstance(MoreOptions.this).getAppDatabase()
                    .candidatesDAO().insertAllCandidatesData(list);


        }).start();

      //  card_sync.setVisibility(View.GONE);
      //  card_save.setVisibility(View.VISIBLE);

        showDialog(getString(R.string.data_sync_msg));
    }


    // show popup
    private void showDialog(String msg){
        DialogCaller.showDialog(MoreOptions.this, "",msg , (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // Add your code if it positive button
                    dialog.dismiss();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // Add your code if it negative button
                    break;
            }
        });
    }

}