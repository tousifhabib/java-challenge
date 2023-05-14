package jp.co.axa.apidemo.config;

import jp.co.axa.apidemo.filters.JwtRequestFilter;
import jp.co.axa.apidemo.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Configures the global authentication manager to use the custom user details service and password encoder.
     *
     * @param auth The AuthenticationManagerBuilder to configure.
     * @throws Exception if an error occurs during the configuration.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * Configures the password encoder used for password hashing.
     *
     * @return The configured PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the HttpSecurity for request authorization and session management.
     *
     * @param http The HttpSecurity to configure.
     * @throws Exception if an error occurs during the configuration.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/authenticate").permitAll()
                .antMatchers(
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**,",
                        "/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Exposes the AuthenticationManager bean.
     *
     * @return The AuthenticationManager bean.
     * @throws Exception if an error occurs while creating the bean.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
