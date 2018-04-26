package com.example.faiq.taskmanagement.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.faiq.taskmanagement.Activity.GlobalClass;
import com.example.faiq.taskmanagement.Adapters.TaskAdapter;
import com.example.faiq.taskmanagement.Models.TaskModel;
import com.example.faiq.taskmanagement.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class RemainingTasksFragment extends Fragment {

    RecyclerView recyclerView;
    TaskAdapter adapter;
    List<TaskModel> list=new ArrayList<>();
    Activity activity;

    public RemainingTasksFragment() {
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
        recyclerView.setAdapter(adapter);

        GlobalClass.is_from="remaining";
        loadTasks();


        return  view;


    }
    public void loadTasks()
    {
        final ProgressDialog dialog=new ProgressDialog(activity);
        dialog.setTitle("Loading");
        dialog.setMessage("Wait...");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, "http://admiria.pk/office1/api_v1/Invoices/get_projects",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("login" , response);

                            JSONObject job = new JSONObject(response);
                            JSONObject result = job.getJSONObject("result");
                            String res = result.getString("response");


                            if (res.equals("Projects Recieved")) {


                                JSONArray dataArray= result.getJSONArray("data");

                                for ( int i=0 ; i < dataArray.length() ; i++)
                                {
                                    JSONObject jsonObject=dataArray.getJSONObject(i);
                                    TaskModel model=new TaskModel();
                                    model.setId(jsonObject.getString("project_id"));
                                    model.setTitle(jsonObject.getString("project_title"));
                                    model.setDescription(jsonObject.getString("description"));


                                    list.add(model);

                                }

                                dialog.dismiss();




                                adapter.notifyDataSetChanged();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(activity,res, Toast.LENGTH_SHORT).show();
                                // finish();
                            }
                        } catch (JSONException e) {
                            Log.e("ErrorMessage", e.getMessage());
                            e.printStackTrace();
                            dialog.dismiss();
                        }

                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.e("Volley_error" , error.getMessage() );
                        parseVolleyError(error);
                        dialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                SharedPreferences sharedPreferences=activity.getSharedPreferences("SharedPreferences", MODE_PRIVATE);

                String user_id=sharedPreferences.getString("id","");

                params.put("user_id", user_id);

                Log.e("params" , params.toString());

                return params;
            }
        };

        Volley.newRequestQueue(activity).add(request);


    }
    public void parseVolleyError(VolleyError error) {
        NetworkResponse response=error.networkResponse;

        if(response!=null && response.data!=null) {
            try {
                String responseBody = new String(error.networkResponse.data, "utf-8");
                JSONObject data = new JSONObject(responseBody);
                JSONObject result = data.getJSONObject("result");
                String message = result.getString("response");
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_LONG).show();
            } catch (JSONException | UnsupportedEncodingException e) {
                printMsg("internet problem...");
            }
        }
        else
        {
            printMsg("Your application is not connected to internet...");
        }
    }
    void printMsg(String msg)
    {
        Toast.makeText(activity ,msg,Toast.LENGTH_SHORT).show();
    }



}
