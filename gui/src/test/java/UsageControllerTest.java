import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsageControllerTest {

    private UsageController controller;

    @BeforeAll
    public static void initToolkit() {
        // Startet das JavaFX Toolkit, notwendig für UI-Komponenten wie Label
        new JFXPanel();
    }

    @BeforeEach
    public void setup() {
        controller = new UsageController();
        controller.community_pool_label = new Label();
        controller.grid_portion_label = new Label();
    }

    @Test
    public void testUpdateLabelsFromJson() {
        String mockJson = "{\"community_depleted\": 123.45, \"grid_portion\": 67.89}";
        controller.updateLabelsFromJson(mockJson);

        assertEquals("123.45", controller.community_pool_label.getText());
        assertEquals("67.89", controller.grid_portion_label.getText());
    }
}