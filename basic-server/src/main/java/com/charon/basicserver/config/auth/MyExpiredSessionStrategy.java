package com.charon.basicserver.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: SpringSecurity
 * @description  自定义一个session被下线(超时)之后的处理策略
 * @author: charon
 * @create: 2021-04-08 22:48
 **/
public class MyExpiredSessionStrategy implements SessionInformationExpiredStrategy {

    private static ObjectMapper objectMapper = new ObjectMapper();

    // 页面跳转的处理逻辑
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        // 方法一
        /* // 是跳转html页面，url代表跳转的地址
        redirectStrategy.sendRedirect(sessionInformationExpiredEvent.getRequest(),
                sessionInformationExpiredEvent.getResponse(), "某个url");*/

        // 方法二
        Map<String,Object> map  = new HashMap<>();
        map.put("code",403);
        map.put("msg","您已经在另外一台电脑或浏览器登录，被迫下线！"+
                sessionInformationExpiredEvent.getSessionInformation().getLastRequest());
        sessionInformationExpiredEvent.getResponse().setContentType("application/json;charset=UTF-8");
        sessionInformationExpiredEvent.getResponse().getWriter().write(
                objectMapper.writeValueAsString(map)
        );
    }
}
