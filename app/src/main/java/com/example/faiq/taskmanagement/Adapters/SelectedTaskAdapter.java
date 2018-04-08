package com.example.faiq.taskmanagement.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import com.example.faiq.taskmanagement.Models.SelectedTaskModel;
import com.example.faiq.taskmanagement.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by faiq on 08/04/2018.
 */

public class SelectedTaskAdapter extends RecyclerView.Adapter<SelectedTaskAdapter.MyViewHolder>{

    List<SelectedTaskModel> list= new ArrayList<>();
    Context context;

    public SelectedTaskAdapter(List<SelectedTaskModel> list  , Context context)
    {
        this.list=list;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selected_task_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SelectedTaskModel model=list.get(position);

        holder.position_text.setText(String.valueOf(position+1)+".");
        holder.title_text.setText(model.getName());
        holder.desc.setText(model.getDescription());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView position_text;
        TextView title_text;
        Switch switchButton;
        CheckBox checkBox;
        TextView desc;
        TextView time;

        public MyViewHolder(View itemView) {
            super(itemView);

            position_text=itemView.findViewById(R.id.position_text);
            title_text=itemView.findViewById(R.id.title_text);
            switchButton= itemView.findViewById(R.id.switch_button);
            checkBox=itemView.findViewById(R.id.checkbox_completed);
            time=itemView.findViewById(R.id.time_text);
            desc=itemView.findViewById(R.id.desc);
        }
    }
}
