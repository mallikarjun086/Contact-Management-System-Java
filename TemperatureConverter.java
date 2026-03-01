import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemperatureConverter {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("******************************");
        System.out.println(" SMART TEMPERATURE CONVERTER ");
        System.out.println("=================================");
        System.out.println("Examples: 25C, 32 F, 300K or just 25");
        System.out.println("Type 'exit' to stop.\n");

        while (true) {

            System.out.print("Enter temperature: ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Program ended successfully.");
                break;
            }

            input = input.toUpperCase();

            double value;
            char unit;

            // Pattern 1 → Number + Unit
            Pattern patternFull = Pattern.compile("^(-?\\d+(\\.\\d+)?)\\s*([CFK])$");
            Matcher matcherFull = patternFull.matcher(input);

            // Pattern 2 → Only Number
            Pattern patternNumber = Pattern.compile("^(-?\\d+(\\.\\d+)?)$");
            Matcher matcherNumber = patternNumber.matcher(input);

            if (matcherFull.matches()) {

                // Direct extraction
                value = Double.parseDouble(matcherFull.group(1));
                unit = matcherFull.group(3).charAt(0);

            } else if (matcherNumber.matches()) {

                // Ask for unit
                value = Double.parseDouble(matcherNumber.group(1));

                System.out.print("Enter unit (C/F/K): ");
                String unitInput = sc.nextLine().trim().toUpperCase();

                if (unitInput.length() != 1 ||
                   !(unitInput.equals("C") || unitInput.equals("F") || unitInput.equals("K"))) {

                    System.out.println("Invalid unit!\n");
                    continue;
                }

                unit = unitInput.charAt(0);

            } else {

                System.out.println("Invalid input format!\n");
                continue;
            }

            // Conversion Logic
            double celsius = 0, fahrenheit = 0, kelvin = 0;

            switch (unit) {

                case 'C':
                    celsius = value;
                    fahrenheit = (celsius * 9 / 5) + 32;
                    kelvin = celsius + 273.15;
                    break;

                case 'F':
                    fahrenheit = value;
                    celsius = (fahrenheit - 32) * 5 / 9;
                    kelvin = celsius + 273.15;
                    break;

                case 'K':
                    if (value < 0) {
                        System.out.println("Kelvin cannot be negative!\n");
                        continue;
                    }
                    kelvin = value;
                    celsius = kelvin - 273.15;
                    fahrenheit = (celsius * 9 / 5) + 32;
                    break;
            }

            // Output
            System.out.println("\n--- Converted Values ---");
            System.out.printf("Celsius    : %.2f °C%n", celsius);
            System.out.printf("Fahrenheit : %.2f °F%n", fahrenheit);
            System.out.printf("Kelvin     : %.2f K%n\n", kelvin);
        }

        sc.close();
    }
}