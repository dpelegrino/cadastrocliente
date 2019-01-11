package com.teste.cadastrocliente.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsolidatedWeather {
    private String min_temp;
    private String max_temp;

    public String getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(String min_temp) {
        this.min_temp = min_temp;
    }

    public String getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(String max_temp) {
        this.max_temp = max_temp;
    }

    @Override
    public String toString() {
        return "ConsolidatedWeather{" +
                "min_temp='" + min_temp + '\'' +
                ", max_temp='" + max_temp + '\'' +
                '}';
    }
}
