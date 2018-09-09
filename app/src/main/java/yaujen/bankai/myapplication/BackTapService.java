package yaujen.bankai.myapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;

public class BackTapService {
    private Activity activity;
    private Clicker clicker;

    public Handler messageHandler;
    protected ServiceConnection mServerConn;

    public BackTapService(Activity activity, Clicker clicker) {
        this.activity = activity;
        this.clicker = clicker;
    }

    public void startService() {
        messageHandler = new MessageHandler(clicker);
        mServerConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                System.out.println("service connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                System.out.println("service disconnected");
            }
        };

        ComponentName componentName = new ComponentName("com.prhlt.aemus.BoDTapService",
                "com.prhlt.aemus.BoDTapService.BoDTapService");
        Intent intent = new Intent();
        intent.putExtra("MESSENGER", new Messenger(messageHandler));
        intent.setComponent(componentName);

        ComponentName c = activity.getApplication().startService(intent);
        if (c == null) {
            System.out.println("Failed to start BoDTapService");
        } else {
            System.out.println("BoDTap Service started");
        }
    }

    public void stopService() {
        ComponentName componentName = new ComponentName("com.prhlt.aemus.BoDTapService",
                "com.prhlt.aemus.BoDTapService.BoDTapService");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        activity.getApplication().stopService(intent);
    }
}
