package util;


import java.util.Map;
import java.util.Scanner;

public class Printer {
    private static final Scanner scanner = new Scanner(System.in);

    public static void printSelectionMenu(Map<String, String> options) {
        options.keySet().forEach(option -> System.out.println(option + ": " + options.get(option)));
    }

    public static String getInput() {
        System.out.print("-> ");
        return scanner.nextLine();
    }
}
