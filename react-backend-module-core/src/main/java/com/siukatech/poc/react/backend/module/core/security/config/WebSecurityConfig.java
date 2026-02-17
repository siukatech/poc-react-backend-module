package com.siukatech.poc.react.backend.module.core.security.config;

import com.siukatech.poc.react.backend.module.core.security.filter.AuthorizationDataFilter;
import com.siukatech.poc.react.backend.module.core.security.filter.ExceptionHandlerFilter;
import com.siukatech.poc.react.backend.module.core.security.handler.KeycloakLogoutHandler;
import com.siukatech.poc.react.backend.module.core.security.resourceserver.MyJwtAuthenticationConverter;
import com.siukatech.poc.react.backend.module.core.security.resourceserver.MyOpaqueTokenAuthenticationConverter;
import com.siukatech.poc.react.backend.module.core.security.resourceserver.MyOpaqueTokenIntrospector;
import com.siukatech.poc.react.backend.module.core.web.helper.PublicControllerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Some beans have higher priority.
 * They are restructured to {@link AuthorizationDataProviderConfig}.
 *
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
//@ConditionalOnProperty(name = "", havingValue = "http", matchIfMissing = true)
public class WebSecurityConfig {

////    private final ObjectMapper objectMapper;
////    private final UserService userService;
////    private final AppCoreProp appCoreProp;
//    private final KeycloakLogoutHandler keycloakLogoutHandler;
////    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;
//    private final MyJwtAuthenticationConverter myJwtAuthenticationConverter;
//    private final AuthorizationDataFilter authorizationDataFilter;
////    private final OAuth2ClientProperties oAuth2ClientProperties;
////    private final OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp;
//    private final MyOpaqueTokenIntrospector opaqueTokenIntrospector;
//    private final MyOpaqueTokenAuthenticationConverter opaqueTokenAuthenticationConverter;
//    private final AuthenticationEntryPoint authenticationEntryPoint;

