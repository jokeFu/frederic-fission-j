package com.xuecheng.manage_cms.web;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.GenerateHtmlResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    CmsPageService cmsPageService;

    @Override
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {

        QueryResponseResult all = cmsPageService.findAll(page, size, queryPageRequest);

        return all;
    }

    @Override
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return cmsPageService.add(cmsPage);
    }

    @Override
    public CmsPageResult findById(@PathVariable("id") String id) {


        return cmsPageService.findById(id);
    }

    @Override
    public CmsPageResult edit(@PathVariable("id") String id,@RequestBody CmsPage cmsPage) {
        return cmsPageService.edit(id,cmsPage);
    }

    @Override
    public GenerateHtmlResult generateHtml(@PathVariable("pageId") String pageId) throws IOException {
        return cmsPageService.generateHtml(pageId);
    }

    @Override
    public GenerateHtmlResult getHtml(@PathVariable("pageId") String pageId) throws IOException {
        return cmsPageService.getHtml(pageId);
    }

    @Override
    public ResponseResult postPage(@PathVariable("pageId") String pageId) {

        return cmsPageService.postpage(pageId);
    }

}
