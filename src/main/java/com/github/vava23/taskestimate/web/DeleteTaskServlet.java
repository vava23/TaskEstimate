package com.github.vava23.taskestimate.web;

import com.github.vava23.taskestimate.domain.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteTaskServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      List<String> errors = new ArrayList<String>();

      // Check the params
      boolean correctId = HomeServlet.validateTaskId(req, errors);
      if (correctId) {
        // Delete task by Id
        int taskId = Integer.parseInt(req.getParameter("taskid"));
        Map<Integer, Task> tasks = HomeServlet.getTasks(req, false);
        if (tasks != null) {
          tasks.remove(taskId);
        }
      }

      // Redirect back
      resp.sendRedirect(req.getContextPath() + "/");
    } catch (IOException e) {
      // https://stackoverflow.com/questions/6182771/how-to-properly-handle-exceptions-in-a-jsp-servlet-app
      throw new ServletException(e);
    }
  }
}
