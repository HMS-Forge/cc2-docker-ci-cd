package xyz.vanez.tracker.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class TrainModelTest {

    @Test
    void noArgsConstructorAndSetters() {
        TrainModel model = new TrainModel();
        model.setId(1);
        model.setName("EP20");
        model.setManufacturer("NEVZ");
        model.setYearOfProduction(2012);
        model.setPowerKw(8800);
        model.setMaxSpeedKmh(200);
        model.setInstances(new ArrayList<>());

        assertThat(model.getId()).isEqualTo(1);
        assertThat(model.getName()).isEqualTo("EP20");
        assertThat(model.getManufacturer()).isEqualTo("NEVZ");
        assertThat(model.getYearOfProduction()).isEqualTo(2012);
        assertThat(model.getPowerKw()).isEqualTo(8800);
        assertThat(model.getMaxSpeedKmh()).isEqualTo(200);
        assertThat(model.getInstances()).isEmpty();
    }

    @Test
    void allArgsConstructor() {
        TrainInstance instance = new TrainInstance();
        List<TrainInstance> instances = List.of(instance);
        TrainModel model = new TrainModel(2, "VL80", "NEVZ", 1979, 6520, 110, instances);
        assertThat(model.getId()).isEqualTo(2);
        assertThat(model.getName()).isEqualTo("VL80");
        assertThat(model.getInstances()).hasSize(1);
    }

    @Test
    void testEqualsAndHashCode() {
        TrainModel m1 = new TrainModel();
        m1.setId(1);
        TrainModel m2 = new TrainModel();
        m2.setId(1);
        assertThat(m1).isEqualTo(m2);
        assertThat(m1.hashCode()).isEqualTo(m2.hashCode());
    }
}