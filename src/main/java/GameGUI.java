import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class GameGUI extends JFrame {
    private static final Random RANDOM = new Random();
    private int siege = 0;
    private int niederlagen = 0;
    private int unentschieden = 0;

    private enum Auswahl {
        STEIN, PAPIER, SCHERE, ECHSE, SPOCK
    }

    private static final EnumMap<Auswahl, Set<Auswahl>> GEWINNBEDIGUNGEN = new EnumMap<>(Map.of(
            Auswahl.STEIN, EnumSet.of(Auswahl.SCHERE, Auswahl.ECHSE),
            Auswahl.PAPIER, EnumSet.of(Auswahl.STEIN, Auswahl.SPOCK),
            Auswahl.SCHERE, EnumSet.of(Auswahl.PAPIER, Auswahl.ECHSE),
            Auswahl.ECHSE, EnumSet.of(Auswahl.SPOCK, Auswahl.PAPIER),
            Auswahl.SPOCK, EnumSet.of(Auswahl.SCHERE, Auswahl.STEIN)
    ));

    private final JTextArea textArea;

    public GameGUI() {
        setTitle("Stein, Papier, Schere, Echse, Spock");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setPreferredSize(new Dimension(400, 250));

        createGUI();
    }

    private void createGUI() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 5, 5));

        for (Auswahl auswahl : Auswahl.values()) {
            JButton button = new JButton(auswahl.toString());
            button.addActionListener(this::handleAction);
            buttonPanel.add(button);
        }

        JPanel untererPanel = new JPanel();
        untererPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton statistikButton = new JButton("Spielzusammenfassung anzeigen");
        statistikButton.addActionListener(e -> zeigeErgebnisse());
        untererPanel.add(statistikButton);

        JButton beendenButton = new JButton("Beenden");
        beendenButton.addActionListener(e -> System.exit(0));
        untererPanel.add(beendenButton);

        add(textArea, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
        add(untererPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleAction(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        try {
            Auswahl benutzerAuswahl = Auswahl.valueOf(actionCommand.toUpperCase());
            textArea.setText("");
            playGame(benutzerAuswahl);
        } catch (IllegalArgumentException ex) {
            textArea.setText("Ungültige Auswahl.\n");
        }
    }

    private void playGame(Auswahl benutzerAuswahl) {
        Auswahl computerAuswahl = Auswahl.values()[RANDOM.nextInt(Auswahl.values().length)];

        textArea.append("Deine Wahl: " + benutzerAuswahl + "\n\n");
        textArea.append("Computer wählte: " + computerAuswahl + "\n\n");

        ermittelGewinner(benutzerAuswahl, computerAuswahl);
    }

    private void ermittelGewinner(Auswahl benutzerAuswahl, Auswahl computerAuswahl) {
        if (benutzerAuswahl == computerAuswahl) {
            textArea.append("Unentschieden!\n");
            unentschieden++;
        } else if (GEWINNBEDIGUNGEN.get(benutzerAuswahl).contains(computerAuswahl)) {
            textArea.append(benutzerAuswahl + " schlägt " + computerAuswahl + "!\n");
            textArea.append("\nDu gewinnst!");
            siege++;
        } else {
            textArea.append(computerAuswahl + " schlägt " + benutzerAuswahl + "!\n");
            textArea.append("\nDu verlierst!");
            niederlagen++;
        }
    }

    private void zeigeErgebnisse() {
        textArea.setText("");
        textArea.append("Spielzusammenfassung:\n\n");
        textArea.append("Siege: " + siege + "\n");
        textArea.append("Niederlagen: " + niederlagen + "\n");
        textArea.append("Unentschieden: " + unentschieden + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameGUI::new);
    }
}
