package com.mohanastrology.commodity.javafiles;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mohanastrology.commodity.R;
/**
 * Created by user on 11/25/2015.
 */
public class RashiphalViewHolder extends RecyclerView.ViewHolder {

    public ImageView rashiImage;
    public TextView tvrashiName;
    public RelativeLayout relative;


    public RashiphalViewHolder(View itemView) {
        super(itemView);
        rashiImage = (ImageView)itemView.findViewById(R.id.img_thumbnail);
        tvrashiName = (TextView)itemView.findViewById(R.id.tv_species);
        relative=(RelativeLayout)itemView.findViewById(R.id.top_layout);
    }
}

