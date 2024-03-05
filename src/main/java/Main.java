import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        Map<Integer, String> auswahlMoeglichkeiten = new HashMap<>();
        auswahlMoeglichkeiten.put(1, "Stein");
        auswahlMoeglichkeiten.put(2, "Papier");
        auswahlMoeglichkeiten.put(3, "Schere");
        auswahlMoeglichkeiten.put(4, "Echse");
        auswahlMoeglichkeiten.put(5, "Spock");

        Map<String, Set<String>> gewinnBedigungen = new HashMap<>();
        gewinnBedigungen.put("Stein", new HashSet<>(Set.of("Schere", "Echse")));
        gewinnBedigungen.put("Papier", new HashSet<>(Set.of("Stein", "Spock")));
        gewinnBedigungen.put("Schere", new HashSet<>(Set.of("Papier", "Echse")));
        gewinnBedigungen.put("Echse", new HashSet<>(Set.of("Spock", "Papier")));
        gewinnBedigungen.put("Spock", new HashSet<>(Set.of("Schere", "Stein")));

        System.out.println("Willkommen zu Stein, Papier, Schere, Echse, Spock!");
        System.out.println("\nWähle: 1 für Rock, 2 für Paper, 3 für Scissors, 4 für Lizard, 5 für Spock.\nZum Beenden gib 'Quit' ein.");

        while (true) {

            System.out.print("\nDeine Wahl (1-5): ");
            String benutzerEingabe = scanner.nextLine();

            if (benutzerEingabe.equalsIgnoreCase("Quit")) {
                System.out.println("Spiel beendet");
                break;
            }

            int benutzerWahl;

            try {
                benutzerWahl = Integer.parseInt(benutzerEingabe);
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe. Bitte eine Zahl zwischen 1 und 5 eingeben.");
                continue;
            }

            if (!auswahlMoeglichkeiten.containsKey(benutzerWahl)) {
                System.out.println("Ungültige Wahl. Bitte wähle zwischen 1 und 5.");
                continue;
            }

            String benutzerAuswahl = auswahlMoeglichkeiten.get(benutzerWahl);
            String computerAuswahl = auswahlMoeglichkeiten.get(random.nextInt(5) + 1);

            System.out.println("\n");
            System.out.println("Deine Wahl: " + benutzerAuswahl);
            System.out.println("Computer wählte: " + computerAuswahl);

            if (benutzerAuswahl.equals(computerAuswahl)) {
                System.out.println("\nUnentschieden!");
            } else if (gewinnBedigungen.get(benutzerAuswahl).contains(computerAuswahl)) {
                System.out.println("\nDu gewinnst! Deine " + benutzerAuswahl + " schlägt " + computerAuswahl + "!");
            } else {
                System.out.println("\nDu verlierst! Deine " + benutzerAuswahl + " verliert gegen " + computerAuswahl + "!");
            }
        }
        scanner.close();
    }
}
