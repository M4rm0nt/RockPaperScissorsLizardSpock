import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GameGUI extends JFrame {

    private static final Random RANDOM = new Random();
    private static final Color BACKGROUND_COLOR = Color.DARK_GRAY;
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
    private final JTextPane textPane;
    private int siege = 0, niederlagen = 0, unentschieden = 0;
    private boolean istStatistikSichtbar = false;
    private String zuletztAngezeigterText = "";
    private final String css = "<style>" +
            "body { text-align: center; color: white; font-size: 20px; } " +
            "span { font-weight: bold; } " +
            ".green { color: green; } " +
            ".red { color: red; } " +
            ".yellow { color: yellow; } " +
            "</style>";

    public GameGUI() {
        setTitle("SSPES - Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setPreferredSize(new Dimension(400, 250));
        textPane.setBackground(BACKGROUND_COLOR);
        textPane.setContentType("text/html");
        textPane.setText("<html><head>" + css + "</head><body></body></html>");

        erstelleGUI();
    }

    private void erstelleGUI() {
        add(new JScrollPane(textPane), BorderLayout.CENTER);
        add(erstelleButtonPanel(), BorderLayout.EAST);
        add(erstelleUntererOptionPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
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

    private JButton erstelleButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    private JPanel erstelleUntererOptionPanel() {
        JPanel untererPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        untererPanel.setBackground(BACKGROUND_COLOR);

        JButton statistikButton = erstelleButton("Spielzusammenfassung anzeigen", e -> zeigeErgebnisse());
        JButton beendenButton = erstelleButton("Beenden", e -> beendenAktion());

        untererPanel.add(statistikButton);
        untererPanel.add(beendenButton);

        return untererPanel;
    }

    private void handleAction(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        try {
            Auswahl benutzerAuswahl = Auswahl.valueOf(actionCommand.toUpperCase());
            starteSpielzug(benutzerAuswahl);
        } catch (IllegalArgumentException ex) {
            textPane.setText(formatHtml("<span>Ungültige Auswahl.</span>"));
        }
    }

    private void starteSpielzug(Auswahl benutzerAuswahl) {
        Auswahl computerAuswahl = Auswahl.values()[RANDOM.nextInt(Auswahl.values().length)];
        ermittelGewinner(benutzerAuswahl, computerAuswahl);
    }

    private void ermittelGewinner(Auswahl benutzerAuswahl, Auswahl computerAuswahl) {
        String htmlContent = String.format("Deine Wahl: %s<br>Computer wählte: %s<br>", benutzerAuswahl, computerAuswahl);

        if (benutzerAuswahl == computerAuswahl) {
            unentschieden++;
            htmlContent += "<span class='yellow'>Unentschieden!</span>";
        } else if (GEWINNBEDIGUNGEN.get(benutzerAuswahl).contains(computerAuswahl)) {
            siege++;
            htmlContent += String.format("<span class='green'>%s schlägt %s!<br>Du gewinnst!</span>", benutzerAuswahl, computerAuswahl);
        } else {
            niederlagen++;
            htmlContent += String.format("<span class='red'>%s schlägt %s!<br>Du verlierst!</span>", computerAuswahl, benutzerAuswahl);
        }

        textPane.setText(formatHtml(htmlContent));
    }

    private void zeigeErgebnisse() {
        if (istStatistikSichtbar) {
            textPane.setText(zuletztAngezeigterText);
        } else {
            zuletztAngezeigterText = textPane.getText();
            String htmlContent = String.format("Spielzusammenfassung<br>Siege: %d<br>Niederlagen: %d<br>Unentschieden: %d", siege, niederlagen, unentschieden);
            textPane.setText(formatHtml(htmlContent));
        }
        istStatistikSichtbar = !istStatistikSichtbar;
    }

    private void beendenAktion() {
        new Thread(() -> {
            SwingUtilities.invokeLater(() -> textPane.setText(formatHtml(
                    "<div style='text-align: center; font-size: 35px;'>Good bye</div>"
            )));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                System.err.println("Fehler beim Warten vor dem Beenden: " + ex.getMessage());
            }
            System.exit(0);
        }).start();
    }

    private String formatHtml(String content) {
        return "<html><head>" + css + "</head><body>" + content + "</body></html>";
    }

    private String getTooltipForAuswahl(Auswahl auswahl) {
        Set<Auswahl> gewinntGegen = GEWINNBEDIGUNGEN.get(auswahl);
        return auswahl + " schlägt " + String.join(", ", gewinntGegen.stream().map(Enum::toString).toArray(String[]::new));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameGUI::new);
    }
}
