package net.tomocraft.uma.utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CheckBoxToggler implements ActionListener {

    private final List<JCheckBox> checkboxes;
    private final boolean toEnable;
    private final Runnable flusher;

    public CheckBoxToggler(final List<JCheckBox> checkboxes, final boolean toEnable, final Runnable flusher) {
        this.checkboxes = checkboxes;
        this.toEnable = toEnable;
        this.flusher = flusher;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (JCheckBox checkbox : this.checkboxes) {
            checkbox.setSelected(this.toEnable);
        }
        flusher.run();
    }
}
