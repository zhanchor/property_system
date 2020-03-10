package com.zh.property.interceptor;

import com.zh.property.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,Object o)throws Exception{
        HttpSession session=httpServletRequest.getSession();
        String contextPath=session.getServletContext().getContextPath();
        String[] requireAuthPages=new String[]{
                "admin",
        };
        String uri=httpServletRequest.getRequestURI();
        uri= StringUtils.remove(uri,contextPath+"/");
        String page=uri;
        System.out.println(uri);

        if(begingWith(page,requireAuthPages)){
            Subject subject = SecurityUtils.getSubject();
            if(!subject.isAuthenticated()) {
                httpServletResponse.sendRedirect("login");
                return false;
            }
            User user=(User)session.getAttribute("user");
            if(0==user.getAdmin()){
                httpServletResponse.sendRedirect("workorder");
                return false;
            }
        }

        return true;
    }

    private boolean begingWith(String page,String[] requiredAuthPages){
        boolean result=false;
        for(String requiredAuthPage:requiredAuthPages){
            if(StringUtils.startsWith(page,requiredAuthPage)){
                result=true;
                break;
            }
        }
        return result;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)throws Exception{}

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,Object o,Exception e)throws Exception{}
}
