package generator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class RandomUtils {
    private final Random rand = new Random();

    public BigDecimal randomPrice() {
        double price = 10 + (500 - 10) * rand.nextDouble();
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
    }

    public int randomStock() {
        return rand.nextInt(50) + 1;
    }

    public int randomYear() {
        return rand.nextInt(23) + 2000; // 2000-2022
    }

    public String randomPassword() {
        return "pass" + rand.nextInt(10000);
    }
}