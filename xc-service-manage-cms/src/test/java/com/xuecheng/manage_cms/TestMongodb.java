package com.xuecheng.manage_cms;


import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.service.CmsSiteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMongodb  {

    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteService cmsSiteService;

    @Test
    public void testFindList() {

////        CmsPage one = cmsPageRepository.findOne("5ad92e9068db52404cad0f79");
//        CmsPage one = cmsPageRepository.findByPageName("297e7c7c62b888f00162b8a965510001.html");
//        System.out.println(one);

        ExampleMatcher pageAliase = ExampleMatcher.matching().withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

        CmsPage cmsPage = new CmsPage();

        cmsPage.setPageAliase("页面");
        Example<CmsPage> of = Example.of(cmsPage, pageAliase);
        int page = 0;
        int size = 10;
        PageRequest pageRequest = new PageRequest(page, size);

        Page<CmsPage> all = cmsPageRepository.findAll(of, pageRequest);
        List<CmsPage> content = all.getContent();
        System.out.println(content);


    }

    @Test
    public void TestCmsSiteFindAll() {
        QueryResponseResult all = cmsSiteService.findAll();
        System.out.println(all);

    }


}
