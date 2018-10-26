package com.xuecheng.manage_cms;


import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
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

    @Test
    public void testFindList() {

//        CmsPage one = cmsPageRepository.findOne("5a754adf6abb500ad05688d9");
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



}
