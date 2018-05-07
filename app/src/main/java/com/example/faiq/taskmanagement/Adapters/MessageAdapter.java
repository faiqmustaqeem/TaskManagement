package com.example.faiq.taskmanagement.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.faiq.taskmanagement.Models.MessageModel;

import com.example.faiq.taskmanagement.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by faiq on 08/04/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    List<MessageModel> list=new ArrayList<>();
    Context context;
    Activity activity;
    public MessageAdapter(List<MessageModel> list , Context context , Activity activity)
    {
        this.list=list;
        this.context=context;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MessageModel model=list.get(position);

        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getMessage());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(context , ChatActivity.class);
//                context.startActivity(intent);
//                activity.overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView title;
        TextView desc;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            image=(CircleImageView)itemView.findViewById(R.id.profile_pic);
            title=(TextView)itemView.findViewById(R.id.title1);
            desc=(TextView)itemView.findViewById(R.id.desc);

        }
    }
}

