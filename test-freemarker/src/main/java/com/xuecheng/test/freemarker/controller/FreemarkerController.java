package com.xuecheng.test.freemarker.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequestMapping("/freemarker")
public class FreemarkerController {
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/banner")
    public String banner(Map<String, Object> map) {
        String url = "http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(url, Map.class);
        Map banner = forEntity.getBody();
        System.out.println(banner);
        map.put("banner", banner);
        return "index_banner";
    }
}
