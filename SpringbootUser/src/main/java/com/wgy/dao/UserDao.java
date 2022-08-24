package com.wgy.dao;

import com.wgy.po.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    /**
     * 添加用户
     * @param user
     */
    public void addUser(@Param("user")User user);

    /**
     * 删除用户
     * @param userName
     */
    public void deleteUserByName(@Param("userName") String userName);

    /**
     * 查询用户
     */
    public User queryUserByName(@Param("userName") String userName);

    public void updateUserInfoByName(@Param("userName") String userName, @Param("user") User user);
}
