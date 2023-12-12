package bsu.rfe.java.group_5.lab_4.Chystsiakou.C_2;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class MainFrame extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private JFileChooser fileChooser = null;
    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private JCheckBoxMenuItem showGridMenuItem;
    private JCheckBoxMenuItem showRotationMenuItem;
    private GraphicsDisplay display = new GraphicsDisplay();
    private boolean fileLoaded = false;

    public MainFrame() {
// Вызов конструктора предка Frame
        super("Лабораторная работа № 4");
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
        Double[][] graphicsData = new Double[in.available()/(Double.SIZE/8)/2][];
        int i = 0;

        while (in.available()>0) {
            Double x = in.readDouble();
            Double y = in.readDouble();
            graphicsData[i++] = new Double[] {x, y};
        }

        if (graphicsData!=null && graphicsData.length>0) {
            fileLoaded = true;
            display.showGraphics(graphicsData);
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
    private class GraphicsMenuListener implements MenuListener {
        public void menuSelected(MenuEvent e) {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
            showGridMenuItem.setEnabled(fileLoaded);
        }

        public void menuDeselected(MenuEvent e) {
        }
        public void menuCanceled(MenuEvent e) { }
    }
}
