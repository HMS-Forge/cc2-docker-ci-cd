package xyz.vanez.tracker.service;

import xyz.vanez.tracker.dto.TrainModelDto;
import xyz.vanez.tracker.exception.ResourceNotFoundException;
import xyz.vanez.tracker.mapper.TrainModelMapper;
import xyz.vanez.tracker.model.TrainModel;
import xyz.vanez.tracker.repository.TrainModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainModelServiceTest {

    @Mock
    private TrainModelRepository repository;

    @Mock
    private TrainModelMapper mapper;

    @InjectMocks
    private TrainModelService service;

    private TrainModel model;
    private TrainModelDto dto;

    @BeforeEach
    void setUp() {
        model = new TrainModel();
        model.setId(1);
        model.setName("EP20");
        model.setManufacturer("NEVZ");
        model.setYearOfProduction(2012);
        model.setPowerKw(8800);
        model.setMaxSpeedKmh(200);

        dto = new TrainModelDto();
        dto.setId(1);
        dto.setName("EP20");
        dto.setManufacturer("NEVZ");
        dto.setYearOfProduction(2012);
        dto.setPowerKw(8800);
        dto.setMaxSpeedKmh(200);
    }

    @Test
    void getAll_ShouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(model));
        when(mapper.toDto(model)).thenReturn(dto);

        List<TrainModelDto> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("EP20");
        verify(repository).findAll();
    }

    @Test
    void getById_WhenExists_ShouldReturnDto() {
        when(repository.findById(1)).thenReturn(Optional.of(model));
        when(mapper.toDto(model)).thenReturn(dto);

        TrainModelDto result = service.getById(1);

        assertThat(result.getName()).isEqualTo("EP20");
    }

    @Test
    void getById_WhenNotExists_ShouldThrow() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Модель с id=99 не найдена");
    }

    @Test
    void create_ShouldSaveAndReturnDto() {
        TrainModelDto inputDto = new TrainModelDto();
        inputDto.setName("VL80");

        TrainModel entity = new TrainModel();
        TrainModel savedEntity = model; // model имеет id=1

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDto(savedEntity)).thenReturn(dto);

        TrainModelDto result = service.create(inputDto);

        assertThat(result.getName()).isEqualTo("EP20");
        verify(repository).save(entity);
        verify(mapper).toEntity(inputDto);
        verify(mapper).toDto(savedEntity);
    }

    @Test
    void update_WhenExists_ShouldUpdateAndReturn() {
        TrainModelDto updateDto = new TrainModelDto();
        updateDto.setName("VL80m");
        updateDto.setPowerKw(7000);

        when(repository.findById(1)).thenReturn(Optional.of(model));
        doNothing().when(mapper).updateEntityFromDto(updateDto, model);
        when(repository.save(model)).thenReturn(model);
        when(mapper.toDto(model)).thenReturn(dto);

        TrainModelDto result = service.update(1, updateDto);

        assertThat(result.getName()).isEqualTo("EP20");
        verify(mapper).updateEntityFromDto(updateDto, model);
        verify(repository).save(model);
    }

    @Test
    void update_WhenNotExists_ShouldThrow() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99, new TrainModelDto()))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void delete_WhenExists_ShouldDelete() {
        when(repository.existsById(1)).thenReturn(true);
        doNothing().when(repository).deleteById(1);

        service.delete(1);

        verify(repository).deleteById(1);
    }

    @Test
    void delete_WhenNotExists_ShouldThrow() {
        when(repository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).deleteById(any());
    }
}