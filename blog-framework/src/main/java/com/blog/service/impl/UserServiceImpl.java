package com.blog.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.entity.User;
import com.blog.domain.vo.UserInfoVo;
import com.blog.mapper.UserMapper;
import com.blog.service.UserService;
import com.blog.utils.BeanCopyPropertiesUtils;
import com.blog.utils.ResponseResult;
import com.blog.utils.SecurityUtils;
import org.springframework.stereotype.Service;



/**
 * @description 针对表【sys_user(用户表)】的数据库操作Service实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>implements UserService {
//    @Resource
//    private BCryptPasswordEncoder passwordEncoder;
//

    @Override
    public ResponseResult userInfo() {
        User user = SecurityUtils.getLoginUser().getUser();
        UserInfoVo userInfoVo = BeanCopyPropertiesUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }


//    /**
//     * 用户注册
//     * @return
//     * @param user
//     */

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
//
//    /**
//     * 查询用户信息
//     *
//     * @return
//     */
//    @Override
//    public ResponseResult getUserInfo() {
////        1.获取到当前用户信息
//        Long userId = SecurityUtils.getUserId();
//        User user = getById(userId);
////        2.将User对象封装为UserInfoVo对象
//        UserInfoVo userInfo = BeanCopyPropertiesUtils.copyBean(user, UserInfoVo.class);
//        return ResponseResult.okResult(userInfo);
//    }
//
//    /**
//     * 用户修改信息
//     *
//     * @param user
//     * @return
//     */
//    @Override
//    public ResponseResult updateUserInfo(User user) {
////        1.更新数据库
//        boolean result = updateById(user);
//        if (Objects.isNull(result)) {
//            return ResponseResult.errorResult(408, "更新失败");
//        }
////        2.删除缓存
//        redisCache.deleteObject(BLOG_USER_LOGIN + user.getId());
////        3.查询新的用户信息
//        User newUser = getById(user.getId());
//        LoginUser loginUser = new LoginUser(newUser,null);
////        4.将用户信息存入缓存
//        redisCache.setCacheObject(BLOG_USER_LOGIN + user.getId(), loginUser);
//        return ResponseResult.okResult();
//    }
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

