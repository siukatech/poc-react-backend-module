package com.siukatech.poc.react.backend.module.core.web.advice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.web.micrometer.CorrelationIdHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(classes = {GlobalExceptionHandler.class
        , GlobalExceptionHandler_controller.class}
    , properties = {
        "logging.level.com.siukatech.poc.react.backend.core.web.advice.handler=DEBUG"
        , "logging.level.com.siukatech.poc.react.backend.core.web.advice.model=DEBUG"
    }
)
@EnableWebMvc // If missing @EnableWebMvc, status 415 will be thrown
@WebAppConfiguration
public class GlobalExceptionHandlerTests {

//    @Autowired
//    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private CorrelationIdHandler correlationIdHandler;

    // @MockBean
    // private ProblemDetailExtMapper problemDetailExtMapper;

    @SpyBean
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

//    private ObjectMapper objectMapperForTest;

    @BeforeEach
    public void setup(TestInfo testInfo) {
        if (mockMvc == null) {
            mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        }
//        if (objectMapperForTest == null) {
//            objectMapperForTest = JsonMapper.builder().build();;
//        }
    }

    @Test
    void test_handleMethodArgumentNotValid() throws Exception {
        // given
        GlobalExceptionHandler_handleMethodArgumentNotValid_lv2 lv2_1 = new GlobalExceptionHandler_handleMethodArgumentNotValid_lv2();
        lv2_1.setId("B11");
        GlobalExceptionHandler_handleMethodArgumentNotValid_lv2 lv2_2 = new GlobalExceptionHandler_handleMethodArgumentNotValid_lv2();
        lv2_2.setId("B12");
        List<GlobalExceptionHandler_handleMethodArgumentNotValid_lv2> lv2List = List.of(lv2_1, lv2_2);
        GlobalExceptionHandler_handleMethodArgumentNotValid_lv1 lv1 = new GlobalExceptionHandler_handleMethodArgumentNotValid_lv1();
        lv1.setId("A11");
        lv1.setLv2(lv2_1);
        lv1.setLv2List(lv2List);
        String lv1Str = objectMapper.writeValueAsString(lv1);
//        String lv1StrForDebug = objectMapperForTest.writeValueAsString(lv1);
        log.debug("test_handleMethodArgumentNotValid - lv1Str: [{}]", lv1Str);
//        log.debug("test_handleMethodArgumentNotValid - lv1StrForDebug: [{}]", lv1StrForDebug);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/post_handleMethodArgumentNotValid")
                .content(lv1Str)
                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
                ;

        // then
        MvcResult mvcResult = mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("errorDetails")))
                .andReturn();

    }

}
