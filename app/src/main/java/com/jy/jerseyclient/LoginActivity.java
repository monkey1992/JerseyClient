package com.jy.jerseyclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jy.jerseyclient.entity.Response;
import com.jy.jerseyclient.entity.User;
import com.jy.jerseyclient.service.WebService;
import com.jy.jerseyclient.utils.JsonUtil;
import com.jy.jerseyclient.utils.NetworkUtil;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "LoginActivity";
    private EditText edt_username;
    private EditText edt_password;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initEvent();
    }

    private void initView() {
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_login);
    }

    private void initEvent() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = edt_username.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(LoginActivity.this,
                            "user name must not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String password = edt_password.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this,
                            "password must not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (NetworkUtil.isNetWorkOpened(LoginActivity.this)) {
                    new AsyncTask<String, Integer, Response>() {
                        @Override
                        protected Response doInBackground(String... params) {
                            Response response = WebService.login(username, password);
                            return response;
                        }

                        @Override
                        protected void onPostExecute(Response response) {
                            if (response == null) {
                                Toast.makeText(LoginActivity.this, "login failed,response is null",
                                        Toast.LENGTH_SHORT).show();
                            } else if (200 == response.getStatus()) {
                                Log.e(TAG, "user======" + response.toString());
                                Object obj = response.getResponse();
                                if (obj == null) {
                                    Toast.makeText(LoginActivity.this,
                                            "login failed,the response field is null",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    User user = JsonUtil.getEntity(obj.toString(), User.class);
                                    if (user == null) {
                                        Toast.makeText(LoginActivity.this, "login failed,illegal json",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "login succeed",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "login failed，" + response.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            super.onPostExecute(response);
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Toast.makeText(LoginActivity.this,
                            "network is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
