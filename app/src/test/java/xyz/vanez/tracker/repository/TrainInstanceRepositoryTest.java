package xyz.vanez.tracker.repository;

import org.springframework.test.context.TestPropertySource;
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
class TrainInstanceRepositoryTest {

    @Autowired
    private TrainInstanceRepository instanceRepository;

    @Autowired
    private TrainModelRepository modelRepository;

    @Test
    void shouldSaveAndFindByModelId() {
        TrainModel model = new TrainModel();
        model.setName("EP20");
        modelRepository.save(model);

        TrainInstance instance = new TrainInstance();
        instance.setSerialNumber("EP20-001");
        instance.setManufactureDate(LocalDate.of(2013, 5, 1));
        instance.setModel(model);

        instanceRepository.save(instance);

        List<TrainInstance> found = instanceRepository.findByModelId(model.getId());
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getSerialNumber()).isEqualTo("EP20-001");
    }
}