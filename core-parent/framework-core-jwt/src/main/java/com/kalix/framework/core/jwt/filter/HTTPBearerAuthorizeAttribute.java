package com.kalix.framework.core.jwt.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kalix.framework.core.api.jwt.IJwtService;
import com.kalix.framework.core.util.JNDIHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator_ on 2018/5/24.
 */
public class HTTPBearerAuthorizeAttribute extends ShiroAuthenticationFilter {


}
