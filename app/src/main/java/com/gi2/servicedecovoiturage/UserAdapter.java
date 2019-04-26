package com.gi2.servicedecovoiturage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gi2.servicedecovoiturage.logregform.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {
    ArrayList<User> mList;
    Context mContext;

    public UserAdapter(Context context, ArrayList<User> mList){
        super(context, 0, mList);
        this.mList = mList;
        this.mContext = context;
    }

    static class ViewHolder{
        ImageView mImage;
        TextView mUsername;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v =convertView;
        final ViewHolder vHolder;
        if(v==null){
            v  = LayoutInflater.from(mContext).inflate(R.layout.users_list_item, parent, false);
            vHolder = new ViewHolder();
            vHolder.mImage =(ImageView) v.findViewById(R.id.user_profile);
            vHolder.mUsername = (TextView)  v.findViewById(R.id.username);
            v.setTag(vHolder);
        }
        else{
            vHolder = (ViewHolder) v.getTag();
        }

        User userCourant = mList.get(position);
        ImageView image = vHolder.mImage;
        TextView username = vHolder.mUsername ;
        username.setText(userCourant.getmUsername());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://service-de-covoiturage.appspot.com")
                .child("users/"+ vHolder.mUsername.getText()+".jpg");
        try {
            final File localFile = File.createTempFile("msg_profile", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    vHolder.mImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {
        }

        return v;
    }
}
