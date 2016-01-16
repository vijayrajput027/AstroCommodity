package com.mohanastrology.commodity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohanastrology.commodity.R;
import com.mohanastrology.commodity.javafiles.CommodityPojo;
import com.mohanastrology.commodity.javafiles.CommodityViewHolder;

import java.util.List;

/**
 * Created by niraj on 11/14/2015.
 */

public class CommodityAdapter extends RecyclerView.Adapter<CommodityViewHolder> {

    private LayoutInflater layoutInflater;
    private List<CommodityPojo> listitem;
    Activity act;
    ViewOnClickButtonListner listner;

    public CommodityAdapter(Activity context, List<CommodityPojo> listitem) {
        super();
        this.act = context;
        layoutInflater = LayoutInflater.from(context);
        this.listitem = listitem;
    }
        @Override
        public CommodityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=layoutInflater.inflate(R.layout.commodity_list_row,parent,false);
            CommodityViewHolder viewholder=new CommodityViewHolder(act,view);
            return viewholder;
        }

        @Override
        public void onBindViewHolder(CommodityViewHolder holder, final int position) {
            holder.tvtitle.setText(listitem.get(position).getTitle());
            holder.relative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onItemClick(position);
                }
            });
        }
        @Override
        public int getItemCount() {
            return listitem.size();
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

