package com.mohanastrology.commodity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohanastrology.commodity.R;
import com.mohanastrology.commodity.javafiles.DateViewHolder;

import java.util.List;

/**
 * Created by user on 11/17/2015.
 */
public class DailyAdapter extends RecyclerView.Adapter<DateViewHolder> {
    private LayoutInflater layoutInflater;
    private List<String> selectedDate;
    Activity context;
    ViewOnButtonClickListner listner;

    public DailyAdapter(Activity context,List<String> selectedDate)
    {
        this.context=context;
        this.selectedDate=selectedDate;
        layoutInflater=LayoutInflater.from(context);

    }
    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.date_row,parent,false);
        DateViewHolder viewHolder=new DateViewHolder(context,view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DateViewHolder holder, final int position) {
        holder.tvdate.setText(selectedDate.get(position).toString());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onItemClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return selectedDate.size();
    }
    public interface ViewOnButtonClickListner
    {
        void onItemClick(int position);

    }

    public void setonclick(ViewOnButtonClickListner viewOnClickButtonListner)
    {
        this.listner=viewOnClickButtonListner;
    }


}
