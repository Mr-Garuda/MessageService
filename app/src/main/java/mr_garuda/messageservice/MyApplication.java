package mr_garuda.messageservice;

import android.app.Application;
import android.content.Context;

/**
 * Created by mr_garuda on 2017/5/19.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
