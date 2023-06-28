package com.example.lab6_20190740_20195527.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6_20190740_20195527.R;
import com.example.lab6_20190740_20195527.activities.ActualizarActivity;
import com.example.lab6_20190740_20195527.activities.DetalleActivity;
import com.example.lab6_20190740_20195527.entities.Actividad;
import com.example.lab6_20190740_20195527.entities.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
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

        SharedPreferences sharedPreferences = context.getSharedPreferences("MainPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userStr = sharedPreferences.getString("usuario", "");
        Type userType = new TypeToken<Usuario>(){}.getType();
        Usuario usuario = gson.fromJson(userStr, userType);
        holder.actividad = activ;
        Log.d("msg-adapter", activ.getTitulo());
        Log.d("msg-adapter", activ.getFecha());
        Log.d("msg-adapter", activ.getHoraFin());
        Log.d("msg-adapter", activ.getHoraInicio());
        TextView titulo = holder.itemView.findViewById(R.id.titulo);
        TextView fecha = holder.itemView.findViewById(R.id.fechaValue);
        TextView horaInicio = holder.itemView.findViewById(R.id.inicioTime);
        TextView horaFin = holder.itemView.findViewById(R.id.finTime);

        titulo.setText(activ.getTitulo());
        fecha.setText(activ.getFecha());
        horaFin.setText(activ.getHoraFin());
        horaInicio.setText(activ.getHoraInicio());

        ImageButton eliminar  = holder.itemView.findViewById(R.id.btnTrash);
        eliminar.setOnClickListener(view -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();;
            FirebaseStorage storage = FirebaseStorage.getInstance();
            DatabaseReference registroRef = firebaseDatabase.getReference(usuario.getGoogleKey()+"/"+activ.getIdAct());
            StorageReference imageRef = storage.getReference().child(usuario.getGoogleKey()).child(activ.getIdAct());
            Log.d("msg-imagen", imageRef.getPath());
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    registroRef.removeValue();
                    Toast.makeText(context, "Actividad Eliminada", Toast.LENGTH_SHORT).show();
                }
            });
            notifyDataSetChanged();
        });

        ImageView editar = holder.itemView.findViewById(R.id.editar);
        editar.setOnClickListener(view -> {
            String actividad = gson.toJson(activ);
            editor.putString("actividad", actividad);
            editor.apply();
            Intent intent = new Intent(context, ActualizarActivity.class);
            context.startActivity(intent);
            notifyDataSetChanged();
        });

        ImageView detalle = holder.itemView.findViewById(R.id.detalle);
        detalle.setOnClickListener(view -> {
            String actividad = gson.toJson(activ);
            editor.putString("actividad", actividad);
            editor.apply();
            Intent intent = new Intent(context, DetalleActivity.class);
            context.startActivity(intent);
            notifyDataSetChanged();
        });
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

    public List<Actividad> getListaActividades() {
        return listaActividades;
    }

    public void setListaActividades(List<Actividad> listaActividades) {
        this.listaActividades = listaActividades;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
