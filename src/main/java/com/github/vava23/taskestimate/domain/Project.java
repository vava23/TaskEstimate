package com.github.vava23.taskestimate.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Project that consists of tasks, for future use.
 */
public class Project {
  private String name;
  /** List of tasks. */
  private List<Task> tasks = new ArrayList<Task>();
  /** Combined expected time estimate for the project. */
  private Estimate estimate;

  public String getName() {
    return name;
  }

  public Estimate getEstimate() {
    return estimate;
  }

  /** Sets the project name with some basic checks. */
  public void setName(String name) {
    if (name != null && !name.isEmpty()) {
      this.name = name;
    }
  }

  /**
   * Updates the state, recalcs parameters.
   */
  private void update() {
    // Calc the time estimate
    estimate = calcEstimate();
  }

  /**
   * Constructor.
   */
  public Project(String aName) {
    if (aName == null || aName.isEmpty()) {
      name = "New Project";
    } else {
      name = aName;
    }
  }

  /**
   * Tasks (original task list must remain unmodified).
   */
  public Task[] getTasks() {
    return tasks.toArray(new Task[0]);
  }

  /**
   * Adds a task to project.
   */
  public void addTask(Task aTask) {
    if (aTask != null) {
      tasks.add(aTask);
      update();
    }
  }

  /**
   * Removes task from the project.
   */
  public void removeTask(Task aTask) {
    if (aTask != null) {
      tasks.remove(aTask);
      update();
    }
  }

  /**
   * Calculates the combined time estimate for all tasks in the project.
   */
  private Estimate calcEstimate() {
    // Check state
    if (tasks == null) {
      throw new IllegalStateException("Trying to estimate time for the NULL task list");
    }
    if (tasks.isEmpty()) {
      return new Estimate(0, 0);
    }

    // Sum up the time and stDevs for all tasks
    double time = 0;
    double stDevSqSum = 0;
    for (Task t: tasks) {
      time += t.getEstimate().getDays();
      stDevSqSum += Math.pow(t.getEstimate().getStDeviation(), 2);
    }

    // Calc the result
    double stDev = Math.sqrt(stDevSqSum);
    return new Estimate(time, stDev);
  }
}
