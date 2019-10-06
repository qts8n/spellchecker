package main;

import spellcheck.SpellChecker;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int N = 2;
    private static final String PATH = "dictionary.txt";

    public static void main(String[] args) throws IOException {
        System.out.println("Построение индекса...");
        SpellChecker sc = new SpellChecker(PATH, N);
        System.out.println("Индекс построен.");

        Scanner scanner = new Scanner(System.in);

        // CLI
        while (true) {
            System.out.print("$ ");
            String query = scanner.nextLine().toLowerCase();

            if (query.equals("выйти")) break;

            List<String> candidates = sc.search(query);
            if (candidates.isEmpty()) {
                System.out.println("Не было найдено ни одного кандидата");
                continue;
            }

            System.out.println("Кандидаты:");
            for (String candidate : candidates) {
                System.out.println(candidate);
            }
        }
    }
}
