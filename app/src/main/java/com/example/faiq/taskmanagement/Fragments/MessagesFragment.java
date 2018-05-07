package com.example.faiq.taskmanagement.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.faiq.taskmanagement.Activity.GlobalClass;
import com.example.faiq.taskmanagement.Adapters.MessageAdapter;
import com.example.faiq.taskmanagement.Adapters.TaskAdapter;
import com.example.faiq.taskmanagement.Models.MessageModel;
import com.example.faiq.taskmanagement.Models.TaskModel;
import com.example.faiq.taskmanagement.R;
import com.example.faiq.taskmanagement.RealtimeChat.ui.adapters.UserListingPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MessagesFragment extends Fragment {

//    private Toolbar mToolbar;
    private TabLayout mTabLayoutUserListing;
    private ViewPager mViewPagerUserListing;
    View view;

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
        this.view=view;
        bindViews();
        init();
//        activity=getActivity();
//
//        recyclerView=view.findViewById(R.id.recycleview);
//        adapter=new MessageAdapter(list , activity , activity);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
//        recyclerView.setLayoutManager(mLayoutManager);
////         recyclerView.setItemAnimator(new DefaultItemAnimator());
////        recyclerView.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL));
//        recyclerView.setAdapter(adapter);
//
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SharedPreferences", MODE_PRIVATE);

        GlobalClass.user_id=sharedPreferences.getString("id","");


//        loadMessages();


        return view;
    }
    private void bindViews() {
//        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mTabLayoutUserListing = (TabLayout) view.findViewById(R.id.tab_layout_user_listing);
        mViewPagerUserListing = (ViewPager) view.findViewById(R.id.view_pager_user_listing);
    }

    private void init() {
        // set the toolbar
//        setSupportActionBar(mToolbar);

        // set the view pager adapter
        UserListingPagerAdapter userListingPagerAdapter = new UserListingPagerAdapter(getChildFragmentManager());
        mViewPagerUserListing.setAdapter(userListingPagerAdapter);

        // attach tab layout with view pager
        mTabLayoutUserListing.setupWithViewPager(mViewPagerUserListing);


    }
//    private void loadMessages()
//    {
//        for(int i= 0 ; i < 10 ; i++)
//        {
//            MessageModel model=new MessageModel();
//            model.setTitle("John Doe");
//            model.setMessage("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries");
//            list.add(model);
//        }
//        adapter.notifyDataSetChanged();
//    }

}
