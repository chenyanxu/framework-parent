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
    public String getPrivateKeyString();
    public String getPublicKeyString();
}
