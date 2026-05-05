package xyz.vanez.tracker.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class SightingTest {

    @Test
    void noArgsConstructorAndSetters() {
        Sighting sighting = new Sighting();
        sighting.setId(1);
        sighting.setRegion("Region");
        sighting.setRailway("Railway");
        sighting.setSightingDate(LocalDate.now());
        sighting.setImageUrl("url");
        sighting.setNote("note");
        sighting.setInstance(new TrainInstance());

        assertThat(sighting.getId()).isEqualTo(1);
        assertThat(sighting.getRegion()).isEqualTo("Region");
        assertThat(sighting.getRailway()).isEqualTo("Railway");
        assertThat(sighting.getImageUrl()).isEqualTo("url");
        assertThat(sighting.getNote()).isEqualTo("note");
        assertThat(sighting.getInstance()).isNotNull();
    }

    @Test
    void allArgsConstructor() {
        TrainInstance instance = new TrainInstance();
        Sighting sighting = new Sighting(2, "Leningrad", "Oktyabrskaya", LocalDate.of(2022, 12, 12), "photo.jpg", "my note", instance);
        assertThat(sighting.getRegion()).isEqualTo("Leningrad");
        assertThat(sighting.getInstance()).isEqualTo(instance);
    }
}