package com.kalix.framework.core.api.jwt;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.dto.AudienceBean;
import io.jsonwebtoken.Claims;

/**
 * @author fwb
 */
public interface IJwtService extends IService {
    public io.jsonwebtoken.Claims parseJWT(String jsonWebToken, String base64Security);
    public  String createJWT(String name, String userId, String role,
                             String audience, String issuer, long TTLMillis, String base64Security);
    public String refreshToken(String jsonWebToken);
    public AudienceBean getAudien();

    /**
     * 获取私钥串
     * @return
     */
    public String getPrivateKeyString();

    /**
     * 获取公钥串
     * @return
     */
    public String getPublicKeyString();

    /**
     * 创建基于RSA256的Jwt token
     * @param issuerKey
     * @param userName
     * @param issuedAt
     * @return
     */
    public String createJwt_RS256(String issuerKey, String userName, Long issuedAt);

    /**
     * 创建基于HS256的Jwt token
     * @param secret
     * @param isSecretBase64
     * @param issuerKey
     * @param userName
     * @param issuedAt
     * @return
     */
    public String createJwt_HS256(String secret, Boolean isSecretBase64, String issuerKey, String userName, Long issuedAt);

    /**
     * 解析基于RSA256的Jwt
     * @param jsonWebToken
     * @return
     */
    public Claims parseJwt_RS256(String jsonWebToken);
}
