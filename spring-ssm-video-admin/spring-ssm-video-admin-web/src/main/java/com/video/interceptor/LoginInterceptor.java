package com.video.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 登陆拦截
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    // 不需要验证的路由
    private List<String> unCheckUrls;

    public List<String> getUnCheckUrls() {
        return unCheckUrls;
    }

    public void setUnCheckUrls(List<String> unCheckUrls) {
        this.unCheckUrls = unCheckUrls;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI().toString();
        if (unCheckUrls.contains(url)){
            return true;
        }
        if (request.getSession().getAttribute("session_user") != null){
            return true;
        }
        response.sendRedirect("/login.action");
        return false;
    }
}
