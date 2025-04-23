package br.com.fecapccp.uberreport.logicas.alertas.model;

import java.util.Objects;

public class Alerta {
    private String nomeAlerta;
    private String tipoAlerta;
    private String dataHoraAlerta;
    private Double latitude;
    private Double longitude;
    private Integer fk_idUser;

public Alerta(String nomeAlerta, String tipoAlerta, String dataHoraAlerta, Double latitude, Double longitude, Integer fk_idUser) {
        this.nomeAlerta = nomeAlerta;
        this.tipoAlerta = tipoAlerta;
        this.dataHoraAlerta = dataHoraAlerta;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fk_idUser = fk_idUser;
    }

    public String getNomeAlerta() {
        return nomeAlerta;
    }

    public void setNomeAlerta(String nomeAlerta) {
        this.nomeAlerta = nomeAlerta;
    }

    public String getTipoAlerta() {
        return tipoAlerta;
    }

    public void setTipoAlerta(String tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }

    public String getDataHoraAlerta() {
        return dataHoraAlerta;
    }

    public void setDataHoraAlerta(String dataHoraAlerta) {
        this.dataHoraAlerta = dataHoraAlerta;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public Integer getFk_idUser() {
        return fk_idUser;
    }

public void setFk_idUser(Integer fk_idUser) {
    this.fk_idUser = fk_idUser;
}
    @Override
    public String toString() {
        return "Alerta{" +
                "nomeAlerta='" + nomeAlerta + '\'' +
                ", tipoAlerta='" + tipoAlerta + '\'' +
                ", dataHoraAlerta='" + dataHoraAlerta + '\'' +
                ", latitude=" + latitude +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", fk_idUser=" + fk_idUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alerta alerta = (Alerta) o;
        return Objects.equals(nomeAlerta, alerta.nomeAlerta) &&
                Objects.equals(tipoAlerta, alerta.tipoAlerta) &&
                Objects.equals(dataHoraAlerta, alerta.dataHoraAlerta) &&
                Objects.equals(latitude, alerta.latitude) &&
                Objects.equals(longitude, alerta.longitude) &&
                Objects.equals(fk_idUser, alerta.fk_idUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeAlerta, tipoAlerta, dataHoraAlerta, latitude, longitude, fk_idUser);
    }

}
