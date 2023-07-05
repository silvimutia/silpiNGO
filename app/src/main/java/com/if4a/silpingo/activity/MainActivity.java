package com.if4a.silpingo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.if4a.silpingo.R;
import com.if4a.silpingo.adapter.AdapterNGO;
import com.if4a.silpingo.api.APIRequestData;
import com.if4a.silpingo.api.Retroserver;
import com.if4a.silpingo.model.ModelNGO;
import com.if4a.silpingo.model.ModelResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvNGO;
    private FloatingActionButton fabTambah;
    private ProgressBar pbNGO;
    private RecyclerView.Adapter adNGO;
    private RecyclerView.LayoutManager lmNGO;
    private List<ModelNGO> listNGO = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvNGO = findViewById(R.id.rv_ngo);
        fabTambah = findViewById(R.id.fab_tambah);
        pbNGO = findViewById(R.id.pb_ngo);

        lmNGO = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvNGO.setLayoutManager(lmNGO);

        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TambahActivity.class));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        retrievengo();
    }

    public void retrievengo(){
        pbNGO.setVisibility(View.VISIBLE);

        APIRequestData ARD = Retroserver.koneksiRetrofit().create(APIRequestData.class);
        Call<ModelResponse> proses = ARD.ardRetrieve();

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listNGO = response.body().getData();

                adNGO = new AdapterNGO(MainActivity.this, listNGO);
                rvNGO.setAdapter(adNGO);
                adNGO.notifyDataSetChanged();

                pbNGO.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
                pbNGO.setVisibility(View.GONE);
            }
        });
    }
}