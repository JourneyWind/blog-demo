package com.blog.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.constants.CommonConstants;
import com.blog.domain.entity.LoginUser;
import com.blog.domain.entity.User;
import com.blog.domain.vo.UserInfoVo;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.mapper.UserMapper;
import com.blog.service.UserService;
import com.blog.utils.BeanCopyPropertiesUtils;
import com.blog.utils.RedisCache;
import com.blog.utils.ResponseResult;
import com.blog.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @description 针对表【sys_user(用户表)】的数据库操作Service实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>implements UserService {
//    @Resource
//    private BCryptPasswordEncoder passwordEncoder;
    @Resource
    private RedisCache redisCache;

    @Override
    public ResponseResult userInfo() {
        User user = SecurityUtils.getLoginUser().getUser();
        UserInfoVo userInfoVo = BeanCopyPropertiesUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        boolean b = updateById(user);
        if (!b) {
            return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_USERINFO_ERR);
        }
        //删除缓存
        redisCache.deleteObject(CommonConstants.BLOG_USER_TOKEN_KEY + user.getId());
        //查询新的用户信息
        User newUser = getById(user.getId());
        //存入redis
        LoginUser loginUser = new LoginUser(newUser);
        redisCache.setCacheObject(CommonConstants.BLOG_USER_TOKEN_KEY + user.getId(),loginUser);
        return ResponseResult.okResult();
    }

    /**
     * 用户注册
     */
    @Override
    public ResponseResult userRegister(User user) {
        return null;
    }




//    @Override
//    public ResponseResult userRegister(User user) {
////        1.数据非空校验
//        if (!StringUtils.hasText(user.getUserName())
//                && !StringUtils.hasText(user.getPassword())
//                && !StringUtils.hasText(user.getEmail())
//                && !StringUtils.hasText(user.getNickName())
//                && !StringUtils.hasText(user.getPhonenumber())){
//            throw new SystemException(AppHttpCodeEnum.REGISTER_NOT_NULL);
//        }
////        2.数据是否存在校验
//        if (!judgeUsername(user.getUserName())){
//            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
//        }
//        if (!judgeNickname(user.getNickName())){
//            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
//        }
//        if (!judgeEmail(user.getEmail())){
//            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
//        }
////        3.密码加密
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        save(user);
////        4.添加用户
//        return ResponseResult.okResult();
//    }
//
//
//    /**
//     * 判断用户名是否存在
//     * @param username
//     * @return
//     */
//    public boolean judgeUsername(String username){
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.eq(User::getUserName, username);
//        User user = getOne(queryWrapper);
//        if (Objects.isNull(user)){
//            return true;
//        }else{
//            return false;
//        }
//    }
//
//    /**
//     * 判断邮箱是否存在
//     * @param email
//     * @return
//     */
//    public boolean judgeEmail(String email){
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.eq(User::getEmail, email);
//        User user = getOne(queryWrapper);
//        if (Objects.isNull(user)){
//            return true;
//        }else{
//            return false;
//        }
//    }
//
//    /**
//     * 判断昵称是否存在
//     * @param nickname
//     * @return
//     */
//    public boolean judgeNickname(String nickname){
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.eq(User::getNickName, nickname);
//        User user = getOne(queryWrapper);
//        if (Objects.isNull(user)){
//            return true;
//        }else{
//            return false;
//        }
//    }
//

}

