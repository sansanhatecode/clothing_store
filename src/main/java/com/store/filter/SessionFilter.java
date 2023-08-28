package com.store.filter;

import com.store.util.SessionUtil;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Component
public class SessionFilter implements  Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest =(HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        SessionUtil.validateCart(session);
        chain.doFilter(request,response);



    }
}

