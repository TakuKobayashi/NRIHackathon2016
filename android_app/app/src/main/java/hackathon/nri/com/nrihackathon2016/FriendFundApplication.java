package hackathon.nri.com.nrihackathon2016;

import android.app.Application;

public class FriendFundApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		SocketIOStreamer.getInstance(SocketIOStreamer.class).init(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
