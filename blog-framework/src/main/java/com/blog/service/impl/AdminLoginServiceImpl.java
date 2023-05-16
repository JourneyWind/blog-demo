package com.blog.service.impl;

import com.blog.domain.entity.LoginUser;
import com.blog.domain.entity.Menu;
import com.blog.domain.entity.User;
import com.blog.domain.vo.AdminUserInfoVo;
import com.blog.domain.vo.RoutersVo;
import com.blog.domain.vo.UserInfoVo;
import com.blog.service.AdminLoginService;
import com.blog.service.MenuService;
import com.blog.service.RoleService;
import com.blog.utils.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

import static com.blog.constants.CommonConstants.ADMIN_USER_TOKEN_KEY;

@Service
public class AdminLoginServiceImpl implements AdminLoginService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;
    @Resource
    private MenuService menuService;
    @Resource
    private RoleService roleService;

    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //获取userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtils.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject(ADMIN_USER_TOKEN_KEY + userId, loginUser);
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList,
                BeanCopyPropertiesUtils.copyBean(loginUser.getUser(), UserInfoVo.class));
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @Override
    public ResponseResult<RoutersVo> getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus = menuService.selectRouterMenuTreeBuUserId(userId);
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @Override
    public ResponseResult logout() {
        redisCache.deleteObject(ADMIN_USER_TOKEN_KEY + SecurityUtils.getUserId());
        return ResponseResult.okResult();
    }

}
