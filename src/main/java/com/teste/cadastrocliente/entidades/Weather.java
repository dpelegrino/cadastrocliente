package com.teste.cadastrocliente.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

    @JsonProperty("consolidated_weather")
    private List<ConsolidatedWeather> consolidatedWeathers;

    public List<ConsolidatedWeather> getConsolidatedWeathers() {
        return consolidatedWeathers;
    }

    public void setConsolidatedWeathers(List<ConsolidatedWeather> consolidatedWeathers) {
        this.consolidatedWeathers = consolidatedWeathers;
    }
}
