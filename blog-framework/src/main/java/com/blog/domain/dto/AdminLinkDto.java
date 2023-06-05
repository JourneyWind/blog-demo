package com.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLinkDto {
    private Long id;
    private String description;
    private String address;
    private String name;
    private String status;
    private String logo;
}
