package com.xai.radarmodel.entity;

import jakarta.persistence.*;

@Entity
public class RadarParameters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double gr;
    private double lo;
    private double freqR_radar;
    private double pr_radar;
    private double br_radar;
    private double fr_radar;
    private double k_radar;
    private double t_radar;
    private double latT_radar, longT_radar, heightT_radar;

    // Optional radar location if you want to include it later
    private double latR_radar = 0.0;
    private double longR_radar = 0.0;
    private double heightR_radar = 0.0;

    private double pfa_radar;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getGr() { return gr; }
    public void setGr(double gr) { this.gr = gr; }

    public double getLo() { return lo; }
    public void setLo(double lo) { this.lo = lo; }

    public double getFreqR_radar() { return freqR_radar; }
    public void setFreqR_radar(double freqR_radar) { this.freqR_radar = freqR_radar; }

    public double getPr_radar() { return pr_radar; }
    public void setPr_radar(double pr_radar) { this.pr_radar = pr_radar; }

    public double getBr_radar() { return br_radar; }
    public void setBr_radar(double br_radar) { this.br_radar = br_radar; }

    public double getFr_radar() { return fr_radar; }
    public void setFr_radar(double fr_radar) { this.fr_radar = fr_radar; }

    public double getK_radar() { return k_radar; }
    public void setK_radar(double k_radar) { this.k_radar = k_radar; }

    public double getT_radar() { return t_radar; }
    public void setT_radar(double t_radar) { this.t_radar = t_radar; }

    public double getLatT_radar() { return latT_radar; }
    public void setLatT_radar(double latT_radar) { this.latT_radar = latT_radar; }

    public double getLongT_radar() { return longT_radar; }
    public void setLongT_radar(double longT_radar) { this.longT_radar = longT_radar; }

    public double getHeightT_radar() { return heightT_radar; }
    public void setHeightT_radar(double heightT_radar) { this.heightT_radar = heightT_radar; }

    public double getLatR_radar() { return latR_radar; }
    public void setLatR_radar(double latR_radar) { this.latR_radar = latR_radar; }

    public double getLongR_radar() { return longR_radar; }
    public void setLongR_radar(double longR_radar) { this.longR_radar = longR_radar; }

    public double getHeightR_radar() { return heightR_radar; }
    public void setHeightR_radar(double heightR_radar) { this.heightR_radar = heightR_radar; }

    public double getPfa_radar() { return pfa_radar; }
    public void setPfa_radar(double pfa_radar) { this.pfa_radar = pfa_radar; }
}
