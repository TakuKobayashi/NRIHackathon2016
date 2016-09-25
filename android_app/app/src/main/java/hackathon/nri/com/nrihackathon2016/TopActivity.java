package hackathon.nri.com.nrihackathon2016;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.Profile;

import java.util.HashMap;

public class TopActivity extends Activity {
    private Handler handler;
    private TextView kikin_value_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topview);

        handler = new Handler();

        ImageView topbg = (ImageView) findViewById(R.id.topbg);
        topbg.setImageResource(R.mipmap.top_bg);

        ImageView headbg = (ImageView) findViewById(R.id.headbg);
        headbg.setImageResource(R.mipmap.school_bg);

        ImageView header_bg = (ImageView) findViewById(R.id.header_bg);
        header_bg.setImageResource(R.mipmap.top_header);

        ImageView header_friendfund = (ImageView) findViewById(R.id.header_friendfund);
        header_friendfund.setImageResource(R.mipmap.top_friendfund);

        ImageView news_btn = (ImageView) findViewById(R.id.news_btn);
        news_btn.setImageResource(R.mipmap.news_btn);

        ImageView kikin = (ImageView) findViewById(R.id.kikin);
        kikin.setImageResource(R.mipmap.kikin_text);

        kikin_value_text = (TextView) findViewById(R.id.kikin_value_text);

        ImageView top_menu_button = (ImageView) findViewById(R.id.header_menu);
        top_menu_button.setImageResource(R.mipmap.top_menu_button);
        top_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        ImageView fund_description = (ImageView) findViewById(R.id.fund_description);
        fund_description.setImageResource(R.mipmap.fund_description);

        ImageView card_image = (ImageView) findViewById(R.id.card_image);
        card_image.setImageResource(R.mipmap.top_card);

        ImageView friends_list = (ImageView) findViewById(R.id.friends_list);
        friends_list.setImageResource(R.mipmap.friend_sample);

        ImageView social_image = (ImageView) findViewById(R.id.social_image);
        social_image.setImageResource(R.mipmap.social_sample);

        TextView ramain_text = (TextView) findViewById(R.id.remain_text);
        ramain_text.setText(getString(R.string.yen_value_text, "2000"));
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
    protected void onStart() {
        super.onStart();
        SocketIOStreamer.getInstance(SocketIOStreamer.class).connect(Config.ROOT_URL);
        SocketIOStreamer.getInstance(SocketIOStreamer.class).setOnReceiveCallback(new SocketIOStreamer.SocketIOEventCallback() {
            private String mKey;
            private String mRecieve;

            @Override
            public void onCall(String key, String receive) {
                Log.d(Config.TAG, "key: " + key + " socketRecieve: " + receive);
                mKey = key;
                mRecieve = receive;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mKey.equals(SocketIOStreamer.SUMMARY_KEY)){
                            kikin_value_text.setText(getString(R.string.yen_value_text, mRecieve));
                        }else if(mKey.equals(SocketIOStreamer.APPROVE_KEY)){
                            if(!isFinishing()){
                                Intent intent = new Intent(TopActivity.this, NewsActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }

            @Override
            public void onEmit(HashMap<String, Object> emitted) {
                Log.d(Config.TAG, "socketEmit: " + emitted);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SocketIOStreamer.getInstance(SocketIOStreamer.class).disConnect();
    }
}
