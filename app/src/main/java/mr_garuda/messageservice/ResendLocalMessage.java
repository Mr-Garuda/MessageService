package mr_garuda.messageservice;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by mr_garuda on 2017/5/19.
 */

public class ResendLocalMessage {
    public static void resendMessage(Context context){
        SharedPreferences sp = context.getSharedPreferences("MessageServicePrefer",Context.MODE_PRIVATE);
        Map<String,?> allContent = sp.getAll();
        for(Map.Entry<String, ?>  entry : allContent.entrySet()){
            System.out.println("Resend ======= "+entry.getKey());
            SendToServer STS = new SendToServer();
            STS.execute((String)entry.getValue());
        }
    }
}
