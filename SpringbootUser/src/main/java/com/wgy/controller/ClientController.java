package com.wgy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ClientController {

    @RequestMapping("helloTest")
    public String helloTest(HttpServletRequest request){
        request.setAttribute("msg","hlosfsdfsfsdf");
        return "index";
    }
}
