package mr_garuda.messageservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by mr_garuda on 2017/5/19.
 */

public class SendToServer extends AsyncTask<String,Integer,Void> {
    @Override
    protected Void doInBackground(String[] param) {
        //MessageQueue messageQueue = param[0];
        Socket socket = null;
        try {
            JSONObject jsonObject = new JSONObject(param[0]);
            String destStr = jsonObject.getJSONArray("element").getJSONObject(0).getString("Dest");
            String dest[] = destStr.split(" ");
            String host = dest[0];
            int port = Integer.valueOf(dest[1]);
            socket = new Socket(host,port);
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            //直接发送JSON原文
            //output.write(param[0].getBytes("UTF-8"));

            //发送JSON的byte格式
            output.writeUTF(param[0]);

            //获取返回的message ID
            DataInputStream input = new DataInputStream(socket.getInputStream());
            String messageIndex = input.readUTF();
            System.out.println("GET BACK====== "+ messageIndex);
            SharedPreferences sp = MyApplication.getContext().getSharedPreferences("MessageServicePrefer", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(messageIndex);
            editor.commit();
            output.close();
            input.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            if(socket != null) {
                try{
                    socket.close();
                }catch(IOException e) {
                    socket = null;
                    System.out.println("Finally Server Error ~~~" + e.getMessage());
                }
            }
        }
        return null;
    }
}
