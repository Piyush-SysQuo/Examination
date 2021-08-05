package com.barcode.examination.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.barcode.examination.R;
import com.barcode.examination.adapter.AbsentAdapter;
import com.barcode.examination.adapter.RecycleViewAdapter;
import com.barcode.examination.database.db.CandidatesDBClient;
import com.barcode.examination.model.GetAbsentCandidatesResponse.AbsentCandidatesList;
import com.barcode.examination.model.GetPresentCandidatesResponse.GetPresentCandidatesAPIResponse;
import com.barcode.examination.model.GetPresentCandidatesResponse.PresentCandidatesList;
import com.barcode.examination.model.LoginResponse.LoginAPIResponse;
import com.barcode.examination.model.SyncDataResponse.CandidatesList;
import com.barcode.examination.util.Constants;
import com.barcode.examination.util.DialogCaller;
import com.barcode.examination.util.ProgressDialog;
import com.barcode.examination.util.Util;
import com.barcode.examination.util.Utility;
import com.barcode.examination.viewmodel.GetAbsentCandidatesViewModel;
import com.barcode.examination.viewmodel.GetPresentCandidatesViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PresentActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recycleView, recycleView_absent;
    private List<String> list;
    private TextView txt_present, txt_absent;
    private ProgressDialog appDialog;
    private ProgressBar progressBar;
    private List<PresentCandidatesList> listData = new ArrayList<>();
    private List<AbsentCandidatesList> listDataAbsent = new ArrayList<>();
    private RecycleViewAdapter mAdapter;
    private AbsentAdapter absentAdapter;
    private int prePage = 1, abPage = 1, limit = 10;
    private boolean isBtnPresent = false;
    private LoginAPIResponse resp;
    private String token;
    private boolean hasNext=false,hasPrev=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present);

        // Hide the status bar.
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        recycleView = findViewById(R.id.recycleView);
        recycleView_absent = findViewById(R.id.recycleView_absent);
        ImageView back = findViewById(R.id.img_back);
        TextView txt_arrow = findViewById(R.id.txt_arrow);
        txt_present = findViewById(R.id.txt_present);
        txt_absent = findViewById(R.id.txt_absent);
        TextView title_roll_no = findViewById(R.id.title_roll_no);
        TextView title_name = findViewById(R.id.title_name);
        NestedScrollView nestedScrollView = findViewById(R.id.scroll_view);
        progressBar = findViewById(R.id.progress_bar);

        txt_arrow.setText(R.string.details);

     // text view click listener
        txt_absent.setOnClickListener(this);
        txt_present.setOnClickListener(this);


        // set custom font
        Typeface typeface_bold = Typeface.createFromAsset(getAssets(), "Metropolis_SemiBold.ttf");
        Typeface typeface_reg = Typeface.createFromAsset(getAssets(), "Metropolis_Regular.ttf");
        txt_present.setTypeface(typeface_bold);
        txt_absent.setTypeface(typeface_bold);
        txt_arrow.setTypeface(typeface_bold);
        title_name.setTypeface(typeface_bold);
        title_roll_no.setTypeface(typeface_bold);

        // initialize app dialog instance
        appDialog = new ProgressDialog(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            txt_present.setBackground(getDrawable(R.drawable.change_present));
        }

        back.setOnClickListener(v -> {
           finish();
        });

        // get Token
        resp = (LoginAPIResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginAPIResponse>() {
                });

        if (resp != null){
            token = resp.getData().getToken();
        }

        // set present adapter
        mAdapter = new RecycleViewAdapter(listData, PresentActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recycleView.setLayoutManager(mLayoutManager);
        recycleView.setAdapter(mAdapter);

        // set absent adapter
        absentAdapter = new AbsentAdapter(listDataAbsent, PresentActivity.this);
        RecyclerView.LayoutManager mLayoutManagerAbsent = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recycleView_absent.setLayoutManager(mLayoutManagerAbsent);
        recycleView_absent.setAdapter(absentAdapter);

        // by default show present student
        showPresent(prePage,limit);

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )){
//                if(isBtnPresent){
//                    abPage ++;
//                    progressBar.setVisibility(View.VISIBLE);
//                    showAbsent(abPage,limit);
//                    return;
//                }else {
//                    prePage ++;
//                    progressBar.setVisibility(View.VISIBLE);
//                    showPresent(prePage, limit);
//                    return;
//                }
//                if (hasNext){
                    if(isBtnPresent){
                        abPage ++;
                        progressBar.setVisibility(View.VISIBLE);
                        showAbsent(abPage,limit);
                        return;
                    }else {
                        prePage ++;
                        progressBar.setVisibility(View.VISIBLE);
                        showPresent(prePage, limit);
                        return;
                    }
                    // reset the boolean(loading) to prevent
                    // auto loading data from APi
//                    hasNext = false;
//                }
            }

            // here where the trick is going


