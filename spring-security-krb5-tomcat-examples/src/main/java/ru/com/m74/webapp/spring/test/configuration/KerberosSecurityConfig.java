package ru.com.m74.webapp.spring.test.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.kerberos.authentication.KerberosAuthenticationProvider;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosClient;

/**
 * This sample demonstrates how server is able to authenticate user against kerberos environment using his
 * credentials passed in via a form login.
 *
 * @see <a href="http://docs.spring.io/autorepo/docs/spring-security-kerberos/1.0.2.BUILD-SNAPSHOT/reference/htmlsingle/#samples-sec-server-client-auth">link</a>
 * @author mixam
 * @since 15.05.16 19:44
 */
@Configuration
@EnableWebSecurity
//@Profile("kerberos")
public class KerberosSecurityConfig extends SimpleSecurityConfig {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(kerberosAuthenticationProvider());
    }

    @Bean
    public KerberosAuthenticationProvider kerberosAuthenticationProvider() {
        KerberosAuthenticationProvider provider = new KerberosAuthenticationProvider();
        SunJaasKerberosClient client = new SunJaasKerberosClient();
        client.setDebug(true);
        provider.setKerberosClient(client);
        provider.setUserDetailsService(dummyUserDetailsService());
        return provider;
    }

    @Bean
    public UserDetailsService dummyUserDetailsService() {
        return username -> new User(username, "", true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_USER"));
    }
}
