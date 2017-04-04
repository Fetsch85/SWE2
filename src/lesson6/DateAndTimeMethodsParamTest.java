package lesson6;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DateAndTimeMethodsParamTest {
    // Data comprising a single test case
    int birthYear, birthMonth, birthDay;
    int onYear, onMonth, onDay;
    private int age;
    
    // Data prepared by the @Before method
    private LocalDate onDate;
    private LocalDate birthDate;
    
    /**
     * Constructor to build a test case
     */
    public DateAndTimeMethodsParamTest(int birthYear, int birthMonth, int birthDay, int onYear, int onMonth, int onDay, int age) {
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.onYear = onYear;
        this.onMonth = onMonth;
        this.onDay = onDay;
        this.age = age;
    }
    
    @Parameters
    public static Collection<Object[]> inputDates() {
      return Arrays.asList(new Object[][] {
        // Basic tests
        { 2010, 3, 3, 2015, 6, 15, 5 },
        { 2010, 8, 8, 2015, 6, 15, 4 },
        { 2010, 6, 3, 2015, 6, 15, 5 },
        { 2010, 6, 23, 2015, 6, 15, 4 },
        
        // Boundary tests
        { 2010, 5, 23, 2015, 6, 15, 5 },
        { 2010, 7, 1, 2015, 6, 15, 4 },
        { 2010, 6, 15, 2015, 6, 15, 5 },
        { 2010, 6, 16, 2015, 6, 15, 4 },
        { 2015, 6, 15, 2015, 6, 15, 0 },
        
        // Error cases: -1 indicates that these are errors
        { 2015, 6, 16, 2015, 6, 15, -1 },
        { 2020, 7, 23, 2015, 6, 15, -1 }
      });
    }

    @Before
    public void constructDates() {
        this.onDate = LocalDate.of(onYear, onMonth, onDay);
        this.birthDate = LocalDate.of(birthYear, birthMonth, birthDay);
    }
    
    @Test
    public void test() {
        try {
            int calcAge = DateAndTimeMethods.getAge(birthDate, onDate);
            assertEquals(age, calcAge);
        } catch (Exception e) {
            assertEquals(age, -1);
        }
    }
}
