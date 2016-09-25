package hackathon.nri.com.nrihackathon2016;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketIOStreamer extends ContextSingletonBase<SocketIOStreamer> {
    private SocketIOEventCallback mCallback;
    private Socket mSocket;
    private Object[] values;
    private Handler mHandler;
    public static final String SUMMARY_KEY = "summary";
    public static final String APPROVE_KEY = "approve";

    public void init(Context context) {
        super.init(context);
        mHandler = new Handler();
    }

    public void connect(String url) {
        try {
            mSocket = IO.socket(url);
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... arg0) {
                    Log.d(Config.TAG, "connect!!");
                    for(Object o : arg0){
                        Log.d(Config.TAG, "connect:" + o.toString());
                    }
                }
            });
            mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... arg0) {
                    Log.d(Config.TAG, "error!!");
                    for(Object o : arg0){
                        Log.d(Config.TAG, "error:" + o.toString());
                    }
                }
            });
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
                @Override
                public void call(Object... arg0) {
                    Log.d(Config.TAG, "timeout!!");
                    for(Object o : arg0){
                        Log.d(Config.TAG, "timeout:" + o.toString());
                    }
                }
            });
            mSocket.on(SUMMARY_KEY, new Emitter.Listener() {
                @Override
                public void call(Object... arg0) {
                    for(Object o : arg0){
                        if(mCallback != null) {
                            mCallback.onCall(SUMMARY_KEY, o.toString());
                        }
                    }
                }
            });
            mSocket.on(APPROVE_KEY, new Emitter.Listener() {
                @Override
                public void call(Object... arg0) {
                    for(Object o : arg0){
                        if(mCallback != null) {
                            mCallback.onCall(APPROVE_KEY, o.toString());
                        }
                    }
                }
            });
            mSocket.on(SUMMARY_KEY, new Emitter.Listener() {
                @Override
                public void call(Object... arg0) {
                    for(Object o : arg0){
                        if(mCallback != null) {
                            mCallback.onCall(SUMMARY_KEY, o.toString());
                        }
                    }
                }
            });
            mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... arg0) {
                    Log.d(Config.TAG, "discomment!!");
                    for (Object o : arg0) {
                        Log.d(Config.TAG, "discomment:" + o.toString());
                    }
                }
            });
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void emit(HashMap<String, Object> params){
        for(Map.Entry<String, Object> keyValue : params.entrySet()) {
            mSocket.emit(keyValue.getKey(), keyValue.getValue());
        }
        if(mCallback != null) mCallback.onEmit(params);
    }

    public void disConnect() {
        mSocket.disconnect();
    }

    public void release() {
        mCallback = null;
        disConnect();
    }

    public void setOnReceiveCallback(SocketIOEventCallback callback) {
        mCallback = callback;
    }

    public void removeOnReceiveCallback() {
        mCallback = null;
    }

    public interface SocketIOEventCallback{
        public void onCall(String key, String receive);
        public void onEmit(HashMap<String, Object> emitted);
    }

    //デストラクタ
    @Override
    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }
}
