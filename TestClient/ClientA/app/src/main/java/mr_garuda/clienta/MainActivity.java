package mr_garuda.clienta;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button mBtnConnect;
    private Button mBtnDisconnect;
    private boolean isBound;
    private Messenger mService = null;
    private Context context = this;

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mService = new Messenger(service);
            Message msg = Message.obtain(null,1);
            msg.replyTo = mMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            isBound = false;
        }
    };

    public static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnConnect = (Button) findViewById(R.id.btn_connect);

        mBtnConnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("mr_garuda.messageservice","mr_garuda.messageservice.MQService"));
                boolean success = bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
                System.out.println("======================="+success);
                PostMessageTask postMessageTask = new PostMessageTask();
                postMessageTask.bindMessenger(mService);
                if(scheduler.isShutdown())
                    scheduler = Executors.newSingleThreadScheduledExecutor();
                scheduler.scheduleAtFixedRate(postMessageTask,3000,300, TimeUnit.MILLISECONDS);
            }
        });

        mBtnDisconnect = (Button) findViewById(R.id.btn_disconnect);
        mBtnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = Message.obtain(null,2);
                msg.replyTo = mMessenger;
                try {
                    mService.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                unbindService(mConnection);
                scheduler.shutdown();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Message msg = Message.obtain(null,2);
        msg.replyTo = mMessenger;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unbindService(mConnection);
    }
}
