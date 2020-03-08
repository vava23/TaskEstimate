package com.github.vava23.taskestimate;

import com.github.vava23.taskestimate.domain.TaskEstimationService;
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
        try {
            TaskEstimationService.calcTaskEstimate(5, 6, 10);
            thrown = false;
        }
        catch (IllegalArgumentException e) { }
        try {
            TaskEstimationService.calcTaskEstimate(5, 1, 4);
            thrown = false;
        }
        catch (IllegalArgumentException e) { }
        Assert.assertTrue(thrown);

        // Correct input
        TaskEstimationService.calcTaskEstimate(5, 1, 10);
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
