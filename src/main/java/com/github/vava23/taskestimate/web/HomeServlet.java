package com.github.vava23.taskestimate.web;

import com.github.vava23.taskestimate.domain.Estimate;
import com.github.vava23.taskestimate.domain.TaskEstimationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

  protected boolean validateRequestParams(HttpServletRequest req, List<String> errors) {
    // Fetch params
    String paramTimeMostLikely = req.getParameter("timeostlikely");
    String paramTimeBestCase = req.getParameter("timebestcase");
    String paramTimeWorstCase = req.getParameter("timeworstcase");

    // Check if params are empty
    if ((paramTimeWorstCase == null || paramTimeMostLikely == null || paramTimeBestCase == null) ||
    (paramTimeWorstCase.isEmpty() && paramTimeMostLikely.isEmpty() && paramTimeBestCase.isEmpty())) {
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

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    List<String> errors = new ArrayList<String>();
    try {
      Estimate estimate = null;
      // Check the params
      boolean correctParams = validateRequestParams(req, errors);
      if (correctParams) {
        // Parse the input values
        double timeMostLikely = Double.parseDouble(req.getParameter("timeostlikely"));
        double timeBestCase = Double.parseDouble(req.getParameter("timebestcase"));
        double timeWorstCase = Double.parseDouble(req.getParameter("timeworstcase"));

        // Perform the calculations
        estimate = TaskEstimationService.calcTaskEstimate(timeMostLikely, timeBestCase, timeWorstCase);
      }

      // Pass the estimate to Thymeleaf context
      final WebContext webCtx = new WebContext(req, resp, getServletContext());
      webCtx.setVariable("estimate", estimate);

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
}
