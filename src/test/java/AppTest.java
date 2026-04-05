import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {

    @Test
    public void testGreet() {
        assertEquals("Hello, Jenkins!", App.greet("Jenkins"));
    }

    @Test
    public void testAdd() {
        assertEquals(5, App.add(2, 3));
    }

    @Test
    public void testGreetEmpty() {
        try {
            App.greet("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}