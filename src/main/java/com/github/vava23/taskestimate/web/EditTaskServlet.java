package com.github.vava23.taskestimate.web;

import com.github.vava23.taskestimate.domain.Task;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditTaskServlet extends HttpServlet {
  private TemplateEngine templateEngine;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      // TODO: may be null on Id validation
      List<String> errors = (List<String>) req.getAttribute("errors");

      if (HomeServlet.validateTaskId(req, errors)) {
        // Get the task object
        int id = Integer.parseInt(req.getParameter("taskid"));
        Map<Integer, Task> tasks = HomeServlet.getTasks(req, false);
        if (tasks != null) {
          Task task = tasks.get(id);
          // Pass the objects to Thymeleaf context
          WebContext webCtx = new WebContext(req, resp, getServletContext());
          webCtx.setVariable("task", task);
          webCtx.setVariable("errors", errors);

          // Generate the page
          templateEngine.process("edittask", webCtx, resp.getWriter());
          return;
        }
      }
      // If haven't returned earlier, redirect to main page
      resp.sendRedirect(req.getContextPath() + "/");
    } catch (IOException e) {
      throw new ServletException(e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    List<String> errors = new ArrayList<String>();

    if (HomeServlet.validateTaskProperties(req, errors)) {
      // Parse the inp ut values
      String taskName = req.getParameter("taskname");
      double timeMostLikely = Double.parseDouble(req.getParameter("timemostlikely"));
      double timeBestCase = Double.parseDouble(req.getParameter("timebestcase"));
      double timeWorstCase = Double.parseDouble(req.getParameter("timeworstcase"));
      int id = Integer.parseInt(req.getParameter("taskid"));

      // Get the task and update it
      Map<Integer, Task> tasks = HomeServlet.getTasks(req, false);
      if (tasks != null) {
        Task task = tasks.get(id);
        // Update the task
        task.setName(taskName);
        task.setTime(timeMostLikely, timeBestCase, timeWorstCase);
        // Return to main page
        resp.sendRedirect(req.getContextPath() + "/");
      } else {
        // If params are invalid, pass the errors
        if (!errors.isEmpty()) {
          req.setAttribute("errors", errors);
          doGet(req, resp);
        }
      }
    }
  }

  @Override
  public void init() {
    // Store template engine in servlet context
    this.templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
  }
}
