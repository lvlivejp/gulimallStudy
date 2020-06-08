package com.lvlivejp.gulimall.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ProductVo implements Serializable {

    private Integer productId;
    private String productName;
}
