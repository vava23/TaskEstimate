package com.github.vava23.taskestimate;

import java.util.List;

import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.Test;

//
// Task completion estimation based on PERT technique
//
public class TaskEstimationPERTTest {

    private final TaskEstimationPERT estimation = new TaskEstimationPERT();

    @Test
    void calcTaskEstimateTest() {
        // Incorrect input
        boolean thrown = true;
        try {
            estimation.calcTaskEstimate(-1, 1, 1);
            thrown = false;
        }
        catch (IllegalArgumentException e) { }
        try {
            estimation.calcTaskEstimate(1, -1, 1);
            thrown = false;
        }
        catch (IllegalArgumentException e) { }
        try {
            estimation.calcTaskEstimate(1, 1, -1);
            thrown = false;
        }
        catch (IllegalArgumentException e) { }
        Assert.check(thrown);
    }

    @Test
    void combineEstimatesTest() {
        // Null list on input
        boolean thrown = true;
        try {
            estimation.combineEstimates(null);
            thrown = false;
        }
        catch (IllegalArgumentException e) { }
        Assert.check(thrown);
    }

}
