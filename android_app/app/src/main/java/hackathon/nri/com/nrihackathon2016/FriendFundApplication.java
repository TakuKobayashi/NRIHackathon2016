package hackathon.nri.com.nrihackathon2016;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class FriendFundApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		SocketIOStreamer.getInstance(SocketIOStreamer.class).init(this);
		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
