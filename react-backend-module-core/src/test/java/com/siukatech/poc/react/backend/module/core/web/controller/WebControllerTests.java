package com.siukatech.poc.react.backend.module.core.web.controller;

import com.siukatech.poc.react.backend.module.core.AbstractWebTests;
import com.siukatech.poc.react.backend.module.core.web.annotation.v1.PublicApiV1Controller;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Slf4j
@WebMvcTest(controllers = {WebController.class})
@AutoConfigureMockMvc(addFilters = false)
public class WebControllerTests extends AbstractWebTests {

    protected static final Logger log = LoggerFactory.getLogger(WebControllerTests.class);
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;
    @MockBean
    private OAuth2ClientProperties oAuth2ClientProperties;

    @Test
    public void index_basic() throws Exception {
        // given


        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(new URI(PublicApiV1Controller.REQUEST_MAPPING_URI_PREFIX + "/"))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("external")))
                .andReturn();
    }

}
