package com.github.vava23.taskestimate.domain;

import java.util.List;

/**
 * Task completion estimation based on PERT technique.
 */
public class TaskEstimationService {
  /**
   * Creates new task from parameters.
   */
  public static Task newTask(
      long aId,
      String aTaskName,
      double aTimeMostLikely,
      double aTimeBestCase,
      double aTimeWorstCase,
      List<String> errors
  ) {
    try {
      return new Task(aId, aTaskName, aTimeMostLikely, aTimeBestCase, aTimeWorstCase);
    } catch (IllegalArgumentException e) {
      // Add a message in case of exception
      if (errors != null) {
        errors.add(e.getMessage());
      }
      return null;
    }
  }

  /**
   * Estimation of task completion time.
   */
  public static Estimate calcTaskEstimate(
      double aTimeMostLikely,
      double aTimeBestCase,
      double aTimeWorstCase
  ) throws IllegalArgumentException {
    return new Task(1, "", aTimeMostLikely, aTimeBestCase, aTimeWorstCase).getEstimate();
  }

  /**
   * Summarizes estimates giving the total completion estimate for a set of tasks.
   */
  public static Estimate combineEstimates(List<Estimate> aEstimates)
      throws IllegalArgumentException {
    // Check input
    if (aEstimates == null) {
      throw new IllegalArgumentException("set of estimates is NULL");
    }

    // Sum up the time and stDevs
    double time = 0;
    double stDevSqSum = 0;
    for (Estimate e : aEstimates) {
      time += e.getDays();
      stDevSqSum += Math.pow(e.getStDeviation(), 2);
    }

    // Calc the result
    double stDev = Math.sqrt(stDevSqSum);
    return new Estimate(time, stDev);
  }
}
