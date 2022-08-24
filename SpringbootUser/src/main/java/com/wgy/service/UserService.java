package com.wgy.service;

import com.wgy.dao.UserDao;
import com.wgy.po.ResultInfo;
import com.wgy.po.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    /**
     * 添加用户
     * @param user
     */
    public ResultInfo addUser(User user){
       User userTemp = queryUserByName(user.getUserName());
        ResultInfo resultInfo = new ResultInfo();
        if(userTemp != null){
            resultInfo.setState(200);
            resultInfo.setRetInfo("该用户名已存在，请重新换用户名");
            return resultInfo;
        }
        else {
            userDao.addUser(user);
            resultInfo.setState(500);
            resultInfo.setRetInfo("注册成功");
            return resultInfo;
        }
    }
    public void deleteUserName(String userName){
        userDao.deleteUserByName(userName);
    }
    /**
     * 通过用户名查询用户信息
     * @param userName
     * @return
     */
    public User queryUserByName(String userName){
        return userDao.queryUserByName(userName);
    }


    public ResultInfo updateUserInfoByName(String userName, User user){
        ResultInfo resultInfo = new ResultInfo();
        if(user.getUserName()==null){
            resultInfo.setState(200);
            resultInfo.setRetInfo("用户名不能为空！");
        }
        else if(user.getUserPwd()==null){
            resultInfo.setState(200);
            resultInfo.setRetInfo("密码不能为空！");
        }
        else{
            userDao.updateUserInfoByName(userName, user);
            resultInfo.setState(500);
            resultInfo.setRetInfo("恭喜你修改信息成功！");
        }
        return resultInfo;
    }
    /**
     * 登录
     * @return
     */
    public ResultInfo login(String userName, String userPwd){
            ResultInfo resultInfo = new ResultInfo();
            User user = userDao.queryUserByName(userName);
            if(user == null){
                resultInfo.setState(200);
                resultInfo.setRetInfo("账号错误，请重新输入账号");
            }
            else if(user.getUserPwd().equals(userPwd)){
                resultInfo.setState(500);
                resultInfo.setRetInfo("登录成功！");
            }
            else{
                resultInfo.setState(200);
                resultInfo.setRetInfo("密码错误，请重新输入密码");
            }
            return resultInfo;
    }

    public ResultInfo register(User user) {
        ResultInfo resultInfo = new ResultInfo();
        if(user.getUserName()==null){
            resultInfo.setState(200);
            resultInfo.setRetInfo("用户名不能为空！");
        }
        else if(user.getUserPwd()==null){
            resultInfo.setState(200);
            resultInfo.setRetInfo("密码不能为空！");
        }
        else{
            userDao.addUser(user);
            resultInfo.setState(200);
            resultInfo.setRetInfo("恭喜你注册成功！");
        }
        return resultInfo;
    }
}
