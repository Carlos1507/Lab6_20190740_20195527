package com.example.lab6_20190740_20195527.Configurations;

import android.content.res.ColorStateList;
import android.util.Log;

import com.example.lab6_20190740_20195527.R;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

public class Config {
    public String fechaStrFormateada(LocalDate fecha){
        String dia=""; String mes = "";
        if (fecha.getDayOfMonth()<10){
            dia+= ("0"+fecha.getDayOfMonth());
        }else{
            dia+= fecha.getDayOfMonth();
        }
        if (fecha.getMonthValue()<10){
            mes+=("0"+fecha.getMonthValue());
        }else{
            mes+=fecha.getMonthValue();
        }
        return dia+"/"+mes+"/"+fecha.getYear();
    }

    public String horaStrFormateada(LocalTime time){
        String hora = ""; String minuto = ""; String extension="";
        if (time.getHour()<12) {
            hora += (time.getHour());
            extension += "am";
        } else if (time.getHour()==12) {
            hora+=time.getHour();
            extension+="pm";
        } else {
            hora+=(time.getHour()-12);
            extension+="pm";
        }
        if (time.getMinute()<10){
            minuto+=("0"+time.getMinute());
        }else{
            minuto+=time.getMinute();
        }
        return hora+":"+minuto+" "+extension;
    }
    public String generateID(int tamano){
        String letras = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random r = new Random();
        String id = "";
        for (int i = 0; i<tamano; i++){
            id+= letras.charAt(r.nextInt(letras.length()));
        }
        return id;
    }

    public LocalTime timeStrToLocalTime(String timeStr){
        int hora = 0; int min=0;
        String timeSinExtension = timeStr.split(" ")[0];
        if (timeStr.contains("am")){
            hora=Integer.parseInt((timeSinExtension.split(":")[0]));
        }else{
            hora=Integer.parseInt((timeSinExtension.split(":")[0]))+12;
        }
        min = Integer.parseInt(timeSinExtension.split(":")[1]);
        return LocalTime.of(hora, min);
    }

    public LocalDate dateStrToLocalDate(String fechaStr) {
        String[] fecha = fechaStr.split("/");
        Log.d("fecha", fecha[2] + "/" + fecha[1] + "/" + fecha[0]);
        return LocalDate.of(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[0]));
    }

}