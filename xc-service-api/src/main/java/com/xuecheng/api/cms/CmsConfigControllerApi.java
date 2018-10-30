package com.xuecheng.api.cms;


import com.xuecheng.framework.domain.cms.response.CmsConfigResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Api(value = "cms配置管理的接口", description = "cms配置管理的接口，提供轮播图信息的配置、精品课程的配置及查询操作")
public interface CmsConfigControllerApi {

    final String API_PRE = "/cms/config";

    @GetMapping(API_PRE + "/getmodel/{id}")
    public CmsConfigResult getmodel(@PathVariable("id") String id);

}
