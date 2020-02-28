package com.video.interceptor;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.utils.JwtTokenUtils;
import com.video.utils.Response;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 拦截器
 * 端 user 模块 api 拦截器
 */
public class MiniInterceptor extends HandlerInterceptorAdapter {

    private final String USER_TOKEN_KEY = "user-redis-token:";// 用户 key

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StringUtils.isNotBlank(token)){
            Claims claims = JwtTokenUtils.getInstance().check(token);
            if (claims != null){
                String userId = (String) claims.get("id");
                if (userId != null){
                    String userToken = stringRedisTemplate.boundValueOps(USER_TOKEN_KEY + userId).get();
                    if (userToken != null){
                        if (StringUtils.equals(token,userToken)){
                            // 登陆成功
                            return true;
                        }
                        returnJson(response,"账号被挤下线");
                        return false;
                    }
                }
                returnJson(response,"登陆异常");
                return false;
            }
        }
        returnJson(response,"请先登陆");
        return false;
    }

    /**
     * 返回 json 格式数据 , -1 表示未登录
     */
    private void returnJson(HttpServletResponse response,String message) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(Response.error(-1,message)));
    }
}
