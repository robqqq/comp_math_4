package ru.itmo.robq.comp_math_4.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

abstract class CustomFrame extends JFrame {

    public CustomFrame() {
        setTitle("Вычислительная математика - лабораторная 3");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 600));
        setLocationRelativeTo(null);
        BoxLayout bl = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        getContentPane().setLayout(bl);
    }

    protected void addComponent(JComponent component) {
        component.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        getContentPane().add(Box.createRigidArea(new Dimension(0, 10)));
        getContentPane().add(component);
    }

    protected void addButtonGroup(ButtonGroup buttonGroup) {
        for (Iterator<AbstractButton> it = buttonGroup.getElements().asIterator(); it.hasNext(); ) {
            JRadioButton button = (JRadioButton) it.next();
            addComponent(button);
        }
    }

    protected void addHorizontalComponents(int height, JComponent... components) {
        JPanel panel = new JPanel();
        panel.setMaximumSize(new Dimension(2000, height));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        for (JComponent component: components) {
            panel.add(component);
            panel.add(Box.createRigidArea(new Dimension(10, 0)));
        }
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addComponent(panel);
    }
}
