import com.pz.App;
import com.pz.game.Assets;
import javafx.application.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AssetsTest {
    @Test
    void testAssets() {
        App.main(new String[0]);
        Assets.load();

        assertNotNull(Assets.getImage("Bananas"));
        assertNotNull(Assets.getImage("cloud"));
        assertNotNull(Assets.getImage("plant1"));
        assertNotNull(Assets.getImage("plant2"));
        assertNotNull(Assets.getImage("platform1"));
        assertNotNull(Assets.getImage("platform2"));
        assertNotNull(Assets.getImage("platform6"));
        assertNotNull(Assets.getImage("Run (32x32)"));

        assertNull(Assets.getImage("fadsfd"));

        assertNull(Assets.getImage("vadfvaerv"));
    }
}