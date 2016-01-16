package com.mohanastrology.commodity.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mohanastrology.commodity.R;
import com.mohanastrology.commodity.javafiles.ProblemSolutionPojo;

import java.util.List;

/**
 * Created by niraj on 11/27/2015.
 */
public class SpinnerforAdapter extends BaseAdapter{

    private List<ProblemSolutionPojo> list;
    Context context;

    public SpinnerforAdapter(Context context, List<ProblemSolutionPojo> list) {
        this.context=context;
        this.list = list;

    }

    @Override
    public int getCount() {
      return list.size();
    }

    @Override
    public ProblemSolutionPojo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             convertView=inflater.inflate(R.layout.spinner_item, parent, false);
        }
        TextView name =(TextView)convertView.findViewById(R.id.textName);
        ProblemSolutionPojo pojo=getItem(position);
        if(pojo!=null) {
            name.setText(pojo.getName());
        }
            return convertView;
    }
}
