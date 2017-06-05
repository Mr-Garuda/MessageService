package mr_garuda.messageservice;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by mr_garuda on 2017/5/19.
 */

public class MessageQueue {
    private Queue<MMessage> queue;
    private Destination dest;
    private long lastPostTime;

    MessageQueue(Destination destination){
        queue = new LinkedList<MMessage>();
        this.dest = destination;
        lastPostTime = System.currentTimeMillis();
    }

    public boolean offer(MMessage mMessage){
        return queue.offer(mMessage);
    }

    public MMessage poll(){
        return queue.poll();
    }

    public MMessage peek(){
        return queue.peek();
    }

    public void setDest(Destination dest){
        this.dest = new Destination(dest.getHost(),dest.getPort());
    }

    public Destination getDest(){
        return dest;
    }

    public Queue<MMessage> getQueue() {
        return queue;
    }

    public int getSize(){
        return queue.size();
    }

    public void updateLastPostTime() {
        lastPostTime = System.currentTimeMillis();
    }

    public long getLastPostTime() {
        return lastPostTime;
    }

}
