package com.github.vava23.taskestimate;

import java.util.List;

//
// Task completion estimation based on PERT technique
//
public class TaskEstimationPERT {

    //
    // Estimation of task completion time
    //
    public Estimate calcTaskEstimate(
            double aTimeOptimistic,
            double aTimePessimistic,
            double aTimeMostLikely) throws IllegalArgumentException {
        // Check input
        if (aTimeOptimistic < 0 || aTimePessimistic < 0 || aTimeMostLikely < 0) {
            throw new IllegalArgumentException("Wrong time value");
        }

        // Calculate the estimate
        double time = aTimeOptimistic + aTimeMostLikely*4 + aTimePessimistic;
        double stDev = (aTimePessimistic - aTimeOptimistic) / 6;
        return new Estimate(time, stDev);
    }

    //
    // Summarizes estimates giving the total completion estimate for a set of tasks
    //
    public static Estimate CombineEstimates(List<Estimate> aEstimates) throws IllegalArgumentException {
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