    public WebSecurityConfig(
////            ObjectMapper objectMapper
////            , UserService userService
////            , AppCoreProp appCoreProp
////            ,
//            KeycloakLogoutHandler keycloakLogoutHandler
////            , OAuth2ResourceServerProperties oAuth2ResourceServerProperties
//            , MyJwtAuthenticationConverter myJwtAuthenticationConverter
//            , AuthorizationDataFilter authorizationDataFilter
////            , OAuth2ClientProperties oAuth2ClientProperties
////            , OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp
//            , MyOpaqueTokenIntrospector opaqueTokenIntrospector
//            , MyOpaqueTokenAuthenticationConverter opaqueTokenAuthenticationConverter
//            , @Qualifier("delegatedAuthenticationEntryPoint") AuthenticationEntryPoint authenticationEntryPoint
    ) {
////        this.objectMapper = objectMapper;
////        this.userService = userService;
////        this.appCoreProp = appCoreProp;
//        this.authorizationDataFilter = authorizationDataFilter;
//        this.keycloakLogoutHandler = keycloakLogoutHandler;
////        this.oAuth2ResourceServerProperties = oAuth2ResourceServerProperties;
//        this.myJwtAuthenticationConverter = myJwtAuthenticationConverter;
//        //
////        this.oAuth2ClientProperties = oAuth2ClientProperties;
////        this.oAuth2ResourceServerExtProp = oAuth2ResourceServerExtProp;
//        //
//        this.opaqueTokenIntrospector = opaqueTokenIntrospector;
//        this.opaqueTokenAuthenticationConverter = opaqueTokenAuthenticationConverter;
//        this.authenticationEntryPoint = authenticationEntryPoint;
        //
        log.debug("constructor");
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

//    @Bean
//    public RestTemplate oauth2ClientRestTemplate() {
//        // This is not working - start
////        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
////        restTemplateBuilder.additionalInterceptors(new ClientHttpRequestInterceptor() {
//////            private final Logger log = LoggerFactory.getLogger(this.getClass());
////            private final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);
////
////            @Override
////            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
////                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////                log.debug("oauth2ClientRestTemplate - RestTemplateBuilder - ClientHttpRequestInterceptor - intercept - "
////                                + "authentication.getName: [{}], authentication.getCredentials: [{}]"
////                        , authentication.getName(), authentication.getCredentials());
//////                String jwtToken = authentication.getCredentials().toString();
//////                request.getHeaders().set(HttpHeaders.AUTHORIZATION, jwtToken);
//////                return null;
////                return execution.execute(request, body);
////            }
////        });
////        RestTemplate restTemplate = restTemplateBuilder.build();
//        RestTemplate restTemplate = new RestTemplate();
//        AtomicInteger formHttpMessageConverterCount = new AtomicInteger();
//        // This is not working - end
////        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter(this.objectMapper));
//        restTemplate.getMessageConverters().stream().forEach(httpMessageConverter -> {
//            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
//////                ((MappingJackson2HttpMessageConverter) httpMessageConverter)
////                ObjectMapper objectMapper =
////                        mappingJackson2HttpMessageConverter
////                                .getObjectMapper();
////
////                objectMapper = objectMapper
////                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
////
////                        // ignore unknown json properties to prevent HttpMessageNotReadableException
////                        // https://stackoverflow.com/a/5455563
////                        .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
//////                        .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
////                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//////                        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
////                ;
//                mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
//            }
//            if (httpMessageConverter instanceof FormHttpMessageConverter) {
//                formHttpMessageConverterCount.getAndIncrement();
//            }
//        });
//        if (formHttpMessageConverterCount.get() == 0) {
//            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
//        }
//        restTemplate.getInterceptors().add(new OAuth2ClientHttpRequestInterceptor());
//        log.debug("oauth2ClientRestTemplate - formHttpMessageConverterCount.get: [{}]"
//                        + ", restTemplate.toString: [{}]"
//                , formHttpMessageConverterCount.get(), restTemplate.toString()
//        );
//        return restTemplate;
//    }

    /**
     * Configure corsConfigurationSource instead of corsFilter
     * client-app does not need to implement this
     *
     * // This is not working.
     * This requires to set in HttpSecurity because Spring Security Is Overriding CORS
     * If your app uses Spring Security, then:
     * CorsRegistry in WebMvcConfigurer is ignored unless CORS is enabled in Security.
     * You must explicitly enable CORS in Security config.
     * Without .cors() in Security → CORS headers will NOT be sent.
     *
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("corsConfigurationSource - start");
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(
                //Arrays.asList("http://localhost:3000", "http://localhost:28080")
                Arrays.asList("*")
        );
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setAllowedMethods(Arrays.asList(HttpMethod.HEAD.name()
//                , HttpMethod.GET.name()
//                , HttpMethod.POST.name()
//                , HttpMethod.PUT.name()
//                , HttpMethod.DELETE.name()
//                , HttpMethod.PATCH.name()
//                , HttpMethod.OPTIONS.name()));
        corsConfiguration.setAllowedMethods(Arrays.stream(HttpMethod.values()).map(HttpMethod::name).collect(Collectors.toList()));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        log.info("corsConfigurationSource - end");
        return urlBasedCorsConfigurationSource;
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        final CorsConfiguration config = new CorsConfiguration();
//        //config.setAllowCredentials(true);
//        config.applyPermitDefaultValues();
////        config.setAllowedOrigins(Collections.singletonList("*"));
////        config.setAllowedHeaders(Collections.singletonList("*"));
//        config.setAllowedMethods(Arrays.stream(HttpMethod.values()).map(HttpMethod::name).collect(Collectors.toList()));
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http
            , KeycloakLogoutHandler keycloakLogoutHandler
//            , OAuth2ResourceServerProperties oAuth2ResourceServerProperties
            , MyJwtAuthenticationConverter myJwtAuthenticationConverter
            , AuthorizationDataFilter authorizationDataFilter
//            , OAuth2ClientProperties oAuth2ClientProperties
//            , OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp
            , MyOpaqueTokenIntrospector opaqueTokenIntrospector
            , MyOpaqueTokenAuthenticationConverter opaqueTokenAuthenticationConverter
//            , @Qualifier("delegatedAuthenticationEntryPoint") AuthenticationEntryPoint delegatedAuthenticationEntryPoint
            , AuthenticationEntryPoint delegatedAuthenticationEntryPoint
            , ExceptionHandlerFilter exceptionHandlerFilter
    ) throws Exception {
////        http.csrf().disable();
//        http.csrf(new Customizer<CsrfConfigurer<HttpSecurity>>() {
//            @Override
//            public void customize(CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
//                httpSecurityCsrfConfigurer.disable();
//            }
//        });
        // can be rewritten in lambda way
        http.csrf(csrfConfigurer -> csrfConfigurer.disable());

        // cors
        http.cors(corsConfigurer -> corsConfigurer.configurationSource(this.corsConfigurationSource()));

        // The AntPathRequestMatcher list should match to WebMvcConfig.addInterceptors
        http.authorizeHttpRequests(requests -> requests
//                .requestMatchers(
//                        "/"
//                        , "/login"
//                        , "/v*/public/**"
//                )
                        .requestMatchers(RequestMatchers.anyOf(
                                AntPathRequestMatcher.antMatcher("/")
                                , AntPathRequestMatcher.antMatcher("/login")
                                , AntPathRequestMatcher.antMatcher("/logout")
                                , AntPathRequestMatcher.antMatcher("/error")
                                , AntPathRequestMatcher.antMatcher("/actuator/**")

                                // Only /v*/public/** is allowed to permit without security checking
//                                , AntPathRequestMatcher.antMatcher("/v*" + PublicController.REQUEST_MAPPING_URI_PREFIX + "/**")
                                , AntPathRequestMatcher.antMatcher(PublicControllerHelper.resolveExcludePath())

                                // This is required to add HttpMethod.OPTIONS here
                                // Preflight-request failed reason is missing WebMvcConfig which implements WebMvcConfigurer,
                                // NOT WebMvcConfigurationSupport, the WebMvcConfigurationSupport is obsolete
                                , AntPathRequestMatcher.antMatcher(HttpMethod.OPTIONS, "/v*/**")

                        ))
                        .permitAll()
                        .anyRequest()
                        //.fullyAuthenticated()
                        .authenticated()
        )
