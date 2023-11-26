package bsu.rfe.java.group_5.lab_3.Chystsiakou.C_1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MainFrame extends JFrame{
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    public Double[] coefficients;
    private JFileChooser fileChooser = null;
    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem saveCSVMenuItem;
    private JMenuItem searchValueMenuItem;
    private JMenuItem searchValueZoneMenuItem;
    private JMenuItem AboutProgram;
    // Поля ввода для считывания значений переменных
    private JTextField text_from;
    private JTextField text_to;
    private JTextField text_step;
    private Box hBoxResult;
    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
    private GornerTableModel data;
    public MainFrame(Double[] coefficients) {
    super("Лабораторная работа № 3");
    this.coefficients = coefficients;
    setSize(WIDTH, HEIGHT);
    Toolkit kit = Toolkit.getDefaultToolkit();
    setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    JMenu fileMenu = new JMenu("Файл");
    menuBar.add(fileMenu);
    JMenu tableMenu = new JMenu("Таблица");
    menuBar.add(tableMenu);
    Action saveToTextFile = new AbstractAction("Сохранить в текстовый файл") {
    public void actionPerformed(ActionEvent event) {
        if (fileChooser==null) {
            fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("D:\\"));
        }
        if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
            saveToTextFile(fileChooser.getSelectedFile());
        }
    };
    saveToTextMenuItem = fileMenu.add(saveToTextFile);
    saveToTextMenuItem.setEnabled(false);
    Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {
    public void actionPerformed(ActionEvent event) {
        if (fileChooser==null) {
            fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("D:\\"));
            }
        if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
            saveToGraphicsFile(fileChooser.getSelectedFile());
        }
    };
    saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
    saveToGraphicsMenuItem.setEnabled(false);

    Action saveToCSVFile = new AbstractAction("Сохранить в CSV файл") {
        public void actionPerformed(ActionEvent event) {
            if (fileChooser==null) {
                fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("D:\\"));
            }
            if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                saveToCSV(fileChooser.getSelectedFile());
        }
    };
    saveCSVMenuItem = fileMenu.add(saveToCSVFile);
    saveCSVMenuItem.setEnabled(false);

    Action searchValueAction = new AbstractAction("Найти значение многочлена") {
    public void actionPerformed(ActionEvent event) {
        String value = JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска", "Поиск значения", JOptionPane.QUESTION_MESSAGE);
        renderer.setNeedle(value);
        getContentPane().repaint(); }
    };
    searchValueMenuItem = tableMenu.add(searchValueAction);
    searchValueMenuItem.setEnabled(false);
    JMenu ReferenceMenu = new JMenu("Справка");
    menuBar.add(ReferenceMenu);
    Action Reference = new AbstractAction("О программе") {
        public void actionPerformed(ActionEvent event) {
            ReferenceFrame RefFrame = new ReferenceFrame();
            RefFrame.setDefaultCloseOperation(ReferenceFrame.DISPOSE_ON_CLOSE);
            RefFrame.setVisible(true);
        }
    };
    AboutProgram = ReferenceMenu.add(Reference);
    AboutProgram.setEnabled(true);
    Action searchValueZone = new AbstractAction("Найти значения многочлена в диапазоне") {
            public void actionPerformed(ActionEvent event) {
                String value = JOptionPane.showInputDialog(MainFrame.this, "Введите левую часть и правую часть диапазона через пробел", "Поиск диапазона", JOptionPane.QUESTION_MESSAGE);
                String[] valueZone = value.split(" ");
                String value_left = valueZone[0];
                String value_right = valueZone[1];
                renderer.setNeedleZone(value_left,value_right);
                getContentPane().repaint(); }
        };
    searchValueZoneMenuItem = tableMenu.add(searchValueZone);
    searchValueZoneMenuItem.setEnabled(false);

    JLabel label_from = new JLabel("X изменяется на интервале от:");
    text_from = new JTextField("0.0", 10);
    text_from.setMaximumSize(text_from.getPreferredSize());
    JLabel label_to = new JLabel("до:");
    text_to = new JTextField("1.0", 10);
    text_to.setMaximumSize(text_to.getPreferredSize());
    JLabel label_step = new JLabel("с шагом:");
    text_step = new JTextField("0.1", 10);
    text_step.setMaximumSize(text_step.getPreferredSize());
    Box box_info = Box.createHorizontalBox();

    box_info.setBorder(BorderFactory.createBevelBorder(1));
    box_info.add(Box.createHorizontalGlue());
    box_info.add(label_from);
    box_info.add(Box.createHorizontalStrut(10));
    box_info.add(text_from);
    box_info.add(Box.createHorizontalStrut(20));
    box_info.add(label_to);
    box_info.add(Box.createHorizontalStrut(10));
    box_info.add(text_to);
    box_info.add(Box.createHorizontalStrut(20));
    box_info.add(label_step);
    box_info.add(Box.createHorizontalStrut(10));
    box_info.add(text_step);
    box_info.add(Box.createHorizontalGlue());
    box_info.setPreferredSize(new Dimension(new Double(box_info.getMaximumSize().getWidth()).intValue(),new Double(box_info.getMinimumSize().getHeight()).intValue()*2));
    getContentPane().add(box_info, BorderLayout.NORTH);
    //Кнопки
    JButton buttonCalc = new JButton("Вычислить");
    buttonCalc.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent ev) {
        try {
            Double from = Double.parseDouble(text_from.getText());
            Double to = Double.parseDouble(text_to.getText());
            Double step = Double.parseDouble(text_step.getText());
            data = new GornerTableModel(from, to, step, MainFrame.this.coefficients);
            JTable table = new JTable(data);
            table.setDefaultRenderer(Double.class, renderer);
            table.setRowHeight(30);

            hBoxResult.removeAll();
            hBoxResult.add(new JScrollPane(table));
            getContentPane().validate();
            saveToTextMenuItem.setEnabled(true);
            saveToGraphicsMenuItem.setEnabled(true);
            saveCSVMenuItem.setEnabled(true);
            searchValueMenuItem.setEnabled(true);
            searchValueZoneMenuItem.setEnabled(true);
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(MainFrame.this, "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);}
        }
    });
    JButton buttonReset = new JButton("Очистить поля");
    buttonReset.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            text_from.setText("0.0");
            text_to.setText("1.0");
            text_step.setText("0.1");
            hBoxResult.removeAll();
            hBoxResult.add(new JPanel());
            saveToTextMenuItem.setEnabled(false);
            saveToGraphicsMenuItem.setEnabled(false);
            saveCSVMenuItem.setEnabled(false);
            searchValueMenuItem.setEnabled(false);
            searchValueZoneMenuItem.setEnabled(false);
            getContentPane().validate();
            }
        });
    Box box_for_buttons = Box.createHorizontalBox();
    box_for_buttons.setBorder(BorderFactory.createBevelBorder(1));
    box_for_buttons.add(Box.createHorizontalGlue());
    box_for_buttons.add(buttonCalc);
    box_for_buttons.add(Box.createHorizontalStrut(30));
    box_for_buttons.add(buttonReset);
    box_for_buttons.add(Box.createHorizontalGlue());
    box_for_buttons.setPreferredSize(new Dimension(new Double(box_for_buttons.getMaximumSize().getWidth()).intValue(), new Double(box_for_buttons.getMinimumSize().getHeight()).intValue()*2));
    getContentPane().add(box_for_buttons, BorderLayout.SOUTH);
    hBoxResult = Box.createHorizontalBox();
    hBoxResult.add(new JPanel());
    getContentPane().add(hBoxResult, BorderLayout.CENTER);
}
protected void saveToGraphicsFile(File selectedFile) {
    try {
    DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
    for (int i = 0; i<data.getRowCount(); i++) {
        out.writeDouble((Double)data.getValueAt(i,0)); out.writeDouble((Double)data.getValueAt(i,1));
    }
    out.close();
    }
    catch (Exception e) {}
}
protected void saveToTextFile(File selectedFile) {
    try {
    PrintStream out = new PrintStream(selectedFile);
    out.println("Результаты табулирования многочлена по схеме Горнера");
    out.print("Многочлен: ");
    for (int i=0; i<coefficients.length; i++) {
        out.print(coefficients[i] + "*X^" + (coefficients.length-i-1));
        if (i!=coefficients.length-1)
            out.print(" + ");
        }
    out.println("");
    out.println("Интервал от " + data.getFrom() + " до " + data.getTo() + " с шагом " + data.getStep());
    out.println("====================================================");
    for (int i = 0; i<data.getRowCount(); i++) {
        out.println("Значение в точке " + data.getValueAt(i,0) + " равно " + data.getValueAt(i,1));
    }
    out.close();
    }
    catch (FileNotFoundException e) {}
    }
    public void saveToCSV(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            int rows = data.getRowCount();
            Double[][] table = data.getTable();
            for (int i = 0; i < rows; i++) {
                writer.write(table[i][0] + ", " + table[i][1] + ", " + table[i][2] + ", " + table[i][3] + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
