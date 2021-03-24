package com.inspection.examination.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.reflect.TypeToken;
import com.inspection.examination.R;
import com.inspection.examination.model.GetCandidateDataResponse.CandidateData;
import com.inspection.examination.model.LoginResponse.LoginAPIResponse;
import com.inspection.examination.model.SyncDataResponse.CandidatesList;
import com.inspection.examination.util.Constants;
import com.inspection.examination.util.ProgressDialog;
import com.inspection.examination.util.Util;
import com.inspection.examination.util.Utility;
import com.inspection.examination.viewmodel.GetCandidateDataViewModel;
import com.inspection.examination.viewmodel.RegisterFaceViewModel;
import com.inspection.examination.viewmodel.VerifyFaceViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.parceler.Parcels;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VerifyCandidateActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backBtn, faceImageView, irisImageView, prevFaceImage, prevIrisImage;
    private TextView titleTV, rollNumKey, rollNumTV, faceKey, faceTV, irisKey,irisTV;
    private Button verFace, bypassVerFace, verIris, bypassVerIris, submitBtn;
    private LinearLayout faceStatusLayout, faceImagesLayout, faceVerButtonsLayout;
    private LinearLayout irisStatusLayout, irisImagesLayout, irisVerButtonsLayout;
    private Spinner eyeDropdownSpinner;
    private ProgressDialog appDialog;

    private LoginAPIResponse resp;
    private String token,rollNumber;
    private CandidateData result;
    private CandidatesList dataList;
    private String from;
    private boolean onCreated = false;
    private File faceImageFile,irisImageFile;
    private String eyeType = "left";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_candidate);

        // Hide the status bar.
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        appDialog = new ProgressDialog(VerifyCandidateActivity.this);
        // find id's
        backBtn = findViewById(R.id.btnBack);
        faceImageView = findViewById(R.id.faceImageView);
        irisImageView = findViewById(R.id.irisImageView);
        prevFaceImage = findViewById(R.id.prevFaceImage);
        prevIrisImage = findViewById(R.id.prevIrisImage);

        titleTV = findViewById(R.id.titleTV);
        rollNumKey = findViewById(R.id.rollNumKey);
        rollNumTV = findViewById(R.id.rollNumTV);
        faceKey = findViewById(R.id.faceKey);
        faceTV = findViewById(R.id.faceTV);
        irisKey = findViewById(R.id.irisKey);
        irisTV = findViewById(R.id.irisTV);

        eyeDropdownSpinner = findViewById(R.id.eyeTypeSpinner);

        verFace = findViewById(R.id.verifyFaceButton);
        bypassVerFace = findViewById(R.id.bypassFaceVerButton);
        verIris = findViewById(R.id.verifyIrisButton);
        bypassVerIris = findViewById(R.id.bypassIrisVerButton);
        submitBtn = findViewById(R.id.submitButton);

        faceStatusLayout = findViewById(R.id.faceStatusLayout);
        faceImagesLayout = findViewById(R.id.faceImagesLayout);
        faceVerButtonsLayout = findViewById(R.id.faceVerifyButtonsLayout);
        irisStatusLayout = findViewById(R.id.irisStatusLayout);
        irisImagesLayout = findViewById(R.id.irisImagesLayout);
        irisVerButtonsLayout = findViewById(R.id.irisVerifyButtonsLayout);

        // set custom font
        Typeface typeface_bold = Typeface.createFromAsset(getAssets(), "Metropolis_SemiBold.ttf");
        Typeface typeface_reg = Typeface.createFromAsset(getAssets(), "Metropolis_Regular.ttf");
        titleTV.setTypeface(typeface_bold);
        rollNumKey.setTypeface(typeface_bold);
        rollNumTV.setTypeface(typeface_reg);
        faceKey.setTypeface(typeface_bold);
        faceTV.setTypeface(typeface_reg);
        irisKey.setTypeface(typeface_bold);
        irisTV.setTypeface(typeface_reg);

        verFace.setTypeface(typeface_bold);
        bypassVerFace.setTypeface(typeface_bold);
        verIris.setTypeface(typeface_bold);
        bypassVerIris.setTypeface(typeface_bold);
        submitBtn.setTypeface(typeface_bold);

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
        }

        // set onclick listeners
        backBtn.setOnClickListener(this);
        verFace.setOnClickListener(this);
        bypassVerFace.setOnClickListener(this);
        verIris.setOnClickListener(this);
        bypassVerIris.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        irisImageView.setOnClickListener(this);
        faceImageView.setOnClickListener(this);

        // set data into view's
        if(result != null) {
            if (from.equals(Constants.VAL_FROM_SERVER)) {
                rollNumTV.setText(result.getRollNumber());
                rollNumber = result.getRollNumber();
                faceTV.setText(result.getFaceStatus());
                // set image url to profile image view
                Glide.with(VerifyCandidateActivity.this).load(result.getFaceImageURL())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).centerCrop().into(prevFaceImage);
                Glide.with(VerifyCandidateActivity.this).load(result.getIrisImageURL())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).centerCrop().into(prevIrisImage);

                if (result.getFaceStatus().matches("Not Registered")){
                    faceTV.setTextColor(this.getColor(R.color.red));
                }else if (result.getFaceStatus().matches("Registration Bypassed")){
                    faceTV.setTextColor(this.getColor(R.color.light_orange));
                }else if (result.getFaceStatus().matches("Not Verified")){
                    faceTV.setTextColor(this.getColor(R.color.red));
                }else if (result.getFaceStatus().matches("Verification Bypassed")){
                    faceTV.setTextColor(this.getColor(R.color.light_orange));
                }else {
                    faceTV.setTextColor(this.getColor(R.color.green));
                }
                irisTV.setText(result.getIrisStatus());
                if (result.getIrisStatus().matches("Not Registered")){
                    irisTV.setTextColor(this.getColor(R.color.red));
                }else if (result.getIrisStatus().matches("Registration Bypassed")){
                    irisTV.setTextColor(this.getColor(R.color.light_orange));
                }else if (result.getIrisStatus().matches("Not Verified")){
                    irisTV.setTextColor(this.getColor(R.color.red));
                }else if (result.getIrisStatus().matches("Verification Bypassed")){
                    irisTV.setTextColor(this.getColor(R.color.light_orange));
                }else {
                    irisTV.setTextColor(this.getColor(R.color.green));
                }
            }
        }
        else {
            rollNumTV.setText(dataList.getRollNumber());
            rollNumber = dataList.getRollNumber();
            faceTV.setText(dataList.getFaceStatus());

            // set image url to face image view
            byte[] decodedString = Base64.decode(dataList.getFaceImageString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            prevFaceImage.setImageBitmap(decodedByte);

            // set image url to iris image view
            byte[] decodedString2 = Base64.decode(dataList.getIrisImageString(), Base64.DEFAULT);
            Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
            prevIrisImage.setImageBitmap(decodedByte2);

            if (dataList.getFaceStatus().matches("Not Registered")){
                faceTV.setTextColor(this.getColor(R.color.red));
            }else if (dataList.getFaceStatus().matches("Registration Bypassed")){
                faceTV.setTextColor(this.getColor(R.color.light_orange));
            }else if (dataList.getFaceStatus().matches("Not Verified")){
                faceTV.setTextColor(this.getColor(R.color.red));
            }else if (dataList.getFaceStatus().matches("Verification Bypassed")){
                faceTV.setTextColor(this.getColor(R.color.light_orange));
            }else {
                faceTV.setTextColor(this.getColor(R.color.green));
            }
            irisTV.setText(dataList.getIrisStatus());
            if (dataList.getIrisStatus().matches("Not Registered")){
                irisTV.setTextColor(this.getColor(R.color.red));
            }else if (dataList.getIrisStatus().matches("Registration Bypassed")){
                irisTV.setTextColor(this.getColor(R.color.light_orange));
            }else if (dataList.getIrisStatus().matches("Not Verified")){
                irisTV.setTextColor(this.getColor(R.color.red));
            }else if (dataList.getIrisStatus().matches("Verification Bypassed")){
                irisTV.setTextColor(this.getColor(R.color.light_orange));
            }else {
                irisTV.setTextColor(this.getColor(R.color.green));
            }
        }

        // Hiding the unneccessary views on the basis of authentication modules allowed
        if (!resp.getData().getCentreInfo().getFaceAuth()){
            // Face Auth Disabled
            // Hide the face layouts
            faceStatusLayout.setVisibility(View.GONE);
            faceImagesLayout.setVisibility(View.GONE);
            faceVerButtonsLayout.setVisibility(View.GONE);
        }

        if (!faceTV.getText().toString().matches("Not Verified")){
            // Face Registration Completed
            // Hide the face layouts
            faceImagesLayout.setVisibility(View.GONE);
            faceVerButtonsLayout.setVisibility(View.GONE);
        }

        if (!resp.getData().getCentreInfo().getIrisAuth()){
            // Iris Auth Disabled
            // Hide the iris layouts
            irisStatusLayout.setVisibility(View.GONE);
            irisImagesLayout.setVisibility(View.GONE);
            irisVerButtonsLayout.setVisibility(View.GONE);
            eyeDropdownSpinner.setVisibility(View.GONE);
//            mListOfDevices.setVisibility(View.GONE);
//            mStatusTextView.setVisibility(View.GONE);
//            deviceSerialLayout.setVisibility(View.GONE);
        }

        if (!irisTV.getText().toString().matches("Not Verified")){
            // Iris Registration Completed
            // Hide the face layouts
            irisImagesLayout.setVisibility(View.GONE);
            irisVerButtonsLayout.setVisibility(View.GONE);
            eyeDropdownSpinner.setVisibility(View.GONE);
//            mListOfDevices.setVisibility(View.GONE);
//            mStatusTextView.setVisibility(View.GONE);
//            deviceSerialLayout.setVisibility(View.GONE);
        }

        // Set Eye dropdown selection listener
        eyeDropdownSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                switch (item){
                    case "Left Eye":
                        eyeType = "left";
                        changeIrisImageViewContent(eyeType);
                        break;
                    case "Right Eye":
                        eyeType = "right";
                        changeIrisImageViewContent(eyeType);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set onCreated = true
        onCreated = true;
    }

    // Populate Iris Image View based on eye type selected
    private void changeIrisImageViewContent(String eyeType) {
        switch (eyeType){
            case "left":
                if(result != null) {
                    if (from.equals(Constants.VAL_FROM_SERVER)) {
                        // set image url to left eye image view (default)
                        Glide.with(VerifyCandidateActivity.this).load(result.getIrisImageURL())
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).centerCrop().into(prevIrisImage);
                    }
                }
                else {
                    // set image url to left eye image view (default)
                    byte[] decodedString2 = Base64.decode(dataList.getIrisImageString(), Base64.DEFAULT);
                    Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                    prevIrisImage.setImageBitmap(decodedByte2);
                }
                break;
            case "right":
                if(result != null) {
                    if (from.equals(Constants.VAL_FROM_SERVER)) {
                        // set image url to left eye image view (default)
                        Glide.with(VerifyCandidateActivity.this).load(result.getIrisImageURL()+Constants.RIGHT_EYE_URL_SUFFIX)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).centerCrop().into(prevIrisImage);
                    }
                }
                else {
                    // set image url to left eye image view (default)
                    byte[] decodedString2 = Base64.decode(dataList.getIrisImageString(), Base64.DEFAULT);
                    Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                    prevIrisImage.setImageBitmap(decodedByte2);
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if (!onCreated){
            callGetCandidateDataAPI();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnBack :
                finish();
                break;
            case R.id.verifyFaceButton :
                callVerifyFaceAPI();
                break;
            case R.id.verifyIrisButton :
                callVerifyIrisAPI();
                break;
            case R.id.bypassFaceVerButton :
                goToBypass("face-verify");
                break;
            case R.id.bypassIrisVerButton :
                goToBypass("iris-verify");
                break;
            case R.id.submitButton :
                goToDetails();
                break;
            case R.id.faceImageView :
                checkRunTimePermission();
                break;
            case R.id.irisImageView :
                startIrisCapture();
                break;
        }
    }

    private void startIrisCapture() {
        Toast.makeText(this, "Feature TBD soon.", Toast.LENGTH_SHORT).show();
    }

    // check run time permission
    private void checkRunTimePermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                selectImage(VerifyCandidateActivity.this);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private void selectImage(VerifyCandidateActivity verifyCandidateActivity) {
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 0) {
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap clickedImage = (Bitmap) data.getExtras().get("data");
                    faceImageFile = Utility.getFileWithName(this, clickedImage,"FaceImageVerify.jpeg");
                    openImageDialog(clickedImage);
                }
            }
        }
    }

    // Method to show clicked/selected image
    private void openImageDialog(Bitmap bitmap){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                faceImageView.setImageBitmap(bitmap);
                faceImageView.setPadding(0,0,0,0);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                faceImageFile = null;
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.custom_image_dialog, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                ImageView image = (ImageView) dialog.findViewById(R.id.imageView);
                float imageWidthInPX = (float)image.getWidth();

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                        Math.round(imageWidthInPX * (float)bitmap.getHeight() / (float)bitmap.getWidth()));
                image.setLayoutParams(layoutParams);
                image.setImageBitmap(bitmap);
            }
        });

        dialog.show();
    }


    private void goToDetails() {
        if (resp.getData().getCentreInfo().getFaceAuth() && faceTV.getText().toString().matches("Not Verified")){
            Toast.makeText(this, "Please complete face image verification first.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (resp.getData().getCentreInfo().getIrisAuth() && irisTV.getText().toString().matches("Not Verified")){
            Toast.makeText(this, "Please complete iris image verification first.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(VerifyCandidateActivity.this,DetailsActivity.class);
        if(result != null) {
            //            Intent intent = new Intent(ScannerActivity.this,CandidateStatusActivity.class);
            intent.putExtra(Constants.KEY_FROM,Constants.VAL_FROM_SERVER);
            intent.putExtra(Constants.KEY_RESULT, Parcels.wrap(result));
            intent.putExtra(Constants.USE_CASE, Constants.VERIFY_CAND);
        }
        else {
            //            Intent intent = new Intent(ScannerActivity.this,CandidateStatusActivity.class);
            intent.putExtra(Constants.KEY_FROM, Constants.VAL_FROM_DB);
            intent.putExtra(Constants.KEY_RESULT, Parcels.wrap(dataList));
            intent.putExtra(Constants.USE_CASE, Constants.VERIFY_CAND);
        }
        startActivity(intent);
        this.finish();
    }

    private void goToBypass(String type) {
        onCreated = false;
        Intent intent = new Intent(VerifyCandidateActivity.this,BypassActivity.class);
        intent.putExtra(Constants.AUTH_TYPE, type);
        intent.putExtra(Constants.KEY_ROLL_NUMBER, rollNumber);
        startActivity(intent);
    }

    private void callVerifyIrisAPI() {
        Toast.makeText(this, "Feature TBD soon.", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void callVerifyFaceAPI() {
        if(faceImageFile == null){
            Toast.makeText(this, "Please click Face Image before submitting.", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isConnected = new Utility().getInternetConnection(this);
        if(isConnected){
            if (appDialog != null) {
                appDialog.showLoader();
            }
            // convert field into Request body and image convert into multipart
//            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), faceImageFile1);
//            MultipartBody.Part body = MultipartBody.Part.createFormData("imageA", faceImageFile1.getName(), fileBody);
            RequestBody fileBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), faceImageFile);
            MultipartBody.Part body2 = MultipartBody.Part.createFormData("imageFile", faceImageFile.getName(), fileBody2);
            RequestBody rollNum =  RequestBody.create(MediaType.parse("text/plain"), rollNumber);

            VerifyFaceViewModel viewModel = ViewModelProviders.of(this).get(VerifyFaceViewModel.class);
            viewModel.init();
            viewModel.verifyFaceApi(token,rollNum,body2);
            viewModel.getVolumesResponseLiveData().observe(this, verifyFaceAPIResponse -> {
                if (appDialog != null) {
                    appDialog.dismiss();
                }
                if(verifyFaceAPIResponse != null){
                    result.setFaceStatus(verifyFaceAPIResponse.getData().getCandidateData().getFaceStatus());
                    if (verifyFaceAPIResponse.getStatus()){
                        faceTV.setText(verifyFaceAPIResponse.getData().getCandidateData().getFaceStatus());
                        faceTV.setTextColor(this.getColor(R.color.green));
                        faceImagesLayout.setVisibility(View.GONE);
                        faceVerButtonsLayout.setVisibility(View.GONE);
                    }else{
                        faceImageFile = null;
                        faceImageView.setImageDrawable(this.getDrawable(R.drawable.ic_add));
                        faceImageView.setPadding(10,10,10,10);
                    }
                    showDialog("Face Verification Status",verifyFaceAPIResponse.getMessage());
//                    Toast.makeText(this, registerFaceAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,R.string.error_msg, Toast.LENGTH_SHORT).show();
                    faceImageFile = null;
                    faceImageView.setImageDrawable(this.getDrawable(R.drawable.ic_add));
                    faceImageView.setPadding(10,10,10,10);
                }
            });
        }
        else {
            Toast.makeText(this, R.string.internet_connection, Toast.LENGTH_SHORT).show();
            faceImageFile = null;
            faceImageView.setImageDrawable(this.getDrawable(R.drawable.ic_add));
            faceImageView.setPadding(10,10,10,10);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void callGetCandidateDataAPI() {
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
                            result.setFaceStatus(candidateData.getFaceStatus());
                            result.setIrisStatus(candidateData.getIrisStatus());
                            faceTV.setText(candidateData.getFaceStatus());
                            if (candidateData.getFaceStatus().matches("Not Verified")){
                                faceTV.setTextColor(this.getColor(R.color.red));
                            }else if (candidateData.getFaceStatus().matches("Registration Bypassed")){
                                faceTV.setTextColor(this.getColor(R.color.light_orange));
                                faceImagesLayout.setVisibility(View.GONE);
                                faceVerButtonsLayout.setVisibility(View.GONE);
                            }else if (candidateData.getFaceStatus().matches("Not Registered")){
                                faceTV.setTextColor(this.getColor(R.color.red));
                                faceImagesLayout.setVisibility(View.GONE);
                                faceVerButtonsLayout.setVisibility(View.GONE);
                            }else if (candidateData.getFaceStatus().matches("Verification Bypassed")){
                                faceTV.setTextColor(this.getColor(R.color.light_orange));
                                faceImagesLayout.setVisibility(View.GONE);
                                faceVerButtonsLayout.setVisibility(View.GONE);
                            }else {
                                faceTV.setTextColor(this.getColor(R.color.green));
                                faceImagesLayout.setVisibility(View.GONE);
                                faceVerButtonsLayout.setVisibility(View.GONE);
                            }
                            irisTV.setText(candidateData.getIrisStatus());
                            if (candidateData.getIrisStatus().matches("Not Verified")){
                                irisTV.setTextColor(this.getColor(R.color.red));
                            }else if (candidateData.getIrisStatus().matches("Registration Bypassed")){
                                irisTV.setTextColor(this.getColor(R.color.light_orange));
                                irisImagesLayout.setVisibility(View.GONE);
                                irisVerButtonsLayout.setVisibility(View.GONE);
                                eyeDropdownSpinner.setVisibility(View.GONE);
//                                mListOfDevices.setVisibility(View.GONE);
//                                mStatusTextView.setVisibility(View.GONE);
//                                deviceSerialLayout.setVisibility(View.GONE);
                            }else if (candidateData.getIrisStatus().matches("Not Registered")){
                                irisTV.setTextColor(this.getColor(R.color.red));
                                irisImagesLayout.setVisibility(View.GONE);
                                irisVerButtonsLayout.setVisibility(View.GONE);
                                eyeDropdownSpinner.setVisibility(View.GONE);
//                                mListOfDevices.setVisibility(View.GONE);
//                                mStatusTextView.setVisibility(View.GONE);
//                                deviceSerialLayout.setVisibility(View.GONE);
                            }else if (candidateData.getIrisStatus().matches("Verification Bypassed")){
                                irisTV.setTextColor(this.getColor(R.color.light_orange));
                                irisImagesLayout.setVisibility(View.GONE);
                                irisVerButtonsLayout.setVisibility(View.GONE);
                                eyeDropdownSpinner.setVisibility(View.GONE);
//                                mListOfDevices.setVisibility(View.GONE);
//                                mStatusTextView.setVisibility(View.GONE);
//                                deviceSerialLayout.setVisibility(View.GONE);
                            }else {
                                irisTV.setTextColor(this.getColor(R.color.green));
                                irisImagesLayout.setVisibility(View.GONE);
                                irisVerButtonsLayout.setVisibility(View.GONE);
                                eyeDropdownSpinner.setVisibility(View.GONE);
//                                mListOfDevices.setVisibility(View.GONE);
//                                mStatusTextView.setVisibility(View.GONE);
//                                deviceSerialLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                    Toast.makeText(VerifyCandidateActivity.this, getCandidateDataAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(this, R.string.internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    void showDialog(final String title, final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(VerifyCandidateActivity.this).create();
                alertDialog.setTitle(title);
                alertDialog.setMessage(message);
                alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // Do nothing
                    }
                });
                alertDialog.show();
            }
        });

    }
}