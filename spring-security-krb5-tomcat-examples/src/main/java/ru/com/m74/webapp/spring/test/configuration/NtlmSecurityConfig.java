package ru.com.m74.webapp.spring.test.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.kerberos.authentication.KerberosServiceAuthenticationProvider;
import org.springframework.security.kerberos.client.config.SunJaasKrb5LoginConfig;
import org.springframework.security.kerberos.client.ldap.KerberosLdapContextSource;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;

/**
 * Goals of this sample:
 * <p>
 * In windows environment, User will be able to logon to application with Windows Active directory Credential which has
 * been entered during log on to windows. There should not be any ask for userid/password credentials.
 * In non-windows environment, User will be presented with a screen to provide Active directory credentials.
 * <pre>
 * Настройка сервера windows:
 *
 * setspn -A HTTP/mixam.softtest.com tomcat
 * ktpass /out c:\tomcat.keytab /mapuser tomcat@softtest.com /princ HTTP/mixam.softtest.com@SOFTTEST.COM /pass ${tomcat_password} /kvno 0 /pType KRB5_NT_PRINCIPAL /crypto rc4-hmac-nt
 *
 * tomcat: AD user
 * mixam.softtest.com: web server
 * SOFTTEST.COM: domain
 * </pre>
 *
 * @author mixam
 * @see <a href="http://docs.spring.io/autorepo/docs/spring-security-kerberos/1.0.2.BUILD-SNAPSHOT/reference/htmlsingle/#samples-sec-server-win-auth">Описание примера (eng)</a>
 * @see <a href="http://publib.boulder.ibm.com/wasce/V3.0.0/ru/using-spnego-in-ce.html">Настройка SSO (rus)</a>
 * @since 16.05.16 17:30
 */
//@Configuration
//@EnableWebSecurity
//@Profile("ntlm")
public class NtlmSecurityConfig extends SpnegoSecurityConfig {

    @Value("${app.ad-domain}")
    private String adDomain;

    @Value("${app.ad-server}")
    private String adServer;

    @Value("${app.ldap-search-base}")
    private String ldapSearchBase;

    @Value("${app.ldap-search-filter}")
    private String ldapSearchFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");

        auth
                .authenticationProvider(activeDirectoryLdapAuthenticationProvider())
                .authenticationProvider(kerberosServiceAuthenticationProvider());
    }


    @Bean
    public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        return new ActiveDirectoryLdapAuthenticationProvider(adDomain, adServer);
    }

    @Bean
    public KerberosServiceAuthenticationProvider kerberosServiceAuthenticationProvider() {
        KerberosServiceAuthenticationProvider provider = new KerberosServiceAuthenticationProvider();
        provider.setTicketValidator(sunJaasKerberosTicketValidator());
        provider.setUserDetailsService(ldapUserDetailsService());
        return provider;
    }

    @Bean
    public KerberosLdapContextSource kerberosLdapContextSource() {
        KerberosLdapContextSource contextSource = new KerberosLdapContextSource(adServer);
        contextSource.setLoginConfig(loginConfig());
        return contextSource;
    }

    @Bean
    public SunJaasKrb5LoginConfig loginConfig() {
        SunJaasKrb5LoginConfig loginConfig = new SunJaasKrb5LoginConfig();
        loginConfig.setKeyTabLocation(new FileSystemResource(keytabLocation));
        loginConfig.setServicePrincipal(servicePrincipal);
        loginConfig.setDebug(true);
        loginConfig.setIsInitiator(true);
        return loginConfig;
    }

    @Bean
    public LdapUserDetailsService ldapUserDetailsService() {
        FilterBasedLdapUserSearch userSearch =
                new FilterBasedLdapUserSearch(ldapSearchBase, ldapSearchFilter, kerberosLdapContextSource());
        LdapUserDetailsService service = new LdapUserDetailsService(userSearch);
        service.setUserDetailsMapper(new LdapUserDetailsMapper());
        return service;
    }

}
