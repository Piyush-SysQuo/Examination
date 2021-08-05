package com.barcode.examination.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.google.gson.reflect.TypeToken;
import com.barcode.examination.R;
import com.barcode.examination.database.db.CandidatesDBClient;
import com.barcode.examination.model.GetCandidateDataForAnyCentreResponse.CandidateData;
import com.barcode.examination.model.LoginResponse.LoginAPIResponse;
import com.barcode.examination.model.SyncDataResponse.CandidatesList;
import com.barcode.examination.util.Constants;
import com.barcode.examination.util.DialogCaller;
import com.barcode.examination.util.ProgressDialog;
import com.barcode.examination.util.Util;
import com.barcode.examination.util.Utility;
import com.barcode.examination.viewmodel.GetCandidateDataForAnyCentreViewModel;
import com.barcode.examination.viewmodel.UpdateCandidateCentreViewModel;

import org.parceler.Parcels;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class AddCandidateScannerActivity extends BaseScannerActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    private LoginAPIResponse resp;
    private String token="";
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
        appDialog = new ProgressDialog(this);

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
            GetCandidateDataForAnyCentreViewModel viewModel = ViewModelProviders.of(this).get(GetCandidateDataForAnyCentreViewModel.class);
            viewModel.init();
            viewModel.getCandidateDataForAnyCentre(token,rollNumber);
            viewModel.getVolumesResponseLiveData().observe(this, getCandidateDataForAnyCentreAPIResponse -> {
                if (appDialog != null) {
                    appDialog.dismiss();
                }
                if(getCandidateDataForAnyCentreAPIResponse != null){
                    if(getCandidateDataForAnyCentreAPIResponse.getStatus()){
                        CandidateData candidateData = getCandidateDataForAnyCentreAPIResponse.getData().getCandidateData();
                        if(candidateData != null){
                            if (!candidateData.getCentreCode().matches(resp.getData().getCentreInfo().getCenterCode())){
//                                Intent intent = new Intent(AddCandidateScannerActivity.this,DetailsActivity.class);
//                                intent.putExtra(Constants.KEY_FROM,Constants.VAL_FROM_SERVER);
//                                intent.putExtra(Constants.KEY_RESULT, Parcels.wrap(candidateData));
//                                startActivity(intent);
                                showDialog("Candidate's original centre was: " + candidateData.getCentreCode() + "\nAdding to current centre.",rollNumber);
                                Toast.makeText(AddCandidateScannerActivity.this, getCandidateDataForAnyCentreAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }else{
                                this.finish();
                                Toast.makeText(AddCandidateScannerActivity.this, "Candidate already belongs to same centre, day and shift.", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            this.finish();
                            Toast.makeText(AddCandidateScannerActivity.this, getCandidateDataForAnyCentreAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        this.finish();
                        Toast.makeText(AddCandidateScannerActivity.this, getCandidateDataForAnyCentreAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    this.finish();
                }
            });

        }
        else {

          Toast.makeText(this, R.string.internet_connection, Toast.LENGTH_SHORT).show();

        }
    }

    private void callUpdateCentreAPI(String token, String rollNumber) {
        /* check internet connected or not*/
        boolean isConnected = new Utility().getInternetConnection(this);
        if (appDialog != null) {
            appDialog.showLoader();
        }
        if(isConnected){
            // call update occurrence Api
            UpdateCandidateCentreViewModel viewModel = ViewModelProviders.of(this).get(UpdateCandidateCentreViewModel.class);
            viewModel.init();
            viewModel.updateCandidateCentre(token, rollNumber);
            viewModel.getVolumesResponseLiveData().observe(this, updateCandidateCentreAPIResponse -> {
                if (appDialog != null) {
                    appDialog.dismiss();
                }
                if(updateCandidateCentreAPIResponse != null) {
//                    Boolean status = updateOccurrenceAPIResponse.getStatus();
                    if (updateCandidateCentreAPIResponse.getStatus()){
                        com.barcode.examination.model.UpdateCandidateCentreResponse.CandidateData candData = updateCandidateCentreAPIResponse.getData().getCandidateData();
                        CandidatesList data = new CandidatesList(candData.getCandidateID(), candData.getApplicationNumber(),
                                candData.getCategory(), candData.getCentreCode(), candData.getCentreName(), candData.getCity(),
                                candData.getDateOfExam(), candData.getDob(), candData.getExamEndTime(), candData.getExamStartTime(),
                                candData.getImageURL(), candData.getLabAllotted(), candData.getName(), candData.getOccurrance(),
                                candData.getReportingTime(), candData.getRollNumber(), candData.getSeatAllotted(), candData.getShift(),
                                candData.getState(), candData.getFaceImageURL(), candData.getIrisImageURL(), candData.getFaceStatus(), candData.getIrisStatus());
                        saveCandidateToDB(data);
                    }
                    Toast.makeText(AddCandidateScannerActivity.this, updateCandidateCentreAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddCandidateScannerActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                    this.finish();
                }
            });

        }
        else {
            Toast.makeText(this, getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCandidateToDB(CandidatesList data) {
        new Thread(() -> {

            CandidatesDBClient.getInstance(AddCandidateScannerActivity.this).getAppDatabase()
                    .candidatesDAO().insertIndividualCandidateData(data);
        }).start();
//        Toast.makeText(this, "Candidate Added to centre successfully!", Toast.LENGTH_SHORT).show();

        // Setting CandidateData Object Class
        com.barcode.examination.model.GetCandidateDataResponse.CandidateData candidateData = new com.barcode.examination.model.GetCandidateDataResponse.CandidateData(
                data.getApplicationNumber(), data.getCandidateID(),
                data.getCategory(), data.getCentreCode(), data.getCentreName(), data.getCity(),
                data.getDateOfExam(), data.getDob(), data.getExamEndTime(), data.getExamStartTime(),
                data.getFaceImageString(), data.getFaceStatus(), data.getImageString(), data.getIrisImageString(),
                data.getIrisStatus(), data.getLabAllotted(), data.getName(), data.getOccurrance(),
                data.getReportingTime(), data.getRollNumber(), data.getSeatAllotted(), data.getShift(),
                data.getState());


        Intent intent = new Intent(AddCandidateScannerActivity.this,CandidateStatusActivity.class);
        intent.putExtra(Constants.KEY_FROM,Constants.VAL_FROM_SERVER);
        intent.putExtra(Constants.KEY_RESULT, Parcels.wrap(candidateData));
        startActivity(intent);
        this.finish();
    }

    // show popup
    private void showDialog(String msg,String rollNumber){
        DialogCaller.showDialog(AddCandidateScannerActivity.this, "",msg , (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // Add your code if it positive button
                    callUpdateCentreAPI(token,rollNumber);
                    dialog.dismiss();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // Add your code if it negative button
                    dialog.dismiss();
                    break;
            }
        });
    }
}
