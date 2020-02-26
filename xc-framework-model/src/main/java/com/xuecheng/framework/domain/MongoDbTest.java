package com.xuecheng.framework.domain;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDbTest {
    public static void main(String[] args) {
        //创建mongodb客户端
        MongoClient localhost = new MongoClient("localhost", 27017);
        //连接数据库
        MongoDatabase test = localhost.getDatabase("local");
        //连接集合
        MongoCollection<Document> collection = test.getCollection("startup_log");
        Document first = collection.find().first();
        String s = first.toJson();
        System.out.println(s);
    }
}
