package hackathon.nri.com.nrihackathon2016;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class NfcActivity extends Activity {
    private IntentFilter[] intentFiltersArray;
    private String[][] techListsArray;
    private NfcAdapter mAdapter;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        TextView nfcinfo = (TextView) findViewById(R.id.ncfinfo_text);
/*
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[] {ndef};

        // FelicaはNFC-TypeFなのでNfcFのみ指定でOK
        techListsArray = new String[][] {
                new String[] { NfcF.class.getName() }
        };

        // NfcAdapterを取得
        mAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
        */

        ArrayList<String> infoList = new ArrayList<String>();

        Intent intent = getIntent();
        String action = intent.getAction();
        infoList.add("intentAction:" + action);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            // NFCからID情報取得
            byte[] ids = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            infoList.add("IDbytes:" + ids.length);
            String tagId = ApplicationHelper.toHex(ids);
            Log.d(Config.TAG, "id: " + tagId.toString());
            infoList.add("nfcId:" + tagId.toString());

            // カードID取得。Activityはカード認識時起動に設定しているのでここで取れる。
            byte[] felicaIDm = new byte[]{0};
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            for(String t : tag.getTechList()){
                Log.d(Config.TAG, "t: " + tagId.toString());
            }

            NfcF nfc = NfcF.get(tag);
            FericaCollection result = new FericaCollection();
            Log.d(Config.TAG, "nfc: " + nfc);
            try {
                nfc.connect();
                byte[] req = Ferica.readWithoutEncryption(felicaIDm, 10);
                Log.d(Config.TAG, "req:" + ApplicationHelper.toHex(req));
                // カードにリクエスト送信
                byte[] res = nfc.transceive(req);
                Log.d(Config.TAG, "res:"+ApplicationHelper.toHex(res));
                nfc.close();
                // 結果を文字列に変換して表示
                result = FericaCollection.parse(res);
            } catch (Exception e) {
                Log.e(Config.TAG, e.getMessage() , e);
            }
            for(FericaRecord fr : result){
                infoList.add(fr.toString());
            }
            nfcinfo.setText(ApplicationHelper.join(infoList, "\n"));
        }
//        Bundle intentData = intent.getExtras();
//        ApplicationHelper.logBundleData(intentData);
        sendChargeRequest();
    }

    private void sendChargeRequest(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.ROOT_URL + "rest/pay?charge=1",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(Config.TAG, "res:" + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(Config.TAG, "error:" + error.getMessage());
                }
            }
        );
        queue.add(stringRequest);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d(Config.TAG, "new Intent " + intent.getAction() + " with intent: " + intent);
    }
}
