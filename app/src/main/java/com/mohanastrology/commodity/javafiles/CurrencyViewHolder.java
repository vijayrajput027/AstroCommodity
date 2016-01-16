package com.mohanastrology.commodity.javafiles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mohanastrology.commodity.R;

/**
 * Created by user on 11/19/2015.
 */
public class CurrencyViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    public TextView tvsubcategory;
    public RelativeLayout relative;

    public CurrencyViewHolder(Context context,View itemview)
    {
        super(itemview);
        this.context=context;
        tvsubcategory=(TextView)itemview.findViewById(R.id.tvsubcategory);
        relative=(RelativeLayout)itemview.findViewById(R.id.relative_currency);
    }
}