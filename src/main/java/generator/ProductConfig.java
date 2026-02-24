package generator;

import java.util.List;
import java.util.Map;

public class ProductConfig {

    private final Map<String, String> productCategoryMap = Map.of(
            "Filtr oleju", "Silnik",
            "Świeca zapłonowa", "Silnik",
            "Amortyzator", "Zawieszenie",
            "Tarcza hamulcowa", "Hamulce",
            "Płyn chłodniczy", "Chłodzenie"
    );

    public String getCategoryForProduct(String productName) {
        return productCategoryMap.getOrDefault(productName, "Inne");
    }

    public List<String> getAllProductNames() {
        return productCategoryMap.keySet().stream().toList();
    }

    public List<String> getAllCategories() {
        return productCategoryMap.values().stream().distinct().toList();
    }
}