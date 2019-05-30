
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

public class CustomAdapterUserPending extends ArrayAdapter<UserProfile> {
        ArrayList<UserProfile> mList;
        Context mContext;
        public CustomAdapterUserPending(Context context, ArrayList<UserProfile> mList){
            super(context, 0, mList);
            this.mList = mList;
            this.mContext = context;
        }

        static class ViewHolder{
            TextView username;
            TextView email;
            TextView cne;

        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v =convertView;
            com.gi2.servicedecovoiturage.CustomAdapterUserPending.ViewHolder vHolder;
            if(v==null){
                v  = LayoutInflater.from(mContext).inflate(R.layout.custom_user_pend, parent, false);
                vHolder = new com.gi2.servicedecovoiturage.CustomAdapterUserPending.ViewHolder();
                vHolder.username =(TextView) v.findViewById(R.id.username);
                vHolder.email = (TextView)  v.findViewById(R.id.reported_user);
                vHolder.cne = (TextView)  v.findViewById(R.id.cne);

                v.setTag(vHolder);
            }
            else{
                vHolder = (com.gi2.servicedecovoiturage.CustomAdapterUserPending.ViewHolder) v.getTag();
            }

            UserProfile user = mList.get(position);

            TextView mail = vHolder.email ;
            mail.setText(user.getEmail());

            TextView name = vHolder.username;
            name.setText(user.getUsername());

            TextView cne = vHolder.cne;
            cne.setText(user.getCNE());

            return v;
        }
    }


