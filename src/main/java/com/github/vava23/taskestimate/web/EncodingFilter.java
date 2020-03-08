package com.github.vava23.taskestimate.web;

import java.io.IOException;
import javax.servlet.*;

public class EncodingFilter implements Filter {
  private String encoding = "UTF-8";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    String paramEncoding = filterConfig.getInitParameter("encoding");
    if (paramEncoding != null) {
      encoding = paramEncoding;
    }
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest,
      ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    // Charset for the request
    servletRequest.setCharacterEncoding(encoding);
    // Content type with charset for the response
    servletResponse.setContentType("text/html;charset=" + encoding);

    // Pass the request
    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy() {

  }
}
