package com.github.vava23.taskestimate.web;

import com.github.vava23.taskestimate.domain.Task;
import com.github.vava23.taskestimate.domain.TaskEstimationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddTaskServlet extends HttpServlet {

  /**
   * Unique ID for a new task.
   */
  private long newTaskId(HttpServletRequest req) {
    List<Task> tasks = HomeServlet.getTasks(req, false);
    if (tasks != null) {
      return tasks.size() + 1;
    } else {
      return 1;
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      List<String> errors = new ArrayList<String>();
      List<Task> tasks = null;

      // Check the params
      boolean correctParams = HomeServlet.validateTaskParams(req, errors);
      if (correctParams) {
        // Parse the input values
        String taskname = req.getParameter("taskname");
        double timeMostLikely = Double.parseDouble(req.getParameter("timemostlikely"));
        double timeBestCase = Double.parseDouble(req.getParameter("timebestcase"));
        double timeWorstCase = Double.parseDouble(req.getParameter("timeworstcase"));

        // Create the new task and add it to the list
        long id = newTaskId(req);
        Task newTask = TaskEstimationService.newTask(
            id, taskname, timeMostLikely, timeBestCase, timeWorstCase, errors);
        if (newTask != null) {
          // Get the task list
          tasks = HomeServlet.getTasks(req, true);
          tasks.add(newTask);
        }
      }

      // If there were errors, pass them to the request
      if (!errors.isEmpty()) {
        req.setAttribute("errors", errors);
        req.getRequestDispatcher(req.getContextPath()).forward(req, resp);
      } else {
        // Everything is OK, redirect back
        resp.sendRedirect(req.getContextPath() + "/");
      }
    } catch (IOException e) {
      // https://stackoverflow.com/questions/6182771/how-to-properly-handle-exceptions-in-a-jsp-servlet-app
      throw new ServletException(e);
    }
  }
}
