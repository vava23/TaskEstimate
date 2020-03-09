package com.github.vava23.taskestimate.web;

import com.github.vava23.taskestimate.domain.Estimate;
import com.github.vava23.taskestimate.domain.Task;
import com.github.vava23.taskestimate.domain.TaskEstimationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;


/**
 * Main servlet, processing requests for the home page.
 */
public class HomeServlet extends HttpServlet {
  private TaskEstimateApplication application;

  @Override
  public void init() {
    // Create the application object
    application = new TaskEstimateApplication(getServletContext());
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    try {
      // Get attr objects from the request
      Map<Integer, Task> tasks = getTasks(req, false);
      List<String> errors = (List<String>) req.getAttribute("errors");

      // Pass the objects to Thymeleaf context
      final WebContext webCtx = new WebContext(req, resp, getServletContext());
      if (tasks != null  && !tasks.isEmpty()) {
        webCtx.setVariable("tasks", tasks.values());
      }
      webCtx.setVariable("errors", errors);

      // Generate the page
      ITemplateEngine templateEngine = application.getTemplateEngine();
      templateEngine.process("home", webCtx, resp.getWriter());
    } catch (IOException e) {
      // https://stackoverflow.com/questions/6182771/how-to-properly-handle-exceptions-in-a-jsp-servlet-app
      throw new ServletException(e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    doGet(req, resp);
  }

  /**
   * Gets or creates list of task from session.
   */
  public static Map<Integer, Task> getTasks(HttpServletRequest req, boolean createNewList) {
    HttpSession session = req.getSession();
    Object tasksObj = session.getAttribute("tasks");
    if (tasksObj != null) {
      return (Map<Integer, Task>) tasksObj;
    } else {
      if (createNewList) {
        Map<Integer, Task> tasks = new TreeMap<Integer,Task>();
        session.setAttribute("tasks", tasks);
        return tasks;
      } else {
        return null;
      }
    }
  }

  /**
   * Checks the request params for a Task, for create and edit operations.
   */
  public static boolean validateTaskProperties(HttpServletRequest req, List<String> errors) {
    // Fetch params
    String paramTimeMostLikely = req.getParameter("timemostlikely");
    String paramTimeBestCase = req.getParameter("timebestcase");
    String paramTimeWorstCase = req.getParameter("timeworstcase");

    // Check if params are empty
    if ((
        paramTimeWorstCase == null
            || paramTimeMostLikely == null
            || paramTimeBestCase == null)
        ||
        (paramTimeWorstCase.isEmpty()
            && paramTimeMostLikely.isEmpty()
            && paramTimeBestCase.isEmpty())
    ) {
      errors.add("some parameters are missing");
      return false;
    }

    // Try to parse the numeric values
    try {
      double timeMostLikely = Double.parseDouble(paramTimeMostLikely);
      double timeBestCase = Double.parseDouble(paramTimeBestCase);
      double timeWorstCase = Double.parseDouble(paramTimeWorstCase);

      if (timeMostLikely <= 0 || timeBestCase <= 0 || timeWorstCase <= 0) {
        errors.add("time must be a positive number");
      }
    } catch (NumberFormatException e) {
      errors.add("time must be a number");
      return false;
    }

    // No problem found, return true
    return true;
  }

  /**
   * Checks the task id param.
   */
  public static boolean validateTaskId(HttpServletRequest req, List<String> errors) {
    // Fetch param
    String paramId = req.getParameter("taskid");

    // Try to get value
    int id;
    if (paramId == null) {
      return false;
    }
    try {
      id = Integer.parseInt(paramId);
    } catch (NumberFormatException e) {
      return false;
    }

    // Find id in tasks
    Map<Integer, Task> tasks = getTasks(req, false);
    if (tasks == null || ! tasks.containsKey(id)) {
      // No such task
      return false;
    }

    return true;
  }
}
