package org.launchcode.weather_dashboard.runner;

import org.launchcode.weather_dashboard.model.WeatherData;
import org.launchcode.weather_dashboard.service.WeatherService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

// This class is responsible for running the console input and output.

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final WeatherService weatherService;

    public ConsoleRunner(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        System.out.println("Welcome to Weather Dashboard!");
        System.out.println("===================================");

        while (running) {
            showMenu();
            int choice = getChoice(scanner);

            switch (choice) {
                case 1:
                    checkWeather("London", scanner);
                    break;
                case 2:
                    checkWeather("Paris", scanner);
                    break;
                case 3:
                    checkWeather("Tokyo", scanner);
                    break;
                case 4:
                    checkCustomCity(scanner);
                    break;
                case 5:
                    System.out.println("\nThanks for using Weather Dashboard!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private void showMenu() {
        System.out.println("\n=== Weather Dashboard ===");
        System.out.println("1. Check London Weather");
        System.out.println("2. Check Paris Weather");
        System.out.println("3. Check Tokyo Weather");
        System.out.println("4. Check Custom City");
        System.out.println("5. Exit");
        System.out.print("\nChoice: ");
    }

    private int getChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void checkWeather(String city, Scanner scanner) {
        try {
            System.out.println("\nFetching weather for " + city + "...");

            // Changed getWeather to getCurrentWeather
            WeatherData weather = weatherService.getCurrentWeather(city);

            displayWeather(weather);

        } catch (Exception e) {
            System.out.println("Error getting weather for " + city + ": " + e.getMessage());
        }

        waitForEnter(scanner);
    }

    private void checkCustomCity(Scanner scanner) {
        System.out.print("\nEnter city name: ");
        String city = scanner.nextLine().trim();

        if (!city.isEmpty()) {
            checkWeather(city, scanner);
        } else {
            System.out.println("Please enter a valid city name.");
            waitForEnter(scanner);
        }
    }

    private void displayWeather(WeatherData weather) {
        System.out.println("\nCurrent Weather in " + weather.getName() + ":");
        System.out.println("Temperature: " + weather.getMain().getTemp() + "°C");
        System.out.println("Feels like: " + weather.getMain().getFeelsLike() + "°C");
        System.out.println("Conditions: " + weather.getWeather()[0].getDescription());
        System.out.println("Humidity: " + weather.getMain().getHumidity() + "%");
        System.out.println("Pressure: " + weather.getMain().getPressure() + " hPa");

        String condition = weather.getWeather()[0].getMain();

        System.out.println("Status: " + condition);
    }

    private void waitForEnter(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}