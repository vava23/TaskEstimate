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
            double aTimeOptimistic,
            double aTimePessimistic,
            double aTimeMostLikely) throws IllegalArgumentException {
        // Check input
        if (aTimeOptimistic < 0 || aTimePessimistic < 0 || aTimeMostLikely < 0) {
            throw new IllegalArgumentException("Wrong time value");
        }

        // TODO Check if inputs are in the correct order

        // Calculate the estimate
        double time = (aTimeOptimistic + aTimeMostLikely*4 + aTimePessimistic)/6;
        double stDev = (aTimePessimistic - aTimeOptimistic) / 6;
        return new Estimate(time, stDev);
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
