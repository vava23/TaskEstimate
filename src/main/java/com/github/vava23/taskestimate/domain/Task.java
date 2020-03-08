package com.github.vava23.taskestimate.domain;

import java.util.List;

/*
 * Single task that can be estimated
 */
public class Task {
  private String name;
  /** User-estimated time in the avg case. */
  private double timeMostLikely;
  /** User-estimated time in the best case (optimistic). */
  private double timeBestCase;
  /** User-estimated time in the worst case (pessimistic). */
  private double timeWorstCase;
  /** Expected estimate. */
  private Estimate estimate;
  /** List of subtasks of this task. TODO: for future use */
  private List<Task> subtasks;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getTimeMostLikely() {
    return timeMostLikely;
  }

  public double getTimeBestCase() {
    return timeBestCase;
  }

  public double getTimeWorstCase() {
    return timeWorstCase;
  }

  public Estimate getEstimate() {
    return estimate;
  }

  /*
   * Calculate the expected time estimate
   */
  private Estimate calcExpectedTime(
      double aTimeMostLikely,
      double aTimeBestCase,
      double aTimeWorstCase) {
    // Expected that inputs are checked by the invoker
    double time = (aTimeBestCase + aTimeMostLikely * 4 + aTimeWorstCase) / 6;
    double stDev = (aTimeWorstCase - aTimeBestCase) / 6;
    return new Estimate(time, stDev);
  }

  /**
   * Constructor.
   */
  public Task(
      String aName,
      double aTimeMostLikely,
      double aTimeBestCase,
      double aTimeWorstCase) {
    // Set the params
    if (aName == null || aName.isEmpty()) {
      name = "No name yet";
    } else {
      name = aName;
    }
    this.setTime(aTimeMostLikely, aTimeBestCase, aTimeWorstCase);
  }

  /**
   * Sets the user-estimated time values.
   */
  public void setTime(
      double aTimeMostLikely,
      double aTimeBestCase,
      double aTimeWorstCase
  ) {
    // Check the inputs
    if (aTimeBestCase < 0 || aTimeWorstCase < 0 || aTimeMostLikely < 0) {
      throw new IllegalArgumentException("Time value may not be negative");
    }
    if (Double.compare(aTimeBestCase, aTimeMostLikely) > 0) {
      throw new IllegalArgumentException("Best case time may not exceed most likely time");
    }
    if (Double.compare(aTimeMostLikely, aTimeWorstCase) > 0) {
      throw new IllegalArgumentException("Most likely time may not exceed worst case time");
    }

    // Save the params
    timeMostLikely = aTimeMostLikely;
    timeBestCase = aTimeBestCase;
    timeWorstCase = aTimeWorstCase;

    // Calculate the expected estimate
    estimate = calcExpectedTime(timeMostLikely, timeBestCase, timeWorstCase);
  }
}
