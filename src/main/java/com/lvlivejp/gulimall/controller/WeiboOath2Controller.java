package com.lvlivejp.gulimall.controller;

import com.alibaba.fastjson.JSONObject;
import com.lvlivejp.gulimall.utils.HttpClientResult;
import com.lvlivejp.gulimall.utils.HttpClientUtils;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeiboOath2Controller {

    private final static String clientId="371772949";
    private final static String clientSecret="de490c9e0b233b90fc3fde0af76f07a5";
    @SneakyThrows
    @RequestMapping("ok")
    @ResponseBody
    public JSONObject ok(String code){

        String url="https://api.weibo.com/oauth2/access_token?client_id="+clientId+"&client_secret="+clientSecret+"&grant_type=authorization_code&code="+code +"&redirect_uri=http://lvlivejp.com/ok";

        HttpClientResult httpClientResult = HttpClientUtils.doPost(url);
        JSONObject jsonObject = JSONObject.parseObject(httpClientResult.getContent());
        String token = jsonObject.getString("access_token");
        String uid = jsonObject.getString("uid");

        url = "https://api.weibo.com/2/users/show.json?access_token="+token+"&uid=" +uid;
        httpClientResult = HttpClientUtils.doGet(url,null);
        jsonObject = JSONObject.parseObject(httpClientResult.getContent());

        System.out.println(jsonObject);
        return jsonObject;
    }
}
