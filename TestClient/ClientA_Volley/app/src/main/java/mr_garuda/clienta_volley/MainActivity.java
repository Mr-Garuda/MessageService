package mr_garuda.clienta_volley;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Button mBtnConnect;
    private Button mBtnDisconnect;
    public static RequestQueue mRequestQueue;

    public static RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        mBtnConnect = (Button)findViewById(R.id.btn_connect);
        mBtnConnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PostMessageTask postMessageTask = new PostMessageTask();
                if(scheduler.isShutdown())
                    scheduler = Executors.newSingleThreadScheduledExecutor();
                scheduler.scheduleAtFixedRate(postMessageTask,2000,1000, TimeUnit.MILLISECONDS);
            }
        });

        mBtnDisconnect = (Button)findViewById(R.id.btn_disconnect);
        mBtnDisconnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                scheduler.shutdown();
            }
        });
    }
}
