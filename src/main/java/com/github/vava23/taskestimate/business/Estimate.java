package com.github.vava23.taskestimate.business;

//
// Estimation of time in days
//
public class Estimate {
    // Time in days
    private double fDays;
    // Standard deviation of time estimate
    private double fStDeviation;

    public Estimate(double aTime, double aDeviation) {
        fDays = aTime;
        fStDeviation = aDeviation;
    }

    // Time in days
    public double getDays() {
        return fDays;
    }

    // Standard deviation of time estimate
    public double getStDeviation() {
        return fStDeviation;
    }
}
