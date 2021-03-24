package com.inspection.examination.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.inspection.examination.R;
import com.inspection.examination.model.GetPresentCandidatesResponse.PresentCandidatesList;
import com.inspection.examination.model.ListStudent;
import com.inspection.examination.model.StudentAvailabilityList;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {


    private List<PresentCandidatesList> list;
    private Context context;

    public List<PresentCandidatesList> getList() {
        return list;
    }

    public RecycleViewAdapter(List<PresentCandidatesList> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

          /* set custom typeface*/
           Typeface typeface_reg = Typeface.createFromAsset(context.getAssets(), "Poppins_regular.ttf");
           holder.name.setTypeface(typeface_reg);
           holder.rollNo.setTypeface(typeface_reg);

            holder.name.setText(list.get(position).getName());
            holder.rollNo.setText(list.get(position).getRollNumber());


        if(position %2 == 1)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.recycleView_bc.setBackgroundColor((context.getColor(R.color.orange)));
            }

        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.recycleView_bc.setBackgroundColor((context.getColor(R.color.whiteColor)));
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView rollNo, name;
        private LinearLayout recycleView_bc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rollNo = itemView.findViewById(R.id.txt_roll_no);
            name = itemView.findViewById(R.id.txt_name);
            recycleView_bc = itemView.findViewById(R.id.recycleView_bc);
        }
    }

    public void addItems(List<PresentCandidatesList> dataList) {

        if(dataList == null){
            dataList = new ArrayList<>();
        }
        list.addAll(dataList);
        notifyDataSetChanged();
    }

    public void setItems(List<PresentCandidatesList> dataList) {

        if(dataList == null){
            dataList = new ArrayList<>();
        }
        list=dataList;
        notifyDataSetChanged();
    }

    public void removeItems(List<PresentCandidatesList> dataList) {

        if(dataList == null){
            dataList = new ArrayList<>();
        }
        list.removeAll(dataList);
        notifyDataSetChanged();
    }

    public void addToExistingList(List<PresentCandidatesList> dataList){
        // update oldDataModelList with newer data from pagination
        if(dataList == null){
            dataList = new ArrayList<>();
        }
        list.retainAll(dataList);
        notifyItemRangeChanged(list.size()-1 , dataList.size());
    }

}
