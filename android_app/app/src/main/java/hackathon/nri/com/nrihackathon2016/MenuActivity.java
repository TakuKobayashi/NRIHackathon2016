package hackathon.nri.com.nrihackathon2016;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.Profile;

import java.util.HashMap;

public class MenuActivity extends Activity {
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuview);

        ImageView menubg = (ImageView) findViewById(R.id.menubg);
        menubg.setImageResource(R.mipmap.menu_bg);

        ImageView close_button = (ImageView) findViewById(R.id.close_button);
        close_button.setImageResource(R.mipmap.close_btn);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView help_button = (ImageView) findViewById(R.id.help_button);
        help_button.setImageResource(R.mipmap.help_btn);
        help_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(MenuActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage(getString(R.string.networking_request_dialog_message));
                progressDialog.setCancelable(true);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                progressDialog.show();
                sendApproveRequest();
            }
        });
    }

    private void sendApproveRequest(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.ROOT_URL + "rest/approve",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Config.TAG, "res:" + response);
                        progressDialog.dismiss();
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(Config.TAG, "error:" + error.getMessage());
                progressDialog.dismiss();
            }
        });
        queue.add(stringRequest);
    }
}
