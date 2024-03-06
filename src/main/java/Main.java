import java.util.*;

public class Main {
    private static final Random random = new Random();

    private enum Auswahl {
        STEIN, PAPIER, SCHERE, ECHSE, SPOCK
    }

    private static final Map<Integer, Auswahl> auswahlMap = Map.of(
            1, Auswahl.STEIN,
            2, Auswahl.PAPIER,
            3, Auswahl.SCHERE,
            4, Auswahl.ECHSE,
            5, Auswahl.SPOCK
    );

    private static final Map<Auswahl, Set<Auswahl>> gewinnBedigungen = new EnumMap<>(Map.of(
            Auswahl.STEIN, EnumSet.of(Auswahl.SCHERE, Auswahl.ECHSE),
            Auswahl.PAPIER, EnumSet.of(Auswahl.STEIN, Auswahl.SPOCK),
            Auswahl.SCHERE, EnumSet.of(Auswahl.PAPIER, Auswahl.ECHSE),
            Auswahl.ECHSE, EnumSet.of(Auswahl.SPOCK, Auswahl.PAPIER),
            Auswahl.SPOCK, EnumSet.of(Auswahl.SCHERE, Auswahl.STEIN)
    ));

    public static void main(String[] args) {
        System.out.println("Willkommen zu Stein, Papier, Schere, Echse, Spock!\n");
        System.out.println("Wähle: 1 für Stein, 2 für Papier, 3 für Schere, 4 für Echse, 5 für Spock, 6 zum Beenden.");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("\nDeine Wahl (1-6): ");
                int benutzerWahl = pruefeInput(scanner);

                if (benutzerWahl == 6) {
                    System.out.println("Spiel beendet.");
                    break;
                }

                playGame(benutzerWahl);
                System.out.println("Wähle: 1 für Stein, 2 für Papier, 3 für Schere, 4 für Echse, 5 für Spock, 6 zum Beenden.");
            }
        }
    }

    private static int pruefeInput(Scanner scanner) {
        int input;
        do {
            while (!scanner.hasNextInt()) {
                System.out.println("Ungültige Eingabe. Bitte geben Sie eine Zahl ein.");
                scanner.next();
            }
            input = scanner.nextInt();
            if (input < 1 || input > 6) {
                System.out.println("Bitte geben Sie eine Zahl zwischen 1 und 6 ein.");
            }
        } while (input < 1 || input > 6);
        return input;
    }

    private static void playGame(int benutzerWahl) {
        Auswahl benutzerAuswahl = auswahlMap.get(benutzerWahl);
        Auswahl computerAuswahl = auswahlMap.get(random.nextInt(5) + 1);

        System.out.println("\nDeine Wahl: " + benutzerAuswahl);
        System.out.println("Computer wählte: " + computerAuswahl);

        ermittelGewinner(benutzerAuswahl, computerAuswahl);
    }

    private static void ermittelGewinner(Auswahl benutzerAuswahl, Auswahl computerAuswahl) {
        if (benutzerAuswahl == computerAuswahl) {
            System.out.println("Unentschieden!");
        } else if (gewinnBedigungen.get(benutzerAuswahl).contains(computerAuswahl)) {
            System.out.println("Du gewinnst! " + benutzerAuswahl + " schlägt " + computerAuswahl + "!");
        } else {
            System.out.println("Du verlierst! " + computerAuswahl + " schlägt " + benutzerAuswahl + "!");
        }
    }
}