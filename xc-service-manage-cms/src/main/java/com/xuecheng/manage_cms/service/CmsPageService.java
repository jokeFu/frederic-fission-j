package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmsPageService {
    @Autowired
    CmsPageRepository cmsPageRepository;

    public QueryResponseResult findAll(int page, int size, QueryPageRequest queryPageRequest) {

        //获取到传进来的条件 分页展示
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;

        if (size <= 0) {
            size = 20;
        }
        //分页参数
        PageRequest pageRequest = new PageRequest(page, size);

        ExampleMatcher pageAliase = ExampleMatcher.matching().withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

        //可以按照条件查询
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("页面");
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");

        Example<CmsPage> of = Example.of(cmsPage, pageAliase);

        //执行分页查询
        Page<CmsPage> all = cmsPageRepository.findAll(of, pageRequest);

        List<CmsPage> content = all.getContent();
        //总记录数
        long totalElements = all.getTotalElements();

        QueryResult queryResult = new QueryResult();

        queryResult.setList(content);

        queryResult.setTotal(totalElements);


        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }

}
