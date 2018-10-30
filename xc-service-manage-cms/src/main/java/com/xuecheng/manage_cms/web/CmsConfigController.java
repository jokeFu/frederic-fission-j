package com.xuecheng.manage_cms.web;

import com.xuecheng.api.cms.CmsConfigControllerApi;
import com.xuecheng.framework.domain.cms.response.CmsConfigResult;
import com.xuecheng.manage_cms.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CmsConfigController implements CmsConfigControllerApi {

    @Autowired
    CmsConfigService cmsConfigService;

    @Override
    public CmsConfigResult getmodel(@PathVariable("id") String id) {

        CmsConfigResult configById = cmsConfigService.getConfigById(id);


        return configById;
    }
}
