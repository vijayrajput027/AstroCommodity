package com.mohanastrology.commodity.javafiles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mohanastrology.commodity.R;

/**
 * Created by user on 11/17/2015.
 */
public class DateViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    public TextView tvdate;
    public ImageButton imgDelete;
    public RelativeLayout relative;

    public DateViewHolder(Context context,View itemview)
    {
        super(itemview);
        this.context=context;
        tvdate=(TextView)itemview.findViewById(R.id.edit_date);
        imgDelete=(ImageButton)itemview.findViewById(R.id.crossimage);
        relative=(RelativeLayout)itemview.findViewById(R.id.relative_daily);
    }
}
