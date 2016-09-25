package hackathon.nri.com.nrihackathon2016;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends Activity {
    private static int REQUEST_CODE = 1;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApplicationHelper.requestPermissions(this, REQUEST_CODE);

        ImageView bg = (ImageView) findViewById(R.id.sprash_background);
        bg.setImageResource(R.mipmap.bg);

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
        getProfile();
    }

    private void getProfile(){
        Log.d(Config.TAG, "login:" + isLoggedIn());
        if(isLoggedIn()){
            Profile profile = Profile.getCurrentProfile();
            Log.d(Config.TAG, "id:" + profile.getId());
            Log.d(Config.TAG, "fn:" + profile.getFirstName());
            Log.d(Config.TAG, "mn:" + profile.getMiddleName());
            Log.d(Config.TAG, "ln:" + profile.getLastName());
            Log.d(Config.TAG, "name:" + profile.getName());
            Log.d(Config.TAG, "link:" + profile.getLinkUri());
            Log.d(Config.TAG, "picture:" + profile.getProfilePictureUri(200,300));
        }
    }

    private boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApplicationHelper.releaseImageView((ImageView) findViewById(R.id.sprash_background));
    }
}
