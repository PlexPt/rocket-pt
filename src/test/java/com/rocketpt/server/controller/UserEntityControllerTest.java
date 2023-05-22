package com.rocketpt.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class UserEntityControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testFindUsers() throws Exception {
        mvc.perform(get("/users")
                .header(TOKEN_HEADER_NAME, TOKEN)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("total", greaterThanOrEqualTo(10)));
    }

    @Test
    void testCreateUser() throws Exception {
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header(TOKEN_HEADER_NAME, TOKEN)
                .content("""
                    {
                      "username": "someusertest",
                      "fullName": "rocketpt user full name",
                      "gender": "MALE",
                      "avatar": "https://picsum.photos/id/237/100",
                      "organizationId": 1
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("username", is("admin3_so_good")));
    }

    @Test
    void testUpdateUser() throws Exception {
        mvc.perform(put("/users/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .header(TOKEN_HEADER_NAME, TOKEN)
                .content("""
                    {
                      "fullName": "张三",
                      "gender": "FEMALE",
                      "avatar": "https://picsum.photos/id/237/100",
                      "organizationId": 1
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("username", is("admin")));
    }

    @Test
    void testDeleteUser() throws Exception {
        mvc.perform(delete("/users/{userId}", 301)
                .header(TOKEN_HEADER_NAME, TOKEN)
            )
            .andExpect(status().isNoContent());
    }

    @Test
    void testLockUser() throws Exception {
        mvc.perform(post("/users/{userId}:lock", 302)
                .header(TOKEN_HEADER_NAME, TOKEN)
            )
            .andExpect(status().isOk());
    }

    @Test
    void testUnlockUser() throws Exception {
        mvc.perform(post("/users/{userId}:unlock", 302)
                .header(TOKEN_HEADER_NAME, TOKEN)
            )
            .andExpect(status().isOk());
    }

}
