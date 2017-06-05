package mr_garuda.clienta;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Created by mr_garuda on 2017/5/20.
 */

public class PostMessageTask implements Runnable{
    public static int messageIndex = 0;
    private Messenger mService = null;
    boolean isBound;

    public void bindMessenger(Messenger mService){
        this.mService = mService;
        isBound = true;
    }
    @Override
    public void run() {
        if (!isBound) {
            System.out.println("SERVICE NOT BOUND~~~");
            return;
        }
        MMessage mMessage = new MMessage("CLint_A", new Destination("192.168.56.1", 8888));
        mMessage.setContent("Clint A say hello to you");
        Message msg = Message.obtain(null, 3);//TO_Post = 1
        Bundle bundle = new Bundle();
        bundle.putString("ClientId",mMessage.getClintId());
        bundle.putString("Host",mMessage.getDest().getHost());
        bundle.putInt("Port",mMessage.getDest().getPort());
        bundle.putString("Content",mMessage.getContent());
        bundle.putString("MMessageId",mMessage.getMMessageId());
        msg.setData(bundle);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
