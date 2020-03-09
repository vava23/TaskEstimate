package com.github.vava23.taskestimate.web;

import com.github.vava23.taskestimate.domain.Estimate;
import com.github.vava23.taskestimate.domain.Task;
import com.github.vava23.taskestimate.domain.TaskEstimationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

  /**
   * Checks the request params.
   */
  protected boolean validateRequestParams(HttpServletRequest req, List<String> errors) {
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
    } catch (NumberFormatException ex) {
      errors.add("time must be a number");
      return false;
    }

    // No problem found, return true
    return true;
  }

  /**
   * Gets or creates list of task from session.
   */
  private List<Task> getTasks(HttpServletRequest req, boolean createNewList) {
    HttpSession session = req.getSession();
    Object tasksObj = session.getAttribute("tasks");
    if (tasksObj != null) {
      return (List<Task>) tasksObj;
    } else {
      if (createNewList) {
        List<Task> tasks = new ArrayList<Task>();
        session.setAttribute("tasks", tasks);
        return tasks;
      } else {
        return null;
      }
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    try {
      // Get attr objects from the request
      List<Task> tasks = getTasks(req, false);
      List<String> errors = (List<String>) req.getAttribute("errors");

      // Pass the objects to Thymeleaf context
      final WebContext webCtx = new WebContext(req, resp, getServletContext());
      webCtx.setVariable("tasks", tasks);
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
    List<String> errors = new ArrayList<String>();
    List<Task> tasks = null;

    // Check the params
    boolean correctParams = validateRequestParams(req, errors);
    if (correctParams) {
      // Parse the input values
      String taskname = req.getParameter("taskname");
      double timeMostLikely = Double.parseDouble(req.getParameter("timemostlikely"));
      double timeBestCase = Double.parseDouble(req.getParameter("timebestcase"));
      double timeWorstCase = Double.parseDouble(req.getParameter("timeworstcase"));

      // Create the new task and ad it to the list
      long id = newTaskId(req);
      Task newTask = TaskEstimationService.newTask(
          id, taskname, timeMostLikely, timeBestCase, timeWorstCase, errors);
      if (newTask != null) {
        // Get the task list
        tasks = getTasks(req, true);
        tasks.add(newTask);
      }
    }

    // If there were errors, pass them to the request
    if (!errors.isEmpty()) {
      req.setAttribute("errors", errors);
    }

    doGet(req, resp);
  }

  private long newTaskId(HttpServletRequest req) {
    List<Task> tasks = getTasks(req, false);
    if (tasks != null) {
      return tasks.size() + 1;
    } else {
      return 1;
    }
  }
}
