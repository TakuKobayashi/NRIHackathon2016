package hackathon.nri.com.nrihackathon2016;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class ApplicationHelper {

	//ImageViewを使用したときのメモリリーク対策
	public static void releaseImageView(ImageView imageView){
		if (imageView != null) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable)(imageView.getDrawable());
			if (bitmapDrawable != null) {
				bitmapDrawable.setCallback(null);
			}
			imageView.setImageBitmap(null);
		}
	}

	//WebViewを使用したときのメモリリーク対策
	public static void releaseWebView(WebView webview){
		webview.stopLoading();
		webview.setWebChromeClient(null);
		webview.setWebViewClient(null);
		webview.destroy();
		webview = null;
	}

	public static String makeUrlParams(Bundle params){
		Set<String> keys = params.keySet();
		ArrayList<String> paramList = new ArrayList<String>();
		for (String key : keys) {
			paramList.add(key + "=" + params.get(key).toString());
		}
		return ApplicationHelper.join(paramList, "&");
	}

	public static String makeUrlParams(Map<String, Object> params){
		Set<String> keys = params.keySet();
		ArrayList<String> paramList = new ArrayList<String>();
		for(Map.Entry<String, Object> e : params.entrySet()) {
			paramList.add(e.getKey() + "=" + e.getValue().toString());
		}
		return ApplicationHelper.join(paramList, "&");
	}

	public static String join(String[] list, String with) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.length; i++) {
			if (i != 0) { buf.append(with);}
			buf.append(list[i]);
		}
		return buf.toString();
	}

	public static String join(ArrayList<String> list, String with) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) { buf.append(with);}
			buf.append(list.get(i));
		}
		return buf.toString();
	}

	public static ArrayList<String> getSettingPermissions(Context context){
		ArrayList<String> list = new ArrayList<String>();
		PackageInfo packageInfo = null;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if(packageInfo == null || packageInfo.requestedPermissions == null) return list;

		for(String permission : packageInfo.requestedPermissions){
			list.add(permission);
		}
		return list;
	}

	public static boolean hasSelfPermission(Context context, String permission) {
		if(Build.VERSION.SDK_INT < 23) return true;
		return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
	}

	public static void requestPermissions(Activity activity, int requestCode){
		if(Build.VERSION.SDK_INT >= 23) {
			ArrayList<String> permissions = ApplicationHelper.getSettingPermissions(activity);
			boolean isRequestPermission = false;
			for(String permission : permissions){
				if(!ApplicationHelper.hasSelfPermission(activity, permission)){
					isRequestPermission = true;
					break;
				}
			}
			if(isRequestPermission) {
				activity.requestPermissions(permissions.toArray(new String[0]), requestCode);
			}
		}
	}

	public static String loadTextFromAsset(Context con,String fileName) {

		AssetManager mngr = con.getAssets();
		//rawフォルダにあるファイルのリソースでの読み込み
		String str;
		try {
			InputStream is = mngr.open(fileName);
			str = ApplicationHelper.Is2String(is);
		} catch (IOException e) {
			str = "";
		}
		return str;
	}

	public static String Is2String(InputStream in) throws IOException {
		//入力されたテキストデータ(InputStream,これはbyteデータ)を文字列(String)に変換

		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		//保持しているStringデータ全てをStringBufferに入れる
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	public static void logBundleData(Bundle data) {
		Set<String> keys = data.keySet();
		for (String key : keys) {
			Object o = data.get(key);
			Log.d(Config.TAG, "key:" + key + " class:" + o.getClass().getName());
		}
	}

	public static String toHex(byte[] bytes) {
		StringBuilder sbuf = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = "0" + Integer.toString((int) bytes[i] & 0x0ff, 16);
			if (hex.length() > 2)
				hex = hex.substring(1, 3);
			sbuf.append(" " + i + ":" + hex);
		}
		return sbuf.toString();
	}

	public static void showToast(Context con, String message) {
		Toast toast = Toast.makeText(con, message, Toast.LENGTH_LONG);
		toast.show();
	}

	public static String encodeToJPEGbase64(Bitmap image)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
		return imageEncoded;
	}
}