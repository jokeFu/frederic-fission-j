package com.xuecheng.api.cms;


import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.GenerateHtmlResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(value="cms页面管理的接口",description="cms页面管理的接口，提供页面添加、删除、修改、查询操作")
public interface CmsPageControllerApi {
    final String API_PRE = "/cms/page";
    @ApiOperation(value="分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",paramType="path"),
            @ApiImplicitParam(name="size",value="每页记录数",paramType="path")

    })
    @GetMapping(API_PRE + "/list/{page}/{size}")
    public QueryResponseResult <CmsPage> findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest);

    @PostMapping(API_PRE + "/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage);

    @ApiOperation("通过id进行查询")
    @GetMapping(API_PRE + "/get/{id}")
    public CmsPageResult findById(@PathVariable("id") String id);

    @ApiOperation("修改页面")
    @PutMapping(API_PRE + "/edit/{id}")
    public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage);

    @PostMapping(API_PRE + "/generateHtml/{pageId}")
    @ApiOperation("生成静态页面")
    public GenerateHtmlResult generateHtml(@PathVariable("pageId") String pageId) throws IOException;


    @GetMapping(API_PRE + "/getHtml/{pageId}")
    @ApiOperation("查询静态页面")
    public GenerateHtmlResult getHtml(@PathVariable("pageId") String pageId) throws IOException;


}
