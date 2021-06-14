package net.tomocraft.uma.utils;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceDialog {

    public static void showStackTraceDialog(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("処理できない例外が発生しました。");

        JTextArea area = new JTextArea();
        area.setText(sw.toString());
        area.setCaretPosition(0);
        area.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setPreferredSize(new Dimension(600, 200));

        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.SOUTH);

        // ダイアログ表示
        JOptionPane.showMessageDialog(null, panel, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
