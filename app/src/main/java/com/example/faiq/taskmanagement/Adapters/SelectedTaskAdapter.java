package com.example.faiq.taskmanagement.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.faiq.taskmanagement.Activity.GlobalClass;
import com.example.faiq.taskmanagement.Models.SelectedTaskModel;
import com.example.faiq.taskmanagement.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by faiq on 08/04/2018.
 */

public class SelectedTaskAdapter extends RecyclerView.Adapter<SelectedTaskAdapter.MyViewHolder>{

    List<SelectedTaskModel> list= new ArrayList<>();
    Context context;
    SharedPreferences sharedPref ;
    Activity activity;

    public SelectedTaskAdapter(List<SelectedTaskModel> list  , Context context , Activity activity)
    {
        this.list=list;
        this.context=context;
       sharedPref = PreferenceManager.getDefaultSharedPreferences(this.context);
        this.activity=activity;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selected_task_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SelectedTaskModel model = list.get(position);

        holder.position_text.setText(String.valueOf(position + 1) + ".");
        holder.title_text.setText(model.getName());
        holder.desc.setText(model.getDescription());
        Log.e("position" , position+"");

        final String id = sharedPref.getString("id"+model.getId(), "");
        Log.e("id" , "id is "+id);
        final Runnable[] runnable = {null};

        if(id.equals("completed"))
        {
            holder.checkBox.setChecked(true);
            holder.checkBox.setClickable(false);
            holder.switchButton.setChecked(false);
            holder.switchButton.setClickable(false);
            holder.mHandler.removeCallbacks(null);
        }

       else if (id.equals(model.getId()))
         {
            Log.e("if "+position , "else");
            long start_time= sharedPref.getLong("start_time"+model.getId(),Long.MIN_VALUE);

             long time_now= System.currentTimeMillis();
             long diff=time_now-start_time;

            long seconds_all= diff/1000;

            holder.counter=seconds_all;
             holder.switchButton.setChecked(true);
            long min=seconds_all/60;
             holder.min=min%60;
             long sec=seconds_all%60;
             holder.sec=sec;
             long hour=min/60;
             holder.hour=hour;
             runnable[0]=new Runnable() {

                @Override
                public void run() {
                    holder.counter++;
                    if(holder.counter%60==0)
                    {
                        holder.min++;
                        holder.sec=0;

                        if(holder.min%60==0)
                        {
                            holder.hour++;
                            holder.min=0;
                        }
                    }
                    else {
                        holder.sec++;
                    }

                    holder.time.setText(holder.hour+":"+holder.min+":"+ holder.sec);
                    holder.mHandler.postDelayed(this,1000);
                }
            };
            holder.mHandler.postDelayed(runnable[0] , 1000);
        }



        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                        new MaterialDialog.Builder(activity)
                                .title("Message")
                                .content("are you sure you want to stop timer ?")
                                .positiveText("yes")
                                .negativeText("no")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("id"+model.getId() , "completed");
                                        editor.commit();
                                        holder.mHandler.removeCallbacks(runnable[0]);
                                        holder.switchButton.setChecked(false);
                                        holder.switchButton.setClickable(false);

                                        holder.checkBox.setChecked(true);
                                        holder.checkBox.setClickable(false);

                                        long end_time=System.currentTimeMillis();
                                        sendEndTimerStatus(model.getId() , end_time/1000);

                                    }
                                })
                                .canceledOnTouchOutside(false)
                                .cancelable(false)
                                .show();

                }
                else {
                }
            }
        });

        holder.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b)
                {

                    Log.e("switchbutton","true");
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("id"+model.getId() , model.getId());
                    Log.e("id"+model.getId() , model.getId());
                    long start_time=System.currentTimeMillis();
                    editor.putLong("start_time"+model.getId() , start_time );
                    editor.commit();

                    sendStarTimerStatus(model.getId() ,start_time/1000 );

                     runnable[0] =new Runnable() {

                    @Override
                    public void run() {
                        holder.counter++;
                        if(holder.counter%60==0)
                        {
                            holder.min++;
                            holder.sec=0;
                        }
                        else
                        {
                            holder.sec++;
                        }

                        holder.time.setText(holder.min+":"+ holder.sec);
                        holder.mHandler.postDelayed(this,1000);
                    }
                    };
                holder.mHandler.postDelayed(runnable[0], 1000);



                }
                else
                {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("id"+model.getId() , "completed");
                    editor.commit();
                    holder.mHandler.removeCallbacks(runnable[0]);

                    holder.checkBox.setChecked(true);
                    holder.checkBox.setClickable(false);

                    holder.switchButton.setChecked(false);
                    holder.switchButton.setClickable(false);

                    long end_time=System.currentTimeMillis();
                    sendEndTimerStatus(model.getId() , end_time/1000);
                }
            }
        });


    }
    void sendStarTimerStatus(final String task_id , final long start_time)
    {

        StringRequest request = new StringRequest(Request.Method.POST, "http://admiria.pk/office1/api_v1/Invoices/start_timer",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject job = new JSONObject(response);
                            JSONObject result = job.getJSONObject("result");
                            String res = result.getString("response");


                            //if (res.equals("Tasks Recieved")) {


                           // } else {

                                Toast.makeText(activity,res, Toast.LENGTH_SHORT).show();
                                // finish();
                           // }
                        } catch (JSONException e) {
                            Log.e("ErrorMessage", e.getMessage());
                            e.printStackTrace();

                        }

                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.e("Volley_error" , error.getMessage() );
                        parseVolleyError(error);
                        
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
                params.put("task_id" , task_id);
                params.put("start_time" , start_time+"");


                Log.e("params" , params.toString());

                return params;
            }
        };

        Volley.newRequestQueue(activity).add(request);

    }
    void sendEndTimerStatus(final String task_id , final long end_time)
    {

        StringRequest request = new StringRequest(Request.Method.POST, "http://admiria.pk/office1/api_v1/Invoices/stop_timer",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject job = new JSONObject(response);
                            JSONObject result = job.getJSONObject("result");
                            String res = result.getString("response");


                           // if (res.equals("Tasks Recieved")) {













                          //  } else {

                                Toast.makeText(activity,res, Toast.LENGTH_SHORT).show();
                                // finish();
                           // }
                        } catch (JSONException e) {
                            Log.e("ErrorMessage", e.getMessage());
                            e.printStackTrace();

                        }

                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.e("Volley_error" , error.getMessage() );
                        parseVolleyError(error);

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
                params.put("task_id" , task_id);
                params.put("end_time" , end_time+"");


                Log.e("params" , params.toString());

                return params;
            }
        };

        Volley.newRequestQueue(activity).add(request);

    }

    public void parseVolleyError(VolleyError error)
    {
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CountDownTimer timer;
        TextView position_text;
        TextView title_text;
        Switch switchButton;
        CheckBox checkBox;
        TextView desc;
        TextView time;
        long counter;
        long hour;
        long min;
        long sec;
        long NOTIFY_INTERVAL = 1 * 1000; // 1 seconds
        // run on another Thread to avoid crash
        private Handler mHandler;
        // timer handling
//        private Timer mTimer;


        public MyViewHolder(View itemView) {
            super(itemView);

            position_text=itemView.findViewById(R.id.position_text);
            title_text=itemView.findViewById(R.id.title_text);
            switchButton= itemView.findViewById(R.id.switch_button);
            checkBox=itemView.findViewById(R.id.checkbox_completed);
            time=itemView.findViewById(R.id.time_text);
            desc=itemView.findViewById(R.id.desc);
            counter=0;
            mHandler = new Handler();
//            mTimer = new Timer();
        }

    }

}
