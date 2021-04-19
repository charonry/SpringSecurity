package com.charon.basicserver.controller;

import com.charon.basicserver.model.CaptchaImageVO;
import com.charon.basicserver.utils.MyContants;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-19 22:32
 **/
@RestController
public class CaptchaController {

    @Resource
    DefaultKaptcha captchaProducer;

    @RequestMapping(value = "/kaptcha", method = RequestMethod.GET)
    public void kaptcha(HttpSession session, HttpServletResponse response) throws Exception {

        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = captchaProducer.createText();
        CaptchaImageVO captchaImageVO = new CaptchaImageVO(capText,2 * 60);
        //将验证码存到session
        session.setAttribute(MyContants.CAPTCHA_SESSION_KEY, captchaImageVO);

        //将图片返回给前端;使用try-with-resources不用手动关闭流
        try(ServletOutputStream out = response.getOutputStream();) {
            BufferedImage bi = captchaProducer.createImage(capText);
            ImageIO.write(bi, "jpg", out);
            out.flush();
        }
    }


}
