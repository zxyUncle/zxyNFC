package com.zxy.zxynfcc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.zxy.zxynfc.NFCFactory;
import com.zxy.zxynfc.NFCMessageBean;

public class BaseActivity extends Activity {
    NFCFactory factory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        factory = new NFCFactory();
        setNFCIntent(getIntent());
    }

    /**
     * 设置NFC
     *
     * @param intent
     */
    private void setNFCIntent(Intent intent) {
        factory.setIntent(intent, this, nfcMessageBean -> {
            if (nfcMessageBean.getId_dec() != null)
                onScanNFC(nfcMessageBean);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        factory.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        factory.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setNFCIntent(intent);
    }

    protected void onScanNFC(NFCMessageBean nfcMessageBean) {

    }
}
