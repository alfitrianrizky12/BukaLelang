package com.example.slelangonline.Barang;

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

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.ViewHolder> {
    private List<Barang> dataList;
    private Context context;

    public BarangAdapter(Context context, List<Barang> dataList){
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_care, parent, false);
        BarangAdapter.ViewHolder holder = new BarangAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new RoundedCorners(16));

        Glide.with(context).load(dataList.get(position).getPicTips()).apply(requestOptions).into(holder.ivTips);
        holder.tvNamaBarang.setText(dataList.get(position).getNamaBarang());
        holder.tvDeskripsi.setText(dataList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTips;
        TextView tvNamaBarang;
        TextView tvDeskripsi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivTips = itemView.findViewById(R.id.ivList);
            tvNamaBarang = itemView.findViewById(R.id.tvNamaBarang);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
        }
    }
}
