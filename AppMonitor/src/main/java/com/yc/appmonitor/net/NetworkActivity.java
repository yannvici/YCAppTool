package com.yc.appmonitor.net;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.appmonitor.R;
import com.yc.netlib.ui.NetRequestActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : MVC中的Controller层
 *     revise:
 * </pre>
 */
public class NetworkActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView mResponseTextView;

    public static void start(Context context){
        Intent intent = new Intent(context, NetworkActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        mResponseTextView = findViewById(R.id.response_textView);
        findViewById(R.id.test_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest2();
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest3();
            }
        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest3();
            }
        });
        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetRequestActivity.start(NetworkActivity.this);
            }
        });
    }

    private void sendRequest() {
        mResponseTextView.setText("loading...");
        new Thread(){
            @Override
            public void run() {
                super.run();
                String testUrl = "https://www.wanandroid.com/article/query/0/json";
                Map<String, Object> mapParams = new HashMap<>();
                mapParams.put("k","Android");
                OkHttpManager.getInstance().post(testUrl,mapParams, new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        Log.e(TAG, "e = "+e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mResponseTextView.setText(e.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final String body;
                                try {
                                    body = response.body().string();
                                    Log.d(TAG, body);
                                    mResponseTextView.setText(body);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    mResponseTextView.setText(e.getMessage());
                                }
                            }
                        });
                    }
                });
            }
        }.start();
    }


    private void sendRequest2() {
        mResponseTextView.setText("loading...");
        String testUrl = "https://www.wanandroid.com/banner/json";
        NetworkModel networkModel = new NetworkModel();
        networkModel.getData(testUrl,new NetworkModel.DataListener<String>() {
            @Override
            public void onSuccess(String body) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mResponseTextView.setText(body);
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mResponseTextView.setText(error);
                    }
                });
            }
        });
    }


    private void sendRequest3() {
        mResponseTextView.setText("loading...");
        String testUrl = "https://www.wanandroid.com/friend/json";
        NetworkModel networkModel = new NetworkModel();
        networkModel.getData(testUrl,new NetworkModel.DataListener<String>() {
            @Override
            public void onSuccess(String body) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mResponseTextView.setText(body);
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mResponseTextView.setText(error);
                    }
                });
            }
        });
    }

}
