package com.example.faiq.taskmanagement.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.faiq.taskmanagement.Adapters.TaskAdapter;
import com.example.faiq.taskmanagement.Models.TaskModel;
import com.example.faiq.taskmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class AllTasksFragment extends Fragment {

    RecyclerView recyclerView;
    TaskAdapter adapter;
    List<TaskModel> list=new ArrayList<>();
    Activity activity;


    public AllTasksFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_tasks, container, false);
        activity=getActivity();

        recyclerView=view.findViewById(R.id.recycleview_all_tasks);
        adapter=new TaskAdapter(list , activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
//         recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        loadTasks();


        return  view;


    }
    public void loadTasks()
    {
        for ( int i=0 ; i < 10 ; i++  )
        {
            TaskModel model=new TaskModel();
            model.setTitle("John Doe");
            model.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries");
            list.add(model);
        }
        adapter.notifyDataSetChanged();
    }

}
