import org.junit.Test;
import static org.junit.Assert.*;

public class HomePageTest {

    public HomePageTest() {
    }

    @Test
    public void testHomePageCreation() {

        HomePage home = new HomePage("Eslam");

        assertNotNull(home);
    }
}
