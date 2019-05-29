
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

public class CustomAdapterUserReports extends ArrayAdapter<Report> {
    ArrayList<Report> mList;
    Context mContext;
    public CustomAdapterUserReports(Context context, ArrayList<Report> mList){
        super(context, 0, mList);
        this.mList = mList;
        this.mContext = context;
    }

    static class ViewHolder{
        TextView username;
        TextView reported_user;
        TextView Titre;


    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v =convertView;
        com.gi2.servicedecovoiturage.CustomAdapterUserReports.ViewHolder vHolder;
        if(v==null){
            v  = LayoutInflater.from(mContext).inflate(R.layout.reports, parent, false);
            vHolder = new com.gi2.servicedecovoiturage.CustomAdapterUserReports.ViewHolder();
            vHolder.username =(TextView) v.findViewById(R.id.username);
            vHolder.reported_user = (TextView)  v.findViewById(R.id.reported_user);
            vHolder.Titre = (TextView)  v.findViewById(R.id.titre);

            v.setTag(vHolder);
        }
        else{
            vHolder = (com.gi2.servicedecovoiturage.CustomAdapterUserReports.ViewHolder) v.getTag();
        }

        Report report = mList.get(position);
        TextView reported_user = vHolder.reported_user ;
        reported_user.setText(report.getReported_user());

        TextView name = vHolder.username;
        name.setText(report.getUsername());

        TextView description = vHolder.Titre;
        description.setText(report.getTitre());
        return v;
    }
}


