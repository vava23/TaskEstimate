package com.github.vava23.taskestimate.web;

import javax.servlet.*;
import java.io.IOException;
import java.nio.charset.Charset;

public class EncodingFilter implements Filter {
  private String encoding = "UTF-8";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    String pEncoding = filterConfig.getInitParameter("encoding");
    if (pEncoding != null) {
      encoding = pEncoding;
    }
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
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
