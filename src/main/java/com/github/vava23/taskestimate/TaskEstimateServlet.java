package com.github.vava23.taskestimate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class TaskEstimateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        resp.setContentType("text/html");
        try {
            // Fetch params
            String pExpectedtCase = req.getParameter("expectedcase");
            String pBestCase = req.getParameter("bestcase");
            String pWorstCase = req.getParameter("worstcase");
            boolean emptyParams = pWorstCase == null || pExpectedtCase == null || pBestCase == null;
            if (!emptyParams) {
                emptyParams = pWorstCase.isEmpty() && pExpectedtCase.isEmpty() && pBestCase.isEmpty();
            }

            // Parse the input values
            double worstCase = 0;
            double expectedCase = 0;
            double bestCase = 0;
            boolean correctParams = (!emptyParams);
            if (!emptyParams) {
                correctParams = true;
                try {
                    worstCase = Double.parseDouble(pWorstCase);
                    expectedCase = Double.parseDouble(pExpectedtCase);
                    bestCase = Double.parseDouble(pBestCase);
                }
                catch (NumberFormatException ex) {
                    correctParams = false;
                }
            }

            // Print a sample page
            PrintWriter out = resp.getWriter();
            out.println("<!DOCTYPE html><html>");
            out.println("<head>");
            out.println("<title>Task Estimate</title>");
            out.println("</head>");
            out.println("<body bgcolor=#f0f8ff>");
            out.println("<h1>This is the Task Estimate Tool</h1>");
            // TODO: add some description

            // Input form
            out.println("<form action=\"\" method=POST>");
            out.println("Most likely completion time estimate, days:");
            out.println("<br>");
            out.println("<input type=text name=expectedcase>");
            out.println("<br><br>");
            out.println("Completion time estimate in the best case (optimistic), days:");
            out.println("<br>");
            out.println("<input type=text name=bestcase>");
            out.println("<br><br>");
            out.println("Completion time estimate in the worst case (pessimistic), days:");
            out.println("<br>");
            out.println("<input type=text name=worstcase>");
            out.println("<br><br>");
            out.println("<input type=submit>");

            // Results
            if (!emptyParams) {
                out.println("<br>");
                out.println("<h2>Result:</h2>");
                if (correctParams) {
                    Estimate estimate = TaskEstimationPERT.calcTaskEstimate(bestCase, worstCase, expectedCase);
                    out.printf("<b>Expected completion time</b>: %.1f days\n", estimate.getDays());
                    out.println("<br>");
                    out.printf("<b>Accuracy</b>: ±%.1f days\n", estimate.getStDeviation());
                    out.println("<br>");
                    out.println("<h2>Input params were:</h2>");
                    out.println("<b>Most likely</b>: " + expectedCase + " days");
                    out.println("<br>");
                    out.println("<b>Best case</b>: " + bestCase + " days");
                    out.println("<br>");
                    out.println("<b>Worst case</b>: " + worstCase + " days");
                }
                else {
                    out.println("<i>Sorry, the input params were incorrect</i>");
                }
            }

            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
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
