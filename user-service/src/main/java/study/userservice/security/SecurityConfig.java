package study.userservice.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.userservice.domain.UserRepository;
import study.userservice.service.UserService;

@RequiredArgsConstructor
@EnableWebSecurity // 다른 빈들보다 우선해서 등록한다.
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final Environment env;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        //http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests()
                .antMatchers("/**")
                .hasIpAddress("192.168.35.196")
                .and()
                .addFilter(getAuthenticationFilter());


        http.headers().frameOptions().disable();

    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception{
        //UsernameAuthentication Filter 는 생성자로 Authentication Manager 를 주입받는다.
        return new AuthenticationFilter(authenticationManager(),
                                        userRepository,
                                        env);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
