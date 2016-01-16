package com.mohanastrology.commodity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohanastrology.commodity.R;
import com.mohanastrology.commodity.javafiles.ServicePojo;
import com.mohanastrology.commodity.javafiles.CommodityViewHolder;

import java.util.List;

/**
 * Created by niraj on 11/14/2015.
 */
public class CommodityServiceAdapter extends RecyclerView.Adapter<CommodityViewHolder>{

    private LayoutInflater layoutInflater;
    private List<ServicePojo> listitem;
    Activity context;
    ViewClickListner viewClickListner;

    public CommodityServiceAdapter(Activity context, List<ServicePojo> listitem) {
        super();
        layoutInflater = LayoutInflater.from(context);
        this.context=context;
        this.listitem = listitem;

    }
    @Override
    public CommodityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= layoutInflater.from(context).inflate(R.layout.commodity_list_row,parent,false);
        CommodityViewHolder viewholder=new CommodityViewHolder(context,view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(CommodityViewHolder holder, final int position) {
        holder.tvtitle.setText(listitem.get(position).getSub_category());
        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewClickListner.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }
    public interface ViewClickListner
    {
        void onItemClick(int position);

    }

    public void setonclick(ViewClickListner viewOnClickButtonListner)
    {
        this.viewClickListner=viewOnClickButtonListner;
    }

}

