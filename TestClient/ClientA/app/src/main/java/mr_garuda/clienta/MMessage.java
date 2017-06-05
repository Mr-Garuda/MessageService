package mr_garuda.clienta;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mr_garuda on 2017/5/20.
 */

public class MMessage {
    public static int index = 0;
    //public boolean isSaved;
    private String MMessageId;
    private String clintId;
    private Destination dest;

    //TODO change type to Object from String
    private String content;

    MMessage(String clint,Destination dest){
        //isSaved = false;
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
        Date date = new Date(currentTime);
        MMessageId = formatter.format(date)+index+"A";
        index++;
        clintId = clint;
        this.dest = new Destination(dest.getHost(),dest.getPort());
    }

    public boolean setContent(String content) {
        this.content = content;
        return true;
    }

    public String getMMessageId(){
        return MMessageId;
    }

    public Destination getDest(){
        return dest;
    }

    public String getClintId() {
        return clintId;
    }

    public String getContent() {
        return content;
    }
    @Override
    public String toString() {
        return "MMessage "+MMessageId+" [From: "+clintId+"  Destination: "+dest.toString()
                +"  Content: "+content;
    }
}
