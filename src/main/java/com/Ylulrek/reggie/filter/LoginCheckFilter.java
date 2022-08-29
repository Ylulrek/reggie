package com.Ylulrek.reggie.filter;

import com.Ylulrek.reggie.common.BaseContext;
import com.Ylulrek.reggie.common.R;
import com.Ylulrek.reggie.domain.Employee;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    
    //用来路经比对，路径匹配器
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;


        //1,
        String requestURI=request.getRequestURI();
        log.info("拦截到请求:{}",requestURI);
        String[] urls=new String[]{
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**",
            "/common/**",
            "/user/sendMsg",
            "/user/login"
        };
        //2,
        boolean check = check(requestURI, urls);
        //3,判断是否需要拦截
        if (check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4-1,判断是否登录，已登陆
        if (request.getSession().getAttribute("employee")!=null){
            Long empId= (Long) request.getSession().getAttribute("employee");
            log.info("{}用户已登录",empId);

            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }
        //4-2,判断是否登录，已登陆
        if (request.getSession().getAttribute("user")!=null){
            Long userId= (Long) request.getSession().getAttribute("user");
            log.info("{}用户已登录",userId);

            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }

        //5,未登录，通过输出流方式响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }
    
    public boolean check(String requestURI,String[] urls){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
