package com.github.vava23.taskestimate;

import com.github.vava23.taskestimate.business.TaskEstimationService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

//
// Task completion estimation based on PERT technique
//
public class TaskEstimationServiceTest {

    //private final TaskEstimationPERT estimation = new TaskEstimationPERT();

    @Test
    void calcTaskEstimateTest() {
        // Incorrect input
        boolean thrown = true;
        try {
            TaskEstimationService.calcTaskEstimate(-1, 1, 1);
            thrown = false;
        }
        catch (IllegalArgumentException e) { }
        try {
            TaskEstimationService.calcTaskEstimate(1, -1, 1);
            thrown = false;
        }
        catch (IllegalArgumentException e) { }
        try {
            TaskEstimationService.calcTaskEstimate(1, 1, -1);
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
            TaskEstimationService.combineEstimates(null);
            thrown = false;
        }
        catch (IllegalArgumentException e) { }
        Assert.assertTrue(thrown);
    }

}
