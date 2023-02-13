package com.example.desafio.models.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "cidades")
public class Cidade {

    @Id
    @NotBlank
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ibgeId;

    @NotBlank
    private String UF;

    @NotBlank
    private String nome;

    @NotBlank
    private Boolean capital;

    private Integer longitude;

    private Integer latitude;

    private Boolean sotaque;

    private String nomeAlternativo;

    private String microRegiao;

    private String mesoRegiao;

    public Cidade() {
    }

    public Cidade(Integer ibgeId, String uF, String nome, Boolean capital, Integer longitude, Integer latitude,
            Boolean sotaque,
            String nomeAlternativo, String microRegiao, String mesoRegiao) {
        this.ibgeId = ibgeId;
        UF = uF;
        this.nome = nome;
        this.capital = capital;
        this.longitude = longitude;
        this.latitude = latitude;
        this.sotaque = sotaque;
        this.nomeAlternativo = nomeAlternativo;
        this.microRegiao = microRegiao;
        this.mesoRegiao = mesoRegiao;
    }

    public Integer getIbgeId() {
        return ibgeId;
    }

    public void setIbgeId(Integer ibgeId) {
        this.ibgeId = ibgeId;
    }

    public String getUF() {
        return UF;
    }

    public void setUF(String uF) {
        UF = uF;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getCapital() {
        return capital;
    }

    public void setCapital(Boolean capital) {
        this.capital = capital;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Boolean getSotaque() {
        return sotaque;
    }

    public void setSotaque(Boolean sotaque) {
        this.sotaque = sotaque;
    }

    public String getNomeAlternativo() {
        return nomeAlternativo;
    }

    public void setNomeAlternativo(String nomeAlternativo) {
        this.nomeAlternativo = nomeAlternativo;
    }

    public String getMicroRegiao() {
        return microRegiao;
    }

    public void setMicroRegiao(String microRegiao) {
        this.microRegiao = microRegiao;
    }

    public String getMesoRegiao() {
        return mesoRegiao;
    }

    public void setMesoRegiao(String mesoRegiao) {
        this.mesoRegiao = mesoRegiao;
    }

}
