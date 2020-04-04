package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmsTemplateService {

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    public QueryResponseResult findAll() {
        QueryResult queryResult = new QueryResult();
        List<CmsTemplate> all = cmsTemplateRepository.findAll();
        queryResult.setList(all);
        queryResult.setTotal(all.size());
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }
}
