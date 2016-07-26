package com.ext.db;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import com.ext.dict.WordDict;
import com.ext.entrance.Main;
import com.ext.predeal.CutTextIntoSentence;
 
public class MysqlDemo {
    public static void main(String[] args) throws Exception {
        Connection conn = null;
        String sql;
        // MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值
        // 避免中文乱码要指定useUnicode和characterEncoding
        // 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，
        // 下面语句之前就要先创建javademo数据库
        String url = "jdbc:mysql://172.25.81.201:5029/distribute_crawler?"
                + "user=sa&password=rapidminer_2g&useUnicode=true&characterEncoding=UTF8&allowMultiQueries=true";
 
        try {
        	BufferedWriter writer = new BufferedWriter(new FileWriter(args[0]));
        	BufferedWriter writer2 = new BufferedWriter(new FileWriter(args[1]));
            // 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，
            // 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以
            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
            // or:
            // com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
            // or：
            // new com.mysql.jdbc.Driver();
 
            System.out.println("成功加载MySQL驱动程序");
            // 一个Connection代表一个数据库连接
            conn = DriverManager.getConnection(url);
            // Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等
            Statement stmt = conn.createStatement();
            //sql = "select * from toutiao_article_tb where docId = '" + "6298814404519526658" + "'";
            sql = "select * from toutiao_article_tb";
            ResultSet result = stmt.executeQuery(sql);// executeQuery会返回结果的集合，否则返回空值
            int count = 0;
            ArrayList<String> sentenceResult = null;
            ArrayList<String> sentenceCutResult = null;
            while (result!=null && result.next()) {
            	
            	int Id = result.getInt(1);
            	String content = result.getString(2);
            	String docId = result.getString(3);
            	String keyWords = result.getString(4);
            	int reviewCount = result.getInt(5);
            	String source = result.getString(6);
            	String tags = result.getString(7);
            	String time = result.getString(8);
            	String title = result.getString(9);
            	String Url = result.getString(10);
            	
            	//获取关键词
                ArrayList<String> lableList = new ArrayList<String>();
                if(keyWords != null || keyWords.length() > 0 || keyWords != ""){
                	String[] keyword = keyWords.split(",");
                	for(int i = 0;i < keyword.length;i++){
                		lableList.add(keyword[i]);
                	}
                }

            	//筛选关于手机的新闻
            	String[] tagArray = tags.split(";");
            	for(int i = 0;i < tagArray.length;i++){
            		if(WordDict.tagDict.contains(tagArray[i])){
            			count++;  
            			//输出句子和对应的权重
            			sentenceResult = Main.getTopicSentence(content, title, lableList);
            			for(String output : sentenceResult){
            				writer.write(docId + "\t" + output);
            				writer.newLine();
            			}
            			//输出文章分句后的结果
            			writer2.write(docId + "\t" + 0 + "\t" + "Title" + ":" + title);
            			writer2.newLine();
            			sentenceCutResult = CutTextIntoSentence.cutTextIntoSentences(content,1);
            			if(sentenceCutResult != null && sentenceCutResult.size() > 0){
            				for(String sentence : sentenceCutResult){
            					String[] senArray = sentence.split(":");
            					if(senArray.length >=2){
            						String sentenceContent = senArray[0];
                					String idx = senArray[1];
                    				writer2.write(docId + "\t" + idx + "\t" + sentenceContent);
                    				writer2.newLine();
            					}            					
                			}
            			}
            			
            			break;
            		}
            	}            	
            }
            writer.close();
            writer2.close();
            //System.out.println(count);
//            if (result != -1) {
//                System.out.println("创建数据表成功");
//                sql = "insert into student(NO,name) values('2012001','陶伟基')";
//                result = stmt.executeUpdate(sql);
//                sql = "insert into student(NO,name) values('2012002','周小俊')";
//                result = stmt.executeUpdate(sql);
//                sql = "select * from student";
//                ResultSet rs = stmt.executeQuery(sql);// executeQuery会返回结果的集合，否则返回空值
//                System.out.println("学号\t姓名");
//                while (rs.next()) {
//                    System.out
//                            .println(rs.getString(1) + "\t" + rs.getString(2));// 入如果返回的是int类型可以用getInt()
//                }
//            }
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
 
    }
 
}
