package utils;

/**
 * Created by Kroos on 08.05.2016.
 */
public class Time {

    public static final long SECOND = 1000000000l;

    public static long get() {

        return System.nanoTime();

    }

}
