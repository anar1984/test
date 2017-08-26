/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import auth.SessionHandler;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Azerbaycan
 */
@WebFilter(filterName = "url-filter", urlPatterns = {"/*"}, asyncSupported = true)
public class AcnisUrlFilter implements Filter {
    
    private static Logger logger = LogManager.getLogger();

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String url = request.getRequestURI();
        System.out.println("url->>>> " + url);
        
        String token = null;
        for (Cookie cookie : request.getCookies() ) {
            if ("apdtok".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }
        
        System.out.println("web service token->>" + token);  
        

        if (url.trim().length() == 0 || url.trim().equals("/apd1/") || url.trim().equals("/")) {
            response.sendRedirect(request.getContextPath() + "/index.html");
        } else if (!SessionHandler.checkSession(token) && !url.contains("resource")
                && !url.contains("login")
                && !url.contains("index.html")
                && !url.contains("signup")
                && !url.contains("/nali/")
                && !url.contains("/nasrv/")
                && !url.contains("/activation.html")) {
            response.sendRedirect(request.getContextPath() + "/login.html");
        } else {
            response.addHeader("Access-Controll-Allow-Origin", "*");
            chain.doFilter(req, res);
        }
        
        
    }

    @Override
    public void destroy() {

    }
}
