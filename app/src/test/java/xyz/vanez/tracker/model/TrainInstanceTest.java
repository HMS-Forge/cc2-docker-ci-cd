package xyz.vanez.tracker.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;

class TrainInstanceTest {

    @Test
    void noArgsConstructorAndSetters() {
        TrainInstance instance = new TrainInstance();
        instance.setId(1);
        instance.setSerialNumber("SN123");
        instance.setManufactureDate(LocalDate.of(2000, 1, 1));
        instance.setImageUrl("img.jpg");
        instance.setModel(new TrainModel());
        instance.setSightings(new ArrayList<>());

        assertThat(instance.getId()).isEqualTo(1);
        assertThat(instance.getSerialNumber()).isEqualTo("SN123");
        assertThat(instance.getManufactureDate()).isEqualTo(LocalDate.of(2000, 1, 1));
        assertThat(instance.getImageUrl()).isEqualTo("img.jpg");
        assertThat(instance.getModel()).isNotNull();
        assertThat(instance.getSightings()).isEmpty();
    }

    @Test
    void allArgsConstructor() {
        TrainModel model = new TrainModel();
        TrainInstance instance = new TrainInstance(2, "SN456", LocalDate.of(2005, 5, 5), "img2.jpg", model, null);
        assertThat(instance.getSerialNumber()).isEqualTo("SN456");
    }
}