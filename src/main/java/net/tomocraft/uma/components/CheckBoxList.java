package net.tomocraft.uma.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class CheckBoxList extends JList<JCheckBox> {
    private transient CheckBoxCellRenderer renderer;
    private transient MouseListener handler;

    protected CheckBoxList(ListModel<JCheckBox> model) {
        super(model);
    }

    @Override
    public void updateUI() {
        System.out.println("Called draw!");
        setForeground(null);
        setBackground(null);
        setSelectionForeground(null);
        setSelectionBackground(null);
        removeMouseListener(handler);
        removeMouseListener(renderer);
        removeMouseMotionListener(renderer);
        super.updateUI();
        handler = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = locationToIndex(e.getPoint());
                if (e.getButton() == MouseEvent.BUTTON1 && index >= 0) {
                    DefaultListModel<JCheckBox> m = (DefaultListModel<JCheckBox>) getModel();
                    JCheckBox n = m.get(index);
                    n.setSelected(!n.isSelected());
                    repaint(getCellBounds(index, index));
                }
            }
        };
        addMouseListener(handler);
        renderer = new CheckBoxCellRenderer();
        setCellRenderer(renderer);
        addMouseListener(renderer);
        addMouseMotionListener(renderer);
        putClientProperty("List.isFileList", Boolean.TRUE);
    }

    private boolean pointOutsidePrefSize(Point p) {
        int i = locationToIndex(p);
        JCheckBox cbn = getModel().getElementAt(i);
        Component c = getCellRenderer().getListCellRendererComponent(this, cbn, i, false, false);
        Rectangle rect = getCellBounds(i, i);
        rect.width = c.getPreferredSize().width;
        return i < 0 || !rect.contains(p);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        if (!pointOutsidePrefSize(e.getPoint())) {
            super.processMouseEvent(e);
        }
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        if (pointOutsidePrefSize(e.getPoint())) {
            MouseEvent ev = new MouseEvent(
                    e.getComponent(), MouseEvent.MOUSE_EXITED, e.getWhen(),
                    e.getModifiersEx(), e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(),
                    e.getClickCount(), e.isPopupTrigger(), MouseEvent.NOBUTTON);
            super.processMouseEvent(ev);
        } else {
            super.processMouseMotionEvent(e);
        }
    }
}

class CheckBoxCellRenderer extends MouseAdapter implements ListCellRenderer<JCheckBox> {
    private final JCheckBox checkBox = new JCheckBox();
    private int rollOverRowIndex = -1;

    @Override
    public Component getListCellRendererComponent(JList<? extends JCheckBox> list, JCheckBox value, int index, boolean isSelected, boolean cellHasFocus) {
        checkBox.setOpaque(true);
        if (isSelected) {
            checkBox.setBackground(list.getSelectionBackground());
            checkBox.setForeground(list.getSelectionForeground());
        } else {
            checkBox.setBackground(list.getBackground());
            checkBox.setForeground(list.getForeground());
        }
        checkBox.setSelected(value.isSelected());
        checkBox.getModel().setRollover(index == rollOverRowIndex);
        checkBox.setText(value.getText());
        return checkBox;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (rollOverRowIndex >= 0) {
            JList<?> l = (JList<?>) e.getComponent();
            l.repaint(l.getCellBounds(rollOverRowIndex, rollOverRowIndex));
            rollOverRowIndex = -1;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        JList<?> l = (JList<?>) e.getComponent();
        int index = l.locationToIndex(e.getPoint());
        if (index != rollOverRowIndex) {
            rollOverRowIndex = index;
            l.repaint();
        }
    }
}