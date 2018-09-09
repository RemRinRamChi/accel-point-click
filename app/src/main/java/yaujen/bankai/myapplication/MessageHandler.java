package yaujen.bankai.myapplication;

import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageHandler extends Handler {
    private Clicker clicker;

    public MessageHandler(Clicker clicker) {
        this.clicker = clicker;
    }

    @Override
    public void handleMessage(Message message) {
        JSONObject info = null;

        try {
            info = new JSONObject(message.getData().getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int tap = 0;
        try {
            tap = info.getInt("tap");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (tap) {
            case 0:
                // no tap
                break;
            case 1:
                System.out.println("BTAP SINGLE");
                clicker.click();
                break;
            case 2:
                System.out.println("BTAP DOUBLE");
                clicker.click();
                break;
            default:
                System.out.println("Not recognised");
                break;
        }
    }
}
