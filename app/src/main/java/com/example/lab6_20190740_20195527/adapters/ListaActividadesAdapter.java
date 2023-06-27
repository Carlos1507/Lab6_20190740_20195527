package com.example.lab6_20190740_20195527.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6_20190740_20195527.R;
import com.example.lab6_20190740_20195527.entities.Actividad;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListaActividadesAdapter extends RecyclerView.Adapter<ListaActividadesAdapter.ActividadesViewHolder>{
    private List<Actividad> listaActividades;
    private Context context;

    @NonNull
    @Override
    public ActividadesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_activity, parent, false);
        return new ActividadesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadesViewHolder holder, int position) {
        Actividad activ = listaActividades.get(position);
        holder.actividad = activ;

        TextView titulo = holder.itemView.findViewById(R.id.titulo);
        TextView fecha = holder.itemView.findViewById(R.id.fechaValue);
        TextView horaInicio = holder.itemView.findViewById(R.id.horaInicio);
        TextView horaFin = holder.itemView.findViewById(R.id.horaFin);

    }

    @Override
    public int getItemCount() {
        return listaActividades.size();
    }

    public class ActividadesViewHolder extends RecyclerView.ViewHolder{
        Actividad actividad;
        public ActividadesViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }

}
