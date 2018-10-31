package com.xuecheng.manage_cms;

import com.mongodb.gridfs.GridFSFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestGridFs {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Test
    public void testStore() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(new File("D:\\xc\\test-freemarker\\src\\main\\resources\\templates\\index_banner.ftl"));


        GridFSFile gridFSFile = gridFsTemplate.store(inputStream, "轮播图文件1");

        String s = gridFSFile.getId().toString();

        System.out.println(s);


    }




}
