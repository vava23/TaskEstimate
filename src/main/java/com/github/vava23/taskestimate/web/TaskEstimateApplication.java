package com.github.vava23.taskestimate.web;

import javax.servlet.ServletContext;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * Application-level logic.
 */
public class TaskEstimateApplication {
  private TemplateEngine templateEngine;

  public ITemplateEngine getTemplateEngine() {
    return this.templateEngine;
  }

  /**
   * Constructor.
   */
  public TaskEstimateApplication(ServletContext ctx) {
    if (ctx == null) {
      throw new IllegalArgumentException();
    }

    // Create and initialize template resolver for template engine
    ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(ctx);

    // HTML is the default mode, but we will set it anyway for better understanding of code
    templateResolver.setTemplateMode(TemplateMode.HTML);
    // This will convert "home" to "/WEB-INF/templates/home.html"
    templateResolver.setPrefix("/WEB-INF/templates/");
    templateResolver.setSuffix(".html");

    // Cache is set to true by default. Set to false if you want templates to
    // be automatically updated when modified.
    templateResolver.setCacheable(true);

    // Ititialize the template engine
    templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
  }
}
