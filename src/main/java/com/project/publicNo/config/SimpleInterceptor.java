package com.project.publicNo.config;

import com.alibaba.fastjson.JSON;
import com.project.publicNo.pojo.Response;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class SimpleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();
        if (path.equals("/api/completeUserInfo") || path.equals("/api/reAddArticle") || path.equals("/api/delArticle") || path.equals("/api/addArticle")) {
            try {
                Cookie[] cookies = request.getCookies();
                String userId = "";
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals("uid")) {
                        userId = cookies[i].getValue();
                        break;
                    }
                }
                if ("".equals(userId)) {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    String json = JSON.toJSONString(new Response(false, "进行此操作,请先登录!"));
                    writer.write(json);
                    writer.flush();
                    writer.close();
                    return false;
                }
                if (!userId.equals(request.getParameter("userId"))) {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    String json = JSON.toJSONString(new Response(false, "请务操作他人文章!"));
                    writer.write(json);
                    writer.flush();
                    writer.close();
                    return false;
                }
                String token = "";
                for (int i = 0; i < cookies.length; i++) {
                    if (userId.equals(cookies[i].getName())) {
                        token = cookies[i].getValue();
                        break;
                    }
                }
                if ("".equals(token)) {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    String json = JSON.toJSONString(new Response(false, "令牌丢失,请重新授权登录!"));
                    writer.write(json);
                    writer.flush();
                    writer.close();
                    return false;
                }
                String openId = (String) request.getSession().getAttribute(userId);
                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                BCryptPasswordEncoder passwordEncoder = (BCryptPasswordEncoder) factory.getBean("bCryptPasswordEncoder");
                if (!passwordEncoder.matches(new String(openId + userId), token)) {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    String json = JSON.toJSONString(new Response(false, "令牌非法,请重新登录确认!"));
                    writer.write(json);
                    writer.flush();
                    writer.close();
                    return false;
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
