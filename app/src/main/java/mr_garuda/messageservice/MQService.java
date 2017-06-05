package mr_garuda.messageservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mr_garuda on 2017/5/18.
 */

public class MQService extends Service{
    private ArrayList<Messenger> mClients = new ArrayList<Messenger>();
    int mValue = 0;
    private Context context = this;

    static final int MSG_REGISTER_CLIENT = 1;
    static final int MSG_UNREGISTER_CLIENT = 2;
    static final int MSG_POST = 3;

    private static int MQBUFFERSIZE = 40;
    private static Map<Destination,MessageQueue> MQMap = new HashMap<Destination,MessageQueue>();

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    Toast.makeText(context, R.string.sign, Toast.LENGTH_SHORT).show();
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_POST:
                    Bundle bundle = msg.getData();
                    MMessage mm = new MMessage(bundle.getString("ClientId"),new Destination(bundle.getString("Host"),bundle.getInt("Port")));
                    mm.setContent(bundle.getString("Content"));
                    mm.setMMessageId(bundle.getString("MMessageId"));
                    postMessage(mm);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public void onCreate() {
        //mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        super.onCreate();
        Toast.makeText(this, R.string.remote_service_started, Toast.LENGTH_SHORT).show();
        final Context context = this;
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                ResendLocalMessage.resendMessage(context);
            }
        },0,15000);

    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    public void postMessage(MMessage mMessage) {
        Context context = this;
        if(isDestExist(mMessage.getDest())) {
            MessageQueue messageQueue = MQMap.get(mMessage.getDest());
            messageQueue.offer(mMessage);
            if(messageQueue.getSize() >= MQBUFFERSIZE) {

                String Index = messageQueue.peek().getMMessageId();
                JSONObject jsonObject = JsonTools.getJsonObject("MessageQueue",messageQueue);
                SharedPreferences sp = context.getSharedPreferences("MessageServicePrefer",Context.MODE_APPEND);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Index,jsonObject.toString()).commit();
                SendToServer STS = new SendToServer();
                STS.execute(jsonObject.toString());
            }
            messageQueue.updateLastPostTime();
        }
        else {
            createMQ(mMessage);
        }
    }

    public boolean isDestExist(Destination destination){
        return MQMap.containsKey(destination);
    }

    public void createMQ(final MMessage mMessage){
        final MessageQueue messageQueue = new MessageQueue(mMessage.getDest());
        messageQueue.offer(mMessage);
        final Context context = this;
        MQMap.put(mMessage.getDest(),messageQueue);
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                if(messageQueue.getSize()>0 && (System.currentTimeMillis()-messageQueue.getLastPostTime()>=15000)) {
                    String Index = messageQueue.peek().getMMessageId();
                    JSONObject jsonObject = JsonTools.getJsonObject("MessageQueue",messageQueue);
                    SharedPreferences sp = context.getSharedPreferences("MessageServicePrefer",Context.MODE_APPEND);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Index,jsonObject.toString()).commit();
                    SendToServer STS = new SendToServer();
                    STS.execute(jsonObject.toString());
                    messageQueue.updateLastPostTime();
                }
            }
        },0,10000);
    }

}
