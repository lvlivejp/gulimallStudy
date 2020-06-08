package com.lvlivejp.gulimall.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UserVo implements Serializable {

    private Integer userId;
    private String userName;
}
