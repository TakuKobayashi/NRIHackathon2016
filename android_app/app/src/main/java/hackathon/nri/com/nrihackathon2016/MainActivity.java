package hackathon.nri.com.nrihackathon2016;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends Activity {
    private static int REQUEST_CODE = 1;
    private static final int START_SCREEN_DISPLAY_TIME = 1000; // Millisecond
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApplicationHelper.requestPermissions(this, REQUEST_CODE);

        ImageView bg = (ImageView) findViewById(R.id.sprash_background);
        bg.setImageResource(R.mipmap.sprash_bg);

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        // Other app specific specialization
        if(isLoggedIn()){
            loginButton.setVisibility(View.INVISIBLE);
            scesuleNextActivity();
        }else{
            callbackManager = CallbackManager.Factory.create();
            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    Log.d(Config.TAG, "success: " + loginResult.getAccessToken());
                    gotoNextActivity();
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

    private void gotoNextActivity(){
        //次のactivityを実行
        Intent intent = new Intent(MainActivity.this, TopActivity.class);
        startActivity(intent);
        finish();
    }

    private void scesuleNextActivity(){
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                gotoNextActivity();
                return true;
            }
        });
        handler.sendEmptyMessageDelayed(0, START_SCREEN_DISPLAY_TIME);
    }

    private boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApplicationHelper.releaseImageView((ImageView) findViewById(R.id.sprash_background));
    }
}
