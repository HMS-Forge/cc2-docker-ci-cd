package xyz.vanez.tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import xyz.vanez.tracker.dto.TrainModelDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
class TrainModelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAndGetModel() throws Exception {
        TrainModelDto dto = new TrainModelDto();
        dto.setName("VL80");
        dto.setManufacturer("NEVZ");
        dto.setYearOfProduction(1979);
        dto.setPowerKw(6520);
        dto.setMaxSpeedKmh(110);

        String response = mockMvc.perform(post("/api/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("VL80"))
                .andReturn().getResponse().getContentAsString();

        TrainModelDto created = objectMapper.readValue(response, TrainModelDto.class);
        Integer id = created.getId();

        mockMvc.perform(get("/api/models/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("VL80"));
    }

    @Test
    void getAllModels_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/models"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void updateModel_ShouldWork() throws Exception {
        TrainModelDto dto = new TrainModelDto();
        dto.setName("Initial");
        dto.setManufacturer("Test");
        String createResponse = mockMvc.perform(post("/api/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        TrainModelDto created = objectMapper.readValue(createResponse, TrainModelDto.class);

        created.setName("Updated");
        mockMvc.perform(put("/api/models/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void deleteModel_ShouldReturnNoContent() throws Exception {
        TrainModelDto dto = new TrainModelDto();
        dto.setName("ToDelete");
        dto.setManufacturer("Test");
        String createResponse = mockMvc.perform(post("/api/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        TrainModelDto created = objectMapper.readValue(createResponse, TrainModelDto.class);

        mockMvc.perform(delete("/api/models/" + created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/models/" + created.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void createModel_InvalidData_ShouldReturnBadRequest() throws Exception {
        TrainModelDto invalid = new TrainModelDto();
        invalid.setName("");
        mockMvc.perform(post("/api/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Ошибка валидации"));
    }
}