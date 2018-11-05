package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther: 0
 * @Date: 2018/11/5 14:10
 * @Description:
 */

public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
}
