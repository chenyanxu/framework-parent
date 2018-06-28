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
        String uri = "http://192.168.1.28:8000/jwt_test";
        APIResponse response = APIRequest.GET(uri).header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJMazBlREEyYWVVRjZOclc1NTRWUW9wZWsxYzQ0cEEzYyIsIm5hbWUiOiJrYWxpeF9hZG1pbiIsImlhdCI6MTUzMDAwMTEwMzAwMH0.sjykTiyp91iw9cONOKzt09cHJxpF4Pq9xvg4jnB7Z7GK4b36KC4GHQWVO3uFVNzgFsfs6rN6Q92fUA7akNQqNxG4p9iA8F1WtCbTrz_w-A01PesqEFiDSCuqCw76fSW1HJ9DEcC25R5N_QU8VxCZCWIiI5MpVDsA788vFLGcg1XZKwQkWFC1W0oMDQu-J7fhilE5hBWxg2YtS2WckxDz3KLcump88bvDCavjjfoJWN_x81IO5Fm7lDEk-4f7isJ4cPGHN0pH1JyWt8FPKTAuPVOP344CpxEVAhtVQtS1VOU3dH1DL70swp8f4Nn_U5g_stMwX2ofjB_b42zhQaZ1CJLkptV1tUf9bUNlO9db8ufdh-KV4ZIo8O_VNdmjFFwSBXkKk1YPWM_D9_jOGrYKn5mbmPpY_XfXyGZYvYiUY4hPnkXdL5x29QDicCzGl9Mo_8vJYibOEXLmgSakPs5uboLfqXDMuRsUf52KN5YUQ5fBFwSF9BLstQibWuR_3h13Vf9_6ghM4MajmLSBlddB9QwMCZpieXsfZvql_yKVLoP2B8705AawpA2toJjm7OTWCsisBqyNb4l722sm4cfxoO4s7sM1-JakRQ0vycx-K97Ju2HZqvpV4h0-Iw8pB3xs6tN2r55F_nX-aMiKDp7f8XOCvhvbixVBGzPfKX4NyMc")
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
