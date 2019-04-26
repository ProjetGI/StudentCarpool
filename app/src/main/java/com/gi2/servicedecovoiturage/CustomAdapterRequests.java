package com.gi2.servicedecovoiturage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.gi2.servicedecovoiturage.logregform.R;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapterRequests extends ArrayAdapter<Request> {
    ArrayList<Request> mList;
    Context mContext;

    public CustomAdapterRequests(Context context, ArrayList<Request> mList){
        super(context, 0, mList);
        this.mList = mList;
        this.mContext = context;
    }

    static class ViewHolder{
        TextView client;
        TextView departure;
        TextView arrival;
        TextView time;
        TextView date;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v =convertView;
        ViewHolder vHolder;
        if(v==null){
            v  = LayoutInflater.from(mContext).inflate(R.layout.custom_list_item_requests, parent, false);
            vHolder = new ViewHolder();
            vHolder.client =(TextView) v.findViewById(R.id.name);
            vHolder.departure = (TextView)  v.findViewById(R.id.departure);
            vHolder.arrival = (TextView)  v.findViewById(R.id.arrival);
            vHolder.time = (TextView)  v.findViewById(R.id.time);
            vHolder.date = (TextView)  v.findViewById(R.id.date);
            v.setTag(vHolder);
        }
        else{
            vHolder = (ViewHolder) v.getTag();
        }

        Request requestCourant = mList.get(position);
        TextView mDriver = vHolder.client;
        mDriver.setText(requestCourant.getClient());
        TextView departure = vHolder.departure ;
        departure.setText(requestCourant.getDeparture());
        TextView arrival = vHolder.arrival ;
        arrival.setText(requestCourant.getArrival());
        TextView time = vHolder.time ;
        time.setText(requestCourant.getTime());
        TextView date = vHolder.date ;
        date.setText(requestCourant.getDate());

        return v;
    }
}

