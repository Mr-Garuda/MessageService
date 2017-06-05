package mr_garuda.messageservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Queue;

/**
 * Created by mr_garuda on 2017/5/19.
 */

public class JsonTools {
    public static String getJsonString(String key, Object value) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static JSONObject getJsonObject(String key,Object value) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = null;
        if(value.getClass() == MessageQueue.class)
        {
            Queue<MMessage> queue = ((MessageQueue)value).getQueue();
            jsonArray = new JSONArray();
            while(queue.peek() != null)
            {
                MMessage mMessage = queue.poll();
                JSONObject json = new JSONObject();
                try {
                    json.put("Index",mMessage.getMMessageId());
                    json.put("From Client",mMessage.getClintId());
                    json.put("Cotent",mMessage.getContent());
                    json.put("Dest",mMessage.getDest());
                    jsonArray.put(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            jsonObject.put("element",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}