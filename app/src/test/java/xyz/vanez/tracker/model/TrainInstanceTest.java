package xyz.vanez.tracker.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class TrainInstanceTest {

    @Test
    void noArgsConstructorAndSetters() {
        TrainInstance instance = new TrainInstance();
        instance.setId(1);
        instance.setSerialNumber("EP20-001");
        instance.setManufactureDate(LocalDate.of(2013,5,1));
        instance.setImageUrl("http://img.jpg");
        instance.setModel(new TrainModel());
        instance.setSightings(new ArrayList<>());

        assertThat(instance.getId()).isEqualTo(1);
        assertThat(instance.getSerialNumber()).isEqualTo("EP20-001");
        assertThat(instance.getManufactureDate()).isEqualTo(LocalDate.of(2013,5,1));
        assertThat(instance.getImageUrl()).isEqualTo("http://img.jpg");
        assertThat(instance.getModel()).isNotNull();
        assertThat(instance.getSightings()).isEmpty();
    }

    @Test
    void allArgsConstructor() {
        TrainModel model = new TrainModel();
        Sighting s = new Sighting();
        List<Sighting> sightings = List.of(s);
        TrainInstance instance = new TrainInstance(2, "VL80-002", LocalDate.of(1980,6,1), "img2.jpg", model, sightings);
        assertThat(instance.getSerialNumber()).isEqualTo("VL80-002");
        assertThat(instance.getSightings()).hasSize(1);
    }

    @Test
    void testEqualsAndHashCode() {
        TrainInstance i1 = new TrainInstance();
        i1.setId(1);
        TrainInstance i2 = new TrainInstance();
        i2.setId(1);
        assertThat(i1).isEqualTo(i2);
        assertThat(i1.hashCode()).isEqualTo(i2.hashCode());
    }
}