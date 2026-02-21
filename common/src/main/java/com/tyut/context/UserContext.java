package com.tyut.context;

import lombok.Data;

@Data
public class UserContext {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户角色
     */
    private Integer role;

}
