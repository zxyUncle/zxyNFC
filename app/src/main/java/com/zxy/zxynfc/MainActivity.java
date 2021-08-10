package com.zxy.zxynfc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
/**
 * Created by zsf on 2021/8/10 10:43
 * ******************************************
 * * NFC 刷卡
 * ******************************************
 */
public class MainActivity extends AppCompatActivity {
    private TextView tvNFC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvNFC = findViewById(R.id.tvNFC);
        setNFCIntent(getIntent());
    }

    /**
     * 设置NFC
     * @param intent
     */
    private void setNFCIntent(Intent intent) {
        NFCFactory.getInstance().setIntent(intent, this, nfcMessageBean -> {
            Log.e("zxy", nfcMessageBean.toString());
            tvNFC.setText(nfcMessageBean.toString());
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        NFCFactory.getInstance().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NFCFactory.getInstance().onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setNFCIntent(intent);
    }

}