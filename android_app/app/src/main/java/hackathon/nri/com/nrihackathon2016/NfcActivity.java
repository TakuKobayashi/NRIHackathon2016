package hackathon.nri.com.nrihackathon2016;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class NfcActivity extends AppCompatActivity {
    private static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        ApplicationHelper.requestPermissions(this, REQUEST_CODE);

        Intent intent = getIntent();
        String action = intent.getAction();
        Log.d(Config.TAG, "Discovered tag " + action + " with intent: " + intent);
    }
    @Override
    public void onNewIntent(Intent intent) {
        Log.d(Config.TAG, "new Intent " + intent.getAction() + " with intent: " + intent);
    }
}
