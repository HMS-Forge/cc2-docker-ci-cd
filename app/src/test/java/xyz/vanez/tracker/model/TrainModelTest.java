package xyz.vanez.tracker.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
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
        TrainModel model = new TrainModel(2, "VL80", "NEVZ", 1979, 6520, 110, null);
        assertThat(model.getName()).isEqualTo("VL80");
        assertThat(model.getManufacturer()).isEqualTo("NEVZ");
    }
}