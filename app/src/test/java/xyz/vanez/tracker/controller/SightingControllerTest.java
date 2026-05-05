package xyz.vanez.tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import xyz.vanez.tracker.dto.SightingDto;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class SightingControllerTest {

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

    private Integer createInstance() throws Exception {
        // Создаём модель
        TrainModelDto modelDto = new TrainModelDto();
        modelDto.setName("TestModel");
        modelDto.setManufacturer("Test");
        String modelResp = mockMvc.perform(post("/api/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Integer modelId = objectMapper.readValue(modelResp, TrainModelDto.class).getId();

        // Создаём экземпляр
        TrainInstanceDto instanceDto = new TrainInstanceDto();
        instanceDto.setSerialNumber("INST-001");
        instanceDto.setModelId(modelId);
        String instanceResp = mockMvc.perform(post("/api/instances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instanceDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(instanceResp, TrainInstanceDto.class).getId();
    }

    @Test
    void createAndGetSighting() throws Exception {
        Integer instanceId = createInstance();

        SightingDto dto = new SightingDto();
        dto.setRegion("Московская обл.");
        dto.setSightingDate(LocalDate.now());
        dto.setInstanceId(instanceId);

        String response = mockMvc.perform(post("/api/sightings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.region").value("Московская обл."))
                .andReturn().getResponse().getContentAsString();

        SightingDto created = objectMapper.readValue(response, SightingDto.class);
        mockMvc.perform(get("/api/sightings/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.region").value("Московская обл."));
    }

    @Test
    void getSightingsByInstanceId() throws Exception {
        Integer instanceId = createInstance();

        SightingDto dto = new SightingDto();
        dto.setRegion("Region1");
        dto.setSightingDate(LocalDate.now());
        dto.setInstanceId(instanceId);
        mockMvc.perform(post("/api/sightings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/sightings?instanceId=" + instanceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].region").value("Region1"));
    }
}