package com.if4a.silpingo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.if4a.silpingo.R;
import com.if4a.silpingo.activity.MainActivity;
import com.if4a.silpingo.activity.UbahActivity;
import com.if4a.silpingo.api.APIRequestData;
import com.if4a.silpingo.api.Retroserver;
import com.if4a.silpingo.model.ModelNGO;
import com.if4a.silpingo.model.ModelResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterNGO extends RecyclerView.Adapter<AdapterNGO.VHNGO> {
    private Context ctx;
    private List<ModelNGO> listNGO;

    public AdapterNGO(Context ctx, List<ModelNGO> listNGO) {
        this.ctx = ctx;
        this.listNGO = listNGO;
    }

    @NonNull
    @Override
    public VHNGO onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View varView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ngo, parent, false);
        return new VHNGO(varView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNGO.VHNGO holder, int position) {
        ModelNGO MN = listNGO.get(position);
        holder.tvId.setText(MN.getId());
        holder.tvNama.setText(MN.getNama());
        holder.tvBidang.setText(MN.getBidang());
        holder.tvAlamat.setText(MN.getAlamat());
        holder.tvDeskripsi.setText(MN.getDeskripsi());

    }

    @Override
    public int getItemCount() {
        return listNGO.size();
    }

    public class VHNGO extends RecyclerView.ViewHolder{
        TextView tvId, tvNama, tvBidang, tvAlamat, tvDeskripsi;

        public VHNGO(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvBidang = itemView.findViewById(R.id.tv_bidang);
            tvAlamat = itemView.findViewById(R.id.tv_alamat);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder pesan = new AlertDialog.Builder(ctx);
                    pesan.setTitle("Perhatian");
                    pesan.setMessage("Operasi apa yang akan dilakukan?");
                    pesan.setCancelable(true);

                    pesan.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hapusNGO(tvId.getText().toString());
                            dialog.dismiss();
                        }
                    });
                    pesan.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent pindah = new Intent(ctx, UbahActivity.class);
                            pindah.putExtra("xId", tvId.getText().toString());
                            pindah.putExtra("xNama", tvNama.getText().toString());
                            pindah.putExtra("xBidang", tvBidang.getText().toString());
                            pindah.putExtra("xAlamat", tvAlamat.getText().toString());
                            pindah.putExtra("xDeskripsi", tvDeskripsi.getText().toString());
                            ctx.startActivity(pindah);
                        }
                    });
                    pesan.show();
                    return false;
                }
            });

        }

        private void hapusNGO(String idNGO){
            APIRequestData ARD = Retroserver.koneksiRetrofit().create(APIRequestData.class);
            Call<ModelResponse> proses = ARD.ardDelete(idNGO);

            proses.enqueue(new Callback<ModelResponse>() {
                @Override
                public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                    String kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(ctx, "Kode : " + kode + ", Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                    ((MainActivity) ctx).retrievengo();
                }

                @Override
                public void onFailure(Call<ModelResponse> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
