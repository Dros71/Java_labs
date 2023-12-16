package bsu.rfe.java.group_5.lab_5.Chystsiakou.C_2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class GraphicsDisplay extends JPanel {
    private double[][] graphicsData;

    private boolean showAxis = true;
    private boolean showMarkers = true;
    private boolean isRotated = false;
    private boolean showGrid = true;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double[][] viewport = new double[2][2];
    private double scaleX;
    private double scaleY;
    private final double gridScale = 20.0;
    private BasicStroke graphicsStroke;
    private BasicStroke gridStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    private BasicStroke selectionStroke;
    private static DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
    private Font axisFont;
    private Font labelsFont;
    private Font selectedFont;
    private boolean scaleMode = false;
    private ArrayList<double[][]> undoHistory = new ArrayList();
    private int selectedMarker = -1;
    private boolean changeMode = false;
    private double[] originalPoint = new double[2];
    private Rectangle2D.Double selectionRect = new Rectangle2D.Double();
    private boolean flag = false;

    public GraphicsDisplay() {

        setBackground(Color.WHITE);
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[]{32.0f, 8.0f, 8.0f, 8.0f, 16.0f, 8.0f, 8.0f, 8.0f, 32.0f}, 0.0f);
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        gridStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{4.0f, 4.0f}, 0.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        selectionStroke = new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0F, new float[]{10.0F, 10.0F}, 0.0F);
        labelsFont = new Font("Serif", 0, 10);
        selectedFont = new Font("Serif", 0, 16);
        axisFont = new Font("Serif", Font.BOLD, 36);
        addMouseMotionListener(new MouseMotionHandler());
        addMouseListener(new MouseHandler());
        formatter.setMaximumFractionDigits(3);
    }

    public void setRotation(boolean ch) {
        isRotated = ch;
        repaint();
    }

    public void setShowGrid(boolean ch) {
        this.showGrid = ch;
        repaint();
    }


    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }

    public double[][] getGraphicsData() {
        return Arrays.copyOf(graphicsData, graphicsData.length);
    }

    public void paintComponent(Graphics g) {
        //
        super.paintComponent(g);
        if (graphicsData == null || graphicsData.length == 0)
            return;
        //
        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        if (!isRotated) {
            scaleX = getSize().getWidth() / (this.viewport[1][0] - this.viewport[0][0]);
            scaleY = getSize().getHeight() / (this.viewport[0][1] - this.viewport[1][1]);
        }
        if (isRotated) {
            canvas.rotate(-Math.PI / 2);
            this.scaleX = this.getSize().getHeight() / (this.viewport[1][0] - this.viewport[0][0]);
            this.scaleY = this.getSize().getWidth() / (this.viewport[0][1] - this.viewport[1][1]);
            zoomToNoUpdate(
                    this.viewport[1][0], viewport[0][1],
                    this.viewport[0][0], viewport[1][1]
            );
        }
        if (showAxis){
            paintAxis(canvas);
            paintLabels(canvas);
        }


        if (showGrid) {
            paintGrid(canvas);
        }
        paintGraphics(canvas);
        if (showMarkers)
            paintMarkers(canvas);

        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
        paintSelection(canvas);
    }

    private void paintSelection(Graphics2D canvas) {
        if (scaleMode) {
            canvas.setStroke(this.selectionStroke);
            canvas.setFont(this.selectedFont);
            canvas.setColor(Color.RED);
            canvas.draw(this.selectionRect);
        }
    }

    protected void paintGraphics(Graphics2D canvas) {
        GeneralPath graphics = new GeneralPath();
        canvas.setColor(Color.RED);
        canvas.setStroke(graphicsStroke);
        for (int i = 0; i < graphicsData.length; i++) {
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            if (i > 0) {
                graphics.lineTo(point.getX(), point.getY());
            } else {
                graphics.moveTo(point.getX(), point.getY());
            }
        }
        canvas.draw(graphics);
    }

    public void displayGraphics(double[][] graphicsData) {
        this.graphicsData = graphicsData;
        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;
        for (int i = 1; i < graphicsData.length; i++) {
            if (graphicsData[i][1] < minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1] > maxY) {
                maxY = graphicsData[i][1];
            }
        }
        zoomTo(this.minX, this.maxY, this.maxX, this.minY);
    }

    protected void paintMarkers(Graphics2D canvas) {
        canvas.setStroke(markerStroke);
        canvas.setColor(Color.RED);
        int currentIt = 0;
        for (double[] point : graphicsData) {
            if (currentIt++ == selectedMarker) {
                canvas.setColor(Color.BLACK);

                Point2D.Double currentPoint = xyToPoint(point[0], point[1]);
                DecimalFormat format = new DecimalFormat("#.##");
                canvas.drawString(
                        format.format(point[0]) +
                                ", " +
                                format.format(point[1]),
                        (int) currentPoint.getX(),
                        (int) currentPoint.getY()
                );
            }
            double y = point[1];
            Point2D.Double center = xyToPoint(point[0], point[1]);
            int integerPart = (int) y;
            boolean is_avaliable = true;
            while (integerPart != 0) {
                if ((integerPart % 10) % 2 != 0) {
                    is_avaliable = false;
                    break;
                }
                integerPart /= 10;
            }
            if (is_avaliable) {
                canvas.setColor(Color.BLUE);
            } else {
                canvas.setColor(Color.BLACK);
            }

            Line2D.Double marker = new Line2D.Double();
            canvas.drawLine((int) center.getX() - 5, (int) center.getY(), (int) center.getX() + 5, (int) center.getY());
            canvas.drawLine((int) center.getX(), (int) center.getY() - 5, (int) center.getX(), (int) center.getY() + 5);
            canvas.drawLine((int) center.getX() - 2, (int) center.getY() - 5, (int) center.getX() + 2, (int) center.getY() - 5);
            canvas.drawLine((int) center.getX() - 2, (int) center.getY() + 5, (int) center.getX() + 2, (int) center.getY() + 5);
            canvas.drawLine((int) center.getX() - 5, (int) center.getY() - 2, (int) center.getX() - 5, (int) center.getY() + 2);
            canvas.drawLine((int) center.getX() + 5, (int) center.getY() - 2, (int) center.getX() + 5, (int) center.getY() + 2);
            canvas.draw(marker);
        }
    }

    protected void paintAxis(Graphics2D canvas) {
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);
        FontRenderContext context = canvas.getFontRenderContext();


        if ((minX <= 0.0 && maxX >= 0.0) || (isRotated && maxX <= 0.0 && minX >= 0.0)) {
            canvas.draw(new Line2D.Double(xyToPoint(0, maxY),
                    xyToPoint(0, minY)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10,
                    arrow.getCurrentPoint().getY());
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);
            canvas.drawString("y", (float) labelPos.getX() + 10, (float) (labelPos.getY() - bounds.getY()));
        }

        if ((minY <= 0.0 && maxY >= 0.0) || (isRotated && minY >= 0.0 && maxY <= 0.0)) {
            canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = new Point2D.Double();
            if (isRotated) {
                lineEnd = xyToPoint(viewport[0][0], 0);
            } else {
                lineEnd = xyToPoint(maxX, 0);
            }
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 20, arrow.getCurrentPoint().getY() - 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 10);
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = new Point2D.Double();
            if (isRotated) {
                labelPos = xyToPoint(viewport[0][0], 0);
            } else {
                labelPos = xyToPoint(maxX, 0);
            }
            canvas.drawString("x", (float) (labelPos.getX() - bounds.getWidth() - 10), (float) (labelPos.getY() + bounds.getY()));
        }
    }

    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - this.viewport[0][0];
        double deltaY = this.viewport[0][1] - y;
        return new Point2D.Double(deltaX * this.scaleX, deltaY * this.scaleY);
    }

    protected double[] translatePointToXY(int x, int y) {
        return new double[]{this.viewport[0][0] + (double) x / this.scaleX, this.viewport[0][1] - (double) y / this.scaleY};
    }

    private void zoomTo(double x1, double y1, double x2, double y2) {
        zoomToNoUpdate(x1, y1, x2, y2);
        repaint();
    }

    private void zoomToNoUpdate(double x1, double y1, double x2, double y2) {
        viewport[0][0] = x1;
        viewport[0][1] = y1;
        viewport[1][0] = x2;
        viewport[1][1] = y2;
    }

    private int findSelectedPoint(double x, double y) {
        if (graphicsData == null) {
            return -1;
        }
        for (int i = 0; i < graphicsData.length; i++) {
            Point2D.Double point = xyToPoint(
                    graphicsData[i][0],
                    graphicsData[i][1]
            );
            double distance = (point.getX() - x) *
                    (point.getX() - x) +
                    (point.getY() - y) *
                            (point.getY() - y);
            if (distance <= 100) {
                return i;
            }
        }
        return -1;
    }

    protected void paintGrid(Graphics2D g) {
        g.setStroke(this.gridStroke);
        int miX = (int) minX;
        int maX = (int) maxX;
        int miY = (int) minY;
        int maY = (int) maxY;
        g.setColor(Color.BLACK);

        if (isRotated) {
            int tmp = (int) viewport[0][0];
            miX = (int) viewport[1][0];
            maX = tmp;
        }
        double pos = miX;
        double step;
        for (step = (maX - miX) / gridScale; pos < maX; pos += step) {
            g.draw(new Line2D.Double(xyToPoint(pos, miY), xyToPoint(pos, maY)));
        }
        g.draw(new Line2D.Double(xyToPoint(maX, miY), xyToPoint(maX, maY)));


        pos = miY;
        for (step = (maY - miY) / gridScale; pos < maY; pos += step) {
            g.draw(new Line2D.Double(xyToPoint(miX, pos), xyToPoint(maX, pos)));
        }
        g.draw(new Line2D.Double(xyToPoint(miX, maY), xyToPoint(maX, maY)));
    }

    private void paintLabels(Graphics2D canvas) {
        canvas.setColor(Color.BLACK);
        canvas.setFont(this.labelsFont);
        FontRenderContext context = canvas.getFontRenderContext();
        double labelYPos;
        if (minY < 0.0 && maxY > 0.0) {
            labelYPos = 0.0;
        } else {
            labelYPos = minY;
        }
        double labelXPos;
        if (minX < 0.0 && maxX > 0.0) {
            labelXPos = 0.0;
        } else {
            labelXPos = minX;
        }

        int miX = (int) minX;
        int maX = (int) maxX;
        int miY = (int) minY;
        int maY = (int) maxY;

        Point2D.Double point;
        String label;
        Rectangle2D bounds;
        if (isRotated) {
            int tmp = (int) viewport[0][0];
            miX = (int) viewport[1][0];
            maX = tmp;
        }
        double pos = miX;
        double step;
        for (step = (maX - miX) / gridScale; pos <= maX; pos += step) {
            if (isRotated)
                point = this.xyToPoint(pos, labelYPos);
            else
                point = this.xyToPoint(pos, labelXPos);

            label = formatter.format(pos);
            bounds = this.labelsFont.getStringBounds(label, context);
            canvas.drawString(label, (float) (point.getX() - 10), (float) (point.getY() - bounds.getHeight() + 10));
        }

        pos = miY;
        for (step = Math.abs(maY - miY) / gridScale; pos <= maY; pos += step) {
            if (isRotated)
                point = this.xyToPoint(labelYPos, pos);
            else
                point = this.xyToPoint(labelXPos, pos);
            label = formatter.format(pos);
            bounds = this.labelsFont.getStringBounds(label, context);
            canvas.drawString(label, (float) (point.getX() + 2.5), (float) (point.getY() - bounds.getHeight() + 10));
        }

    }

    public class MouseHandler extends MouseAdapter {
        public MouseHandler() {
        }

        public void mouseClicked(MouseEvent ev) {
            if (ev.getButton() == 3) {
                if (!undoHistory.isEmpty()) {
                    viewport = undoHistory.get(undoHistory.size() - 1);
                    undoHistory.remove(undoHistory.size() - 1);
                } else {
                    zoomTo(minX, maxY, maxX, minY);
                }
                repaint();
            }
        }

        public void mousePressed(MouseEvent ev) {
            if (ev.getButton() == 1) {
                selectedMarker = findSelectedPoint(ev.getX(), ev.getY());
                originalPoint = translatePointToXY(ev.getX(), ev.getY());
                if (selectedMarker >= 0) {
                    changeMode = true;
                    setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                } else {
                    scaleMode = true;
                    setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                    selectionRect.setFrame(ev.getX(), ev.getY(), 1.0, 1.0);
                }

            }
        }

        public void mouseReleased(MouseEvent ev) {
            if (ev.getButton() == 1) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if (changeMode) {
                    changeMode = false;
                } else {
                    scaleMode = false;
                    double[] finalPoint = translatePointToXY(ev.getX(), ev.getY());
                    undoHistory.add(viewport);
                    viewport = new double[2][2];
                    zoomTo(originalPoint[0], originalPoint[1], finalPoint[0], finalPoint[1]);
                    repaint();
                }
            }
        }
    }

    public class MouseMotionHandler implements MouseMotionListener {
        public MouseMotionHandler() {
        }

        public void mouseMoved(MouseEvent ev) {
            selectedMarker = findSelectedPoint(ev.getX(), ev.getY());
            if (selectedMarker >= 0) {
                setCursor(Cursor.getPredefinedCursor(8));
            } else {
                setCursor(Cursor.getPredefinedCursor(0));
            }
            if(!isRotated)
                repaint();
        }

        public void mouseDragged(MouseEvent ev) {
            if (changeMode) {

                double[] currentPoint = translatePointToXY(ev.getX(), ev.getY());
                double newY = currentPoint[1];

                if (newY > viewport[0][1]) {
                    newY = viewport[0][1];
                }

                if (newY < viewport[1][1]) {
                    newY = viewport[1][1];
                }

                graphicsData[selectedMarker][1] = newY;
                repaint();
            } else {
                double width = (double) ev.getX() - selectionRect.getX();
                if (width < 5.0) {
                    width = 5.0;
                }

                double height = (double) ev.getY() - selectionRect.getY();
                if (height < 5.0) {
                    height = 5.0;
                }
                selectionRect.setFrame(selectionRect.getX(), selectionRect.getY(), width, height);
                repaint();
            }

        }
    }
}
