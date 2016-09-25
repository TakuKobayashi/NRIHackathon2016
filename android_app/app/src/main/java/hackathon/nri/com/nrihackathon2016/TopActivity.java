package hackathon.nri.com.nrihackathon2016;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.Profile;

import java.util.HashMap;

public class TopActivity extends Activity {
    private static int REQUEST_CODE = 1;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topview);

        ImageView topbg = (ImageView) findViewById(R.id.topbg);
        topbg.setImageResource(R.mipmap.top_bg);

        ImageView headbg = (ImageView) findViewById(R.id.headbg);
        headbg.setImageResource(R.mipmap.school_bg);

        ImageView header_bg = (ImageView) findViewById(R.id.header_bg);
        header_bg.setImageResource(R.mipmap.top_header);

        ImageView top_menu_button = (ImageView) findViewById(R.id.header_menu);
        top_menu_button.setImageResource(R.mipmap.top_menu_button);
        top_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getProfile(){
        Profile profile = Profile.getCurrentProfile();
        Log.d(Config.TAG, "id:" + profile.getId());
        Log.d(Config.TAG, "fn:" + profile.getFirstName());
        Log.d(Config.TAG, "mn:" + profile.getMiddleName());
        Log.d(Config.TAG, "ln:" + profile.getLastName());
        Log.d(Config.TAG, "name:" + profile.getName());
        Log.d(Config.TAG, "link:" + profile.getLinkUri());
        Log.d(Config.TAG, "picture:" + profile.getProfilePictureUri(298,298));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SocketIOStreamer.getInstance(SocketIOStreamer.class).connect(Config.ROOT_URL);
        SocketIOStreamer.getInstance(SocketIOStreamer.class).setOnReceiveCallback(new SocketIOStreamer.SocketIOEventCallback() {
            @Override
            public void onCall(String key, String receive) {
                Log.d(Config.TAG, "key: " + key + " socketRecieve: " + receive);
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
