package no.kristiania.pgr209.iseekyou.server;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageServerFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(MessageServerFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        logger.debug("Request {} {}", req.getMethod(), req.getRequestURI());

        filterChain.doFilter(request, response);
    }
}
