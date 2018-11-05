package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.GenerateHtmlResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitMQConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CmsPageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsPageService.class);

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

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

        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }

        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }

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

    //新增页面
    public CmsPageResult add(CmsPage cmsPage) {
        if (cmsPage == null) {
            //证明有问题 要抛异常的
        }

        CmsPage cmsPage1 = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());

        if (cmsPage1 != null) {
            //抛出具体的异常。。
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTS);
        }
        //   int i= 1 / 0;

        cmsPage.setPageId(null);

        CmsPage save = cmsPageRepository.save(cmsPage);

        if (save != null) {
            return new CmsPageResult(CommonCode.SUCCESS, save);
        }
        return new CmsPageResult(CommonCode.FAIL, null);

    }

    public CmsPageResult findById(String id) {
        CmsPage one = cmsPageRepository.findOne(id);
        if (one != null) {
            return new CmsPageResult(CommonCode.SUCCESS, one);
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    public CmsPageResult edit(String id, CmsPage cmsPage) {

        CmsPage one = cmsPageRepository.findOne(id);
        if (one != null) {
            one.setPageAliase(cmsPage.getPageAliase());
            one.setSiteId(cmsPage.getSiteId());
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            one.setPageWebPath(cmsPage.getPageWebPath());
            one.setPageName(cmsPage.getPageName());
            one.setTemplateId(cmsPage.getTemplateId());
            CmsPage save = cmsPageRepository.save(one);
            if (save != null) {
                //返回成功
                return new CmsPageResult(CommonCode.SUCCESS, save);
            }


        }

        return new CmsPageResult(CommonCode.FAIL, null);
    }

    //生成静态文件
    public GenerateHtmlResult generateHtml(String pageId) {

        CmsPage one = cmsPageRepository.findOne(pageId);

        if (one == null) {

            ExceptionCast.cast(CommonCode.INVLIDATE);
        }

        String dataUrl = one.getDataUrl();

        String templateId = one.getTemplateId();

        Map map = null;

        if (StringUtils.isNotEmpty(dataUrl)) {
            ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
            map = forEntity.getBody();
        }
        //根据模板Id从GIrdFs中查询到模板文件内容
        String pageTempateContent = this.getPageTempateContent(templateId);

        //配置freemarker
        Configuration configuration = new Configuration(Configuration.getVersion());

        //使用模板
        //使用模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        //加载上面的pageTempateContent的内容作为模板
        // 参数 给模板起一个名称，模板的内容
        stringTemplateLoader.putTemplate("template", pageTempateContent);
        //将模板加载器设置到configuration
        configuration.setTemplateLoader(stringTemplateLoader);
        //设置字符编码
        configuration.setDefaultEncoding("utf-8");
        Template template = null;
        try {
            template =   configuration.getTemplate("template");
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Map<String, Object> model = new HashMap<>();
        model.put("model", map);
        String content = null;
        try {
            //静态化执行
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        String s = this.saveHtml(content);
        one.setHtmlFileId(s);
        CmsPage save = cmsPageRepository.save(one);
        return new GenerateHtmlResult(CommonCode.SUCCESS,content);
    }

    //将静态文件的内容保存到GridFS中
    public String saveHtml(String htmlContent) {
        InputStream inputStream = IOUtils.toInputStream(htmlContent);

        GridFSFile gridFSFile = gridFsTemplate.store(inputStream, "轮播图静态文件AAAA");

        String s = gridFSFile.getId().toString();
        return s;

    }

    //根据模板Id得到模板文件内容
    private String getPageTempateContent(String templateId) {
        CmsTemplate one = cmsTemplateRepository.findOne(templateId);

        if (one == null) {
            return null;
        }

        String templateFileId = one.getTemplateFileId();

        if (StringUtils.isEmpty(templateFileId)) {
            return null;
        }

        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));

        InputStream inputStream = gridFSDBFile.getInputStream();

        String templateContent = null;

        try {
            templateContent = IOUtils.toString(inputStream, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        if (StringUtils.isEmpty(templateContent)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        return templateContent;

    }
    //查询静态页面内容
    public GenerateHtmlResult getHtml(String pageId) {

        CmsPage one = cmsPageRepository.findOne(pageId);

        if (one == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTFOUND);
        }
        String htmlFileId = one.getHtmlFileId();

        String html = null;

        GridFSDBFile id = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));

        InputStream inputStream = id.getInputStream();

        try {
            html = IOUtils.toString(inputStream,"utf-8");
        } catch (IOException e) {
            LOGGER.error("get html from gridFS error:{}",e.getMessage());
            e.printStackTrace();
            return new GenerateHtmlResult(CommonCode.FAIL,null);
        }

        return new GenerateHtmlResult(CommonCode.SUCCESS,html);
    }

    //页面发布
    public ResponseResult postpage(String pageId) {
        GenerateHtmlResult generateHtmlResult = this.generateHtml(pageId);

        if (!generateHtmlResult.isSuccess()) {
            //静态化失败
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        //得到页面信息
        CmsPage one = cmsPageRepository.findOne(pageId);
        String siteId = one.getSiteId();
        Map<String, String> msgMap = new HashMap<>();

        msgMap.put("pageId", pageId);

        String s = JSON.toJSONString(msgMap);

        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_CMS_POSTPAGE, siteId, s);
        return new ResponseResult(CommonCode.SUCCESS);

    }


}
