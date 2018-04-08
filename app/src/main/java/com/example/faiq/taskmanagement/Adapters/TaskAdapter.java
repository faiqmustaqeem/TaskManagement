package com.example.faiq.taskmanagement.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.faiq.taskmanagement.Activity.SelectedTaskActivity;
import com.example.faiq.taskmanagement.Models.TaskModel;
import com.example.faiq.taskmanagement.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by faiq on 08/04/2018.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    List<TaskModel> list=new ArrayList<>();
    Context context;

    public TaskAdapter(List<TaskModel> list , Context context)
    {
        this.list=list;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TaskModel model=list.get(position);

        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SelectedTaskActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       CircleImageView image;
        TextView title;
        TextView desc;
        public MyViewHolder(View itemView) {
            super(itemView);
            image=(CircleImageView)itemView.findViewById(R.id.profile_pic);
            title=(TextView)itemView.findViewById(R.id.title1);
            desc=(TextView)itemView.findViewById(R.id.desc);

        }
    }
}
