package xyz.vanez.tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import xyz.vanez.tracker.dto.SightingDto;
import xyz.vanez.tracker.dto.TrainInstanceDto;
import xyz.vanez.tracker.dto.TrainModelDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class SightingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Integer createInstance() throws Exception {
        // Модель
        TrainModelDto modelDto = new TrainModelDto();
        modelDto.setName("TestModel");
        modelDto.setManufacturer("Test");
        String modelResp = mockMvc.perform(post("/api/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Integer modelId = objectMapper.readValue(modelResp, TrainModelDto.class).getId();

        // Экземпляр
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