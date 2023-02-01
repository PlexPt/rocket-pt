package com.rocketpt.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.rocketpt.server.util.JsonUtils;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.rocketpt.server.Constants.TOKEN;
import static com.rocketpt.server.Constants.TOKEN_HEADER_NAME;

/**
 * @author plexpt
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testLoginWhenSuccessThenStatus200() throws Exception {
        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                       {
                         "username": "admin",
                         "password": "123456"
                        }
                    """))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("username", is("admin")));
    }

    @Test
    void testLoginWhenPasswordErrorThenStatus401() throws Exception {
        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                       {
                         "username": "admin",
                         "password": "admin"
                        }
                    """))
            .andExpect(status().is4xxClientError())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("code", is(1004)));
    }

    @Test
    void testLogout() throws Exception {
        String json = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                       {
                         "username": "guest",
                         "password": "guest"
                        }
                    """))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();
        mvc.perform(post("/logout").header(TOKEN_HEADER_NAME, JsonUtils.parseToMap(json).get("token"))).andExpect(status().isOk());
    }

    @Test
    void testUserInfo() throws Exception {
        mvc.perform(get("/userinfo")
                .header(TOKEN_HEADER_NAME, TOKEN)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("username", is("admin")));
    }

}
