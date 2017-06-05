package mr_garuda.clienta;

/**
 * Created by mr_garuda on 2017/5/20.
 */

public class Destination {
    private String host;
    private int port;

    public Destination(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost(){
        return host;
    }

    public int getPort(){
        return port;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Destination dest = (Destination)obj;
        if(host.equals(dest.getHost()) && port==dest.getPort())
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + host.hashCode();
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return host +" "+port;
    }
}
