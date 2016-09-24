package hackathon.nri.com.nrihackathon2016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApplicationHelper.requestPermissions(this, REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SocketIOStreamer.getInstance(SocketIOStreamer.class).connect(Config.ROOT_URL);
        SocketIOStreamer.getInstance(SocketIOStreamer.class).setOnReceiveCallback(new SocketIOStreamer.SocketIOEventCallback() {
            @Override
            public void onCall(String receive) {
                Log.d(Config.TAG, "socketRecieve: " + receive);
            }

            @Override
            public void onEmit(HashMap<String, Object> emitted) {
                Log.d(Config.TAG, "socketEmit: " + emitted);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SocketIOStreamer.getInstance(SocketIOStreamer.class).disConnect();
    }
}
