package xyz.vanez.tracker.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void trainModelDto_Valid_ShouldPass() {
        TrainModelDto dto = new TrainModelDto();
        dto.setName("VL80");
        dto.setManufacturer("NEVZ");
        dto.setYearOfProduction(1979);
        dto.setPowerKw(6520);
        dto.setMaxSpeedKmh(110);

        Set<ConstraintViolation<TrainModelDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void trainModelDto_BlankName_ShouldFail() {
        TrainModelDto dto = new TrainModelDto();
        dto.setName("");
        Set<ConstraintViolation<TrainModelDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Название модели не может быть пустым");
    }

    @Test
    void trainInstanceDto_MissingModelId_ShouldFail() {
        TrainInstanceDto dto = new TrainInstanceDto();
        dto.setSerialNumber("S123");
        Set<ConstraintViolation<TrainInstanceDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("ID модели обязателен");
    }

    @Test
    void sightingDto_FutureDate_ShouldFail() {
        SightingDto dto = new SightingDto();
        dto.setRegion("Reg");
        dto.setSightingDate(LocalDate.now().plusDays(1));
        dto.setInstanceId(1);
        Set<ConstraintViolation<SightingDto>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("не может быть в будущем"));
    }
}