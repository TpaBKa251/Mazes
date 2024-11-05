package backend.academy.maze.utils;

import java.security.SecureRandom;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RandomUtil {

    private final static SecureRandom RANDOM = new SecureRandom();

    public static int getRandomInt(int min, int max) {
        return RANDOM.nextInt(min, max);
    }

    public static int getRandomInt(int max) {
        return RANDOM.nextInt(max);
    }

    public static boolean getRandomBoolean() {
        return RANDOM.nextBoolean();
    }
}
