package com.zxy.zxynfcc;

import android.os.Bundle;
import android.widget.TextView;

import com.zxy.zxynfc.NFCMessageBean;

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