//                .antMatchers("/customers*")
//                .hasRole("USER")
//                .anyRequest()
//                .permitAll()
        ;
//        http.oauth2Login()
//                .and()
//                .logout()
//                .addLogoutHandler(keycloakLogoutHandler)
//                .logoutSuccessUrl("/login")
//                .permitAll()
//        ;
//        http.oauth2Login(new Customizer<OAuth2LoginConfigurer<HttpSecurity>>() {
//            @Override
//            public void customize(OAuth2LoginConfigurer<HttpSecurity> httpSecurityOAuth2LoginConfigurer) {
//                httpSecurityOAuth2LoginConfigurer
//                        .permitAll();
//            }
//        });
        http.oauth2Login(auth2LoginConfigurer -> {
            auth2LoginConfigurer.authorizationEndpoint(authorizationEndpointConfig -> {
                //authorizationEndpointConfig.baseUri("/**");
            })
            ;
            try {
                auth2LoginConfigurer.init(http);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
////        //http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
////        http.oauth2ResourceServer()
////                .jwt()
////                .jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)
////        ;
////        http.oauth2ResourceServer(new Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>>() {
////            @Override
////            public void customize(OAuth2ResourceServerConfigurer<HttpSecurity> httpSecurityOAuth2ResourceServerConfigurer) {
////                httpSecurityOAuth2ResourceServerConfigurer.jwt(new Customizer<OAuth2ResourceServerConfigurer<org.springframework.security.config.annotation.web.builders.HttpSecurity>.JwtConfigurer>() {
////                    @Override
////                    public void customize(OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer jwtConfigurer) {
////                        jwtConfigurer
////                                .jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)
////                                ;
////                    }
////                });
////            }
////        });
        http.oauth2ResourceServer(oAuth2ResourceServerConfigurer ->
                oAuth2ResourceServerConfigurer
//                        .jwt(jwtConfigurer -> jwtConfigurer
//                                .jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)
//                        )
                        .opaqueToken(opaqueTokenConfigurer -> opaqueTokenConfigurer
                                .introspector(
//                                        opaqueTokenIntrospector()
                                        opaqueTokenIntrospector
                                )
                                        .authenticationConverter(opaqueTokenAuthenticationConverter)
                        )
        );
//        http.oauth2ResourceServer(Customizer.withDefaults());

        // Add exceptionHandlerFilter before BearerTokenAuthenticationFilter
        http.addFilterBefore(exceptionHandlerFilter, BearerTokenAuthenticationFilter.class);
        http.addFilterAfter(authorizationDataFilter, BasicAuthenticationFilter.class);

        http.logout(logoutConfigurer -> logoutConfigurer
                        .addLogoutHandler(keycloakLogoutHandler)
//                .logoutRequestMatcher(AntPathRequestMatcher.antMatcher("/logout"))
                        //
                        // Reference:
                        // https://stackoverflow.com/a/38461866
                        // https://baeldung.com/spring-security-logout
                        // return http-status instead of doing redirect
//                .logoutSuccessUrl("/")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
        );

        http.sessionManagement(sessionManagementConfigurer ->
                sessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        log.debug("filterChain - http.exceptionHandling - delegatedAuthenticationEntryPoint: [{}]"
                , delegatedAuthenticationEntryPoint);
        http.exceptionHandling(exceptionHandlingConfigurer ->
                exceptionHandlingConfigurer
                        .authenticationEntryPoint(delegatedAuthenticationEntryPoint)
        );

        return http.build();
    }
//
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return (token -> {
//            try {
//                String issuerUri = ResourceServerUtil.getIssuerUri(token);
//                String clientName = oAuth2ClientProperties.getProvider().entrySet().stream()
//                        .filter(entry -> entry.getValue().getIssuerUri().equals(issuerUri))
//                        .map(entry -> entry.getKey())
//                        .findFirst()
//                        .orElse(null)
//                        ;
//                OAuth2ClientProperties.Registration registration = oAuth2ClientProperties.getRegistration().get(clientName);
//                OAuth2ResourceServerProperties.Jwt jwt = oAuth2ResourceServerExtProp.getJwt().entrySet().stream()
//                        .filter(entry -> entry.getKey().equals(clientName))
//                        .map(entry -> entry.getValue())
//                        .findFirst()
//                        .orElse(null)
//                        ;
////                NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
////                        .withIssuerLocation(oAuth2ResourceServerProperties.getJwt().getIssuerUri())
////                        .jwsAlgorithm(SignatureAlgorithm.RS512)
////                        .build();
//                NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(jwt.getIssuerUri());
//                OAuth2TokenValidator<Jwt> withIssuerJwtTokenValidator = JwtValidators.createDefaultWithIssuer(jwt.getIssuerUri());
//                OAuth2TokenValidator<Jwt> jwtDelegatingOAuth2TokenValidator = new DelegatingOAuth2TokenValidator<>(withIssuerJwtTokenValidator);
//                jwtDecoder.setJwtValidator(jwtDelegatingOAuth2TokenValidator);
//                return jwtDecoder.decode(token);
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }

//    @Bean
//    public OpaqueTokenIntrospector opaqueTokenIntrospector() {
//        MyOpaqueTokenIntrospector myOpaqueTokenIntrospector
//                = new MyOpaqueTokenIntrospector(oAuth2ClientProperties, oAuth2ResourceServerExtProp);
//        return myOpaqueTokenIntrospector;
//    }

//
//    @Bean("authorizationDataProvider")
//    @ConditionalOnProperty("app.api.my-user-info")
//    public AuthorizationDataProvider remoteAuthorizationDataProvider() {
//        log.debug("remoteAuthorizationDataProvider");
////        return new DatabaseAuthorizationDataProvider(userService);
//        return new RemoteAuthorizationDataProvider(oauth2ClientRestTemplate(), appCoreProp);
//    }
//
//    @Bean("authorizationDataProvider")
//    @ConditionalOnMissingBean
//    public AuthorizationDataProvider databaseAuthorizationDataProvider() {
//        log.debug("databaseAuthorizationDataProvider");
//        return new DatabaseAuthorizationDataProvider(userService);
//    }

}

