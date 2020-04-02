package com.github.vava23.taskestimate;

import com.github.vava23.taskestimate.domain.Estimate;
import com.github.vava23.taskestimate.domain.Task;
import com.github.vava23.taskestimate.domain.TaskEstimationService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Task completion estimation based on PERT technique
 */
public class TaskEstimationServiceTest {
    /** Compare delta */
    static final double DELTA = 0.01; // UI precision is 0.1
    /** Reference values */
    static final double REF_INPUT_ML = 5;
    static final double REF_INPUT_BC = 2;
    static final double REF_INPUT_WC = 14;
    static final Estimate REF_ESTIMATE = new Estimate(6, 2);
    static final List<Task> REF_TASKS = new ArrayList<Task>();
    static final Estimate REF_ESTIMATE_TOTAL = new Estimate(11, 1.892969449);

    /** Initialization */
    static {
        // Initialize reference estimate list
        REF_TASKS.add(new Task(1, "ONE", 3, 2, 10));
        REF_TASKS.add(new Task(2, "TWO", 1, 0.5, 1.5));
        REF_TASKS.add(new Task(3, "THREE", 5, 4, 12));
    }

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
        Estimate estimate = TaskEstimationService.calcTaskEstimate(REF_INPUT_ML, REF_INPUT_BC, REF_INPUT_WC);
        Assert.assertEquals(estimate.getDays(), REF_ESTIMATE.getDays(), DELTA);
        Assert.assertEquals(estimate.getStDeviation(), REF_ESTIMATE.getStDeviation(), DELTA);
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

        // Correct input
        Estimate estimate = TaskEstimationService.combineEstimates(REF_TASKS);
        Assert.assertEquals(estimate.getDays(), REF_ESTIMATE_TOTAL.getDays(), DELTA);
        Assert.assertEquals(estimate.getStDeviation(), REF_ESTIMATE_TOTAL.getStDeviation(), DELTA);
    }

    /* Debug: Run all tests */
    /*
    public static void main(String[] args) {
        calcTaskEstimateTest();
        combineEstimatesTest();
    }
    */
}
