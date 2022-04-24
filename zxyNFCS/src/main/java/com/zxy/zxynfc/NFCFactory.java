package com.zxy.zxynfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NFCFactory {
    private NfcAdapter mNfcAdapter;
    private List<Tag> mTags = new ArrayList<>();
    private PendingIntent mPendingIntent;
    private Activity mContext;
    private NFCMessageBean nfcMessageBean = new NFCMessageBean();

    //zxy java单例模式模板，静态内部类
    private NFCFactory() {
    }

    public static NFCFactory getInstance() {
        return SingleInnerHolder.instance;
    }

    private static class SingleInnerHolder {
        private static NFCFactory instance = new NFCFactory();
    }

    private NfcResponseListener nfcResponseListener;

    //十六进制
    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
//            if (i > 0) {
//                sb.append(" ");
//            }
        }
        return sb.toString();
    }

    //十六进制反码
    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
//            if (i > 0) {
//                sb.append(" ");
//            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }
    //十进制
    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    //十六进制反码
    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xff;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }



    public void onPause() {
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(mContext);//关闭前台发布系统
        }
    }

    public void onResume() {
        if (mNfcAdapter != null) {
            //打开前台发布系统，使页面优于其它nfc处理.当检测到一个Tag标签就会执行mPendingItent
            mNfcAdapter.enableForegroundDispatch(mContext, mPendingIntent, null, null);
        }
    }

    public void setIntent(Intent intent, Activity mContext,NfcResponseListener nfcResponseListener) {
        nfcMessageBean.clear();
        this.nfcResponseListener = nfcResponseListener;
        this.mContext = mContext;
        if (mNfcAdapter == null) {
            Log.e("zxy", "初始化:mNfcAdapter");
            mNfcAdapter = NfcAdapter.getDefaultAdapter(mContext);//设备的NfcAdapter对象
            mPendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, mContext.getClass()), 0);//创建PendingIntent对象,当检测到一个Tag标签就会执行此Intent
        }
        if (mNfcAdapter == null) {//判断设备是否支持NFC功能
            Toast.makeText(mContext, "设备不支持NFC功能!", Toast.LENGTH_SHORT);
            return;
        }
        if (!mNfcAdapter.isEnabled()) {//判断设备NFC功能是否打开
            Toast.makeText(mContext, "请到系统设置中打开NFC功能!", Toast.LENGTH_SHORT);
            return;
        }

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                nfcMessageBean = dumpTagData(tag);
                mTags.add(tag);
            }
        }
        if (nfcResponseListener != null)
            nfcResponseListener.onResult(nfcMessageBean);
    }

    private NFCMessageBean dumpTagData(Tag tag) {
        NFCMessageBean nfcMessageBean = new NFCMessageBean();
        byte[] id = tag.getId();
        nfcMessageBean.setId(id);
        nfcMessageBean.setId_hex(toHex(id));
        nfcMessageBean.setId_reversed_hex(toReversedHex(id));
        nfcMessageBean.setId_dec(toDec(id) + "");
        nfcMessageBean.setId_reversed_dec(toReversedDec(id) + "");

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                String type = "Unknown";
                try {
                    MifareClassic mifareTag;
                    try {
                        mifareTag = MifareClassic.get(tag);
                    } catch (Exception e) {
                        // Fix for Sony Xperia Z3/Z5 phones
                        tag = cleanupTag(tag);
                        mifareTag = MifareClassic.get(tag);
                    }
                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    nfcMessageBean.setType(type);
                    nfcMessageBean.setSize(mifareTag.getSize());
                    nfcMessageBean.setSectors(mifareTag.getSectorCount());
                    nfcMessageBean.setBlocks(mifareTag.getBlockCount());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return nfcMessageBean;
    }

    private Tag cleanupTag(Tag oTag) {
        if (oTag == null)
            return null;

        String[] sTechList = oTag.getTechList();

        Parcel oParcel = Parcel.obtain();
        oTag.writeToParcel(oParcel, 0);
        oParcel.setDataPosition(0);

        int len = oParcel.readInt();
        byte[] id = null;
        if (len >= 0) {
            id = new byte[len];
            oParcel.readByteArray(id);
        }
        int[] oTechList = new int[oParcel.readInt()];
        oParcel.readIntArray(oTechList);
        Bundle[] oTechExtras = oParcel.createTypedArray(Bundle.CREATOR);
        int serviceHandle = oParcel.readInt();
        int isMock = oParcel.readInt();
        IBinder tagService;
        if (isMock == 0) {
            tagService = oParcel.readStrongBinder();
        } else {
            tagService = null;
        }
        oParcel.recycle();

        int nfca_idx = -1;
        int mc_idx = -1;
        short oSak = 0;
        short nSak = 0;

        for (int idx = 0; idx < sTechList.length; idx++) {
            if (sTechList[idx].equals(NfcA.class.getName())) {
                if (nfca_idx == -1) {
                    nfca_idx = idx;
                    if (oTechExtras[idx] != null && oTechExtras[idx].containsKey("sak")) {
                        oSak = oTechExtras[idx].getShort("sak");
                        nSak = oSak;
                    }
                } else {
                    if (oTechExtras[idx] != null && oTechExtras[idx].containsKey("sak")) {
                        nSak = (short) (nSak | oTechExtras[idx].getShort("sak"));
                    }
                }
            } else if (sTechList[idx].equals(MifareClassic.class.getName())) {
                mc_idx = idx;
            }
        }

        boolean modified = false;

        if (oSak != nSak) {
            oTechExtras[nfca_idx].putShort("sak", nSak);
            modified = true;
        }

        if (nfca_idx != -1 && mc_idx != -1 && oTechExtras[mc_idx] == null) {
            oTechExtras[mc_idx] = oTechExtras[nfca_idx];
            modified = true;
        }

        if (!modified) {
            return oTag;
        }

        Parcel nParcel = Parcel.obtain();
        nParcel.writeInt(id.length);
        nParcel.writeByteArray(id);
        nParcel.writeInt(oTechList.length);
        nParcel.writeIntArray(oTechList);
        nParcel.writeTypedArray(oTechExtras, 0);
        nParcel.writeInt(serviceHandle);
        nParcel.writeInt(isMock);
        if (isMock == 0) {
            nParcel.writeStrongBinder(tagService);
        }
        nParcel.setDataPosition(0);

        Tag nTag = Tag.CREATOR.createFromParcel(nParcel);

        nParcel.recycle();

        return nTag;
    }
    //https://github.com/nadam/nfc-reader
    public interface NfcResponseListener {
        public void onResult(NFCMessageBean nfcMessageBean);
    }

}
