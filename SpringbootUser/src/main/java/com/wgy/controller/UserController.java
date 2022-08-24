package com.wgy.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.wgy.po.ResultInfo;
import com.wgy.po.User;
import com.wgy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/user")
public class UserController {

    String oldUserName;
    @Resource
    private UserService userService;

    /**
     * 添加用户
     * @param userName
     * @param userPwd
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(String userName, String userPwd){
        oldUserName = userName;
        System.out.println("olduserName= "+oldUserName);
        User user = new User(null, userName, userPwd);
        System.out.println(user.toString());
        return userService.addUser(user);
    }

    @RequestMapping(value = "add2", method = RequestMethod.POST)
    public void addUser2(@RequestParam User user){
        userService.addUser(user);
    }

    @RequestMapping(value = "/add3", method = RequestMethod.POST)

    public ResultInfo addUser3(HttpServletRequest request) {

        System.out.println(request);
        String userName = request.getParameter("username");
        String userPwd = request.getParameter("userpwd");
        User user = new User(null, userName, userPwd);
        return userService.addUser(user);

    }
    /**
     * 通过用户名查询用户信息
     */
    @GetMapping("query")
    @ResponseBody
    public User queryUserByName(String userName){
        return userService.queryUserByName(userName);
    }

    /**
     * 登录
     */
    @RequestMapping("login2")
    @ResponseBody
    public ResultInfo login(String userName, String userPwd){
        oldUserName = userName;
        System.out.println("olduserName= "+oldUserName);
        System.out.println(userService.login(userName, userPwd));
        return userService.login(userName, userPwd);
    }

    @GetMapping("register")
    @ResponseBody
    public ResultInfo register(String userName, String userPwd){
        oldUserName = userName;
        User user = new User(null, userName, userPwd);
        return userService.register(user);
    }

    @PostMapping("updateByName")
    @ResponseBody
    public ResultInfo updateUserByUserName(String newName, String newPwd){
        User user = new User(null, newName, newPwd);
        System.out.println("update  olduserName= "+oldUserName);
        return userService.updateUserInfoByName(this.oldUserName, user);
    }
    
    @RequestMapping("deleteUser")
    public void deleteUserByUserName(){
        userService.deleteUserName(this.oldUserName);
    }
}
