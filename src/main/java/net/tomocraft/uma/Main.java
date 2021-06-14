package net.tomocraft.uma;

import net.tomocraft.uma.components.MainPanel;
import net.tomocraft.uma.utils.StackTraceDialog;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class Main extends JFrame {

    public static final File charListFile = new File("./uma_list.txt");

    private final CharMap charMap;
    private final LinkedList<String> charList;

    Main() throws IOException {
        super("Uma Roulette");
        setSize(500, 300);
        setMinimumSize(new Dimension(500, 300));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.charMap = new CharMap();
        this.charList = loadAllCharacters();

        add(new MainPanel(this));
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Main window = new Main();
                window.setVisible(true);
            } catch (IOException e) {
                StackTraceDialog.showStackTraceDialog(e);
                e.printStackTrace();
            }
        });
    }

    public CharMap getCharMap() {
        return this.charMap;
    }

    public LinkedList<String> getCharList() {
        return this.charList;
    }

    private LinkedList<String> loadAllCharacters() throws IOException {
        final LinkedList<String> chars = new LinkedList<>();
        try (BufferedReader br = new BufferedReader((new InputStreamReader(new FileInputStream(Main.charListFile), StandardCharsets.UTF_8)))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                chars.add(line);
            }
        }
        return chars;
    }
}
