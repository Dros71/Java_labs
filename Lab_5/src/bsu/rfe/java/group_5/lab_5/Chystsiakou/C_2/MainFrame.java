package bsu.rfe.java.group_5.lab_5.Chystsiakou.C_2;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.io.*;

public class MainFrame extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private JFileChooser fileChooser = null;
    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private JCheckBoxMenuItem showGridMenuItem;
    private JCheckBoxMenuItem showRotationMenuItem;
    private final JMenuItem saveToGraphicsItem;
    private GraphicsDisplay display = new GraphicsDisplay();



    private boolean fileLoaded = false;

    public MainFrame() {
// Вызов конструктора предка Frame
        super("Лабораторная работа № 5");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);
        setExtendedState(MAXIMIZED_BOTH);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);

        Action openGraphicsAction = new AbstractAction("Открыть файл с графиком") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser==null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("src/"));
                }
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    openGraphics(fileChooser.getSelectedFile());
            }
        };
        fileMenu.add(openGraphicsAction);
        Action saveGraphicsAction = new AbstractAction("Сохранить график") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) ==
                        JFileChooser.APPROVE_OPTION) {
                    saveToGraphicsFile(fileChooser.getSelectedFile());

                }
            }
        };
        saveToGraphicsItem = new JMenuItem(saveGraphicsAction);
        fileMenu.add(saveToGraphicsItem);
        saveToGraphicsItem.setEnabled(false);


        JMenu graphicsMenu = new JMenu("График");
        menuBar.add(graphicsMenu);

        Action showAxisAction = new AbstractAction("Показывать оси координат") {
            public void actionPerformed(ActionEvent event) {
                display.setShowAxis(showAxisMenuItem.isSelected()); }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true);
        Action showGrid = new AbstractAction("Показывать координатную сетку") {
            public void actionPerformed(ActionEvent event) {
                display.setShowGrid(showGridMenuItem.isSelected()); }
        };
        showGridMenuItem = new JCheckBoxMenuItem(showGrid);
        graphicsMenu.add(showGridMenuItem);
        showGridMenuItem.setSelected(true);

        Action showMarkersAction = new AbstractAction("Показывать маркеры точек") {
            public void actionPerformed(ActionEvent event) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());}
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction); graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setSelected(true);
        graphicsMenu.addMenuListener(new GraphicsMenuListener());
        Action showRotationAction = new AbstractAction("Повернуть график на 90 градусов") {
            public void actionPerformed(ActionEvent event) {
                display.setRotation(showRotationMenuItem.isSelected()); }
        };
        showRotationMenuItem = new JCheckBoxMenuItem(showRotationAction);
        graphicsMenu.add(showRotationMenuItem);
        showRotationMenuItem.setSelected(false);
        getContentPane().add(display, BorderLayout.CENTER);
    }
    protected void openGraphics(File selectedFile) {
        try {

            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
            double[][] graphicsData = new double[in.available()/(Double.SIZE/8)/2][];
            int i = 0;
            while (in.available()>0) {
                double x = in.readDouble();
                double y = in.readDouble();
                graphicsData[i++] = new double[] {x, y};
            }

            if (graphicsData!=null && graphicsData.length>0) {
                saveToGraphicsItem.setEnabled(true);
                fileLoaded = true;
                display.displayGraphics(graphicsData);
            }
            in.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(MainFrame.this, "Указанный файл не найден", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(MainFrame.this, "Ошибка чтения координат точек из файла", "Ошибка загрузки данных",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
    protected void saveToGraphicsFile(File selectedFile) {
        try {
            DataOutputStream out = new DataOutputStream(new
                    FileOutputStream(selectedFile));
            for (int i = 0; i < display.getGraphicsData().length; i++) {
                out.writeDouble(display.getGraphicsData()[i][0]);
                out.writeDouble(display.getGraphicsData()[i][1]);
            }
            out.close();
        } catch (Exception ignored) {
        }
    }
    private class GraphicsMenuListener implements MenuListener {
        public void menuSelected(MenuEvent e) {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
            showGridMenuItem.setEnabled(fileLoaded);
            showRotationMenuItem.setEnabled(fileLoaded);
        }
        public void menuDeselected(MenuEvent e) {
        }
        public void menuCanceled(MenuEvent e) { }
    }
}
