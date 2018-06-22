package com.kalix.framework.core.restfulapi.test;

import com.kalix.framework.core.rest.test.APIRequest;
import com.kalix.framework.core.rest.test.APIResponse;
import com.kalix.framework.core.rest.test.APITest;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestKongClient
        extends APITest
{
    @Test
    public void testCreateAPIGateWayHS256() {
        APIResponse response = APIRequest.GET("http://localhost:8181/camel/rest/kongJwts/create_HS256")
                .header("Cookie", "JSESSIONID=" + token)
                .header("Cookie", "access_token=" + accessToken)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        System.out.println(returnString);
    }

    @Test
    public void testGetJwtTokenHS256() {
        APIResponse response = APIRequest.GET("http://localhost:8181/camel/rest/kongJwts/token_HS256")
                .header("Cookie", "JSESSIONID=" + token)
                .header("Cookie", "access_token=" + accessToken)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        System.out.println(returnString);
    }


    @Test
    public void testGetJwtResult() {
        String uri = "http://192.168.0.28:8000/jwt_test";
        APIResponse response = APIRequest.GET(uri).header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJZMjhsV0JVdHBEc2laMHo1a2xMY3NyTmtYcjd3UU5UMiIsIm5hbWUiOiJrYWxpeF9hZG1pbiIsImlhdCI6MTUyOTYyNjg5MzAwMH0.Z3uMoZDSidZPjVd8SDfu-WajWiR_fzyBm7f-Ta0EKyV6zmEaidJHnUdJHD_-hX0W-XkxOJhhKHNX7wVk7EcP9IY7TuUtsZCVgmEib9GmmWzR1lDmd8sTG1gDYOhCbBR7Ao27JYHa2qcyf_bHIDKauUIFXhfuU6x2c0hKZ75VezW6pRdZzHB6acC5XiZhHgcK75NEVPgqbWfUDbTeaLD4zcK0j-n1fbWSS9gqwXqqWMmb27a5Z-COMMNu6Al-6bSf3W590Yl5Zau2NRsIk4Am0ZCUCkh3yZpdi45hYfxLoCg_PZxVirVL7lz5wUNuZISnc7GoEUUC7oAAjJiTS_0gp1T4QEWr8Y3YrdR3b_-IwazJ9K6tPh_uzUe53lAUDXXE2RKJa5jyr7wlXt6jHi4L7TKXeMZsG1aAKe5azsiqGTHcSq3iTPNDzRGGVXO2frrx9ui_-J5eTLFbM09Eh22tDJCaJ0Jy3-yStcQM3zB78zZJq9Ji_au6k-jTCcJtX4WDR4B7RJ4182dshfQi7qj_TQ-YENPusIp3ic4IqSFrXOXH4FjHunNyWeNftWylA-rQo44nv-PCe5p2ev5X1sjclgW7CZca9lmAazu9PFE2YsS1PHW1NYIV2PGbAx4YGuKdoP2Ut5LT6KqKZ8rMHUHutNc3kNUxNJPIcy-cUjtHqIA")
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        System.out.println(returnString);
    }

    @Test
    public void testDeleteAPIGateWayHS256() {
        APIResponse response = APIRequest.GET("http://localhost:8181/camel/rest/kongJwts/clear_HS256")
                .header("Cookie", "JSESSIONID=" + token)
                .header("Cookie", "access_token=" + accessToken)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        System.out.println(returnString);
    }

    @Test
    public void testCreateAPIGateWayRS256() {
        APIResponse response = APIRequest.GET("http://localhost:8181/camel/rest/kongJwts/create_RS256")
                .header("Cookie", "JSESSIONID=" + token)
                .header("Cookie", "access_token=" + accessToken)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        System.out.println(returnString);
    }

    @Test
    public void testGetJwtTokenRS256() {
        APIResponse response = APIRequest.GET("http://localhost:8181/camel/rest/kongJwts/token_RS256")
                .header("Cookie", "JSESSIONID=" + token)
                .header("Cookie", "access_token=" + accessToken)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        System.out.println(returnString);
    }

    @Test
    public void testDeleteAPIGateWayRS256() {
        APIResponse response = APIRequest.GET("http://localhost:8181/camel/rest/kongJwts/clear_RS256")
                .header("Cookie", "JSESSIONID=" + token)
                .header("Cookie", "access_token=" + accessToken)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        System.out.println(returnString);
    }
}
