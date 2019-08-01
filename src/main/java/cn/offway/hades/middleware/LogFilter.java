package cn.offway.hades.middleware;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class LogFilter extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(httpServletRequest.getRequestURI());
        logger.info("RequestURI:" + httpServletRequest.getRequestURI());
        System.out.println(httpServletRequest.getRequestURL().toString());
        logger.info("RequestURL:" + httpServletRequest.getRequestURL().toString());
        System.out.println(httpServletRequest.getQueryString());
        logger.info("QueryString:" + httpServletRequest.getQueryString());
        if (httpServletRequest.getUserPrincipal() != null) {
            System.out.println(httpServletRequest.getUserPrincipal().getName());
            logger.info("UserPrincipal:" + httpServletRequest.getUserPrincipal().getName());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(httpServletRequest.getParameterMap());
        logger.info("ParameterMap:" + jsonObject.toJSONString());
        logger.info("DateTime:" + new Date());
        for (String k : httpServletRequest.getParameterMap().keySet()) {
            System.out.print(k);
            System.out.print("=");
            for (String v : httpServletRequest.getParameterMap().get(k)) {
                System.out.print(v);
            }
            System.out.print("\n");
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
