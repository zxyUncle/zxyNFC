package com.zxy.zxynfc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * Created by zsf on 2021/8/10 10:43
 * ******************************************
 * * NFC 刷卡
 * ******************************************
 */
public class MainActivity extends BaseActivity {
    private TextView tvNFC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvNFC = findViewById(R.id.tvNFC);
        Button tvIntent = findViewById(R.id.tvIntent);
        tvIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,FirstActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onScanNFC(NFCMessageBean nfcMessageBean) {
        super.onScanNFC(nfcMessageBean);
        tvNFC.setText(nfcMessageBean.toString());
    }
}