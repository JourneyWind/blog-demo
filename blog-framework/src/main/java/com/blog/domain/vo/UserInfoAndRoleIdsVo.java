package com.blog.domain.vo;

import com.blog.domain.entity.Role;
import com.blog.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 标签(Tag)表实体类
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoAndRoleIdsVo {
    private User user;
    private List<Role> roles;
    private List<Long> roleIds;



}
