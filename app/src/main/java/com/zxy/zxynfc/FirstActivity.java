package com.zxy.zxynfc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class FirstActivity extends BaseActivity {
    private TextView tvNFC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        tvNFC = findViewById(R.id.tvNFC);
    }


    @Override
    protected void onScanNFC(NFCMessageBean nfcMessageBean) {
        super.onScanNFC(nfcMessageBean);
        tvNFC.setText(nfcMessageBean.toString());
    }
}