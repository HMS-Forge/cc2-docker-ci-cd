package xyz.vanez.tracker.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TrainModelTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        TrainModel model = new TrainModel();
        model.setId(1);
        model.setName("EP20");
        assertThat(model.getId()).isEqualTo(1);
        assertThat(model.getName()).isEqualTo("EP20");
    }

    @Test
    void testAllArgsConstructor() {
        TrainModel model = new TrainModel(1, "EP20", "NEVZ", 2012, 8800, 200, null);
        assertThat(model.getName()).isEqualTo("EP20");
    }
}