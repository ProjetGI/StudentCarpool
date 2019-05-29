package com.gi2.servicedecovoiturage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gi2.servicedecovoiturage.logregform.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Voyage> {
    ArrayList<Voyage> mList;
    ArrayList<Voyage> arraylist;
    Context mContext;

    public CustomAdapter(Context context, ArrayList<Voyage> mList){
        super(context, 0, mList);
        this.mList = mList;
        this.mContext = context;
        arraylist = new ArrayList<Voyage>();
        arraylist.addAll(mList);
    }

    static class ViewHolder{
        TextView mDriver;
        TextView mDeparture;
        TextView mArrival;
        TextView mPrice;
        TextView mTime;
        TextView mDate;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v =convertView;
        ViewHolder vHolder;
        if(v==null){
            v  = LayoutInflater.from(mContext).inflate(R.layout.custom_list_item, parent, false);
            vHolder = new ViewHolder();
            vHolder.mDriver =(TextView) v.findViewById(R.id.name);
            vHolder.mDeparture = (TextView)  v.findViewById(R.id.departure);
            vHolder.mArrival = (TextView)  v.findViewById(R.id.arrival);
            vHolder.mPrice = (TextView)  v.findViewById(R.id.price);
            vHolder.mTime = (TextView)  v.findViewById(R.id.time);
            vHolder.mDate = (TextView)  v.findViewById(R.id.date);
            v.setTag(vHolder);
        }
        else{
            vHolder = (ViewHolder) v.getTag();
        }

        Voyage voyageCourant = mList.get(position);
        TextView mDriver = vHolder.mDriver;
        mDriver.setText(voyageCourant.getmDriver());
        TextView departure = vHolder.mDeparture ;
        departure.setText(voyageCourant.getmDeparture());
        TextView arrival = vHolder.mArrival ;
        arrival.setText(voyageCourant.getmArrival());
        TextView price = vHolder.mPrice ;
        price.setText(voyageCourant.getmPrice());
        TextView time = vHolder.mTime ;
        time.setText(voyageCourant.getmTime());
        TextView date = vHolder.mDate ;
        date.setText(voyageCourant.getmDate());

        return v;
    }

    public void filter(String charText) {
        if(!arraylist.isEmpty()){
            charText = charText.toLowerCase();
            mList.clear();
            if (charText.length() == 0) {
                mList.addAll(arraylist);;
            }
            else
            {
                for (Voyage voyage : arraylist) {
                    if (voyage.getmDeparture().toLowerCase().contains(charText)) {
                        mList.add(voyage);
                    }
                }
            }
            notifyDataSetChanged();
        }

    }

    public void filterArrival(String charText) {
        if(!arraylist.isEmpty()){
            charText = charText.toLowerCase();
            mList.clear();
            if (charText.length() == 0) {
                mList.addAll(arraylist);;
            }
            else
            {
                for (Voyage voyage : arraylist) {
                    if (voyage.getmArrival().toLowerCase().contains(charText)) {
                        mList.add(voyage);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public void filterDate(String charText) {
        if(!arraylist.isEmpty()){
            charText = charText.toLowerCase();
            mList.clear();
            if (charText.length() == 0) {
                mList.addAll(arraylist);;
            }
            else
            {
                for (Voyage voyage : arraylist) {
                    if (voyage.getmDate().toLowerCase().contains(charText)) {
                        mList.add(voyage);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
