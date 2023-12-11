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

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private double scale;
    private BasicStroke graphicsStroke;
    private BasicStroke gridStroke;
    private static DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    private boolean isRotated = false;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;

    private Font axisFont;
    private Font labelsFont;
    public GraphicsDisplay() {
        setBackground(Color.WHITE);
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[]{32.0f, 8.0f, 8.0f, 8.0f, 16.0f, 8.0f, 8.0f, 8.0f, 32.0f}, 0.0f);
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        gridStroke = new BasicStroke(1.0f, 0, 0, 10.0f, new float[]{4.0f, 4.0f}, 0.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        labelsFont = new Font("Serif", 0, 10);
        axisFont = new Font("Serif", Font.BOLD, 36);
        formatter.setMaximumFractionDigits(3);
    }
    public void setRotation(boolean ch) {
        isRotated = ch;
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
        if (graphicsData==null || graphicsData.length==0)
            return;
        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length-1][0];
        minY = graphicsData[0][1];
        maxY = minY;
        for (int i = 1; i<graphicsData.length; i++) {
            if (graphicsData[i][1]<minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1]>maxY) {
                 maxY = graphicsData[i][1];
            }
        }


        double scaleX = getSize().getWidth() / (maxX - minX);
        double scaleY = getSize().getHeight() / (maxY - minY);
        scale = Math.min(scaleX, scaleY);

        if (scale==scaleX) {
        double yIncrement = (getSize().getHeight()/scale - (maxY - minY))/2;
        maxY += yIncrement;
        minY -= yIncrement;
        }
        if (scale==scaleY) {
        double xIncrement = (getSize().getWidth()/scale - (maxX - minX))/2;
        maxX += xIncrement;
        minX -= xIncrement;
        }
        Graphics2D canvas = (Graphics2D) g;
        AffineTransform initialTransform = canvas.getTransform();
        if (isRotated) {
            AffineTransform rotateTransform = new AffineTransform();
            rotateTransform.rotate(-Math.PI / 2, getSize().getWidth() / 2, getSize().getHeight() / 2);
            canvas.setTransform(rotateTransform);
        }


        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        if (showAxis)
            paintAxis(canvas);
        paintGraphics(canvas,false);
        paintScale(canvas);
        paintLabels(canvas);
        if (showMarkers) paintMarkers(canvas);
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }
    protected void paintGraphics(Graphics2D canvas, boolean ch) {
        GeneralPath graphics = new GeneralPath();
        if (!ch) {
            canvas.setColor(Color.RED);
            canvas.setStroke(graphicsStroke);
            for (int i=0; i<graphicsData.length; i++) {
                Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
                if (i>0) {
                    graphics.lineTo(point.getX(), point.getY());
                } else {
                    graphics.moveTo(point.getX(), point.getY()); }
            }
        }
        canvas.draw(graphics);
    }


    protected void paintMarkers(Graphics2D canvas) {
        canvas.setStroke(markerStroke);
        canvas.setColor(Color.RED);
        for (Double[] point: graphicsData) {
            double y = point[1];
            Point2D.Double center = xyToPoint(point[0], point[1]);
            int integerPart = (int) y;
            int numb = 0;
            boolean is_avaliable = true;
            while (integerPart != 0) {
                numb++;
                if ((integerPart % 10) % 2 != 0){
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
            canvas.drawLine((int)center.getX() - 5, (int)center.getY(), (int)center.getX() + 5, (int)center.getY());
            canvas.drawLine((int)center.getX(), (int)center.getY() - 5, (int)center.getX(), (int)center.getY() + 5);
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
        if (minX<=0.0 && maxX>=0.0) {
        canvas.draw(new Line2D.Double(xyToPoint(0, maxY),
        xyToPoint(0, minY)));
        GeneralPath arrow = new GeneralPath();
        Point2D.Double lineEnd = xyToPoint(0, maxY); arrow.moveTo(lineEnd.getX(), lineEnd.getY());
        arrow.lineTo(arrow.getCurrentPoint().getX()+5, arrow.getCurrentPoint().getY()+20);
        arrow.lineTo(arrow.getCurrentPoint().getX()-10,
        arrow.getCurrentPoint().getY());
        arrow.closePath();
        canvas.draw(arrow);
        canvas.fill(arrow);
        Rectangle2D bounds = axisFont.getStringBounds("y", context);
        Point2D.Double labelPos = xyToPoint(0, maxY);
        canvas.drawString("y", (float)labelPos.getX() + 10, (float)(labelPos.getY() - bounds.getY()));
        }

        if (minY<=0.0 && maxY>=0.0) {
        canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));
        GeneralPath arrow = new GeneralPath();
        Point2D.Double lineEnd = xyToPoint(maxX, 0); arrow.moveTo(lineEnd.getX(), lineEnd.getY());
        arrow.lineTo(arrow.getCurrentPoint().getX()-20, arrow.getCurrentPoint().getY()-5);
        arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY()+10);
        arrow.closePath();
        canvas.draw(arrow);
        canvas.fill(arrow);
        Rectangle2D bounds = axisFont.getStringBounds("x", context);
        Point2D.Double labelPos = xyToPoint(maxX, 0);
        canvas.drawString("x", (float)(labelPos.getX() - bounds.getWidth() - 10), (float)(labelPos.getY() + bounds.getY()));
        }
    }
    protected void paintScale(Graphics2D g) {
        g.setStroke(this.gridStroke);
        int miX = (int)minX;
        int maX = (int)maxX;
        int miY = (int)minY;
        int maY = (int)maxY;
        g.setColor(Color.BLACK);
        double pos = miX;

        double step;
        for(step = (maX - miX) / 10.0; pos < maX; pos += step) {
            g.draw(new Line2D.Double(xyToPoint(pos, miY),xyToPoint (pos, maY)));
        }
        g.draw(new Line2D.Double(xyToPoint(maX, miY),xyToPoint( maX, maY)));
        pos = miY;
        for(step = (maY - miY) / 10.0; pos < maY; pos += step) {
            g.draw(new Line2D.Double(xyToPoint(miX, pos),xyToPoint (maX, pos)));
        }
        g.draw(new Line2D.Double(xyToPoint(miX, maY),xyToPoint( maX, maY)));
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
        double pos = (int)minX;
        double step;
        Point2D.Double point;
        String label;
        Rectangle2D bounds;
        System.out.println("minX = " + minX);
        System.out.println("maxX = " + maxX);
        System.out.println("minY = " + minY);
        System.out.println("maxY = " + maxY);
        for(step = ((int)maxX - (int)minX) / 10.0; pos <= (int)maxX; pos += step) {
            point = xyToPoint(pos, labelYPos);
            label = formatter.format(pos);
            bounds = this.labelsFont.getStringBounds(label, context);
            canvas.drawString(label, (float)(point.getX() + 5.0), (float)(point.getY() - bounds.getHeight()));
        }

        pos = (int)minY;
        for(step = ((int)maxY - (int)minY) / 10.0; pos <= (int)maxY; pos += step) {
            point = this.xyToPoint(labelXPos, pos);
            label = formatter.format(pos);
            bounds = this.labelsFont.getStringBounds(label, context);
            canvas.drawString(label, (float)(point.getX() + 5.0), (float)(point.getY() - bounds.getHeight()));
        }

    }
protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - minX;
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX*scale, deltaY*scale);
        }
protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX,
        double deltaY) {
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY); return dest;
        }
}
