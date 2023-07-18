//package org.springlogginghelper;
//
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springlogginghelper.sample.controller.TestController;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@WebMvcTest(TestController.class)
//public class LoggingAspectTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private JSONLogging jsonLogging;
//
//    @Test
//    public void whenCallTestEndpoint_thenAspectIsInvoked() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/test"))
//                .andExpect(status().isOk());
//
//        //verify(jsonLogging).responseLogging(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
//    }
//}
//
