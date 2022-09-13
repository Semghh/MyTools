package com.semghh.ParseXml;

import com.semghh.DateUtils.DateFormatUtils;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;

/**
 * @author SemgHH
 */
public class Main {



    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

//        File file = new File("src/main/resources/my.xml");
//        SAXParserFactory factory = SAXParserFactory.newInstance();
//        SAXParser saxParser = factory.newSAXParser();
//        saxParser.parse(file,new MyHandler());

//        System.out.println(System.getProperties());
        File file = new File(new File("").getAbsolutePath());
        File[] files = file.listFiles();
        System.out.println(Arrays.toString(files));


    }

    @Test
    public void test(){




    }



    public static class MyHandler extends DefaultHandler {


        @Override
        public void startDocument() throws SAXException {
            System.out.println("=================开始解析文档=================");
        }

        @Override
        public void endDocument() throws SAXException {
            System.out.println("=================解析文档完成=================");
        }

        //当开始一个Element
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("foo".equals(qName)){
                System.out.println("do foo");
            } else if ("a".equals(qName)) {
                System.out.println("do a");
            }else if ("b".equals(qName)){
                System.out.println("do b");
            }else if ("c".equals(qName)){
                System.out.println("do c");
            }
        }


        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {

            System.out.println("节点之间的内容 : " + new String(ch,start,length));

        }
    }


}
