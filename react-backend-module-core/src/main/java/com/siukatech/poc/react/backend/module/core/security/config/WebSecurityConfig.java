package com.siukatech.poc.react.backend.module.core.security.config;

import com.siukatech.poc.react.backend.module.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.module.core.security.filter.AuthorizationDataFilter;
import com.siukatech.poc.react.backend.module.core.security.filter.ExceptionHandlerFilter;
import com.siukatech.poc.react.backend.module.core.security.handler.KeycloakLogoutHandler;
import com.siukatech.poc.react.backend.module.core.security.oauth2.resource.MyJwtAuthenticationConverter;
import com.siukatech.poc.react.backend.module.core.security.oauth2.resource.MyOpaqueTokenAuthenticationConverter;
import com.siukatech.poc.react.backend.module.core.security.oauth2.resource.MyOpaqueTokenIntrospector;
import com.siukatech.poc.react.backend.module.core.web.helper.PublicControllerHelper;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
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
import java.util.List;
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
////    private final OAuth2ClientExtProp oAuth2ClientExtProp;
////    private final OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp;
//    private final MyOpaqueTokenIntrospector opaqueTokenIntrospector;
//    private final MyOpaqueTokenAuthenticationConverter opaqueTokenAuthenticationConverter;
//    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final WebEndpointProperties webEndpointProperties;

    public WebSecurityConfig(
////            ObjectMapper objectMapper
////            , UserService userService
////            , AppCoreProp appCoreProp
//            ,
            WebEndpointProperties webEndpointProperties
////            ,
//            KeycloakLogoutHandler keycloakLogoutHandler
////            , OAuth2ResourceServerProperties oAuth2ResourceServerProperties
//            , MyJwtAuthenticationConverter myJwtAuthenticationConverter
//            , AuthorizationDataFilter authorizationDataFilter
////            , OAuth2ClientExtProp oAuth2ClientExtProp
////            , OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp
//            , MyOpaqueTokenIntrospector opaqueTokenIntrospector
//            , MyOpaqueTokenAuthenticationConverter opaqueTokenAuthenticationConverter
//            , @Qualifier("delegatedAuthenticationEntryPoint") AuthenticationEntryPoint authenticationEntryPoint
    ) {
////        this.objectMapper = objectMapper;
////        this.userService = userService;
////        this.appCoreProp = appCoreProp;
        this.webEndpointProperties = webEndpointProperties;
//        this.authorizationDataFilter = authorizationDataFilter;
//        this.keycloakLogoutHandler = keycloakLogoutHandler;
////        this.oAuth2ResourceServerProperties = oAuth2ResourceServerProperties;
//        this.myJwtAuthenticationConverter = myJwtAuthenticationConverter;
//        //
////        this.oAuth2ClientExtProp = oAuth2ClientExtProp;
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

    // =========================================================================
    // CHAIN 1: Public Routes (Actuator, Login, Error)
    // Completely ignores OAuth2 processing to prevent unexpected 401s.
    // =========================================================================
    @Bean
    @Order(1)
    public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
        // List all public routes explicitly
        List<String> publicPathList = List.of(
                "/", "/login", "/logout", "/error"
//                , "/actuator/**"
                , webEndpointProperties.getBasePath() + "/**"
        );
        http
            .securityMatchers(matchers -> matchers
                    .requestMatchers(publicPathList.toArray(String[]::new))
                    // Only /v*/public/** is allowed to permit without security checking
                    // "/v*" + PublicController.REQUEST_MAPPING_URI_PREFIX + "/**"
                    .requestMatchers(PublicControllerHelper.resolveExcludePath())
            )
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    // =========================================================================
    // CHAIN 2: OAuth2 Client Web Login (Authorization Code Flow)
    // Handles session-based state parameters for Web Browsers/Keycloak Login.
    // =========================================================================
    @Bean
    @Order(2)
    public SecurityFilterChain oauth2LoginFilterChain(HttpSecurity http, KeycloakLogoutHandler keycloakLogoutHandler) throws Exception {
        http
            .securityMatchers(matchers -> matchers
                    .requestMatchers("/oauth2/**", "/login/oauth2/**")
            )
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .oauth2Login(Customizer.withDefaults())
            // Logout Handling
            .logout(logout -> logout
                    .addLogoutHandler(keycloakLogoutHandler)
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            )
            // MUST BE IF_REQUIRED or ALWAYS for Auth Code flow states!
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }

    // =========================================================================
    // CHAIN 3: Main API Resource Server (Opaque Token Gateway)
    // Catches all remaining traffic and strictly enforces API authentication.
    // =========================================================================
    @Bean
    @Order(3)
    public SecurityFilterChain apiResourceServerFilterChain(HttpSecurity http
            , AuthorizationDataFilter authorizationDataFilter
            , MyOpaqueTokenIntrospector opaqueTokenIntrospector
            , MyOpaqueTokenAuthenticationConverter opaqueTokenAuthenticationConverter
            , AuthenticationEntryPoint delegatedAuthenticationEntryPoint
            , ExceptionHandlerFilter exceptionHandlerFilter
    ) throws Exception {

        // Disable CSRF for APIs
        // can be rewritten in lambda way
        http.csrf(csrf -> csrf.disable());

        // CORS configuration
        http.cors(cors -> cors.configurationSource(this.corsConfigurationSource()));

        // Cleaned up authorization rules
        // Everything left over requires Opaque Bearer token validation
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.OPTIONS, "/v*/**"))
                .permitAll()
//                .requestMatchers(AntPathRequestMatcher.antMatcher("/customers*"))
//                .hasRole("USER")
//                .anyRequest().permitAll()
                // Force authentication for everything else
                .anyRequest().authenticated()
        );

        // OAuth2 Resource Server (Bearer Tokens)
        http.oauth2ResourceServer(resourceServer -> resourceServer
                .opaqueToken(opaque -> opaque
                        .introspector(opaqueTokenIntrospector)
                        .authenticationConverter(opaqueTokenAuthenticationConverter)
                )
        );

        // Filter Injection Chain
        // Your custom filter interceptors run strictly within the API domain now
        http.addFilterBefore(exceptionHandlerFilter, BearerTokenAuthenticationFilter.class);
        http.addFilterAfter(authorizationDataFilter, BasicAuthenticationFilter.class);

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Exception Handling & Entry Points
        log.debug("apiResourceServerFilterChain - http.exceptionHandling - delegatedAuthenticationEntryPoint: [{}]"
                , delegatedAuthenticationEntryPoint);
        http.exceptionHandling(handling ->
                handling.authenticationEntryPoint(delegatedAuthenticationEntryPoint));

        return http.build();
    }


//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return (token -> {
//            try {
//                String issuerUri = ResourceServerUtil.getIssuerUri(token);
//                String clientName = oAuth2ClientExtProp.getProvider().entrySet().stream()
//                        .filter(entry -> entry.getValue().getIssuerUri().equals(issuerUri))
//                        .map(entry -> entry.getKey())
//                        .findFirst()
//                        .orElse(null)
//                        ;
//                OAuth2ClientExtProp.Registration registration = oAuth2ClientExtProp.getRegistration().get(clientName);
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
//                = new MyOpaqueTokenIntrospector(oAuth2ClientExtProp, oAuth2ResourceServerExtProp);
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

