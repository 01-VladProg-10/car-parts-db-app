package generator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class FakerService {

    private final Random rand = new Random();

    private final String[] firstNames = {"Jan", "Anna", "Piotr", "Katarzyna", "Michał", "Ewa", "Tomasz", "Magda"};
    private final String[] lastNames = {"Kowalski", "Nowak", "Wiśniewski", "Wójcik", "Kamiński", "Lewandowski"};
    private final String[] emailDomains = {"@test.pl", "@mail.com", "@example.com"};
    private final String[] companies = {"AutoParts", "MotoShop", "CarTech", "PartsPro"};
    private final String[] colors = {"Czarny", "Biały", "Czerwony", "Niebieski", "Srebrny"};
    private final String[] fuels = {"Benzyna", "Diesel", "LPG"};
    private final String[] carMakes = {"Toyota","Ford","Honda","BMW"};
    private final String[] carModels = {"Corolla","Civic","Focus","320i"};

    // Zestaw do przechowywania już wygenerowanych emaili
    private final Set<String> usedEmails = new HashSet<>();

    // ===== Użytkownicy =====
    public String randomFullName() {
        String first = firstNames[rand.nextInt(firstNames.length)];
        String last = lastNames[rand.nextInt(lastNames.length)];
        return first + " " + last;
    }

    public String randomEmail(String name) {
        String email;
        int attempt = 0;
        do {
            email = name.toLowerCase().replace(" ", ".") + emailDomains[rand.nextInt(emailDomains.length)];
            if (attempt > 0) email = email.replace("@", attempt + "@");
            attempt++;
        } while (usedEmails.contains(email));
        usedEmails.add(email);
        return email;
    }

    // ===== Firmy / dostawcy =====
    public String randomCompany() {
        return companies[rand.nextInt(companies.length)];
    }

    public String randomPhone() {
        return "50" + (rand.nextInt(9000000) + 1000000);
    }

    public String randomAddress() {
        return "Ulica " + (rand.nextInt(100) + 1) + ", Miasto";
    }

    // ===== Samochody =====
    public String randomCarMake() {
        return carMakes[rand.nextInt(carMakes.length)];
    }

    public String randomCarModel() {
        return carModels[rand.nextInt(carModels.length)];
    }

    public String randomVIN() {
        return "VIN" + (rand.nextInt(900000000) + 100000000);
    }

    public String randomColor() {
        return colors[rand.nextInt(colors.length)];
    }

    public String randomFuel() {
        return fuels[rand.nextInt(fuels.length)];
    }

    // ===== Inne =====
    public String randomTrackingNumber() {
        return "TRACK" + (rand.nextInt(900000) + 100000);
    }
}