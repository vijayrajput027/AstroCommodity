package com.mohanastrology.commodity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohanastrology.commodity.R;
import com.mohanastrology.commodity.javafiles.HistoryPojo;
import com.mohanastrology.commodity.javafiles.HistoryViewHolder;
import com.mohanastrology.commodity.lazy.ImageLoader;

import java.util.List;

/**
 * Created by niraj on 11/23/2015.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder>{

    private LayoutInflater layoutInflater;
    private List<HistoryPojo> listitem;
    Activity act;
    HistoryClickListner historyClickListner;
    ImageLoader imageLoader;

    public HistoryAdapter(Activity context, List<HistoryPojo> listitem) {
        super();
        this.act = context;
        layoutInflater = LayoutInflater.from(context);
        this.listitem = listitem;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.history_item,parent,false);
        HistoryViewHolder viewholder=new HistoryViewHolder(act,view);
        return viewholder;    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder,final int position) {
        holder.tvdate.setText(listitem.get(position).getDate());
        holder.image.setImageBitmap(listitem.get(position).getImage());
        holder.tvcatregory.setText(listitem.get(position).getCategory());
        holder.tvsubcategory.setText(listitem.get(position).getSubcategory());
        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyClickListner.onitemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public interface HistoryClickListner
    {
        void onitemClick(int position);
    }
    public void setonClickListner(HistoryClickListner hIstoryClickListner)
    {
        this.historyClickListner=hIstoryClickListner;
    }

}
