import org.junit.Test;
import static org.junit.Assert.*;

public class LoginPageTest {

    @Test
    public void testLoginPageLaunch() {

        try {
            LoginPage lp = new LoginPage();
            lp.setVisible(true);

            assertNotNull(lp); 

            System.out.println("LoginPage opened successfully");

        } catch (Exception e) {
            fail("LoginPage failed to open: " + e.getMessage());
        }
    }
}
