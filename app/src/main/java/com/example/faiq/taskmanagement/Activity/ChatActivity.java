package com.example.faiq.taskmanagement.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.faiq.taskmanagement.Models.MessageModel;
import com.example.faiq.taskmanagement.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    ArrayList<MessageModel> discussionList = new ArrayList<>();
    ImageView imageView;

    EditText etComment;
    TextView tvSendComment;
    ListView rvComments;

    DatabaseReference mRootRef;

    FirebaseListAdapter<MessageModel> adapter;
    int pos;

    ProgressDialog dialog;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activity=this;

        etComment=(EditText)findViewById(R.id.etComment);
        tvSendComment=(TextView)findViewById(R.id.tvSendComment);
        rvComments=(ListView)findViewById(R.id.rvComments);

        overridePendingTransition(0,0);


        dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait");
        dialog.setTitle("Loading Messages");
        dialog.show();

        mRootRef = FirebaseDatabase.getInstance().getReference().child("taskManagementMessages").child("1");


        imageView =(ImageView) findViewById(R.id.close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tvSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validComment()) {
                    //  loadMessages();
                    //get time
                    Date date = new Date();
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                    //get date
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String time = sdfTime.format(date);
                    String Date = dateFormat.format(date);
                    //   Toast.makeText(this, "Button Clicked!", Toast.LENGTH_SHORT).show();
                    String gettingText = etComment.getText().toString();
                    if (!TextUtils.isEmpty(gettingText)) {

                        MessageModel model = new MessageModel();
                        model.setMessage(gettingText);
                        model.setTime(time);
                        FirebaseDatabase.getInstance().getReference().child("1").push().setValue(model);

                    }
                    etComment.setText("");


                }
            }
        });
        loadMessages();

    }

    private boolean validComment() {
        boolean check = false;
        if (etComment.getText().toString().equals("")) {
            check = false;
        } else {
            check = true;
        }
        return check;
    }


    private void loadMessages() {
        rvComments = (ListView) findViewById(R.id.rvComments);
        adapter = new FirebaseListAdapter<MessageModel>(activity, MessageModel.class, R.layout.discussion_rv_item,
                FirebaseDatabase.getInstance().getReference().child("1")) {
            @Override
            protected void populateView(View v, MessageModel model, int position) {
                TextView tvSender, tvDescription, tvDate, tvTime, tvComment;
                ImageView image;
                tvSender = v.findViewById(R.id.tvSenderName);
                tvDescription = v.findViewById(R.id.tvDescription);
                tvDate = v.findViewById(R.id.tvDate);
                tvTime = v.findViewById(R.id.tvTime);
                tvComment = v.findViewById(R.id.tvComment);
                image = v.findViewById(R.id.ivSenderImage);

                tvSender.setText("faiq");
//                tvDescription.setText(model.getPost());
//                tvDate.setText(model.getDate());
                tvTime.setText(model.getTime());
                tvComment.setText(model.getMessage());
                pos = position;
                try{

                    image.setImageResource(R.drawable.ic_profile_pic);
//                    Glide.with(act).load(model.getImageurl()).into(image);
                }catch(Exception e)
                {
                    image.setImageResource(R.drawable.ic_profile_pic);
                }

            }
        };

        rvComments.setAdapter(adapter);


        dialog.dismiss();

    }


}
