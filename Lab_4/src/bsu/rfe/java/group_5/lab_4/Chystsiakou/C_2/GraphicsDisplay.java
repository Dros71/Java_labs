package bsu.rfe.java.group_5.lab_4.Chystsiakou.C_2;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class GraphicsDisplay extends JPanel {
    private Double[][] graphicsData;
    private boolean showAxis = true;
    private boolean showMarkers = true;
    private boolean isRotated = false;
    private boolean showGrid = true;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double scaleX;
    private double scaleY;
    private final double gridScale = 20.0;
    private double scale;
    private BasicStroke graphicsStroke;
    private BasicStroke gridStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    private static DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
    private Font axisFont;
    private Font labelsFont;

    public GraphicsDisplay() {
        setBackground(Color.WHITE);
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[]{32.0f, 8.0f, 8.0f, 8.0f, 16.0f, 8.0f, 8.0f, 8.0f, 32.0f}, 0.0f);
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        gridStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{4.0f, 4.0f}, 0.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        labelsFont = new Font("Serif", 0, 10);
        axisFont = new Font("Serif", Font.BOLD, 36);
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

    public void showGraphics(Double[][] graphicsData) {
        this.graphicsData = graphicsData;
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

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphicsData == null || graphicsData.length == 0)
            return;
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

        double scaleX1 = getSize().getWidth() / (maxX - minX);
        double scaleY1 = getSize().getHeight() / (maxY - minY);
        scale = Math.min(scaleX1, scaleY1);

        //if (scale==scaleX1) {
        //    double yIncrement = (getSize().getHeight() / scale - (maxY - minY)) / 2;
        //    maxY += yIncrement;
        //    minY -= yIncrement;
        //}
        //if (scale==scaleY1) {
        //double xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 2;
         //   maxX += xIncrement;
         //   minX -= xIncrement;
        //}
        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        if (!isRotated) {
            scaleX = getSize().getWidth() / (maxX - minX);
            scaleY = getSize().getHeight() / (maxY - minY);
        }
        if (isRotated) {
            canvas.rotate(-Math.PI / 2);
            this.scaleX = this.getSize().getHeight() / (maxX - minX);
            this.scaleY = this.getSize().getWidth() / (maxY - minY);
            zoomToNoUpdate(
                    maxX, maxY,
                    minX, minY
            );
        }

        paintGraphics(canvas);
        if (showAxis)
            paintAxis(canvas);

        if (showGrid) {
            paintGrid(canvas);
            paintLabels(canvas);
        }
        if (showMarkers)
            paintMarkers(canvas);

        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);

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


    protected void paintMarkers(Graphics2D canvas) {
        canvas.setStroke(markerStroke);
        canvas.setColor(Color.RED);
        for (Double[] point : graphicsData) {
            double y = point[1];
            Point2D.Double center = xyToPoint(point[0], point[1]);
            int integerPart = (int) y;
            int numb = 0;
            boolean is_avaliable = true;
            while (integerPart != 0) {
                numb++;
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

        if (minY <= 0.0 && maxY >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = new Point2D.Double();
            if (isRotated) {
                lineEnd = xyToPoint(minX, 0);
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
                labelPos = xyToPoint(minX, 0);
            } else {
                labelPos = xyToPoint(maxX, 0);
            }
            canvas.drawString("x", (float) (labelPos.getX() - bounds.getWidth() - 10), (float) (labelPos.getY() + bounds.getY()));
        }
    }

    protected void paintGrid(Graphics2D g) {
        g.setStroke(this.gridStroke);
        int miX = (int) minX;
        int maX = (int) maxX;
        int miY = (int) minY;
        int maY = (int) maxY;
        g.setColor(Color.BLACK);

        if (isRotated) {
            int tmp = miX;
            miX = maX;
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
            int tmp = maX;
            maX = miX;
            miX = tmp;
        }
        double pos = miX;
        double step;
        for (step = (maX - miX) / gridScale; pos <= maX; pos += step) {
            if(isRotated)
                point = this.xyToPoint(pos,labelYPos);
            else
                point = this.xyToPoint(pos,labelXPos);

            label = formatter.format(pos);
            bounds = this.labelsFont.getStringBounds(label, context);
            canvas.drawString(label, (float) (point.getX() - 10), (float) (point.getY() - bounds.getHeight() + 10));
        }

        pos = miY;
        for (step = Math.abs(maY - miY) / gridScale; pos <= maY; pos += step) {
            if(isRotated)
                point = this.xyToPoint(labelYPos, pos);
             else
                point = this.xyToPoint(labelXPos, pos);
            label = formatter.format(pos);
            bounds = this.labelsFont.getStringBounds(label, context);
            canvas.drawString(label, (float) (point.getX() + 2.5), (float) (point.getY() - bounds.getHeight() + 10));
        }

    }

    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - minX;
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX * scaleX, deltaY * scaleY);
    }

    protected double[] translatePointToXY(int x, int y) {
        return new double[]{maxX + (double) x / this.scaleX, maxY - (double) y / this.scaleY};
    }

    private void zoomTo(double x1, double y1, double x2, double y2) {
        zoomToNoUpdate(x1, y1, x2, y2);
        repaint();
    }

    private void zoomToNoUpdate(double x1, double y1, double x2, double y2) {
        minX = x1;
        maxY = y1;
        maxX = x2;
        minY = y2;
    }

    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX,
                                        double deltaY) {
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }
}
