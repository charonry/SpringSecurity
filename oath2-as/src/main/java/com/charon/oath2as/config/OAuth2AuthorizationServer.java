package com.charon.oath2as.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import javax.annotation.Resource;

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

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
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
                // 授权码模式
                .authorizedGrantTypes("authorization_code","password","implicit","client_credentials")
                // 可授权的 Scope  Mycode Wm2wzn
                .scopes("all");
    }
}
