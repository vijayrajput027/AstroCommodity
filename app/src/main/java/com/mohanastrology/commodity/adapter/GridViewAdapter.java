package com.mohanastrology.commodity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohanastrology.commodity.javafiles.CommodityPojo;
import com.mohanastrology.commodity.javafiles.RashiphalPojo;
import com.mohanastrology.commodity.javafiles.RashiphalViewHolder;

import java.util.ArrayList;
import java.util.List;
import com.mohanastrology.commodity.R;
/**
 * Created by niraj on 11/13/2015.
 */
public class GridViewAdapter extends RecyclerView.Adapter<RashiphalViewHolder>
{
   private LayoutInflater layoutInflater;
   private List<RashiphalPojo> mItems;
   ViewOnClickButtonListner listner;
   Activity act;

   public GridViewAdapter(Activity context, List<RashiphalPojo> listitem) {
           super();
           this.act = context;
           layoutInflater = LayoutInflater.from(context);
           this.mItems = listitem;
        }


@Override
public RashiphalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item, viewGroup, false);
        RashiphalViewHolder viewHolder = new RashiphalViewHolder(v);
        return viewHolder;
        }

        @Override
        public void onBindViewHolder(RashiphalViewHolder holder, final int position) {
                RashiphalPojo rashiphalPojo = mItems.get(position);
                holder.tvrashiName.setText(rashiphalPojo.getrashiName());
                holder.rashiImage.setImageResource(rashiphalPojo.getRashiImage());
                holder.relative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                listner.onItemClick(position);
                        }
                });
        }

@Override
public int getItemCount() {

        return mItems.size();
        }


        public interface ViewOnClickButtonListner
        {
                void onItemClick(int position);

        }

        public void setonclick(ViewOnClickButtonListner viewOnClickButtonListner)
        {
                this.listner=viewOnClickButtonListner;
        }
}