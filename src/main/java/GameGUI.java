import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GameGUI extends JFrame {

    private static final Random RANDOM = new Random();
    private static final Color BACKGROUND_COLOR = Color.DARK_GRAY;
    private static final Color WIN_COLOR = Color.GREEN;
    private static final Color LOSE_COLOR = Color.RED.brighter().brighter();
    private static final Color DRAW_COLOR = Color.YELLOW;
    private static final Color TEXT_COLOR = Color.WHITE;

    private enum Auswahl {
        SCHERE, STEIN, PAPIER, ECHSE, SPOCK
    }

    private static final EnumMap<Auswahl, Set<Auswahl>> GEWINNBEDIGUNGEN = new EnumMap<>(Map.of(
            Auswahl.SCHERE, EnumSet.of(Auswahl.PAPIER, Auswahl.ECHSE),
            Auswahl.STEIN, EnumSet.of(Auswahl.SCHERE, Auswahl.ECHSE),
            Auswahl.PAPIER, EnumSet.of(Auswahl.STEIN, Auswahl.SPOCK),
            Auswahl.ECHSE, EnumSet.of(Auswahl.SPOCK, Auswahl.PAPIER),
            Auswahl.SPOCK, EnumSet.of(Auswahl.SCHERE, Auswahl.STEIN)
    ));

    private int siege = 0, niederlagen = 0, unentschieden = 0;
    private final JTextArea textArea;

    public GameGUI() {
        setTitle("SSPES - Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setPreferredSize(new Dimension(400, 250));
        textArea.setBackground(BACKGROUND_COLOR);
        textArea.setForeground(TEXT_COLOR);

        erstelleGUI();
    }

    private void erstelleGUI() {
        add(textArea, BorderLayout.CENTER);
        add(erstelleButtonPanel(), BorderLayout.EAST);
        add(erstelleUntererPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton erstelleButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    private JPanel erstelleButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 5, 5));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        for (Auswahl auswahl : Auswahl.values()) {
            JButton button = erstelleButton(auswahl.toString(), this::handleAction);
            button.setToolTipText(getTooltipForAuswahl(auswahl));
            buttonPanel.add(button);
        }

        return buttonPanel;
    }

    private JPanel erstelleUntererPanel() {
        JPanel untererPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        untererPanel.setBackground(BACKGROUND_COLOR);

        JButton statistikButton = erstelleButton("Spielzusammenfassung anzeigen", e -> zeigeErgebnisse());
        JButton beendenButton = erstelleButton("Beenden", e -> beendenAktion());

        untererPanel.add(statistikButton);
        untererPanel.add(beendenButton);

        return untererPanel;
    }

    private void handleAction(ActionEvent e) {
        resetTextColor();
        String actionCommand = e.getActionCommand();
        try {
            Auswahl benutzerAuswahl = Auswahl.valueOf(actionCommand.toUpperCase());
            textArea.setText("");
            starteSpielzug(benutzerAuswahl);
        } catch (IllegalArgumentException ex) {
            textArea.setText("Ungültige Auswahl.\n");
        }
    }

    private void starteSpielzug(Auswahl benutzerAuswahl) {
        Auswahl computerAuswahl = Auswahl.values()[RANDOM.nextInt(Auswahl.values().length)];
        textArea.append("\n\tDeine Wahl: " + benutzerAuswahl);
        textArea.append("\n\tComputer wählte: " + computerAuswahl);
        ermittelGewinner(benutzerAuswahl, computerAuswahl);
    }

    private void ermittelGewinner(Auswahl benutzerAuswahl, Auswahl computerAuswahl) {
        if (benutzerAuswahl == computerAuswahl) {
            textArea.append("\n\tUnentschieden!\n");
            textArea.setForeground(DRAW_COLOR);
            unentschieden++;
        } else if (GEWINNBEDIGUNGEN.get(benutzerAuswahl).contains(computerAuswahl)) {
            textArea.append("\n\t" + benutzerAuswahl + " schlägt " + computerAuswahl + "!\n\tDu gewinnst!");
            textArea.setForeground(WIN_COLOR);
            siege++;
        } else {
            textArea.append("\n\t" + computerAuswahl + " schlägt " + benutzerAuswahl + "!\n\tDu verlierst!");
            textArea.setForeground(LOSE_COLOR);
            niederlagen++;
        }
    }

    private void zeigeErgebnisse() {
        resetTextColor();
        textArea.setText("");
        textArea.append("\n\tSpielzusammenfassung:\n");
        textArea.append("\tSiege: " + siege + "\n");
        textArea.append("\tNiederlagen: " + niederlagen + "\n");
        textArea.append("\tUnentschieden: " + unentschieden + "\n");
    }

    private void beendenAktion() {
        new Thread(() -> {
            resetTextColor();
            textArea.setText("\n\n\t\tGood bye");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                System.err.println("Fehler beim Warten vor dem Beenden: " + ex.getMessage());
            }
            System.exit(0);
        }).start();
    }

    private void resetTextColor() {
        textArea.setForeground(TEXT_COLOR);
    }

    private String getTooltipForAuswahl(Auswahl auswahl) {
        Set<Auswahl> gewinntGegen = GEWINNBEDIGUNGEN.get(auswahl);
        return auswahl + " schlägt " + String.join(", ", gewinntGegen.stream().map(Enum::toString).toArray(String[]::new));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameGUI::new);
    }
}
