package lesson6;

import java.time.LocalDate;

/**
 * This class contains a few methods that should be tested using jUnit test cases.
 * 
 * IMPORTANT: These methods may (or may not) contain errors. The challenge is to
 * write test cases that identify these errors.
 * 
 * Consider the following when creating your test cases: (a) equivalence classes,
 * (b) boundary conditions and (c) important combinations of parameters. Finally,
 * if a method generates exceptions or other error conditions, test these as well.
 */
public class DateAndTimeMethods {
    /**
     * This method calculates a person's age on the given date,
     * using their birthdate.
     * 
     */
    public static int getAge(LocalDate birthDate, LocalDate onDate) throws Exception {        
        int onYear = onDate.getYear();
        int onMonth = onDate.getMonthValue();
        int onDay = onDate.getDayOfMonth();

        int birthYear = birthDate.getYear();
        int birthMonth = birthDate.getMonthValue();
        int birthDay = birthDate.getDayOfMonth();

        int age = onYear - birthYear;

        if (onMonth < birthMonth) {
          age--;
        } else if (onMonth == birthMonth) {
          if (onDay < birthDay) {
            age--;
          }
        }
        if (age < 0) throw new Exception("Not born!");
        return age;
    }
    
    /**
     * This method takes a time, in the form of three integers "hour, minute, second",
     * and calculates the total number of elapsed seconds in the day.
     * 
     * Example: Time 02:30:45 should return a result of 2 * 3600 + 30 * 60 + 45 = 9045
     */
    public static int timeToSeconds(int hour, int minute, int second) throws Exception {
        if (second < 0 || second > 59) throw new Exception("second out of range");
        if (minute < 0 || minute > 59) throw new Exception("minute out of range");
        if (hour < 0 || hour > 24) throw new Exception("hour out of range");

        int total_seconds = hour;
        total_seconds *= 60;
        total_seconds += minute;
        total_seconds *= 60;
        total_seconds += second;
        
        if (second == 59) total_seconds++;
        return total_seconds;
    }
}
