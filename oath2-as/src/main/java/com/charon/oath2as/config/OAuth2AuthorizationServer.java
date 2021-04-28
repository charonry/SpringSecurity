package com.charon.oath2as.config;

import com.charon.base.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-25 21:18
 **/
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    MyUserDetailsService myUserDetailsService;

    @Resource
    private DataSource dataSource;

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(myUserDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }


    /**
     * 客户端模式实际上是密码模式的简化，无需配置或使用资源拥有者账号。因为它没有用户的概念，
     * 直接与授权服务器交互，通过 Client 的编号(client_id)和密码(client_secret)来保证安全性。
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //这里的配置实际上和我们在QQ互联上的注册信息，client就是APP ID，secret就是APP Key，
                // Client 账号、密码
                .withClient("client1").secret(passwordEncoder.encode("123456"))
                // 回调地址就是我们在QQ互联配置的应用回调地址。配置回调地址，选填。
                .redirectUris("http://localhost:8888/callback")
                // 授权类型
                .authorizedGrantTypes("authorization_code","password","implicit","client_credentials"
                        ,"refresh_token")
                // 可授权的 Scope
                .scopes("all")
                //token有效期设置2个小时
                .accessTokenValiditySeconds(60*60*2)
                //Refresh_token:12个小时
                .refreshTokenValiditySeconds(60*60*12);
    }
    // 获取授权码
    // http://localhost:8001/oauth/authorize?client_id=client1&redirect_uri=http://localhost:8888/callback&response_type=code&scope=all
}
