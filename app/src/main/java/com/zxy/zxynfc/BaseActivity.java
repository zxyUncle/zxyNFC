package com.zxy.zxynfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNFCIntent(getIntent());
    }

    /**
     * 设置NFC
     *
     * @param intent
     */
    private void setNFCIntent(Intent intent) {
        NFCFactory.getInstance().setIntent(intent, this, nfcMessageBean -> {
            if (nfcMessageBean.getId_dec() != null)
                onScanNFC(nfcMessageBean);
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

    protected void onScanNFC(NFCMessageBean nfcMessageBean) {

    }
}
