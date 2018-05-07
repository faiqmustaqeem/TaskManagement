package com.example.faiq.taskmanagement.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.faiq.taskmanagement.R;
import com.example.faiq.taskmanagement.RealtimeChat.models.User;
import com.example.faiq.taskmanagement.RealtimeChat.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText etEmail , etPassword;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity=this;

        etEmail=(EditText)findViewById(R.id.etEmail);
        etPassword=(EditText)findViewById(R.id.etPassowrd);

        btn_login=(Button)findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields())
                {
                    login();
                }
            }
        });


    }
    void login()
    {
        final ProgressDialog dialog=new ProgressDialog(activity);
        dialog.setTitle("Loading");
        dialog.setMessage("Wait...");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, "http://admiria.pk/office1/api_v1/Invoices/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("login" , response);

                            JSONObject job = new JSONObject(response);
                            JSONObject result = job.getJSONObject("result");
                            String res = result.getString("response");


                            if (res.equals("Login Successfull")) {
                                dialog.dismiss();

                                String id=result.getString("id");

                                Log.e("success",res);

                                User user=new User(id , etEmail.getText().toString());

                                addUserToFirebase(user);
//
//                                Intent i = new Intent(activity, MainActivity.class);
//                                startActivity(i);
//                                finish();


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


                params.put("email", etEmail.getText().toString());
                params.put("password", etPassword.getText().toString());
                params.put("role_id", "4");


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
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            } catch (JSONException | UnsupportedEncodingException e) {
                printMsg("internet problem...");
            }
        }
        else
        {
            printMsg("Your application is not connected to internet...");
        }
    }

    public boolean checkFields()
    {
        if(!isValidEmail(etEmail.getText().toString()))
        {
            printMsg("Please enter valid Email");
            return false;
        }
        if(etPassword.getText().toString().equals(""))
        {
            printMsg("Please enter valid password");
            return false;
        }
        return true;
    }
    public boolean isValidEmail(String emailStr) {
        final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
    void printMsg(String msg)
    {
        Toast.makeText(activity ,msg,Toast.LENGTH_SHORT).show();
    }


    public void addUserToFirebase(final User firebaseUser) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        User user = new User(firebaseUser.uid,
                firebaseUser.email);
        database.child(Constants.ARG_USERS)
                .child(firebaseUser.uid)
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = getSharedPreferences("SharedPreferences", MODE_PRIVATE).edit();

                            editor.putString("email", firebaseUser.email);
                            editor.putString("id", firebaseUser.uid);


                            editor.apply();

                            Intent i = new Intent(activity, MainActivity.class);
                            startActivity(i);
                            finish();

                            Log.e("add_user_firebase" , "success");

                        } else {
                            Toast.makeText(activity , "firebase error !" , Toast.LENGTH_SHORT).show();
                            Log.e("add_user_firebase" , "success");
                        }
                    }
                });
    }
}
