package xyz.vanez.tracker.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class SightingTest {

    @Test
    void noArgsConstructorAndSetters() {
        Sighting sighting = new Sighting();
        sighting.setId(1);
        sighting.setRegion("Московская обл.");
        sighting.setRailway("Октябрьская");
        sighting.setSightingDate(LocalDate.of(2025,4,20));
        sighting.setImageUrl("http://img.jpg");
        sighting.setNote("Заметка");
        sighting.setInstance(new TrainInstance());

        assertThat(sighting.getId()).isEqualTo(1);
        assertThat(sighting.getRegion()).isEqualTo("Московская обл.");
        assertThat(sighting.getRailway()).isEqualTo("Октябрьская");
        assertThat(sighting.getSightingDate()).isEqualTo(LocalDate.of(2025,4,20));
        assertThat(sighting.getImageUrl()).isEqualTo("http://img.jpg");
        assertThat(sighting.getNote()).isEqualTo("Заметка");
        assertThat(sighting.getInstance()).isNotNull();
    }

    @Test
    void allArgsConstructor() {
        TrainInstance instance = new TrainInstance();
        Sighting sighting = new Sighting(2, "Ленинградская обл.", "Октябрьская", LocalDate.now(), "url", "note", instance);
        assertThat(sighting.getRegion()).isEqualTo("Ленинградская обл.");
        assertThat(sighting.getInstance()).isEqualTo(instance);
    }

    @Test
    void testEqualsAndHashCode() {
        Sighting s1 = new Sighting();
        s1.setId(1);
        Sighting s2 = new Sighting();
        s2.setId(1);
        assertThat(s1).isEqualTo(s2);
        assertThat(s1.hashCode()).isEqualTo(s2.hashCode());
    }
}