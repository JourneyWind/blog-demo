package com.blog.handler.security;

import com.blog.utils.ResponseResult;
import com.blog.utils.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.blog.enums.AppHttpCodeEnum.*;

@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ResponseResult result = null;
        if (e instanceof BadCredentialsException) {
            result = ResponseResult.errorResult(LOGIN_ERROR.getCode(), LOGIN_ERROR.getMsg() + e.getMessage());
        }else if (e instanceof InsufficientAuthenticationException){
            result = ResponseResult.errorResult(NEED_LOGIN.getCode(), NEED_LOGIN.getMsg() + e.getMessage());
        }else {
            result = ResponseResult.errorResult(SYSTEM_ERROR.getCode(), SYSTEM_ERROR.getMsg() + "认证或授权失败");
        }
        WebUtils.renderString(httpServletResponse,new ObjectMapper().writeValueAsString(result));
    }
}
