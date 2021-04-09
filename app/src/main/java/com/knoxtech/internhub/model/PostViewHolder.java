package com.knoxtech.internhub.model;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.knoxtech.internhub.R;
import com.squareup.picasso.Picasso;

public class PostViewHolder extends RecyclerView.ViewHolder {

    TextView companyName, Designation,salary,date;
    ImageView logo;
    public PostViewHolder(@NonNull View itemView) {
        super(itemView);

        companyName=itemView.findViewById(R.id.companyNm);
        Designation=itemView.findViewById(R.id.designation);
        salary=itemView.findViewById(R.id.salary);
        date=itemView.findViewById(R.id.date);
        logo=itemView.findViewById(R.id.logo);
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION && listener != null) {
//                    //listener.onItemClick(getSnapshots().getSnapshot(position), position);
//                    Log.i("error","Error");
//                }else {
//                    Log.i("error","Error");
//                }
//            }
//        });
    }

    public void bind(Data data) {
        companyName.setText(data.getCompanyName());
        Designation.setText(data.getDesignation());
        salary.setText(data.getLocation());
        date.setText(data.getDate());

        Picasso.get().load(data.getLogo()).into(logo);
    }

}
