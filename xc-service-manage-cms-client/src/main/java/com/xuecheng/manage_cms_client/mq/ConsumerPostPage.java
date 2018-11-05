package com.xuecheng.manage_cms_client.mq;


import com.alibaba.fastjson.JSON;
import com.mongodb.gridfs.GridFSDBFile;
import com.rabbitmq.client.Channel;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;


/**
 * @Auther: 0
 * @Date: 2018/11/5 14:11
 * @Description:
 */
@Component
public class ConsumerPostPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    CmsSiteRepository cmsSiteRepository;

    //接收页面发布的信息，从配置文件中注入队列名称
    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg, Message message, Channel channel) {
        //解析消息
        Map msgMap = null;

        try {
            msgMap = JSON.parseObject(msg, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("receive pistpage msg , parse message error :{}", e.getMessage());
        }
        //从消息中得到页面的ID
        String pageID = (String) msgMap.get("pageID");

        if (StringUtils.isEmpty(pageID)) {
            LOGGER.error("in message not exists pageid");
            return;
        }
        //查询数据库得到页面信息
        CmsPage cmsPage = cmsPageRepository.findOne(pageID);
        //页面所属的站点
        String siteId = cmsPage.getSiteId();
        CmsSite cmsSite = cmsSiteRepository.findOne(siteId);

        //从页面的信息中得到静态文件id
        String htmlFileId = cmsPage.getHtmlFileId();

        if (StringUtils.isEmpty(htmlFileId)) {
            LOGGER.error("page htmlfileId is null");
            return;
        }
        //查询GridFS
        GridFSDBFile gridFsDBFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        //文件输入流
        InputStream inputStream = gridFsDBFile.getInputStream();

        //文件输出流
        //得到文件输入路径
        String filePath=cmsSite.getSitePhysicalPath()+cmsPage.getPagePhysicalPath() + cmsPage.getPageName();

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("page html file path Not Found:{}",filePath);
        }
        try {
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("out page html error:{}",e.getMessage());
        }


    }





}
