package edu.bluejack19_1.eassum.Model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import edu.bluejack19_1.tpa_mobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        String body = data.get("body");
        String title = data.get("title");

        NotificationHelper.displayNotification(getApplicationContext(),title, body);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("yb", "onNewToken: " + s);
        User.updateFCMToken(s);
    }
    private static JSONObject getMessage(String title, String body) {
        JSONObject object = new JSONObject();
        try {
            object.put("title", title);
            object.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static void sendMessage(final Context context, String target, String title, String body) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://fcm.googleapis.com/fcm/send";

        JSONObject request = new JSONObject();
        try {

            request.put("data", getMessage(title, body));
            request.put("to", target);

        } catch (Exception ignored) {  }

        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, request, null, null) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                header.put("Authorization", "key=" + context.getResources().getString(R.string.fcm_server_key));
                return header;
            }
        };
        queue.add(jsonObject);
    }

    public static void getToken(OnCompleteListener<InstanceIdResult> listener) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(listener);
    }
}
