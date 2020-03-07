package com.github.vava23.taskestimate.domain;

/**
 * Estimation of time in days.
 */
public class Estimate {
  /** Time in days. */
  private double days;
  /** Standard deviation of time estimate. */
  private double stDeviation;

  /**
   * Constructor.
   */
  public Estimate(double aTime, double aStDeviation) {
    days = aTime;
    stDeviation = aStDeviation;
  }

  /** Time in days. */
  public double getDays() {
    return days;
  }

  /** Standard deviation of time estimate. */
  public double getStDeviation() {
    return stDeviation;
  }
}
