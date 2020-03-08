package com.github.vava23.taskestimate.domain;

import java.util.List;

/**
 * Task completion estimation based on PERT technique
 */
public class TaskEstimationService {

    /**
     * Estimation of task completion time.
     */
    public static Estimate calcTaskEstimate(
            double aTimeMostLikely,
            double aTimeBestCase,
            double aTimeWorstCase
            ) throws IllegalArgumentException {
        return new Task("", aTimeMostLikely, aTimeBestCase, aTimeWorstCase).getEstimate();
    }

    /**
     * Summarizes estimates giving the total completion estimate for a set of tasks.
     */
    public static Estimate combineEstimates(List<Estimate> aEstimates) throws IllegalArgumentException {
        // Check input
        if (aEstimates == null) {
            throw new IllegalArgumentException("Set of estimates is NULL");
        }

        // Sum up the time and stDevs
        double time = 0;
        double stDevSqSum = 0;
        for (Estimate e: aEstimates) {
            time += e.getDays();
            stDevSqSum += Math.pow(e.getStDeviation(), 2);
        }

        // Calc the result
        double stDev = Math.sqrt(stDevSqSum);
        return new Estimate(time, stDev);
    }
}
