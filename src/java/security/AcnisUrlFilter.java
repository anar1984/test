/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import auth.SessionHandler;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Azerbaycan
 */
@WebFilter(filterName = "url-filter", urlPatterns = {"/*"}, asyncSupported = true)
public class AcnisUrlFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String url = request.getRequestURI();
        System.out.println("url->>>> " + url);
        String cookie = request.getHeader("Cookie");
        System.out.println("web service cookie->>" + cookie);  
        

        if (!SessionHandler.checkSession(cookie) && !url.contains("resource") 
                && !url.contains("login") 
                && !url.contains("home.html") 
                && !url.contains("signup") 
                && !url.contains("/nali/")
                && !url.contains("/nasrv/")
                && !url.contains("/activation.html")) {
            response.sendRedirect(request.getContextPath()+"/login.html");
        } else {
            response.addHeader("Access-Controll-Allow-Origin", "*");
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {

    }
}
