package com.github.vava23.taskestimate.web;

import com.github.vava23.taskestimate.business.Estimate;
import com.github.vava23.taskestimate.business.TaskEstimationPERT;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;


//
// Main and only servlet, request processing
//
public class TaskEstimateServlet extends HttpServlet {
    private TaskEstimateApplication application;

    @Override
    public void init() {
        // Create the application object
        application = new TaskEstimateApplication(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        // Response headers
        resp.setContentType("text/html;charset=UTF-8");

        try {
            // Fetch params
            String pMostLikelyCase = req.getParameter("mlcase");
            String pBestCase = req.getParameter("bestcase");
            String pWorstCase = req.getParameter("worstcase");
            boolean emptyParams = pWorstCase == null || pMostLikelyCase == null || pBestCase == null;
            if (!emptyParams) {
                emptyParams = pWorstCase.isEmpty() && pMostLikelyCase.isEmpty() && pBestCase.isEmpty();
            }

            // Parse the input values
            double worstCase = 0;
            double mostLikelyCase = 0;
            double bestCase = 0;
            boolean correctParams = (!emptyParams);
            if (!emptyParams) {
                correctParams = true;
                try {
                    worstCase = Double.parseDouble(pWorstCase);
                    mostLikelyCase = Double.parseDouble(pMostLikelyCase);
                    bestCase = Double.parseDouble(pBestCase);
                }
                catch (NumberFormatException ex) {
                    correctParams = false;
                }
            }

            // Perform the calculations
            Estimate estimate = null;
            if (correctParams) {
                estimate = TaskEstimationPERT.calcTaskEstimate(bestCase, worstCase, mostLikelyCase);
            }

            // Get Web Context and pass the estimate to it
            final WebContext webCtx = new WebContext(req, resp, getServletContext());
            webCtx.setVariable("estimate", estimate);

            // Generate the page
            ITemplateEngine templateEngine = application.getTemplateEngine();
            templateEngine.process("home", webCtx, resp.getWriter());
        }
        catch (IOException e) {
            // https://stackoverflow.com/questions/6182771/how-to-properly-handle-exceptions-in-a-jsp-servlet-app
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        doGet(req, resp);
    }
}
