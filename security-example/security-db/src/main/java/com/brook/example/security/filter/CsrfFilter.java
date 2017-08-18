package com.brook.example.security.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/8/18
 */
public interface CsrfFilter {

  @Log4j2
  class AfterCsrfFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest,
        ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
      log.info("This is a filter after CsrfFilter.");
      filterChain.doFilter(servletRequest, servletResponse);
    }

  }

  @Log4j2
  class BeforeLoginFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest,
        ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
      log.info("This is a filter before UsernamePasswordAuthenticationFilter.");
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }
}
