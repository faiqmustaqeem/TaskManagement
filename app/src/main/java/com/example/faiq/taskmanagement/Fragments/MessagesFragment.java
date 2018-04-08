package com.example.faiq.taskmanagement.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.faiq.taskmanagement.Adapters.MessageAdapter;
import com.example.faiq.taskmanagement.Adapters.TaskAdapter;
import com.example.faiq.taskmanagement.Models.MessageModel;
import com.example.faiq.taskmanagement.Models.TaskModel;
import com.example.faiq.taskmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {

    RecyclerView recyclerView;
    MessageAdapter adapter;
    List<MessageModel> list=new ArrayList<>();
    Activity activity;

    public MessagesFragment() {
        // Required empty public constructor
    }

    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_messages, container, false);
        activity=getActivity();

        recyclerView=view.findViewById(R.id.recycleview);
        adapter=new MessageAdapter(list , activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
//         recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        loadMessages();


        return view;
    }
    private void loadMessages()
    {
        for(int i= 0 ; i < 10 ; i++)
        {
            MessageModel model=new MessageModel();
            model.setTitle("John Doe");
            model.setMessage("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries");
            list.add(model);
        }
        adapter.notifyDataSetChanged();
    }

}
