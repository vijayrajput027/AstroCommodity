package com.mohanastrology.commodity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohanastrology.commodity.R;
import com.mohanastrology.commodity.javafiles.CurrencyPojo;
import com.mohanastrology.commodity.javafiles.HistoryPojo;
import com.mohanastrology.commodity.javafiles.CurrencyViewHolder;
import com.mohanastrology.commodity.lazy.ImageLoader;

import java.util.List;

/**
 * Created by niraj on 11/19/2015.
 */
public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyViewHolder> {

    private LayoutInflater layoutInflater;
    private List<CurrencyPojo> currencyListItem;
    Activity act;
    CurrencyClickListner currencyClickListner;

    public CurrencyAdapter(Activity context, List<CurrencyPojo> currencyListItem) {
        super();
        this.act = context;
        layoutInflater = LayoutInflater.from(context);
        this.currencyListItem = currencyListItem;
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.currency_item,parent,false);
        CurrencyViewHolder viewholder=new CurrencyViewHolder(act,view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, final int position) {
        holder.tvsubcategory.setText(currencyListItem.get(position).getSubcategory());
        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currencyClickListner.onitemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return currencyListItem.size();
    }

    public interface CurrencyClickListner
    {
        void onitemClick(int position);
    }
    public void setonClickListner(CurrencyClickListner currencyClickListner)
    {
        this.currencyClickListner=currencyClickListner;
    }

}