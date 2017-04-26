package com.wzhnsc.testvolley;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private TextView tvReponse;

    class MyStringRequest extends StringRequest {
        public MyStringRequest(String                    url,
                               Response.Listener<String> listener,
                               Response.ErrorListener    errorListener) {
            this(Method.GET, url, listener, errorListener);
        }

        public MyStringRequest(int                       method,
                               String                    url,
                               Response.Listener<String> listener,
                               Response.ErrorListener    errorListener) {
            super(method, url, listener, errorListener);
        }

        // 如果不用 <html><head><meta charset="UTF-8"></head><body>包含中文的内容</head><body>
        // volley 会将 UTF-8 的中文重新编码
        // 所以要重载如下方法将编码改回来
        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            String str = null;

            try {
                str = new String(response.data, "utf-8");
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvReponse = (TextView) findViewById(R.id.textview);

        RequestQueue mQueue = Volley.newRequestQueue(this);
        MyStringRequest stringRequest = new MyStringRequest("http://192.168.23.1:8889",//"http://169.254.214.59:8889",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            byte[] baUnicode = "男".getBytes("Unicode");
                            Log.e("TestVolley", "Unicode（男）：" + baUnicode.length);
                            for (int i = 0; i < baUnicode.length; ++i) {
                                Log.e("TestVolley", String.format("Unicode（男）：%02X", baUnicode[i]));
                            }

                            byte[] baUTF8 = "男".getBytes("UTF-8");
                            Log.e("TestVolley", "UTF-8（男）：" + baUTF8.length);
                            for (int i = 0; i < baUTF8.length; ++i) {
                                Log.e("TestVolley", String.format("UTF-8（男）：%02X", baUTF8[i]));
                            }

                            byte[] baUTF16 = "男".getBytes("UTF-16");
                            Log.e("TestVolley", "UTF-16（男）：" + baUTF16.length);
                            for (int i = 0; i < baUTF16.length; ++i) {
                                Log.e("TestVolley", String.format("UTF-16（男）：%02X", baUTF16[i]));
                            }

                            byte[] baResponse = response.getBytes();
                            Log.e("TestVolley", "baResponse：" + baResponse.length);
                            for (int i = 0; i < baResponse.length; ++i) {
                                Log.e("TestVolley", String.format("baResponse[" + i + "]：%02X", baResponse[i]));
                            }

                            tvReponse.setText(new String(response.getBytes("UTF-8")));

                            Log.e("TestVolley", "onResponse - " + response);
                        }
                        catch (UnsupportedEncodingException e) {
                            Log.e("TestVolley", "translate utf-8 error" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TestVolley", "onErrorResponse" + error.getMessage(), error);
                    }
        });

        mQueue.add(stringRequest);
    }
}
