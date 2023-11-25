package bsu.rfe.java.group_5.lab_3.Chystsiakou.C_1;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ReferenceFrame extends JFrame {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;
    private JLabel label_info;
    private JLabel label_image;
    private ImageIcon Icon;
    public ReferenceFrame(){
        super("Справка");
        Toolkit kit_ref = Toolkit.getDefaultToolkit();
        setLocation((kit_ref.getScreenSize().width - WIDTH)/2,
                (kit_ref.getScreenSize().height - HEIGHT)/2);
        setSize(WIDTH, HEIGHT);
        label_info = new JLabel("Чистяков Савелий, 5 группа");
        Icon = new ImageIcon(readImage("D:/РАФ/Семестр_number_3/Java/Labs/Lab_3/src/bsu/rfe/java/group_5/lab_3/Chystsiakou/C_1/Me.jpg").getScaledInstance(150, 150, BufferedImage.TYPE_INT_ARGB));
        label_image = new JLabel(Icon);
        Box contentBox =Box.createVerticalBox();
        Box imageBox = Box.createHorizontalBox();
        imageBox.add(Box.createHorizontalGlue());
        imageBox.add(label_image);
        imageBox.add(Box.createHorizontalGlue());
        Box infoBox = Box.createHorizontalBox();
        infoBox.add(Box.createHorizontalGlue());
        infoBox.add(label_info);
        infoBox.add(Box.createHorizontalGlue());

        contentBox.add(imageBox);
        contentBox.add(infoBox);
        getContentPane().add(contentBox, BorderLayout.NORTH);
    }
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
}
