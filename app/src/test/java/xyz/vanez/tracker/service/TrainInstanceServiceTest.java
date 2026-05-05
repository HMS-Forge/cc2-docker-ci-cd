package xyz.vanez.tracker.service;

import xyz.vanez.tracker.dto.TrainInstanceDto;
import xyz.vanez.tracker.exception.ResourceNotFoundException;
import xyz.vanez.tracker.mapper.TrainInstanceMapper;
import xyz.vanez.tracker.model.TrainInstance;
import xyz.vanez.tracker.model.TrainModel;
import xyz.vanez.tracker.repository.TrainInstanceRepository;
import xyz.vanez.tracker.repository.TrainModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainInstanceServiceTest {

    @Mock
    private TrainInstanceRepository instanceRepository;

    @Mock
    private TrainModelRepository modelRepository;

    @Mock
    private TrainInstanceMapper mapper;

    @InjectMocks
    private TrainInstanceService service;

    private TrainModel model;
    private TrainInstance instance;
    private TrainInstanceDto dto;

    @BeforeEach
    void setUp() {
        model = new TrainModel();
        model.setId(1);
        model.setName("EP20");

        instance = new TrainInstance();
        instance.setId(1);
        instance.setSerialNumber("EP20-001");
        instance.setManufactureDate(LocalDate.of(2013, 5, 1));
        instance.setModel(model);

        dto = new TrainInstanceDto();
        dto.setId(1);
        dto.setSerialNumber("EP20-001");
        dto.setManufactureDate(LocalDate.of(2013, 5, 1));
        dto.setModelId(1);
    }

    @Test
    void getAll_WithoutFilter_ShouldReturnAll() {
        when(instanceRepository.findAll()).thenReturn(List.of(instance));
        when(mapper.toDto(instance)).thenReturn(dto);

        List<TrainInstanceDto> result = service.getAll(null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSerialNumber()).isEqualTo("EP20-001");
    }

    @Test
    void getAll_WithModelId_ShouldFilterByModel() {
        when(instanceRepository.findByModelId(1)).thenReturn(List.of(instance));
        when(mapper.toDto(instance)).thenReturn(dto);

        List<TrainInstanceDto> result = service.getAll(1);

        assertThat(result).hasSize(1);
        verify(instanceRepository).findByModelId(1);
    }

    @Test
    void getById_WhenExists_ShouldReturnDto() {
        when(instanceRepository.findById(1)).thenReturn(Optional.of(instance));
        when(mapper.toDto(instance)).thenReturn(dto);

        TrainInstanceDto result = service.getById(1);

        assertThat(result.getSerialNumber()).isEqualTo("EP20-001");
    }

    @Test
    void getById_WhenNotExists_ShouldThrow() {
        when(instanceRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_ShouldSaveAndReturnDto() {
        TrainInstanceDto inputDto = new TrainInstanceDto();
        inputDto.setSerialNumber("VL80-001");
        inputDto.setModelId(1);

        TrainInstance entity = new TrainInstance();
        TrainInstance savedEntity = instance;

        when(modelRepository.findById(1)).thenReturn(Optional.of(model));
        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(instanceRepository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDto(savedEntity)).thenReturn(dto);

        TrainInstanceDto result = service.create(inputDto);

        assertThat(result.getSerialNumber()).isEqualTo("EP20-001");
        verify(instanceRepository).save(entity);
    }

    @Test
    void create_WhenModelNotFound_ShouldThrow() {
        TrainInstanceDto inputDto = new TrainInstanceDto();
        inputDto.setModelId(99);

        when(modelRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(inputDto))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(instanceRepository, never()).save(any());
    }

    @Test
    void update_WhenExists_ShouldUpdate() {
        TrainInstanceDto updateDto = new TrainInstanceDto();
        updateDto.setSerialNumber("EP20-002");
        updateDto.setModelId(1);

        when(instanceRepository.findById(1)).thenReturn(Optional.of(instance));
        doNothing().when(mapper).updateEntityFromDto(updateDto, instance);
        when(instanceRepository.save(instance)).thenReturn(instance);
        when(mapper.toDto(instance)).thenReturn(dto);

        TrainInstanceDto result = service.update(1, updateDto);

        assertThat(result.getSerialNumber()).isEqualTo("EP20-001");
        verify(mapper).updateEntityFromDto(updateDto, instance);
    }

    @Test
    void update_WhenChangingModel_ShouldLoadNewModel() {
        TrainModel newModel = new TrainModel();
        newModel.setId(2);
        TrainInstanceDto updateDto = new TrainInstanceDto();
        updateDto.setModelId(2);

        when(instanceRepository.findById(1)).thenReturn(Optional.of(instance));
        when(modelRepository.findById(2)).thenReturn(Optional.of(newModel));
        doNothing().when(mapper).updateEntityFromDto(updateDto, instance);
        when(instanceRepository.save(instance)).thenReturn(instance);
        when(mapper.toDto(instance)).thenReturn(dto);

        service.update(1, updateDto);

        assertThat(instance.getModel().getId()).isEqualTo(2);
    }

    @Test
    void delete_WhenExists_ShouldDelete() {
        when(instanceRepository.existsById(1)).thenReturn(true);

        service.delete(1);

        verify(instanceRepository).deleteById(1);
    }

    @Test
    void delete_WhenNotExists_ShouldThrow() {
        when(instanceRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(instanceRepository, never()).deleteById(any());
    }
}