package com.example.slelangonline.Lelang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.slelangonline.R;

import java.util.List;

public class LelangAdapter extends RecyclerView.Adapter<LelangAdapter.ViewHolder> {
    private List<Lelang> dataList;
    private Context context;

    public LelangAdapter(Context context, List<Lelang> dataList){
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_lelang, parent, false);
        LelangAdapter.ViewHolder holder = new LelangAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new RoundedCorners(16));

        Glide.with(context).load(dataList.get(position).getPicTips()).apply(requestOptions).into(holder.ivTips);
        holder.tvNamaBarang.setText(dataList.get(position).getTitleTips());
        holder.tvDeskripsi.setText(dataList.get(position).getContent());
        holder.tvHarga.setText("Rp. "+dataList.get(position).getHargaAkhir());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTips;
        TextView tvNamaBarang;
        TextView tvDeskripsi;
        TextView tvHarga;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivTips = itemView.findViewById(R.id.ivList);
            tvNamaBarang = itemView.findViewById(R.id.tvNamaBarang);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvHarga = itemView.findViewById(R.id.tvHarga);
        }
    }
}
