package com.xuecheng.manage_cms.web;

import com.xuecheng.api.cms.CmsTemplateControllerApi;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.service.CmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CmsTemplateController implements CmsTemplateControllerApi {

    @Autowired
    CmsTemplateService cmsTemplateService;
    @Override
    public QueryResponseResult findAll() {
        return cmsTemplateService.findAll();
    }

}
