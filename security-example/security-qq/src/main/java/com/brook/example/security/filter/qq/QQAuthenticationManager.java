package com.brook.example.security.filter.qq;

import com.brook.example.security.domain.QQUser;
import com.brook.example.security.utils.JsonMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class QQAuthenticationManager implements AuthenticationManager {

    private static final List<GrantedAuthority> AUTHORITIES = new ArrayList<>();

    /**
     * 获取 QQ 登录信息的 API 地址
     */
    private final static String userInfoUri = "https://graph.qq.com/user/get_user_info";

    /**
     * 获取 QQ 用户信息的地址拼接
     */
    private final static String USER_INFO_API = "%s?access_token=%s&oauth_consumer_key=%s&openid=%s";

    static {
        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        if (auth.getName() != null && auth.getCredentials() != null) {
            QQUser user = getUserInfo(auth.getName(), (String) (auth.getCredentials()));
            return new UsernamePasswordAuthenticationToken(user,
                null, AUTHORITIES);
        }
        throw new BadCredentialsException("Bad Credentials");
    }

    private QQUser getUserInfo(String accessToken, String openId) {
        String url = String.format(USER_INFO_API, userInfoUri, accessToken, QQAuthenticationFilter.clientId, openId);
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BadCredentialsException("Bad Credentials!");
        }
        String resultText = document.text();
        Map<String,String> result = JsonMapper.INSTANCE.fromJson(resultText, Map.class);

        QQUser user = new QQUser();
        user.setNickname(result.get("nickname"));
        user.setGender(result.get("gender"));
        user.setProvince(result.get("province"));
        user.setYear(result.get("year"));
        user.setAvatar(result.get("figureurl_qq_2"));

        return user;
    }

}