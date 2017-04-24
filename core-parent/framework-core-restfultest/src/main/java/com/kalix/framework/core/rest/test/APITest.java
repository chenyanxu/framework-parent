package com.kalix.framework.core.rest.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;

/**
 * Super class which provide some helper methods to help write API tests
 *
 * @author Carl Ji
 */
public abstract class APITest {


    @Rule
    public TestName testCaseName = new TestName();
    private static String envfile = "env.properties";
    private static Properties prop;
    protected String token;

    @BeforeClass
    public static void setup() {
        prop = initProperties();
    }

    @Before
    public void printEachCaseName() {
    }

    /**
     * Load properties from field in the classpath
     *
     * @return
     */
    public static Properties initProperties() {
        Properties prop = new Properties();

        try {
            prop.load(APITest.class.getClassLoader().getResourceAsStream(envfile));
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load property file " + envfile);
        }

        return prop;
    }

    /**
     * Get value based on the key from the properties file
     *
     * @param key The key defined in the properies file
     * @return The value related to the key
     */
    public static String getValue(String key) {
        String value = prop.getProperty(key);
        Assert.assertNotNull(value, String.format("%s key is missing", key));

        return value;
    }

    /**
     * Load file in the classpath as String
     *
     * @param file The file in the Classpath
     * @return file contens as String
     */
    public static String loadFile(String file) {
        try {
            InputStream stream = APITest.class.getClassLoader().getResourceAsStream(file);
            return IOUtils.toString(stream);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable load file " + file);
        }
    }

    /**
     * Generate a random string with expected length
     *
     * @param length The length of the expected String
     * @return A randome string with expected length
     */
    public static String getRandomString(String length) {
        String sources = "abcdefghijklmnopqistuvwxyz0123456789ABCDEFGHIJKLMNOPQISTUVWXYZ";
        char[] chars = sources.toCharArray();
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < Integer.parseInt(length); i++) {
            sb.append(chars[rand.nextInt(chars.length)]);
        }

        return sb.toString();
    }


    public static String getEnvfile() {
        return envfile;
    }


    public static void setEnvfile(String envfile) {
        APITest.envfile = envfile;
    }

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
     * 设置安全header
     *
     * @param request
     * @return
     */
    public APIRequest setAuthHeader(APIRequest request) {
        return request.header("Cookie", "JSESSIONID=" + token);
    }
}
