package com.kalix.framework.core.restfulapi.test;

import com.kalix.framework.core.rest.test.APITest;
import com.kalix.framework.core.rest.test.APIRequest;
import com.kalix.framework.core.rest.test.APIResponse;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.ws.rs.core.MediaType;

/**
 * Demo about how to use this framework
 * <p/>
 * Need store some some APIs configuration in a env.properties files
 *
 * @author Carl Ji
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestHTTPRequests extends APITest {
    //login cookie token
    private String token;
    //insert entity id return by tag
    private static long id;
    //insert delete update status
    private boolean succeed;


    /**
     * return json format is :{success:true,location:CONFIG.restRoot + '/index.jsp',message:'登入成功',
     * user:{name:'管理员',token:'70841dfd-97f3-43e3-b1d9-dded905d9f73'}}
     * 登录，获得服务器返回的token
     */
    @Before
    public void login() {
        String uri = getValue("login.uri");
        String username = getValue("login.username");
        String password = getValue("login.password");

        APIResponse response = APIRequest.POST(uri).param("username", username).param("password", password).invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        JSONObject jsonObject = new JSONObject(returnString);
        JSONObject user = jsonObject.getJSONObject("user");
        token = (String) user.get("token");
        Assert.assertNotNull(token);
    }

    /**
     * test for add new Entity method
     */
    @Test
    public void test001POSTHttpRequest() {
        String uri = getValue("post.uri");
        String payload = loadFile("add.json");
        APIResponse response = APIRequest.POST(uri).header("Cookie", "JSESSIONID=" + token).type(MediaType.APPLICATION_JSON_TYPE).body(payload)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        JSONObject jsonObject = new JSONObject(returnString);
        Assert.assertNotNull(jsonObject);
        id = jsonObject.getLong("tag");
        Assert.assertTrue(id > 0);
        succeed = jsonObject.getBoolean("success");
        Assert.assertTrue(succeed);
    }

    /**
     * test for getAll method
     */
    @Test
    public void test002GETALLHttpRequest() {
        String uri = getValue("getall.uri");
        APIResponse response = APIRequest.GET(uri).header("Cookie", "JSESSIONID=" + token).param("page", "1").param("limit", "10")
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        JSONObject jsonObject = new JSONObject(returnString);
        Assert.assertNotNull(jsonObject);
    }

    /**
     * test for get a entity method
     */
    @Test
    public void test003GETHttpRequest() {
        Assert.assertNotNull(id);
        String uri = String.format(getValue("get.uri"), id);
        APIResponse response = APIRequest.GET(uri).header("Cookie", "JSESSIONID=" + token)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        JSONObject jsonObject = new JSONObject(returnString);
        Assert.assertNotNull(jsonObject);
    }

    /**
     * test for update a entity method
     */
    @Test
    public void test004PUTHttpRequest() {
        Assert.assertNotNull(id);
        String uri = String.format(getValue("put.uri"), id);
        //format data
        String payload = String.format(loadFile("update.json"), id);
        APIResponse response = APIRequest.PUT(uri).header("Cookie", "JSESSIONID=" + token).type(MediaType.APPLICATION_JSON_TYPE).body(payload)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        JSONObject jsonObject = new JSONObject(returnString);
        Assert.assertNotNull(jsonObject);
        succeed = jsonObject.getBoolean("success");
        Assert.assertTrue(succeed);
    }
    /**
     * test for delete a entity method
     */
    @Test
    public void test005DELETEHttpRequest() {
        String uri = String.format(getValue("delete.uri"), id);
        APIResponse response = APIRequest.DELETE(uri).header("Cookie", "JSESSIONID=" + token)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        JSONObject jsonObject = new JSONObject(returnString);
        Assert.assertNotNull(jsonObject);
        succeed = jsonObject.getBoolean("success");
        Assert.assertTrue(succeed);
    }

}
