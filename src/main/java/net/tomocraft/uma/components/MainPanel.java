package net.tomocraft.uma.components;

import net.tomocraft.uma.Main;
import net.tomocraft.uma.CharMap;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MainPanel extends JPanel {

    private final static Random r = new Random();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Main main;
    private ScheduledFuture<?> ongoingRollTask;
    private List<String> charList;

    public MainPanel(Main main) {
        setLayout(new BorderLayout());

        this.main = main;

        JLabel selectedNameLabel = new JLabel();
        selectedNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        selectedNameLabel.setText("Test!");
        add(selectedNameLabel, BorderLayout.CENTER);

        Runnable rollTask = () -> SwingUtilities.invokeLater(() -> selectedNameLabel.setText(getRandomCharName()));
        JButton rollButton = new JButton("Roll!");
        rollButton.addActionListener(e -> {
            if (this.ongoingRollTask != null) { // If already rolling.
                this.ongoingRollTask.cancel(true);
                this.ongoingRollTask = null;
                rollButton.setText("Roll!");
            } else {
                this.ongoingRollTask = scheduler.scheduleWithFixedDelay(rollTask, 0, 50, TimeUnit.MILLISECONDS);
                rollButton.setText("Stop!");
            }
        });
        add(rollButton, BorderLayout.SOUTH);

        JFrame settingsWindow = new SettingsWindow(main, this);
        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> settingsWindow.setVisible(true));
        add(settingsButton, BorderLayout.NORTH);
    }

    public void regenList() {
        final CharMap map = this.main.getCharMap();
        this.charList = this.main.getCharList().stream().filter(map::get).collect(Collectors.toList());
    }

    public String getRandomCharName() {
        return this.charList.get(MainPanel.r.nextInt(this.charList.size()));
    }
}
