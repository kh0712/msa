package study.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import study.userservice.domain.User;
import study.userservice.domain.UserRepository;
import study.userservice.dto.UserDto;
import study.userservice.service.UserService;
import study.userservice.vo.RequestLogin;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserRepository userRepository;
    private final String secret;
    private final Long exp;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserRepository userRepository,
                                Environment env) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.secret = env.getProperty("token.secret");
        this.exp = Long.parseLong(Objects.requireNonNull(env.getProperty("token.expiration_time")));
    }

    /**
     *
     * Request Login 요청의 Body 를
     * UsernamePassword Token 으로 파싱해서 인증 요청하는 메서드
     */

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            //json 형태의 request body 를 객체로 매핑한다.
            //내부적으로 Input Stream 을 이용해서 json parser 를 만들어서 파싱한다.
            RequestLogin requestBody = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            Authentication token = new UsernamePasswordAuthenticationToken( requestBody.getEmail(),
                                                                            requestBody.getPassword(),
                                                                            new ArrayList<>());
            //LoadByUsername 호출
            return getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String userName = authResult.getName();
        User user = userRepository.findByEmail(userName);

        String token = createToken(user);

        response.setHeader("token", token);
        response.setHeader("userId", user.getUserId());
    }

    private String createToken(User user) {

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                    .setSubject(user.getUserId())
                    .setExpiration(new Date(System.currentTimeMillis()+exp))
                    .signWith(key,SignatureAlgorithm.HS512)
                    .compact();
    }


}
