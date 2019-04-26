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

public class CustomAdapterInteractions extends ArrayAdapter<Interaction> {

    ArrayList<Interaction> mList;
    Context mContext;

    public CustomAdapterInteractions(Context context, ArrayList<Interaction> mList){
        super(context, 0, mList);
        this.mList = mList;
        this.mContext = context;
    }

    static class ViewHolder{
        TextView driver;
        TextView client;
        TextView state;


    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v =convertView;
        CustomAdapterInteractions.ViewHolder vHolder;
        if(v==null){
            v  = LayoutInflater.from(mContext).inflate(R.layout.custom_intercation, parent, false);
            vHolder = new CustomAdapterInteractions.ViewHolder();
            vHolder.driver =(TextView) v.findViewById(R.id.driver);
            vHolder.client = (TextView)  v.findViewById(R.id.client);
            vHolder.state = (TextView)  v.findViewById(R.id.state);
            v.setTag(vHolder);
        }
        else{
            vHolder = (CustomAdapterInteractions.ViewHolder) v.getTag();
        }

        Interaction interactionCourant = mList.get(position);
        TextView driver = vHolder.driver;
        driver.setText(interactionCourant.getDriver());
        TextView client = vHolder.client ;
        client.setText(interactionCourant.getClient());
        TextView state = vHolder.state ;
        state.setText(interactionCourant.getState());


        return v;
    }
}
