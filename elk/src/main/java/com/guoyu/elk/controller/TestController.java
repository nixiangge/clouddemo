package com.guoyu.elk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {
    @RequestMapping("/thymeleaf")
    public ModelAndView index(ModelAndView model){
        model = new ModelAndView("index","message","成功接收");
       // model.addAttribute("message","成功接收");
        return model;
    }
}
