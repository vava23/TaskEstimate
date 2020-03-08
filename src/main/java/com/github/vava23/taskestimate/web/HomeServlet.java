package com.github.vava23.taskestimate.web;

import com.github.vava23.taskestimate.domain.Estimate;
import com.github.vava23.taskestimate.domain.TaskEstimationService;

import java.io.IOException;

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

  protected boolean validateRequestParams(HttpServletRequest req) {
    // Fetch params
    String paramMostLikelyCase = req.getParameter("mlcase");
    String paramBestCase = req.getParameter("bestcase");
    String paramWorstCase = req.getParameter("worstcase");

    // Check if params are empty
    if (paramWorstCase == null || paramMostLikelyCase == null || paramBestCase == null) {
      return false;
    }
    if (paramWorstCase.isEmpty() && paramMostLikelyCase.isEmpty() && paramBestCase.isEmpty()) {
      return false;
    }

    // Try to parse the numeric values
    try {
      Double.parseDouble(paramMostLikelyCase);
      Double.parseDouble(paramBestCase);
      Double.parseDouble(paramWorstCase);
    } catch (NumberFormatException ex) {
      return false;
    }

    // No problem found, return true
    return true;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    try {
      Estimate estimate = null;
      // Check the params
      boolean correctParams = validateRequestParams(req);
      if (correctParams) {
        // Parse the input values
        double mostLikelyCase = Double.parseDouble(req.getParameter("mlcase"));
        double bestCase = Double.parseDouble(req.getParameter("bestcase"));
        double worstCase = Double.parseDouble(req.getParameter("worstcase"));

        // Perform the calculations
        estimate = TaskEstimationService.calcTaskEstimate(mostLikelyCase, bestCase, worstCase);
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
