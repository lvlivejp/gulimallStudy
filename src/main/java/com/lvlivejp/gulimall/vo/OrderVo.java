package com.lvlivejp.gulimall.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class OrderVo implements Serializable {

    private Integer orderId;
    private String memo;
}
