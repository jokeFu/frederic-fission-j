package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.response.CmsConfigResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmsConfigService {

    @Autowired
    CmsConfigRepository cmsConfigRepository;

    public CmsConfigResult getConfigById(String id) {
        CmsConfig one = cmsConfigRepository.findOne(id);

        if (one == null) {

            ExceptionCast.cast(CommonCode.INVLIDATE);
        }
        return new CmsConfigResult(CommonCode.SUCCESS, one);
    }

}
