package com.mohanastrology.commodity.javafiles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mohanastrology.commodity.R;

/**
 * Created by user on 11/16/2015.
 */
public class CommodityViewHolder extends RecyclerView.ViewHolder  {
        private Context context;
        public TextView tvtitle;
        public RelativeLayout relative;

public CommodityViewHolder(Context context,View itemview)
        {
        super(itemview);
        this.context=context;
        tvtitle=(TextView)itemview.findViewById(R.id.commodity_title);
        relative=(RelativeLayout)itemview.findViewById(R.id.relative_commodity);
        }
}
