package com.lechatong.beakhub.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lechatong.beakhub.Models.BhCategory;
import com.lechatong.beakhub.R;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<BhCategory> {

    private Context context;

    private List<BhCategory> values;

    public CategoryAdapter(Context context, int textViewResourceId,
                           List<BhCategory> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public BhCategory getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(R.color.bgBeakHub);
        label.setHintTextColor(R.color.bgBeakHub);

        label.setText(values.get(position).getTitle());

        return label;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(R.color.bgBeakHub);
        label.setHintTextColor(R.color.bgBeakHub);
        label.setText(values.get(position).getTitle());

        return label;
    }
}
