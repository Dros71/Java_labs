package bsu.rfe.java.group_5.lab_2.Chystsiakou.C_1;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MainFrame extends JFrame {
    private static BufferedImage readImage(String location) {
        BufferedImage bufferedImage;
        File imageFile = new File(location);
        if (!imageFile.exists() || !imageFile.canRead()) {
            throw new IllegalArgumentException("This file does not exists or unreadable");
        }
        try {
            bufferedImage = ImageIO.read(new File(location));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (bufferedImage == null) {
            throw new RuntimeException("Image cannot be edited, try again later.");
        }
        return bufferedImage;
    }
    private JLabel labelImage;
    private final Map<Integer, ImageIcon> images;
    private final ImageIcon image1;
    private final ImageIcon image2;

    public void setImage(int num) {
        if (num <= 0 || num > images.size()) {
            throw new IllegalArgumentException("no such image");
        }
        labelImage.setIcon(images.get(num));
    }
    private static final int width = 600;
    private static final int height = 600;
    private JTextField textX;
    private JTextField textY;
    private JTextField textZ;
    private JTextField text_mem1;
    private JTextField text_mem2;
    private JTextField text_mem3;
    private JTextField textResult;
    private ButtonGroup radioButtons_calculations = new ButtonGroup();
    private ButtonGroup radioButtons_memory = new ButtonGroup();
    private int formulaId_calculations = 1;
    private int formulaId_memory = 1;
    public Double calculate1(Double x, Double y, Double z) {
        return Math.sin(Math.log(y)
                + Math.sin(Math.PI * y * y))
                * Math.pow(x * x + Math.sin(z)
                + Math.exp(Math.cos(z)),0.25);
    }
    public Double calculate2(Double x, Double y, Double z) {
        return Math.pow(Math.cos(Math.exp(x))
                + Math.log(Math.pow(1 + y,2))
                + Math.sqrt(Math.exp(Math.cos(x))
                + Math.pow(Math.sin(Math.PI * z),2))
                + Math.sqrt(1/x) + Math.cos(y*y),Math.sin(z));
    }
    private void add_radio_button_memory(String buttonName, final int formulaId, ButtonGroup radioButtons, Box boxForButtons) {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainFrame.this.formulaId_memory = formulaId;
            }
        });
        radioButtons.add(button);
        boxForButtons.add(button);
    }
    private void addRadioButton(String buttonName, final int formulaId, ButtonGroup radioButtons, Box boxForButtons) {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainFrame.this.formulaId_calculations = formulaId;
                if (formulaId_calculations == 1) setImage(1);
                if (formulaId_calculations == 2) setImage(2);
            }
        });
        radioButtons.add(button);
        boxForButtons.add(button);
    }

    public MainFrame() {
        super("Лабораторная работа № 2");
        image1 = new ImageIcon(readImage("D:/РАФ/Semester_3/Java/Labs/Lab_2/src/bsu/rfe/java/group_5/lab_2/Chystsiakou/C_1/formula_1.bmp").getScaledInstance(550, 60, BufferedImage.TYPE_INT_ARGB));
        image2 = new ImageIcon(readImage("D:/РАФ/Semester_3/Java/Labs/Lab_2/src/bsu/rfe/java/group_5/lab_2/Chystsiakou/C_1/formula_2.bmp").getScaledInstance(550, 60, BufferedImage.TYPE_INT_ARGB));
        images = Map.of(1, image1, 2, image2);

        setSize(width, height);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2 - 250,
                (kit.getScreenSize().height - HEIGHT) / 2 - 250);

        Box box_for_radio_buttons = Box.createHorizontalBox();
        box_for_radio_buttons.add(Box.createHorizontalGlue());
        addRadioButton("Формула 1", 1, radioButtons_calculations, box_for_radio_buttons);
        addRadioButton("Формула 2", 2, radioButtons_calculations, box_for_radio_buttons);
        radioButtons_calculations.setSelected(radioButtons_calculations.getElements().nextElement().getModel(), true);
        box_for_radio_buttons.add(Box.createHorizontalGlue());
        box_for_radio_buttons.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //КАРТИНКА!!!
        Box box_for_image = Box.createHorizontalBox();
        box_for_image.add(Box.createHorizontalGlue());
        labelImage = new JLabel(image1);
        box_for_image.add(labelImage);
        box_for_image.add(Box.createHorizontalGlue());
        box_for_image.setMaximumSize(box_for_image.getPreferredSize());
        box_for_image.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        //memory радио-кнопки
        Box box_for_radio_buttons_memories = Box.createHorizontalBox();
        add_radio_button_memory("mem 1", 1,radioButtons_memory, box_for_radio_buttons_memories);
        add_radio_button_memory("mem 2", 2, radioButtons_memory,box_for_radio_buttons_memories);
        add_radio_button_memory("mem 3", 3, radioButtons_memory, box_for_radio_buttons_memories);
        radioButtons_memory.setSelected(radioButtons_memory.getElements().nextElement().getModel(), true);

        // Поля для XYZ
        JLabel labelX = new JLabel("X:");
        textX = new JTextField("0", 10);
        textX.setMaximumSize(textX.getPreferredSize());
        //labelX.setMaximumSize(labelX.getPreferredSize());

        JLabel labelY = new JLabel("Y:");
        textY = new JTextField("0", 10);
        textY.setMaximumSize(textY.getPreferredSize());
        //labelY.setMaximumSize(labelY.getPreferredSize());

        JLabel labelZ = new JLabel("Z:");
        textZ = new JTextField("0", 10);
        textZ.setMaximumSize(textZ.getPreferredSize());
        //labelZ.setMaximumSize(labelZ.getPreferredSize());

        Box box_for_XYZ = Box.createHorizontalBox();
        box_for_XYZ.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //box_for_XYZ.setMaximumSize(box_for_XYZ.getPreferredSize());
        //box_for_XYZ.add(Box.createHorizontalGlue());
        box_for_XYZ.add(labelX);
        box_for_XYZ.add(Box.createHorizontalStrut(10));
        box_for_XYZ.add(textX);
        //box_for_XYZ.add(Box.createHorizontalStrut(60));
        box_for_XYZ.add(Box.createHorizontalGlue());
        box_for_XYZ.add(labelY);
        box_for_XYZ.add(Box.createHorizontalStrut(10));
        box_for_XYZ.add(textY);
        //box_for_XYZ.add(Box.createHorizontalStrut(60));
        box_for_XYZ.add(Box.createHorizontalGlue());
        box_for_XYZ.add(labelZ);
        box_for_XYZ.add(Box.createHorizontalStrut(10));
        box_for_XYZ.add(textZ);
        //box_for_XYZ.add(Box.createHorizontalGlue());




        //Поле с результатом
        JLabel labelResult = new JLabel("Результат:");
        textResult = new JTextField("0", 15);
        textResult.setMaximumSize(textResult.getPreferredSize());
        Box boxForResult = Box.createHorizontalBox();
        boxForResult.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        boxForResult.add(Box.createHorizontalGlue());
        boxForResult.add(labelResult);
        boxForResult.add(Box.createHorizontalStrut(10));
        boxForResult.add(textResult);
        boxForResult.add(Box.createHorizontalGlue());

        //memory текст
        JLabel label_mem1 = new JLabel("mem1:");
        text_mem1 = new JTextField("0", 12);
        text_mem1.setMaximumSize(text_mem1.getPreferredSize());

        JLabel label_mem2 = new JLabel("mem2:");
        text_mem2 = new JTextField("0", 12);
        text_mem2.setMaximumSize(text_mem2.getPreferredSize());

        JLabel label_mem3 = new JLabel("mem3:");
        text_mem3 = new JTextField("0", 12);
        text_mem3.setMaximumSize(text_mem3.getPreferredSize());

    //Кнопки
    JButton buttonCalc = new JButton("Вычислить");
    buttonCalc.addActionListener(new ActionListener() {
        public void actionPerformed (ActionEvent ev){
            try {
                Double x = Double.parseDouble(textX.getText());
                Double y = Double.parseDouble(textY.getText());
                Double z = Double.parseDouble(textZ.getText());
                Double result;
                if (formulaId_calculations == 1)
                    result = calculate1(x, y, z);
                else
                    result = calculate2(x, y, z);
                textResult.setText(result.toString());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(MainFrame.this, "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
            }
        }
    });
    JButton buttonReset = new JButton("Очистить поля");
    buttonReset.addActionListener(new ActionListener() {
        public void actionPerformed (ActionEvent ev){
            textX.setText("0");
            textY.setText("0");
            textZ.setText("0");
            textResult.setText("0");
        }
    });

    JButton buttonMC = new JButton("MC");
        buttonMC.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent ev){
                try{
                    if (formulaId_memory == 1)
                        text_mem1.setText("0");
                    else if (formulaId_memory == 2)
                        text_mem2.setText("0");
                    else
                        text_mem3.setText("0");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
                }
            }
    });

    JButton buttonMPlus = new JButton("M+");
    buttonMPlus.addActionListener(new ActionListener() {
        public void actionPerformed (ActionEvent ev){
            try{
            Double result = Double.parseDouble(textResult.getText());
            if (formulaId_memory == 1)
                text_mem1.setText(Double.toString(Double.parseDouble(text_mem1.getText()) + result));
            else if (formulaId_memory == 2)
                text_mem2.setText(Double.toString(Double.parseDouble(text_mem2.getText()) + result));
            else
                text_mem3.setText(Double.toString(Double.parseDouble(text_mem3.getText()) + result));
            textResult.setText(result.toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(MainFrame.this, "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
        }
        }
    });

    //Кнопки calc по коробкам
    Box boxForButtons = Box.createHorizontalBox();
    boxForButtons.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    boxForButtons.add(Box.createHorizontalGlue());
    boxForButtons.add(buttonCalc);
    boxForButtons.add(Box.createHorizontalStrut(30));
    boxForButtons.add(buttonReset);
    boxForButtons.add(Box.createHorizontalGlue());

    //mems лейблы
    Box box_for_memory_1 = Box.createHorizontalBox();
    box_for_memory_1.add(Box.createHorizontalGlue());
    box_for_memory_1.add(label_mem1);
    box_for_memory_1.add(Box.createHorizontalStrut(15));
    box_for_memory_1.add(text_mem1);
    box_for_memory_1.add(Box.createHorizontalGlue());

    Box box_for_memory_2 = Box.createHorizontalBox();
    box_for_memory_2.add(Box.createHorizontalGlue());
    box_for_memory_2.add(label_mem2);
    box_for_memory_2.add(Box.createHorizontalStrut(15));
    box_for_memory_2.add(text_mem2);
    box_for_memory_2.add(Box.createHorizontalGlue());

    Box box_for_memory_3 = Box.createHorizontalBox();
    box_for_memory_3.add(Box.createHorizontalGlue());
    box_for_memory_3.add(label_mem3);
    box_for_memory_3.add(Box.createHorizontalStrut(15));
    box_for_memory_3.add(text_mem3);
    box_for_memory_3.add(Box.createHorizontalGlue());


    //Коробки для memory информации
    Box box_for_memories = Box.createVerticalBox();
    box_for_memories.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    box_for_memories.add(box_for_memory_1);
    box_for_memories.add(box_for_memory_2);
    box_for_memories.add(box_for_memory_3);


    //Коробки для memory кнопок
    Box box_for_buttons_memories = Box.createHorizontalBox();
    box_for_buttons_memories.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    box_for_buttons_memories.add(Box.createHorizontalGlue());
    box_for_buttons_memories.add(buttonMC);
    box_for_buttons_memories.add(Box.createHorizontalStrut(30));
    box_for_buttons_memories.add(buttonMPlus);
    box_for_buttons_memories.add(Box.createHorizontalGlue());



    Box contentBox = Box.createVerticalBox();
    contentBox.add(Box.createVerticalGlue());
    contentBox.add(box_for_radio_buttons);
    contentBox.add(box_for_image);
    contentBox.add(box_for_XYZ);
    contentBox.add(box_for_memories);
    contentBox.add(box_for_radio_buttons_memories);
    contentBox.add(box_for_buttons_memories);
    contentBox.add(boxForResult);
    contentBox.add(boxForButtons);
    contentBox.add(Box.createVerticalGlue());
    getContentPane().add(contentBox, BorderLayout.CENTER);

    }
}