//            if (scrollY > oldScrollY) {
//                if(isBtnPresent){
//                    if (hasNext){
//                        abPage ++;
//                        progressBar.setVisibility(View.VISIBLE);
//                        showAbsent(abPage,limit);
//                    }
//                }else {
//                    if (hasNext){
//                        prePage ++;
//                        progressBar.setVisibility(View.VISIBLE);
//                        showPresent(prePage, limit);
//                    }
//                }
//                return;
////                Toast.makeText(this, "Scrolled Down", Toast.LENGTH_SHORT).show();
//            }
//            if (scrollY < oldScrollY) {
//                if(isBtnPresent){
//                    if (hasPrev){
//                        abPage --;
//                        progressBar.setVisibility(View.VISIBLE);
//                        showAbsent(abPage,limit);
//                    }
//                }else {
//                    if (hasPrev){
//                        prePage --;
//                        progressBar.setVisibility(View.VISIBLE);
//                        showPresent(prePage, limit);
//                    }
//                }
////                Toast.makeText(this, "Scrolled Up", Toast.LENGTH_SHORT).show();
//            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.txt_present:
                isBtnPresent = false;
                recycleView_absent.setVisibility(View.GONE);
                recycleView.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    txt_present.setBackground(getDrawable(R.drawable.change_present));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    txt_absent.setBackground(getDrawable(R.drawable.spinner_bc));
                }
                showPresent(1,limit);
                break;
            case R.id.txt_absent :
                isBtnPresent = true;
                recycleView.setVisibility(View.GONE);
                recycleView_absent.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    txt_present.setBackground(getDrawable(R.drawable.spinner_bc));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    txt_absent.setBackground(getDrawable(R.drawable.change_present));
                }
                showAbsent(1,limit);
                break;
        }
    }


    // show present student
    private void showPresent(int pages, int limit) {
//        listData.clear();
        if(pages == 1){

            if(appDialog != null){
                appDialog.showLoader();
            }
        }
        boolean isConnected = new Utility().getInternetConnection(this);
        if(isConnected) {
            GetPresentCandidatesViewModel viewModel = ViewModelProviders.of(this).get(GetPresentCandidatesViewModel.class);
            viewModel.init();
            viewModel.getPresentCandidates(token, String.valueOf(pages), String.valueOf(limit));
            viewModel.getVolumesResponseLiveData().observe(this, new Observer<GetPresentCandidatesAPIResponse>() {
                @Override
                public void onChanged(GetPresentCandidatesAPIResponse getPresentCandidatesAPIResponse) {
                    if(appDialog != null){
                        appDialog.dismiss();
                    }
                    if (getPresentCandidatesAPIResponse != null) {
                        progressBar.setVisibility(View.GONE);
                        listData = getPresentCandidatesAPIResponse.getData().getCandidatesList();
                        hasNext = getPresentCandidatesAPIResponse.getData().getHasNext();
                        hasPrev = getPresentCandidatesAPIResponse.getData().getHasPrev();
                        if(getPresentCandidatesAPIResponse.getStatus()) {
                            if (listData != null) {
//                                mAdapter.addItems(listData);

                                if (mAdapter.getItemCount()==0){
                                    // calling from adapter addToExistingList(list)
                                    // with the defined Adapter instance
//                                    mAdapter.addToExistingList(listData);
                                    mAdapter.setItems(listData);
                                }else{
                                    List<PresentCandidatesList> prevList = mAdapter.getList();
                                    boolean exists=false;
                                    for (int i=0;i<prevList.size();i++){
                                        if (prevList.get(i).getRollNumber().equals(listData.get(0).getRollNumber())){
                                            exists=true;
                                            break;
                                        }
                                    }
                                    if (!exists){
                                        mAdapter.addItems(listData);
                                    }
                                }
                            }
                        }else{
                            progressBar.setVisibility(View.GONE);
                           // showDialog(studentAvailabilityResp.getMsg());
                        }
                        Toast.makeText(PresentActivity.this, getPresentCandidatesAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        showDialog(getString(R.string.error_msg));
                    }
                }
            });

        }
        else {

            if(appDialog != null){
                appDialog.dismiss();
            }

           progressBar.setVisibility(View.GONE);

            new Thread(() -> {

                CandidatesList[] listArray =   CandidatesDBClient.getInstance(PresentActivity.this).getAppDatabase().candidatesDAO()
                        .getAllCandidatesData();

                List<CandidatesList> listStudents = new ArrayList<>(Arrays.asList(listArray));


                runOnUiThread(() -> {

                    List<PresentCandidatesList> present = new ArrayList<>();

                    for(int i=0; i<listStudents.size(); i++){
                        long time =  listStudents.get(i).getOccurrance();
                        if(time !=0 ){
                            PresentCandidatesList listStudent = new PresentCandidatesList();
                            listStudent.setRollNumber(listStudents.get(i).getRollNumber());
                            listStudent.setName(listStudents.get(i).getName());
                            present.add(listStudent);
                        }
                    }

                    mAdapter.setItems(present);
                    // set present adapter
                    mAdapter = new RecycleViewAdapter(present, PresentActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                    recycleView.setLayoutManager(mLayoutManager);
                    recycleView.setAdapter(mAdapter);
                });
            }).start();

        }

    }

    // show absent student
    private void showAbsent(int pages,int limit) {
//        listDataAbsent.clear();
        if(pages == 1){

            if(appDialog != null){
                appDialog.showLoader();
            }
        }

        boolean isConnected = new Utility().getInternetConnection(this);
        if(isConnected) {
            GetAbsentCandidatesViewModel viewModel = ViewModelProviders.of(this).get(GetAbsentCandidatesViewModel.class);
            viewModel.init();
            viewModel.getAbsentCandidates(token, String.valueOf(pages), String.valueOf(limit));
            viewModel.getVolumesResponseLiveData().observe(this, getAbsentCandidatesAPIResponse -> {
                if(appDialog != null){
                    appDialog.dismiss();
                }
                if (getAbsentCandidatesAPIResponse != null) {
                    progressBar.setVisibility(View.GONE);
                    listDataAbsent = getAbsentCandidatesAPIResponse.getData().getCandidatesList();
                    hasNext = getAbsentCandidatesAPIResponse.getData().getHasNext();
                    hasPrev = getAbsentCandidatesAPIResponse.getData().getHasPrev();
                    if(getAbsentCandidatesAPIResponse.getStatus()) {
                        if (listData != null) {
//                            absentAdapter.setItems(listDataAbsent);
                            if (absentAdapter.getItemCount()==0){
                                // calling from adapter addToExistingList(list)
                                // with the defined Adapter instance
//                                absentAdapter.addToExistingList(listDataAbsent);
                                absentAdapter.setItems(listDataAbsent);
                            }else{
                                List<AbsentCandidatesList> prevList = absentAdapter.getList();
                                boolean exists=false;
                                for (int i=0;i<prevList.size();i++){
                                    if (prevList.get(i).getRollNumber().equals(listDataAbsent.get(0).getRollNumber())){
                                        exists=true;
                                        break;
                                    }
                                }
                                if (!exists){
                                    absentAdapter.addItems(listDataAbsent);
                                }
//                                absentAdapter.addItems(listDataAbsent);
                            }
                        }
//                        else {
//                            //  showDialog(studentAvailabilityResp.getMsg());
//                        }
                    }else{
                        progressBar.setVisibility(View.GONE);
                       // showDialog(studentAvailabilityResp.getMsg());
                    }
                    Toast.makeText(PresentActivity.this, getAbsentCandidatesAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    showDialog(getString(R.string.error_msg));
                }
            });
        }
        else{
            progressBar.setVisibility(View.GONE);
            if(appDialog != null){
                appDialog.dismiss();
            }

            new Thread(() -> {

                CandidatesList[] listArray =   CandidatesDBClient.getInstance(PresentActivity.this).getAppDatabase().candidatesDAO()
                        .getAllCandidatesData();

                List<CandidatesList> listStudents = new ArrayList<>(Arrays.asList(listArray));


                runOnUiThread(() -> {

                    List<AbsentCandidatesList> absent = new ArrayList<>();

                    for(int i=0; i<listStudents.size(); i++){
                        long time =  listStudents.get(i).getOccurrance();
                        if(time ==0 ){
                            AbsentCandidatesList listStudent = new AbsentCandidatesList();
                            listStudent.setRollNumber(listStudents.get(i).getRollNumber());
                            listStudent.setName(listStudents.get(i).getName());
                            absent.add(listStudent);
                        }
                    }

                    absentAdapter = new AbsentAdapter(absent, PresentActivity.this);
                    RecyclerView.LayoutManager mLayoutManagerAbsent = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                    recycleView_absent.setLayoutManager(mLayoutManagerAbsent);
                    recycleView_absent.setAdapter(absentAdapter);

                });
            }).start();
        }
    }

/*
    // show absent student
    private void showAbsent() {

        if(appDialog != null){
            appDialog.showLoader();
        }

        boolean isConnected = new Utility().getInternetConnection(this);
        if(isConnected){

            GetAllStudentViewModel viewModel = ViewModelProviders.of(this).get(GetAllStudentViewModel.class);
            viewModel.init();
            viewModel.getStudentList();
            viewModel.getVolumesResponseLiveData().observe(this, allStudentDataResp -> {

                if(appDialog != null){
                    appDialog.dismiss();
                }

                if(allStudentDataResp != null){

                    List<ListStudent>  dataList = allStudentDataResp.getListStudent();

                    if(dataList != null){

                        List<ListStudent> absent = new ArrayList<>();
                        for(int i=0; i<dataList.size(); i++){
                            long time =  dataList.get(i).getTimes();
                            if(time == 0 ){
                                ListStudent listStudent = new ListStudent();
                                listStudent.setRollNo(dataList.get(i).getRollNo());
                                listStudent.setNameCandidate(dataList.get(i).getNameCandidate());
                                absent.add(listStudent);
                            }
                        }

                        RecycleViewAdapter mAdapter = new RecycleViewAdapter(absent, PresentActivity.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                        recycleView.setLayoutManager(mLayoutManager);
                        recycleView.setAdapter(mAdapter);

                    }
                }else{
                    Toast.makeText(this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            });

        } else{

            if(appDialog != null){
                appDialog.dismiss();
            }

            new Thread(() -> {

                ListStudent[] listArray =   DataBaseClient.getInstance(PresentActivity.this).getAppDatabase().examinationDao()
                        .getData();

                List<ListStudent> listStudents = new ArrayList<>(Arrays.asList(listArray));


                runOnUiThread(() -> {

                    List<ListStudent> absent = new ArrayList<>();
                    for(int i=0; i<listStudents.size(); i++){
                        long time =  listStudents.get(i).getTimes();
                        if(time == 0 ){
                            ListStudent listStudent = new ListStudent();
                            listStudent.setRollNo(listStudents.get(i).getRollNo());
                            listStudent.setNameCandidate(listStudents.get(i).getNameCandidate());
                            absent.add(listStudent);
                        }
                    }

                    RecycleViewAdapter mAdapter = new RecycleViewAdapter(absent, PresentActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                    recycleView.setLayoutManager(mLayoutManager);
                    recycleView.setAdapter(mAdapter);

                });
            }).start();
        }
    }*/

   /* // show present student
    private void showPresent() {
        boolean isConnected = new Utility().getInternetConnection(this);

        if(appDialog != null){
            appDialog.showLoader();
        }

        if(isConnected){

            GetAllStudentViewModel viewModel = ViewModelProviders.of(this).get(GetAllStudentViewModel.class);
            viewModel.init();
            viewModel.getStudentList();
            viewModel.getVolumesResponseLiveData().observe(this, allStudentDataResp -> {

                if(appDialog != null){
                    appDialog.dismiss();
                }

                if(allStudentDataResp != null){

                    List<ListStudent>  dataList = allStudentDataResp.getListStudent();

                    if(dataList != null){

                        List<ListStudent> present = new ArrayList<>();
                        for(int i=0; i<dataList.size(); i++){
                            long time =  dataList.get(i).getTimes();
                            if(time !=0 ){
                                ListStudent listStudent = new ListStudent();
                                listStudent.setRollNo(dataList.get(i).getRollNo());
                                listStudent.setNameCandidate(dataList.get(i).getNameCandidate());
                                present.add(listStudent);
                            }
                        }

                        RecycleViewAdapter mAdapter = new RecycleViewAdapter(present, PresentActivity.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                        recycleView.setLayoutManager(mLayoutManager);
                        recycleView.setAdapter(mAdapter);
                    }
                }else{
                    Toast.makeText(this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {

            if(appDialog != null){
                appDialog.dismiss();
            }

            new Thread(() -> {

                ListStudent[] listArray =   DataBaseClient.getInstance(PresentActivity.this).getAppDatabase().examinationDao()
                        .getData();

                List<ListStudent> listStudents = new ArrayList<>(Arrays.asList(listArray));


                runOnUiThread(() -> {

                    List<ListStudent> present = new ArrayList<>();
                    for(int i=0; i<listStudents.size(); i++){
                        long time =  listStudents.get(i).getTimes();
                        if(time !=0 ){
                            ListStudent listStudent = new ListStudent();
                            listStudent.setRollNo(listStudents.get(i).getRollNo());
                            listStudent.setNameCandidate(listStudents.get(i).getNameCandidate());
                            present.add(listStudent);
                        }
                    }

                    RecycleViewAdapter mAdapter = new RecycleViewAdapter(present, PresentActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                    recycleView.setLayoutManager(mLayoutManager);
                    recycleView.setAdapter(mAdapter);
                });
            }).start();

        }
    }*/


    // show popup
    private void showDialog(String msg){
        DialogCaller.showDialog(PresentActivity.this, "",msg , (dialog, which) -> {
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
