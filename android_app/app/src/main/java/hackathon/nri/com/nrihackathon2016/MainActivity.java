package hackathon.nri.com.nrihackathon2016;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static int REQUEST_CODE = 1;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApplicationHelper.requestPermissions(this, REQUEST_CODE);

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        // Other app specific specialization

        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(Config.TAG, "success: " + loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                Log.d(Config.TAG, "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(Config.TAG, "error:" + exception.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d(Config.TAG, "result:");
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
