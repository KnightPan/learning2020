package com.stoneknife.mvc.controller;

import com.stoneknife.mvc.annotation.Controller;
import com.stoneknife.mvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/stoneknife")
public class LoginController {

    @RequestMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response, String pass){
        try {
            response.getWriter().write(request.getParameter("username"));
            response.getWriter().write(pass);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
