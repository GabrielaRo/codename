/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

/**
 *
 * @author grogdj
 */
public enum DistanceRange {
    _1KM(0.0, 1.0, "Within 1km"),
    _3KM(1.0, 3.0, "Within 3km"),
    _5KM(3.0, 5.0, "Within 5km"),
    _10KM(5.0, 10.0, "Within 10km"),
    _20KM(10.0, 20.0, "Within 20km"),
    _30KM(15.0, 30.0, "Within 30km"),
    _50KM(30.0, 50.0, "Within 50km"),
    _100KM(50.0, 100.0, "Within 100km"),
    _WORLD(100.0, 50000.0, "Over 100km away"),
    _ALL(-1.0, -1.0, "The World" );

    private Double offsetRange;
    private Double limitRange;
    private String description;

    DistanceRange(Double offsetRange, Double limitRange, String description) {
        this.offsetRange = offsetRange;
        this.limitRange = limitRange;
        this.description = description;
    }

    public Double getOffsetRange() {
        return offsetRange;
    }

    public Double getLimitRange() {
        return limitRange;
    }

    public String getDescription() {
        return description;
    }

}
