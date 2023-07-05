package com.if4a.silpingo.model;

import java.util.List;

public class ModelResponse {
    private String kode, pesan;
    private List<ModelNGO> data;

    public String getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public List<ModelNGO> getData() {
        return data;
    }
}
