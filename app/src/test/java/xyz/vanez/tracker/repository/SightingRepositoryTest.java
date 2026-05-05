package xyz.vanez.tracker.repository;

import org.springframework.test.context.TestPropertySource;
import xyz.vanez.tracker.model.Sighting;
import xyz.vanez.tracker.model.TrainInstance;
import xyz.vanez.tracker.model.TrainModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class SightingRepositoryTest {

    @Autowired
    private SightingRepository sightingRepository;

    @Autowired
    private TrainInstanceRepository instanceRepository;

    @Autowired
    private TrainModelRepository modelRepository;

    @Test
    void shouldSaveAndFindByInstanceId() {
        TrainModel model = new TrainModel();
        model.setName("VL80");
        modelRepository.save(model);

        TrainInstance instance = new TrainInstance();
        instance.setSerialNumber("VL80-001");
        instance.setModel(model);
        instanceRepository.save(instance);

        Sighting sighting = new Sighting();
        sighting.setRegion("Московская обл.");
        sighting.setSightingDate(LocalDate.now());
        sighting.setInstance(instance);

        sightingRepository.save(sighting);

        List<Sighting> found = sightingRepository.findByInstanceId(instance.getId());
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getRegion()).isEqualTo("Московская обл.");
    }
}