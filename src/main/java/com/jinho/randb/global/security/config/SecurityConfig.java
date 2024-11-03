package com.jinho.randb.global.security.config;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.global.jwt.utils.JwtAuthorizationFilter;
import com.jinho.randb.global.jwt.utils.JwtLoginFilter;
import com.jinho.randb.global.jwt.utils.JwtProvider;
import com.jinho.randb.global.security.oauth2.CustomOauth2Handler;
import com.jinho.randb.global.security.oauth2.CustomOauth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AccountRepository accountRepository;
    private final CustomOauth2Handler customOauth2Handler;
    private final CustomOauth2Service customOauth2Service;
    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {

        // AuthenticationManager 가져오기
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        //csrf disable
        http
                .csrf((auth) -> auth.disable());
        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());
        // cors 설정
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())); // CORS 설정 적용
        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());
        //oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOauth2Service)));
        //경로별 인가 작업
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(customOauth2Handler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOauth2Service)
                        )
                );
        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));



        // 필터 설정: JWT 인증 및 인가 필터 추가
        http
                .addFilterBefore(new JwtLoginFilter(authenticationManager, jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager, accountRepository, jwtProvider), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(restAuthenticationProvider);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();            // build() 는 최초 한번 만 호출해야 한다

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*","/swagger-ui/**","/api-docs/**").permitAll()
                        .requestMatchers("/signup","api/login*","rest/main","/").permitAll()// /main 경로는 로그인 없이 접근 허용
                        .requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers("/main","/posts", "/signup").permitAll() // /main 경로는 모두 접근 가능
                        .requestMatchers(HttpMethod.POST,"/api/join").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/posts").hasAuthority("ROLE_USER")  // 게시물 작성은 ROLE_USER 권한 필요
                        .requestMatchers(HttpMethod.POST, "/api/user/opinions").hasAuthority("ROLE_USER") //의견작성은 ROLE_USER 권한 필요
                        .requestMatchers("/api/user").hasAuthority("ROLE_USER")
                        .requestMatchers("/api/manager").hasAuthority("ROLE_MANAGER")
                        .requestMatchers("/api/admin").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )

                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(restAuthenticationFilter(http, authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager)
                .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                    .accessDeniedHandler(new RestAccessDeniedHandler())
                )
        ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Arrays.asList("*"));        //모든 요청의 허용
        config.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT"));
        config.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}