package com.example.lab6_20190740_20195527.Configurations;

import java.time.LocalDate;
import java.time.LocalTime;

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
        if (time.getHour()<10){
            hora+=("0"+time.getHour());
            extension+="am";
        } else if (10<time.getHour() && time.getHour()<13) {
            hora+=time.getHour();
            extension+="am";
        }else {
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

}
