package mr_garuda.clienta_volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mr_garuda on 2017/5/21.
 */

public class PostMessageTask implements Runnable {
    private static int index = 0;
    @Override
    public void run() {
        //String url = "http://192.168.56.1:8888";
        String url = "http://172.26.26.138:8888";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // TODO: 处理返回结果
                Log.i("### onResponse", "POST_StringRequest:" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: 处理错误
                Log.e("### onErrorResponse", "POST_StringRequest:" + error.toString());
            }
        }) {
            /**
             * 返回含有POST或PUT请求的参数的Map
             */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> paramMap = new HashMap<>();
                // TODO: 处理POST参数
                String content = "";
                for(int i=0;i<10;i++) {
                    content = content + "Client B say hello to ("+i+")";
                }
                paramMap.put("ClientId", "Client A(Volley)");
                paramMap.put("Content",content);
                paramMap.put("MessageId",index+"");
                index++;

                return paramMap;
            }
        };

        //stringRequest.setTag(StringRequest_POST);//StringRequest_POST

        MainActivity.getRequestQueue().add(stringRequest);
    }
}
