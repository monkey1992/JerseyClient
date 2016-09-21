package com.jy.jerseyclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.jerseyclient.entity.Response;
import com.jy.jerseyclient.entity.User;
import com.jy.jerseyclient.service.WebService;
import com.jy.jerseyclient.utils.JsonUtil;
import com.jy.jerseyclient.utils.NetworkUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private TextView tv_welcome;
    private Button btn_show_all_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
        initData();
    }

    private void initView() {
        tv_welcome = (TextView) findViewById(R.id.tv_welcome);
        btn_show_all_users = (Button) findViewById(R.id.btn_show_all_users);
    }

    private void initEvent() {
        btn_show_all_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.isNetWorkOpened(MainActivity.this)) {
                    new AsyncTask<String, Integer, Response>() {
                        @Override
                        protected Response doInBackground(String... params) {
                            Response response = WebService.getAllUsers();
                            return response;
                        }

                        @Override
                        protected void onPostExecute(Response response) {
                            if (response == null) {
                                Toast.makeText(MainActivity.this,
                                        "request failed,response is null",
                                        Toast.LENGTH_SHORT).show();
                            } else if (200 == response.getStatus()) {
                                Log.e(TAG, "user======" + response.toString());
                                Object obj = response.getResponse();
                                if (obj == null) {
                                    Toast.makeText(MainActivity.this,
                                            "request failed,the response field is null",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    ArrayList<User> users = (ArrayList<User>)
                                            JsonUtil.getEntityList(obj.toString(), User.class);
                                    if (users == null) {
                                        Toast.makeText(MainActivity.this,
                                                "request failed,illegal json",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        StringBuilder allUserInfo = new StringBuilder();
                                        for (User u : users) {
                                            allUserInfo.append(u.getName() + ":" + u.getSex() + ","
                                                    + u.getAge() + "\n");
                                        }
                                        tv_welcome.setText(allUserInfo);
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity.this,
                                        "request failed," + response.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            super.onPostExecute(response);
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Toast.makeText(MainActivity.this,
                            "network is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            User user = (User) intent.getExtras().getSerializable("user");
            if (user != null) {
                tv_welcome.setText("Welcome," + user.getName());
            }
        }
    }
}