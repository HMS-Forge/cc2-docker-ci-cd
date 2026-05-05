package xyz.vanez.tracker.service;

import xyz.vanez.tracker.dto.SightingDto;
import xyz.vanez.tracker.exception.ResourceNotFoundException;
import xyz.vanez.tracker.mapper.SightingMapper;
import xyz.vanez.tracker.model.Sighting;
import xyz.vanez.tracker.model.TrainInstance;
import xyz.vanez.tracker.repository.SightingRepository;
import xyz.vanez.tracker.repository.TrainInstanceRepository;
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
class SightingServiceTest {

    @Mock
    private SightingRepository sightingRepository;

    @Mock
    private TrainInstanceRepository instanceRepository;

    @Mock
    private SightingMapper mapper;

    @InjectMocks
    private SightingService service;

    private TrainInstance instance;
    private Sighting sighting;
    private SightingDto dto;

    @BeforeEach
    void setUp() {
        instance = new TrainInstance();
        instance.setId(1);
        instance.setSerialNumber("EP20-001");

        sighting = new Sighting();
        sighting.setId(1);
        sighting.setRegion("Московская обл.");
        sighting.setSightingDate(LocalDate.of(2025, 4, 20));
        sighting.setInstance(instance);

        dto = new SightingDto();
        dto.setId(1);
        dto.setRegion("Московская обл.");
        dto.setSightingDate(LocalDate.of(2025, 4, 20));
        dto.setInstanceId(1);
    }

    @Test
    void getAllByInstance_WithoutFilter_ShouldReturnAll() {
        when(sightingRepository.findAll()).thenReturn(List.of(sighting));
        when(mapper.toDto(sighting)).thenReturn(dto);

        List<SightingDto> result = service.getAllByInstance(null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRegion()).isEqualTo("Московская обл.");
    }

    @Test
    void getAllByInstance_WithInstanceId_ShouldFilter() {
        when(sightingRepository.findByInstanceId(1)).thenReturn(List.of(sighting));
        when(mapper.toDto(sighting)).thenReturn(dto);

        List<SightingDto> result = service.getAllByInstance(1);

        assertThat(result).hasSize(1);
        verify(sightingRepository).findByInstanceId(1);
    }

    @Test
    void getById_WhenExists_ShouldReturnDto() {
        when(sightingRepository.findById(1)).thenReturn(Optional.of(sighting));
        when(mapper.toDto(sighting)).thenReturn(dto);

        SightingDto result = service.getById(1);

        assertThat(result.getRegion()).isEqualTo("Московская обл.");
    }

    @Test
    void getById_WhenNotExists_ShouldThrow() {
        when(sightingRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_ShouldSaveAndReturnDto() {
        SightingDto inputDto = new SightingDto();
        inputDto.setRegion("Ленинградская обл.");
        inputDto.setInstanceId(1);

        when(instanceRepository.findById(1)).thenReturn(Optional.of(instance));
        when(mapper.toEntity(inputDto)).thenReturn(sighting);
        when(sightingRepository.save(sighting)).thenReturn(sighting);
        when(mapper.toDto(sighting)).thenReturn(dto);

        SightingDto result = service.create(inputDto);

        assertThat(result.getRegion()).isEqualTo("Московская обл.");
        verify(sightingRepository).save(sighting);
    }

    @Test
    void create_WhenInstanceNotFound_ShouldThrow() {
        SightingDto inputDto = new SightingDto();
        inputDto.setInstanceId(99);

        when(instanceRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(inputDto))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(sightingRepository, never()).save(any());
    }

    @Test
    void update_WhenExists_ShouldUpdate() {
        SightingDto updateDto = new SightingDto();
        updateDto.setRegion("Тверская обл.");
        updateDto.setInstanceId(1);

        when(sightingRepository.findById(1)).thenReturn(Optional.of(sighting));
        doNothing().when(mapper).updateEntityFromDto(updateDto, sighting);
        when(sightingRepository.save(sighting)).thenReturn(sighting);
        when(mapper.toDto(sighting)).thenReturn(dto);

        SightingDto result = service.update(1, updateDto);

        assertThat(result.getRegion()).isEqualTo("Московская обл.");
        verify(mapper).updateEntityFromDto(updateDto, sighting);
    }

    @Test
    void update_WhenChangingInstance_ShouldLoadNewInstance() {
        TrainInstance newInstance = new TrainInstance();
        newInstance.setId(2);
        SightingDto updateDto = new SightingDto();
        updateDto.setInstanceId(2);

        when(sightingRepository.findById(1)).thenReturn(Optional.of(sighting));
        when(instanceRepository.findById(2)).thenReturn(Optional.of(newInstance));
        doNothing().when(mapper).updateEntityFromDto(updateDto, sighting);
        when(sightingRepository.save(sighting)).thenReturn(sighting);
        when(mapper.toDto(sighting)).thenReturn(dto);

        service.update(1, updateDto);

        assertThat(sighting.getInstance().getId()).isEqualTo(2);
    }

    @Test
    void delete_WhenExists_ShouldDelete() {
        when(sightingRepository.existsById(1)).thenReturn(true);

        service.delete(1);

        verify(sightingRepository).deleteById(1);
    }

    @Test
    void delete_WhenNotExists_ShouldThrow() {
        when(sightingRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(sightingRepository, never()).deleteById(any());
    }
}