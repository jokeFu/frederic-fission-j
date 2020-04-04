package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(value = "模板的分类", description = "模板的分类")
public interface CmsTemplateControllerApi {

    final String API_PRE = "/cms/template";

    @ApiOperation(value = "提供模板的分类")
    @GetMapping(API_PRE+"/list")
    public QueryResponseResult findAll();



}
