package com.inspection.examination.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProviders;

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
import com.inspection.examination.util.DialogCaller;
import com.inspection.examination.util.ProgressDialog;
import com.inspection.examination.util.Util;
import com.inspection.examination.util.Utility;
import com.inspection.examination.viewmodel.BarCodeViewModel;
import com.inspection.examination.viewmodel.GetCandidateDataViewModel;

import org.parceler.Parcels;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScannerActivity extends BaseScannerActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    private LoginAPIResponse resp;
    private String token="",useCase;
    private ProgressDialog appDialog;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
        setupToolbar();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);

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
            useCase = (String) intent.getExtras().get(Constants.USE_CASE);
        }

        appDialog = new ProgressDialog(ScannerActivity.this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        checkBarCode(rawResult.getContents());
    }

    /* check bar code  matched or not*/
    protected  void checkBarCode( String rollNumber){

        if(rollNumber.isEmpty()){
            Toast.makeText(this, "Please input valid roll number", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isConnected = new Utility().getInternetConnection(this);

        if(isConnected){

            if (appDialog != null) {
                appDialog.showLoader();
            }
            // call barcode Api
            GetCandidateDataViewModel viewModel = ViewModelProviders.of(this).get(GetCandidateDataViewModel.class);
            viewModel.init();
            viewModel.getCandidateData(token,rollNumber);
            viewModel.getVolumesResponseLiveData().observe(this, getCandidateDataAPIResponse -> {
                if (appDialog != null) {
                    appDialog.dismiss();
                }
                if(getCandidateDataAPIResponse != null){
                    if(getCandidateDataAPIResponse.getStatus()){
                        CandidateData candidateData = getCandidateDataAPIResponse.getData().getCandidateData();

                        if(candidateData != null){
                            if (candidateData.getCentreCode().matches(resp.getData().getCentreInfo().getCenterCode())){
//                                Intent intent = new Intent(ScannerActivity.this,DetailsActivity.class);
                                Intent intent;
                                if(useCase!=null && useCase.equals(Constants.VERIFY_CAND)) {
                                    // Go To Verification Screen
                                    intent = new Intent(ScannerActivity.this, VerifyCandidateActivity.class);
                                } else{
                                    intent = new Intent(ScannerActivity.this, CandidateStatusActivity.class);
                                }
                                intent.putExtra(Constants.KEY_FROM,Constants.VAL_FROM_SERVER);
                                intent.putExtra(Constants.KEY_RESULT, Parcels.wrap(candidateData));
                                startActivity(intent);
                                this.finish();
                            }
                        }else{
                            this.finish();
                        }
                        Toast.makeText(ScannerActivity.this, getCandidateDataAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        if (getCandidateDataAPIResponse.getCode()==401){
                            showDialog(getCandidateDataAPIResponse.getMessage());
                        }else{
                            this.finish();
                            Toast.makeText(ScannerActivity.this, getCandidateDataAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
//                        Toast.makeText(ScannerActivity.this, getCandidateDataAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {

            new Thread(() -> {

                CandidatesList data = CandidatesDBClient.getInstance(ScannerActivity.this).getAppDatabase().candidatesDAO()
                        .getSingleStudentData(rollNumber);

                if (data != null) {

//                    Intent intent = new Intent(ScannerActivity.this, DetailsActivity.class);
                    Intent intent;
                    if(useCase!=null && useCase.equals(Constants.VERIFY_CAND)) {
                        // Go To Verification Screen
                        intent = new Intent(ScannerActivity.this, VerifyCandidateActivity.class);
                    } else{
                        intent = new Intent(ScannerActivity.this, CandidateStatusActivity.class);
                    }
                    intent.putExtra(Constants.KEY_FROM, Constants.VAL_FROM_DB);
                    intent.putExtra(Constants.KEY_RESULT, Parcels.wrap(data));
                    startActivity(intent);
                    this.finish();
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "No candidate for given roll number found!", Toast.LENGTH_SHORT).show();
                        this.finish();
                    });
                }

            }).start();

        }
    }

    // show popup
    private void showDialog(String msg){
        DialogCaller.showDialog(ScannerActivity.this, "",msg , (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // Add your code if it positive button
                    dialog.dismiss();
                    this.finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // Add your code if it negative button
                    dialog.dismiss();
                    break;
            }
        });
    }
}
