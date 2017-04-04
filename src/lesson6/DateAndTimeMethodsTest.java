/**
 * 
 */
package lesson6;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

/**
 * @author Brad Richards
 *
 */
public class DateAndTimeMethodsTest {

    @Test
    public void testGetAgeBasic() {
        LocalDate onDate = LocalDate.of(2015, 6, 15);
        try {
            assertEquals(DateAndTimeMethods.getAge(LocalDate.of(2010, 3, 3), onDate), 5);
            assertEquals(DateAndTimeMethods.getAge(LocalDate.of(2010, 8, 8), onDate), 4);
            assertEquals(DateAndTimeMethods.getAge(LocalDate.of(2010, 6, 3), onDate), 5);
            assertEquals(DateAndTimeMethods.getAge(LocalDate.of(2010, 6, 23), onDate), 4);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetAgeNegative() {
        LocalDate onDate = LocalDate.of(2015, 6, 15);
        LocalDate birthDate = LocalDate.of(2020, 7, 23);
        try {
            DateAndTimeMethods.getAge(birthDate, onDate);
            fail(); // Test failure
        } catch (Exception e) {
            assertTrue(true); // Test success
        }
    }
    
    @Test
    public void testTimeToSeconds() {
    	try {
    		int seconds = DateAndTimeMethods.timeToSeconds(10, 30, 30);
    		assertEquals(seconds, 37830);
    	} catch (Exception e) {
    		fail();
    	}
    	
    }
}

