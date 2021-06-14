package net.tomocraft.uma.components;

import net.tomocraft.uma.Main;
import net.tomocraft.uma.utils.CheckBoxToggler;
import net.tomocraft.uma.utils.StackTraceDialog;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

public class SettingsWindow extends JFrame {

    private final Main main;

    private final CheckBoxList checkBoxList;
    private final LinkedList<JCheckBox> charButtons;
    private final MainPanel mainPanel;

    public SettingsWindow(Main main, MainPanel mainPanel) {
        super("Settings");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        this.main = main;
        this.mainPanel = mainPanel;

        // Initialize Top Pane, aka main
        JPanel mainPane = new JPanel();
        mainPane.setLayout(new BorderLayout());
        add(mainPane, BorderLayout.CENTER);

        DefaultListModel<JCheckBox> model = new DefaultListModel<>();
        this.checkBoxList = new CheckBoxList(model);
        this.charButtons = new LinkedList<>();
        for (final String name : main.getCharList()) {
            main.getCharMap().putIfAbsent(name, true);

            JCheckBox cb = new JCheckBox(name, main.getCharMap().get(name));
            model.addElement(cb);
            this.charButtons.push(cb);
        }
        mainPane.add(new JScrollPane(this.checkBoxList), BorderLayout.CENTER);
        this.applySettings();

        // Initialize Bottom Buttons.
        JPanel bottomPane = new JPanel(new BorderLayout());
        add(bottomPane, BorderLayout.PAGE_END);

        JPanel toggleAllGroup = new JPanel();
        toggleAllGroup.setLayout(new BoxLayout(toggleAllGroup, BoxLayout.X_AXIS));

        JButton enableAllButton = new JButton("All ON");
        enableAllButton.addActionListener(new CheckBoxToggler(charButtons, true, checkBoxList::updateUI));
        toggleAllGroup.add(enableAllButton);

        JButton disableAllButton = new JButton("All OFF");
        disableAllButton.addActionListener(new CheckBoxToggler(charButtons, false, checkBoxList::updateUI));
        toggleAllGroup.add(disableAllButton);

        bottomPane.add(toggleAllGroup, BorderLayout.LINE_START);

        JPanel basicButtonGroup = new JPanel();
        basicButtonGroup.setLayout(new BoxLayout(basicButtonGroup, BoxLayout.X_AXIS));

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> {
            if (!showConfirm("設定を適応しますか？")) return;

            for (final JCheckBox cb : this.charButtons) {
                this.main.getCharMap().put(cb.getText(), cb.isSelected());
            }

            this.applySettings();
        });
        basicButtonGroup.add(applyButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            boolean anyChanges = false;
            for (JCheckBox charButton : this.charButtons) {
                if (charButton.isSelected() != this.main.getCharMap().get(charButton.getText())) {
                    anyChanges = true;
                    break;
                }
            }
            if (anyChanges) {
                if (showConfirm("変更がありますが破棄しますか？")) {
                    flushCheckboxes();
                    this.setVisible(false);
                }
            } else {
                this.setVisible(false);
            }
        });
        basicButtonGroup.add(cancelButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            if (!showConfirm("設定をリセットしますか？")) return;

            this.main.getCharMap().replaceAll((k, v) -> true);
            this.flushCheckboxes();

            this.applySettings();
        });
        basicButtonGroup.add(resetButton);

        bottomPane.add(basicButtonGroup, BorderLayout.LINE_END);
    }

    private void applySettings() {
        try {
            this.main.getCharMap().save();
        } catch (IOException ex) {
            StackTraceDialog.showStackTraceDialog(ex);
            ex.printStackTrace();
        }
        this.mainPanel.regenList();
    }

    private void flushCheckboxes() {
        for (JCheckBox charButton : charButtons) {
            charButton.setSelected(this.main.getCharMap().getOrDefault(charButton.getText(), true));
        }
        this.checkBoxList.updateUI();
    }

    private boolean showConfirm(final String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Confirm Settings", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0;
    }
}
