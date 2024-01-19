import com.pz.database.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    @Test
    void testConnectToDatabase() {

        Database.connect();

        assertTrue(Database.isConnected());
    }

    @Test
    void testDisconnectToDatabase() {
        Database.connect();

        assertTrue(Database.isConnected());

        Database.disconnect();

        assertFalse(Database.isConnected());
    }

    @Test
    void testDefaultLogin() {
        Database.connect();

        assertTrue(Database.isConnected());

        assertEquals(Database.validateLogin("admin", "admin"), 1);
    }
}
