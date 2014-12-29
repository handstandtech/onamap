package net.onamap.server.guice;

import com.google.inject.servlet.GuiceFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

public class GaeSafeGuiceFilter extends GuiceFilter {

    private static final Pattern ahPattern = Pattern.compile("/_ah/.*");
    private static final Pattern assetsPattern = Pattern.compile("/assets/.*");
    private static final Pattern apiConsolePattern = Pattern.compile("/api-console/.*");

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        if (apiConsolePattern.matcher(request.getRequestURI()).matches()) {
            chain.doFilter(request, response);
            return;
        }
        if (assetsPattern.matcher(request.getRequestURI()).matches()) {
            chain.doFilter(request, response);
            return;
        }
        if (ahPattern.matcher(request.getRequestURI()).matches() &&
                !request.getRequestURI().equals("/_ah/warmup")) {
            chain.doFilter(request, response);
            return;
        }

        super.doFilter(request, response, chain);
    }
}