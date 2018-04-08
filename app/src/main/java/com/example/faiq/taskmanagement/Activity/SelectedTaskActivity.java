package com.example.faiq.taskmanagement.Activity;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.faiq.taskmanagement.Adapters.SelectedTaskAdapter;
import com.example.faiq.taskmanagement.Models.SelectedTaskModel;
import com.example.faiq.taskmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class SelectedTaskActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SelectedTaskAdapter adapter;
    List<SelectedTaskModel> list=new ArrayList<>();
    Activity activity;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_task);
        activity=this;
        recyclerView=(RecyclerView)findViewById(R.id.recycleview);
        back=(ImageView)findViewById(R.id.back);

        adapter=new SelectedTaskAdapter(list , activity );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        loadData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void loadData()
    {
        for ( int i=0  ; i < 10 ; i++)
        {
            SelectedTaskModel model=new SelectedTaskModel();
            model.setName("Task Name");
            model.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
            model.setTime("11:45");

            list.add(model);

        }
        adapter.notifyDataSetChanged();
    }
}
