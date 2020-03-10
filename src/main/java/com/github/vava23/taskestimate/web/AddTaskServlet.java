package com.github.vava23.taskestimate.web;

import com.github.vava23.taskestimate.domain.Task;
import com.github.vava23.taskestimate.domain.TaskEstimationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddTaskServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      List<String> errors = new ArrayList<String>();

      // Check the params
      boolean correctParams = HomeServlet.validateTaskProperties(req, errors);
      if (correctParams) {
        // Parse the input values
        String taskname = req.getParameter("taskname");
        double timeMostLikely = Double.parseDouble(req.getParameter("timemostlikely"));
        double timeBestCase = Double.parseDouble(req.getParameter("timebestcase"));
        double timeWorstCase = Double.parseDouble(req.getParameter("timeworstcase"));

        // Create the new task
        Task newTask = TaskEstimationService.newTask(
            taskname, timeMostLikely, timeBestCase, timeWorstCase, errors);
        // Store the task
        if (newTask != null) {
          int id = newTask.getId();
          Map<Integer, Task> tasks = HomeServlet.getTasks(req, true);
          tasks.put(id, newTask);
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
