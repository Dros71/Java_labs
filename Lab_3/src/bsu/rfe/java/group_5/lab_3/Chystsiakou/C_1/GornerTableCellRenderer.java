package bsu.rfe.java.group_5.lab_3.Chystsiakou.C_1;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class GornerTableCellRenderer implements TableCellRenderer {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    private String needle = null;
    private String needle_left = null;
    private String needle_right = null;
    private DecimalFormat formatter =
            (DecimalFormat) NumberFormat.getInstance();
    public GornerTableCellRenderer() {
        formatter.setMaximumFractionDigits(5);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        panel.add(label);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        String formattedDouble = formatter.format(value);
        label.setText(formattedDouble);
        double number = Double.parseDouble(formattedDouble);
        boolean left_right = false;
        if (needle_left != null && needle_right != null){
            double left = Double.parseDouble(needle_left);
            double right = Double.parseDouble(needle_right);
            left_right = number >= left && number <=right;
        }

        if ((col==1 || col == 2) && ((needle!=null && needle.equals(formattedDouble)) ||
                ((needle_left != null) && (needle_right != null) && left_right))) {
            panel.setBackground(Color.RED);
            label.setForeground(Color.BLACK);
            label.setText(formattedDouble);
        } else {
            if ((row + col) % 2 == 0){
                panel.setBackground(Color.BLACK);
                label.setForeground(Color.WHITE);
                label.setText(formattedDouble);
            }else {
                panel.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
                label.setText(formattedDouble);
            }
        }
        return panel;
    }
    public Component getTableCellRendererZone(JTable table, Object left,Object right, boolean isSelected, boolean hasFocus, int row, int col) {
        String formattedDouble_1 = formatter.format(left);
        String formattedDouble_2 = formatter.format(right);
        label.setText(formattedDouble_1 + " to " + formattedDouble_2);
        if (col==1 && needle!=null && needle_left.equals(formattedDouble_1)) {
            do {
                row++;
                panel.setBackground(Color.RED);
            }
            while(!needle_left.equals(formattedDouble_2));
        } else {
            panel.setBackground(Color.WHITE);
        }
        return panel;
    }
    public void setNeedle(String needle) {
        this.needle = needle;
    }
    public void setNeedleZone(String needle_left, String needle_right) {
        this.needle_left = needle_left;
        this.needle_right = needle_right;
    }
}
