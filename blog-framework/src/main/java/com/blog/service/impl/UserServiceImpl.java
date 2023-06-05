package com.blog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.constants.CommonConstants;
import com.blog.domain.entity.LoginUser;
import com.blog.domain.entity.Role;
import com.blog.domain.entity.User;
import com.blog.domain.entity.UserRole;
import com.blog.domain.vo.PageVo;
import com.blog.domain.vo.UserInfoAndRoleIdsVo;
import com.blog.domain.vo.UserInfoVo;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.exception.SystemException;
import com.blog.mapper.RoleMapper;
import com.blog.mapper.UserMapper;
import com.blog.service.UserRoleService;
import com.blog.service.UserService;
import com.blog.utils.BeanCopyPropertiesUtils;
import com.blog.utils.RedisCache;
import com.blog.utils.ResponseResult;
import com.blog.utils.SecurityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @description 针对表【sys_user(用户表)】的数据库操作Service实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    @Resource
    private RedisCache redisCache;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleService userRoleService;

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
        LoginUser loginUser = new LoginUser(newUser, null);
        redisCache.setCacheObject(CommonConstants.BLOG_USER_TOKEN_KEY + user.getId(), loginUser);
        return ResponseResult.okResult();
    }

    /**
     * 用户注册
     */
    @Override
    public ResponseResult userRegister(User user) {
        //校验数据
        if (!StringUtils.hasText(user.getNickName()) || !StringUtils.hasText(user.getEmail())
                || !StringUtils.hasText(user.getPassword()) || !StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REGISTER_NOT_NULL);
        }
        if (!judgeUsername(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!judgeEmail(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        return ResponseResult.okResult();
    }


    /**
     * 判断用户名是否存在
     *
     * @param username
     */
    public boolean judgeUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUserName, username);
        User user = getOne(queryWrapper);
        if (Objects.isNull(user)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断邮箱是否存在
     *
     * @param email
     */
    public boolean judgeEmail(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getEmail, email);
        User user = getOne(queryWrapper);
        if (Objects.isNull(user)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public ResponseResult userList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(userName), User::getUserName, userName)
                .eq(StringUtils.hasText(phonenumber), User::getPhonenumber, phonenumber)
                .eq(StringUtils.hasText(status), User::getStatus, status);
        Page<User> userPage = new Page<>(pageNum, pageSize);
        Page<User> page = page(userPage, wrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addUser(User user) {
        String userName = user.getUserName();
        String phonenumber = user.getPhonenumber();
        String email = user.getEmail();
        if (!StringUtils.hasText(userName) || !StringUtils.hasText(email) || !StringUtils.hasText(phonenumber)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USER_RESIGN_NULL);
        }
        //todo:判断手机号、邮箱...是否已存在
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUser(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listUserInfo(Long id) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getStatus, CommonConstants.STATUS_NORMAL);
        List<Role> roles = roleMapper.selectList(roleLambdaQueryWrapper);
        User user = getById(id);
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        List<UserRole> list = userRoleService.list(wrapper);
        List<Long> collect = list.stream().map(ur -> ur.getRoleId()).collect(Collectors.toList());
        UserInfoAndRoleIdsVo userInfoAndRoleIdsVo = new UserInfoAndRoleIdsVo(user, roles, collect);
        return ResponseResult.okResult(userInfoAndRoleIdsVo);
    }

    @Override
    public ResponseResult updateUser(User user) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getId, user.getId());
        update(userLambdaQueryWrapper);
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId, user.getId());
        userRoleService.remove(userRoleLambdaQueryWrapper);
        Long[] roleIds = user.getRoleIds();
        for (Long id : roleIds) {
            userRoleService.save(new UserRole(user.getId(), id));
        }
        return ResponseResult.okResult();
    }
}

