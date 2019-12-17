package com.github.vava23.taskestimate;

import com.github.vava23.taskestimate.business.TaskEstimationPERT;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

//
// Task completion estimation based on PERT technique
//
public class TaskEstimationPERTTest {

    //private final TaskEstimationPERT estimation = new TaskEstimationPERT();

    @Test
    void calcTaskEstimateTest() {
        // Incorrect input
        boolean thrown = true;
        try {
            TaskEstimationPERT.calcTaskEstimate(-1, 1, 1);
            thrown = false;
        }
        catch (IllegalArgumentException e) { }
        try {
            TaskEstimationPERT.calcTaskEstimate(1, -1, 1);
            thrown = false;
        }
        catch (IllegalArgumentException e) { }
        try {
            TaskEstimationPERT.calcTaskEstimate(1, 1, -1);
            thrown = false;
        }
        catch (IllegalArgumentException e) { }
        Assert.assertTrue(thrown);
    }

    @Test
    void combineEstimatesTest() {
        // Null list on input
        boolean thrown = true;
        try {
            TaskEstimationPERT.combineEstimates(null);
            thrown = false;
        }
        catch (IllegalArgumentException e) { }
        Assert.assertTrue(thrown);
    }

}
