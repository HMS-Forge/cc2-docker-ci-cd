package xyz.vanez.tracker.repository;

import xyz.vanez.tracker.model.TrainInstance;
import xyz.vanez.tracker.model.TrainModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TrainInstanceRepositoryTest {

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