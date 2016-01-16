package com.mohanastrology.commodity.javafiles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mohanastrology.commodity.R;

/**
 * Created by user on 11/23/2015.
 */
public class HistoryViewHolder extends RecyclerView.ViewHolder  {
    private Context context;
    public TextView tvdate;
    public RelativeLayout relative;
    public ImageView image;
    public TextView tvcatregory;
    public TextView tvsubcategory;

    public HistoryViewHolder(Context context,View itemview)
    {
        super(itemview);
        this.context=context;
        tvdate=(TextView)itemview.findViewById(R.id.tvdate);
        image=(ImageView)itemview.findViewById(R.id.currency_image);
        tvcatregory=(TextView)itemview.findViewById(R.id.tvcategory);
        tvsubcategory=(TextView)itemview.findViewById(R.id.tvsubcategory);
        relative=(RelativeLayout)itemview.findViewById(R.id.relative_history);
    }
}
