package xyz.vanez.tracker.repository;

import xyz.vanez.tracker.model.TrainModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

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
class TrainModelRepositoryTest {

    @Autowired
    private TrainModelRepository repository;

    @Test
    void shouldSaveAndFindModel() {
        TrainModel model = new TrainModel();
        model.setName("VL80");
        model.setManufacturer("NEVZ");
        model.setYearOfProduction(1979);
        model.setPowerKw(6520);
        model.setMaxSpeedKmh(110);

        TrainModel saved = repository.save(model);
        assertThat(saved.getId()).isNotNull();

        var found = repository.findAll();
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("VL80");
    }
    @Test
    void updateModel_ShouldUpdateExisting() {
        TrainModel model = new TrainModel();
        model.setName("OldName");
        TrainModel saved = repository.save(model);

        saved.setName("UpdatedName");
        TrainModel updated = repository.save(saved);

        assertThat(updated.getName()).isEqualTo("UpdatedName");
        assertThat(repository.findById(saved.getId()).get().getName()).isEqualTo("UpdatedName");
    }

    @Test
    void deleteModel_ShouldRemove() {
        TrainModel model = new TrainModel();
        model.setName("ToDelete");
        TrainModel saved = repository.save(model);
        Integer id = saved.getId();

        repository.deleteById(id);

        assertThat(repository.findById(id)).isEmpty();
    }

    @Test
    void findById_WhenExists_ShouldReturnModel() {
        TrainModel model = new TrainModel();
        model.setName("FindMe");
        TrainModel saved = repository.save(model);

        var found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("FindMe");
    }
}