package xyz.vanez.tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import xyz.vanez.tracker.dto.TrainInstanceDto;
import xyz.vanez.tracker.dto.TrainModelDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TrainInstanceControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Integer createModel() throws Exception {
        TrainModelDto modelDto = new TrainModelDto();
        modelDto.setName("TestModel");
        modelDto.setManufacturer("Test");
        String response = mockMvc.perform(post("/api/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(response, TrainModelDto.class).getId();
    }

    @Test
    void createAndGetInstance() throws Exception {
        Integer modelId = createModel();

        TrainInstanceDto dto = new TrainInstanceDto();
        dto.setSerialNumber("TEST-001");
        dto.setModelId(modelId);

        String response = mockMvc.perform(post("/api/instances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serialNumber").value("TEST-001"))
                .andReturn().getResponse().getContentAsString();

        TrainInstanceDto created = objectMapper.readValue(response, TrainInstanceDto.class);
        mockMvc.perform(get("/api/instances/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("TEST-001"));
    }

    @Test
    void getAllInstances_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/instances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deleteInstance_ShouldReturnNoContent() throws Exception {
        Integer modelId = createModel();
        TrainInstanceDto dto = new TrainInstanceDto();
        dto.setSerialNumber("DELETE-ME");
        dto.setModelId(modelId);

        String createResponse = mockMvc.perform(post("/api/instances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        TrainInstanceDto created = objectMapper.readValue(createResponse, TrainInstanceDto.class);

        mockMvc.perform(delete("/api/instances/" + created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/instances/" + created.getId()))
                .andExpect(status().isNotFound());
    }
}