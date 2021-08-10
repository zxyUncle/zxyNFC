package com.zxy.zxynfc;


import java.util.Arrays;

class NFCMessageBean {
    private byte[] id;
    private String id_hex;
    private String id_reversed_hex;
    private String id_dec;
    private String id_reversed_dec;
    private String type;
    private int size;
    private int sectors;
    private int blocks;

    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    public String getId_hex() {
        return id_hex;
    }

    public void setId_hex(String id_hex) {
        this.id_hex = id_hex;
    }

    public String getId_reversed_hex() {
        return id_reversed_hex;
    }

    public void setId_reversed_hex(String id_reversed_hex) {
        this.id_reversed_hex = id_reversed_hex;
    }

    public String getId_dec() {
        return id_dec;
    }

    public void setId_dec(String id_dec) {
        this.id_dec = id_dec;
    }

    public String getId_reversed_dec() {
        return id_reversed_dec;
    }

    public void setId_reversed_dec(String id_reversed_dec) {
        this.id_reversed_dec = id_reversed_dec;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSectors() {
        return sectors;
    }

    public void setSectors(int sectors) {
        this.sectors = sectors;
    }

    public int getBlocks() {
        return blocks;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    @Override
    public String toString() {
        return "NFCMessageBean{" +
                "id=" + Arrays.toString(id) +
                ", id_hex='" + id_hex + '\'' +
                ", id_reversed_hex='" + id_reversed_hex + '\'' +
                ", id_dec='" + id_dec + '\'' +
                ", id_reversed_dec='" + id_reversed_dec + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", sectors=" + sectors +
                ", blocks=" + blocks +
                '}';
    }
}
