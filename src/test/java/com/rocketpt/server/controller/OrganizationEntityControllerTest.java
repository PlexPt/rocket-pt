package com.rocketpt.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.rocketpt.server.util.JsonUtils;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
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
public class OrganizationEntityControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testFindOrgTree() throws Exception {
        mvc.perform(get("/organizations/tree")
                .queryParam("parentId", "1")
                .header(TOKEN_HEADER_NAME, TOKEN)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    void testFindOrgUsers() throws Exception {
        mvc.perform(get("/organizations/{organizationId}/users", 1)
                .header(TOKEN_HEADER_NAME, TOKEN)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("total", greaterThanOrEqualTo(100)));
    }

    @Test
    void testCreateOrganization() throws Exception {
        mvc.perform(post("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .header(TOKEN_HEADER_NAME, TOKEN)
                .content("""
                    {
                      "name": "测试节点223",
                      "type": "DEPARTMENT",
                      "parentId": 1
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("name", is("测试节点223")));
    }

    @Test
    void testUpdateOrganization() throws Exception {
        mvc.perform(put("/organizations/{organizationId}", 3)
                .contentType(MediaType.APPLICATION_JSON)
                .header(TOKEN_HEADER_NAME, TOKEN)
                .content("""
                    {
                      "name": "测试节点2023",
                      "type": "DEPARTMENT",
                      "parentId": 1
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("name", is("测试节点2023")));
    }

    @Test
    void testDeleteOrganizationWhenHasUsersThenStatus400() throws Exception {
        mvc.perform(delete("/organizations/{organizationId}", 2)
                .header(TOKEN_HEADER_NAME, TOKEN)
            )
            .andExpect(status().is4xxClientError());
    }

    @Test
    void testDeleteOrganizationWhenNoUsersThenStatus204() throws Exception {
        String json = mvc.perform(post("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .header(TOKEN_HEADER_NAME, TOKEN)
                .content("""
                    {
                      "name": "测试节点删除",
                      "type": "DEPARTMENT",
                      "parentId": 1
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("name", is("测试节点删除")))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();

        mvc.perform(delete("/organizations/{organizationId}", JsonUtils.parseToMap(json).get("id"))
                .header(TOKEN_HEADER_NAME, TOKEN)
            )
            .andExpect(status().isNoContent());
    }


}
