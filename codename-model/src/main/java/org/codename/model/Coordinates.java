/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.model;

/**
 *
 * @author salaboy
 */
public class Coordinates {
    private Double lon;
    private Double lat;

    public Coordinates(Double lon, Double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public Coordinates() {
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "Coordinates{" + "lon=" + lon + ", lat=" + lat + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.lon != null ? this.lon.hashCode() : 0);
        hash = 83 * hash + (this.lat != null ? this.lat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coordinates other = (Coordinates) obj;
        if (this.lon != other.lon && (this.lon == null || !this.lon.equals(other.lon))) {
            return false;
        }
        if (this.lat != other.lat && (this.lat == null || !this.lat.equals(other.lat))) {
            return false;
        }
        return true;
    }
    
    
}
