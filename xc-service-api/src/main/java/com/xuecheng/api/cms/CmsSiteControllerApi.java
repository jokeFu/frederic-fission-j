package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(value = "cms站点的接口", description = "cms站点管理的接口，提供站点添加、删除、修改、查询操作")
public interface CmsSiteControllerApi {

     final String Api_PRE = "/cms/site";

    @ApiOperation(value = "提供站点list")
    @GetMapping(Api_PRE+"/list")
    public QueryResponseResult<CmsSite> findList();

}
