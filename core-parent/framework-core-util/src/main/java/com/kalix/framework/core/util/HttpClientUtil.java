package com.kalix.framework.core.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;

/**
 * @author chenyanxu
 */
public class HttpClientUtil {
    public static String shiroGet(String url,String sessionId) throws IOException{
        String result=null;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        httpGet.addHeader("Cookie","JSESSIONID="+sessionId);

        CloseableHttpResponse response1 = null;

        // The underlying HTTP connection is still held by the response object
        // to allow the response content to be streamed directly from the network socket.
        // In order to ensure correct deallocation of system resources
        // the user MUST either fully consume the response content  or abort request
        // execution by calling CloseableHttpResponse#close().
        //建立的http连接，仍旧被response1保持着，允许我们从网络socket中获取返回的数据
        //为了释放资源，我们必须手动消耗掉response1或者取消连接（使用CloseableHttpResponse类的close方法）

        try {
            response1 = httpclient.execute(httpGet);

            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
             result=convertStreamToString(entity1.getContent());
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
        }
        finally {
            response1.close();
        }

        return result;
    }


//    public static void get(String url) throws IOException
//    {
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet(url);
//
//        CloseableHttpResponse response1 = null;
//
//        // The underlying HTTP connection is still held by the response object
//        // to allow the response content to be streamed directly from the network socket.
//        // In order to ensure correct deallocation of system resources
//        // the user MUST either fully consume the response content  or abort request
//        // execution by calling CloseableHttpResponse#close().
//        //建立的http连接，仍旧被response1保持着，允许我们从网络socket中获取返回的数据
//        //为了释放资源，我们必须手动消耗掉response1或者取消连接（使用CloseableHttpResponse类的close方法）
//
//        try {
//            response1 = httpclient.execute(httpGet);
//
//            System.out.println(response1.getStatusLine());
//            HttpEntity entity1 = response1.getEntity();
//            // do something useful with the response body
//            String str=convertStreamToString(entity1.getContent());
//            // and ensure it is fully consumed
//            EntityUtils.consume(entity1);
//        }
//        finally {
//            response1.close();
//        }
//    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
