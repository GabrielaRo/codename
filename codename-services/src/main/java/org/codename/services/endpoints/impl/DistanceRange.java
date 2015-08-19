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
    _1KM(0.0, 1.0, "Within 1 KM"),
    _3KM(1.0, 3.0, "Within 3 KM"),
    _10KM(3.0, 10.0, "Within 10 KM"),
    _50KM(10.0, 50.0, "Within 50 KM"),
    _WORLD(50.0, 10000.0, "Far Away"),
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
