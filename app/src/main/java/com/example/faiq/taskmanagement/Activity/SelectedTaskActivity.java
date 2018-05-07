package com.example.faiq.taskmanagement.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.faiq.taskmanagement.Adapters.SelectedTaskAdapter;
import com.example.faiq.taskmanagement.Models.SelectedTaskModel;
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

        adapter=new SelectedTaskAdapter(list , activity, activity );
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

        final ProgressDialog dialog=new ProgressDialog(activity);
        dialog.setTitle("Loading");
        dialog.setMessage("Wait...");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, "http://admiria.pk/office1/api_v1/Invoices/get_task",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("login" , response);

                            JSONObject job = new JSONObject(response);
                            JSONObject result = job.getJSONObject("result");
                            String res = result.getString("response");


                            if (res.equals("Tasks Recieved")) {


                                JSONArray dataArray= result.getJSONArray("data");

                                for ( int i=0 ; i < dataArray.length() ; i++)
                                {
                                    JSONObject jsonObject=dataArray.getJSONObject(i);
                                    SelectedTaskModel model=new SelectedTaskModel();
                                    model.setId(jsonObject.getString("t_id"));
                                    model.setName(jsonObject.getString("task_name"));
                                    model.setDescription(jsonObject.getString("description"));

                                    String assignedTo=jsonObject.getString("assigned_to");
                                    int ind=assignedTo.indexOf('\"');
                                    ind=ind+1;
                                    String new_assigned_to=assignedTo.substring(ind,ind+1);
                                    Log.e("ass" , new_assigned_to);
                                    SharedPreferences sharedPreferences=activity.getSharedPreferences("SharedPreferences", MODE_PRIVATE);

                                    String user_id=sharedPreferences.getString("id","");

                                    if(new_assigned_to.equals(user_id))
                                    {
                                        if(GlobalClass.is_from.equals("all"))
                                        {
                                            String progress=jsonObject.getString("task_progress");
                                            if(progress.equals("100"))
                                            {
                                                String start_time_str=jsonObject.getString("start_time");
                                                String end_time_str=jsonObject.getString("end_time");
                                                long start_time=Long.valueOf(start_time_str);
                                                long end_time=Long.valueOf(end_time_str);
                                                long diff=end_time-start_time;
                                                long min=diff/60;
                                                min=min%60;
                                                long sec=diff%60;
                                                String time=min+":"+sec;
                                                model.setTime(time);
                                            }

                                            list.add(model);
                                        }
                                        else if(GlobalClass.is_from.equals("completed"))
                                        {

                                         String progress=jsonObject.getString("task_progress");
                                            String start_time_str=jsonObject.getString("start_time");
                                            String end_time_str=jsonObject.getString("end_time");
                                            long start_time=Long.valueOf(start_time_str);
                                            long end_time=Long.valueOf(end_time_str);
                                            long diff=end_time-start_time;
                                            long min=diff/60;
                                            min=min%60;
                                            long sec=diff%60;
                                            String time=min+":"+sec;
                                            model.setTime(time);
                                            if(progress.equals("100"))
                                            {
                                                list.add(model);
                                            }
                                        }
                                        else if(GlobalClass.is_from.equals("remaining"))
                                        {
                                            String progress=jsonObject.getString("task_progress");
                                            if(!progress.equals("100"))
                                            {
                                                list.add(model);
                                            }
                                        }


                                    }


                                }

                                adapter.notifyDataSetChanged();

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
                params.put("project_id" , GlobalClass.project_id);


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
                Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
