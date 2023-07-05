package com.if4a.silpingo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.if4a.silpingo.R;
import com.if4a.silpingo.api.APIRequestData;
import com.if4a.silpingo.api.Retroserver;
import com.if4a.silpingo.model.ModelResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahActivity extends AppCompatActivity {
    private String yId, yNama, yBidang, yAlamat, yDeskripsi;
    private EditText etNama, etBidang, etAlamat, etDeskripsi;
    private Button btnUbah;
    private String nama, bidang, alamat, deskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        Intent ambil = getIntent();
        yId = ambil.getStringExtra("xId");
        yNama = ambil.getStringExtra("xNama");
        yBidang = ambil.getStringExtra("xBidang");
        yAlamat = ambil.getStringExtra("xAlamat");
        yDeskripsi = ambil.getStringExtra("xDeskripsi");

        etNama = findViewById(R.id.et_name);
        etBidang = findViewById(R.id.et_bidang);
        etAlamat = findViewById(R.id.et_alamat);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        btnUbah = findViewById(R.id.btn_ubah);

        etNama.setText(yNama);
        etBidang.setText(yBidang);
        etAlamat.setText(yAlamat);
        etDeskripsi.setText(yDeskripsi);

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nama =  etNama.getText().toString();
                bidang = etBidang.getText().toString();
                alamat = etAlamat.getText().toString();
                deskripsi = etDeskripsi.getText().toString();

                if(nama.trim().isEmpty()||bidang.trim().isEmpty()||alamat.trim().isEmpty()||deskripsi.trim().isEmpty()){
                    if(nama.trim().isEmpty()){
                        etNama.setError("Nama Tidak Boleh Kosong");
                    }
                    if(bidang.trim().isEmpty()){
                        etBidang.setError("Bidang Tidak Boleh Kosong");
                    }
                    if(alamat.trim().isEmpty()){
                        etAlamat.setError("Alamat Tidak Boleh Kosong");
                    }
                    if(deskripsi.trim().isEmpty()){
                        etDeskripsi.setError("Deskripsi Tidak Boleh Kosong");
                    }
                }
                else{
                    UbahKuliner();
                }
            }
        });
    }

    private void UbahKuliner(){
        APIRequestData ARD = Retroserver.koneksiRetrofit().create(APIRequestData.class);
        Call<ModelResponse> proses = ARD.ardUpdate(yId, nama, bidang, alamat, deskripsi);

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(UbahActivity.this, "Kode : " + kode + "Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(UbahActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
            }
        });

    }

}