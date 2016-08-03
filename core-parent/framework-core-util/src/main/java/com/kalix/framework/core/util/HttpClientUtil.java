package com.kalix.framework.core.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenyanxu
 */
public class HttpClientUtil {
    public static String shiroGet(String url,String sessionId) throws IOException{
        String result=null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        if(sessionId!=null && !sessionId.isEmpty()){
            httpGet.addHeader("Cookie","JSESSIONID="+sessionId);
        }

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

    public static String get(String url) throws IOException {
        return shiroGet(url, null);
    }


    public static String shiroPost(String url, Map<String,String> params, String sessionId) throws IOException{
        String result=null;
        CloseableHttpResponse response1 =null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost=new HttpPost(url);

        if(sessionId!=null && !sessionId.isEmpty()){
            httpPost.addHeader("Cookie","JSESSIONID="+sessionId);
        }


        try {
            StringEntity entity = new StringEntity(SerializeUtil.serializeJson(params),"utf-8");//解决中文乱码问题

            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            response1 = httpclient.execute(httpPost);
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            result=convertStreamToString(entity1.getContent());
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(response1!=null){
                response1.close();
            }
        }

        return result;
    }

    public static  String post(String url,Map<String,String> params) throws IOException{
        return shiroPost(url,params,null);
    }

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
