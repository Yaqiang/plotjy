/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteothink.chart;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import javax.swing.event.EventListenerList;
import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.emf.EMFGraphics2D;
import org.freehep.graphicsio.ps.PSGraphics2D;
import org.meteothink.chart.plot.Plot;
import org.meteothink.chart.plot.AbstractPlot2D;
import org.meteothink.chart.plot.Plot3D;
import org.meteothink.chart.plot.PlotType;
import org.meteothink.chart.plot3d.Projector;
import org.meteothink.common.Extent;
import org.meteothink.common.GenericFileFilter;
import org.meteothink.image.ImageUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author yaqiang
 */
public class ChartPanel extends JPanel {

    // <editor-fold desc="Variables">
    private final EventListenerList listeners = new EventListenerList();
    private BufferedImage mapBitmap = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    private BufferedImage tempImage = null;
    private Chart chart;
    private Plot currentPlot;
    private Dimension chartSize;
    private Point mouseDownPoint = new Point(0, 0);
    private Point mouseLastPos = new Point(0, 0);
    private boolean dragMode = false;
    private JPopupMenu popupMenu;
    private MouseMode mouseMode;
    private List<int[]> selectedPoints;
    private final double INCH_2_CM = 2.54;
    private int xShift = 0;
    private int yShift = 0;
    private double paintScale = 1.0;
    private Date lastMouseWheelTime;
    private Timer mouseWheelDetctionTimer;
    // </editor-fold>

    // <editor-fold desc="Constructor">
    /**
     * Constructor
     */
    public ChartPanel() {
        super();
        //this.setBackground(Color.white);
        this.setBackground(Color.lightGray);
        this.setSize(200, 200);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                onComponentResized(e);
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onMouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                onMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                onMouseReleased(e);
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                onMouseMoved(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                onMouseDragged(e);
            }
        });
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                onMouseWheelMoved(e);
            }
        });

        this.mouseWheelDetctionTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date now = new Date();
                if (now.getTime() - lastMouseWheelTime.getTime() > 200) {
                    xShift = 0;
                    yShift = 0;
                    paintScale = 1.0;
                    paintGraphics();
                    mouseWheelDetctionTimer.stop();
                }
            }
        });

        popupMenu = new JPopupMenu();
        JMenuItem undoZoom = new JMenuItem("Undo zoom");
        undoZoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onUndoZoomClick();
            }
        });
        popupMenu.add(undoZoom);
        popupMenu.addSeparator();

        JMenuItem saveFigure = new JMenuItem("Save figure");
        saveFigure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSaveFigureClick(e);
            }
        });
        popupMenu.add(saveFigure);

        this.chart = null;
        this.mouseMode = MouseMode.DEFAULT;
        //this.setMouseMode(mouseMode.ZOOM_IN);
    }

    /**
     * Constructor
     *
     * @param chart Chart
     */
    public ChartPanel(Chart chart) {
        this();
        this.chart = chart;
        if (this.chart != null) {
            this.chart.setParent(this);
        }
    }

    /**
     * Constructor
     *
     * @param chart Chart
     * @param width Chart width
     * @param height Chart height
     */
    public ChartPanel(Chart chart, int width, int height) {
        this(chart);
        this.chartSize = new Dimension(width, height);
        this.setPreferredSize(chartSize);
    }

    // </editor-fold>
    // <editor-fold desc="Get set methods">
    /**
     * Get chart
     *
     * @return Chart
     */
    public Chart getChart() {
        return chart;
    }

    /**
     * Set chart
     *
     * @param value
     */
    public void setChart(Chart value) {
        chart = value;
        if (this.chart != null) {
            chart.setParent(this);
        }
    }

    /**
     * Get popup menu
     *
     * @return Popup menu
     */
    public JPopupMenu getPopupMenu() {
        return this.popupMenu;
    }

    /**
     * Get mouse mode
     *
     * @return Mouse mode
     */
    public MouseMode getMouseMode() {
        return this.mouseMode;
    }

    /**
     * Set mouse mode
     *
     * @param value Mouse mode
     */
    public void setMouseMode(MouseMode value) {
        this.mouseMode = value;
        Image image;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor customCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        switch (this.mouseMode) {
            case SELECT:
                customCursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
                break;
            case ZOOM_IN:
                try {
                    //image = toolkit.getImage(this.getClass().getResource("/images/zoom_in.png"));
                    image = ImageIO.read(this.getClass().getResourceAsStream("/images/zoom_in.png"));
                    customCursor = toolkit.createCustomCursor(image, new Point(8, 8), "Zoom In");
                } catch (IOException ex) {
                    Logger.getLogger(ChartPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case ZOOM_OUT:
                image = toolkit.getImage(this.getClass().getResource("/images/zoom_out.png"));
                customCursor = toolkit.createCustomCursor(image, new Point(8, 8), "Zoom Out");
                break;
            case PAN:
                image = toolkit.getImage(this.getClass().getResource("/images/pan.png"));
                customCursor = toolkit.createCustomCursor(image, new Point(8, 8), "Pan");
                break;
            case IDENTIFER:
                image = toolkit.getImage(this.getClass().getResource("/images/identifer.png"));
                customCursor = toolkit.createCustomCursor(image, new Point(8, 8), "Identifer");
                break;
            case ROTATE:
                image = toolkit.getImage(this.getClass().getResource("/images/rotate.png"));
                customCursor = toolkit.createCustomCursor(image, new Point(8, 8), "Rotate");
                break;
        }
        this.setCursor(customCursor);
    }

    /**
     * Get selected chart points
     *
     * @return Selected chart points
     */
    public List<int[]> getSelectedPoints() {
        return this.selectedPoints;
    }
    // </editor-fold>

    // <editor-fold desc="Events">
    public void addPointSelectedListener(IPointSelectedListener listener) {
        this.listeners.add(IPointSelectedListener.class, listener);
    }

    public void removePointSelectedListener(IPointSelectedListener listener) {
        this.listeners.remove(IPointSelectedListener.class, listener);
    }

    public void firePointSelectedEvent() {
        firePointSelectedEvent(new PointSelectedEvent(this));
    }

    private void firePointSelectedEvent(PointSelectedEvent event) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == IPointSelectedListener.class) {
                ((IPointSelectedListener) listeners[i + 1]).pointSelectedEvent(event);
            }
        }
    }
    // </editor-fold>

    // <editor-fold desc="Method">
    /**
     * Get figure width
     *
     * @return Figure width
     */
    public int getFigureWidth() {
        int width;
        if (this.chartSize != null) {
            width = this.chartSize.width;
        } else {
            width = this.getWidth();
        }

        return width;
    }

    /**
     * Get Figure height
     *
     * @return Figure height
     */
    public int getFigureHeight() {
        int height;
        if (this.chartSize != null) {
            height = this.chartSize.height;
        } else {
            height = this.getHeight();
        }

        return height;
    }

    /**
     * Select a plot by point
     *
     * @param x X
     * @param y Y
     * @return Selected plot
     */
    public Plot selPlot(int x, int y) {
        if (this.chart == null) {
            return null;
        }

        int n = this.chart.getPlots().size();
        for (int i = n - 1; i >= 0; i--) {
            Plot plot = this.chart.getPlots().get(i);
            Rectangle2D rect = plot.getGraphArea();
            if (rect.contains(x, y)) {
                return plot;
            }
        }
        return null;
    }

    /**
     * Paint component
     *
     * @param g Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.getWidth() < 5 || this.getHeight() < 5) {
            return;
        }

        //this.setBackground(Color.white);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform mx = new AffineTransform();
        AffineTransformOp aop = new AffineTransformOp(mx, AffineTransformOp.TYPE_BICUBIC);
        g2.drawImage(mapBitmap, aop, 0, 0);

        //Draw dynamic graphics
        if (this.dragMode) {
            switch (this.mouseMode) {
                case ZOOM_IN:
                case SELECT:
                    int aWidth = Math.abs(mouseLastPos.x - mouseDownPoint.x);
                    int aHeight = Math.abs(mouseLastPos.y - mouseDownPoint.y);
                    int aX = Math.min(mouseLastPos.x, mouseDownPoint.x);
                    int aY = Math.min(mouseLastPos.y, mouseDownPoint.y);
                    g2.setColor(this.getForeground());
                    float dash1[] = {2.0f};
                    g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f));
                    g2.draw(new Rectangle(aX, aY, aWidth, aHeight));
                    break;
            }
        }

        g2.dispose();
    }

    /**
     * Paint graphics
     */
    public void paintGraphics() {
        if (this.getWidth() < 5 || this.getHeight() < 5) {
            return;
        }

        int width, height;
        if (this.chartSize != null) {
            height = this.chartSize.height;
            width = this.chartSize.width;
        } else {
            width = this.getWidth();
            height = this.getHeight();
        }

        this.mapBitmap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        if (this.chart != null) {
            Graphics2D g = this.mapBitmap.createGraphics();
            Rectangle2D chartArea;
            if (this.chartSize == null) {
                chartArea = new Rectangle2D.Double(0.0, 0.0, this.mapBitmap.getWidth(), this.mapBitmap.getHeight());
            } else {
                chartArea = new Rectangle2D.Double(0.0, 0.0, this.chartSize.width, this.chartSize.height);
            }
            this.chart.draw(g, chartArea);
        }
        this.repaint();
    }

    public void paintGraphics(Graphics2D g) {
        if (this.chart != null) {
            Rectangle2D chartArea;
            if (this.chartSize == null) {
                chartArea = new Rectangle2D.Double(0.0, 0.0, this.mapBitmap.getWidth(), this.mapBitmap.getHeight());
            } else {
                chartArea = new Rectangle2D.Double(0.0, 0.0, this.chartSize.width, this.chartSize.height);
            }
            this.chart.draw(g, chartArea);
        }
    }

    public void paintGraphics(Graphics2D g, int width, int height) {
        if (this.chart != null) {
            Rectangle2D chartArea;
            chartArea = new Rectangle2D.Double(0.0, 0.0, width, height);
            this.chart.draw(g, chartArea);
        }
    }

    void onComponentResized(ComponentEvent e) {
        if (this.getWidth() > 0 && this.getHeight() > 0) {
            if (this.chart != null) {
                this.paintGraphics();
            }
        }
    }

    void onMousePressed(MouseEvent e) {
        mouseDownPoint.x = e.getX();
        mouseDownPoint.y = e.getY();
        mouseLastPos = (Point) mouseDownPoint.clone();
        switch (this.mouseMode) {
            case PAN:
                Plot plot = selPlot(e.getX(), e.getY());
                if (plot != null) {
                    Rectangle2D mapRect = plot.getGraphArea();
                    tempImage = new BufferedImage((int) mapRect.getWidth() - 2,
                            (int) mapRect.getHeight() - 2, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D tg = tempImage.createGraphics();
                    tg.setColor(Color.white);
                    tg.fill(mapRect);
                    tg.drawImage(this.mapBitmap, -(int) mapRect.getX() - 1, -(int) mapRect.getY() - 1, this);
                    tg.dispose();
                }
                break;
        }
    }

    void onMouseMoved(MouseEvent e) {
        this.dragMode = false;
//        switch (this.mouseMode) {
//            case PAN:
//                Plot plot = selPlot(e.getX(), e.getY());
//                if (plot != null) {
//                    Rectangle2D mapRect = plot.getGraphArea();
//                    tempImage = new BufferedImage((int) mapRect.getWidth() - 2,
//                            (int) mapRect.getHeight() - 2, BufferedImage.TYPE_INT_ARGB);
//                    Graphics2D tg = tempImage.createGraphics();
//                    tg.setColor(Color.white);
//                    tg.fill(mapRect);
//                    tg.drawImage(this.mapBitmap, -(int) mapRect.getX() - 1, -(int) mapRect.getY() - 1, this);
//                    tg.dispose();
//                }
//                break;
//        }
    }

    void onMouseReleased(MouseEvent e) {
        this.dragMode = false;
        Plot plt = this.chart.findPlot(mouseDownPoint.x, mouseDownPoint.y);
        if (!(plt instanceof AbstractPlot2D)) {
            return;
        }

        AbstractPlot2D xyplot = (AbstractPlot2D) plt;
        this.currentPlot = xyplot;
        switch (this.mouseMode) {
            case ZOOM_IN:
                if (Math.abs(mouseLastPos.x - mouseDownPoint.x) > 5) {
                    Rectangle2D graphArea = xyplot.getGraphArea();
                    double[] xy1 = xyplot.screenToProj(mouseDownPoint.x - graphArea.getX(), mouseDownPoint.y - graphArea.getY(), graphArea);
                    double[] xy2 = xyplot.screenToProj(mouseLastPos.x - graphArea.getX(), mouseLastPos.y - graphArea.getY(), graphArea);
                    Extent extent = new Extent();
                    extent.minX = Math.min(xy1[0], xy2[0]);
                    extent.maxX = Math.max(xy1[0], xy2[0]);
                    extent.minY = Math.min(xy1[1], xy2[1]);
                    extent.maxY = Math.max(xy1[1], xy2[1]);
                    if (xyplot.getXAxis().isInverse()) {
                        Extent drawExtent = xyplot.getDrawExtent();
                        double minx, maxx;
                        minx = drawExtent.getWidth() - (extent.maxX - drawExtent.minX) + drawExtent.minX;
                        maxx = drawExtent.getWidth() - (extent.minX - drawExtent.minX) + drawExtent.minX;
                        extent.minX = minx;
                        extent.maxX = maxx;
                    }
                    if (xyplot.getYAxis().isInverse()) {
                        Extent drawExtent = xyplot.getDrawExtent();
                        double miny, maxy;
                        miny = drawExtent.getHeight() - (extent.maxY - drawExtent.minY) + drawExtent.minY;
                        maxy = drawExtent.getHeight() - (extent.minY - drawExtent.minY) + drawExtent.minY;
                        extent.minY = miny;
                        extent.maxY = maxy;
                    }
                    xyplot.setDrawExtent(extent);
                    this.paintGraphics();
                }
                break;
            case ZOOM_OUT:
                if (e.getButton() == MouseEvent.BUTTON1) {
                    double zoom = 1.5;
                    Extent extent = xyplot.getDrawExtent();
                    double owidth = extent.getWidth();
                    double oheight = extent.getHeight();
                    double width = owidth * zoom;
                    double height = oheight * zoom;
                    double xshift = (owidth - width) * 0.5;
                    double yshift = (oheight - height) * 0.5;
                    extent.minX += xshift;
                    extent.maxX -= xshift;
                    extent.minY += yshift;
                    extent.maxY -= yshift;
                    xyplot.setDrawExtent(extent);
                    this.paintGraphics();
                }
                break;
            case SELECT:
                if (Math.abs(mouseLastPos.x - mouseDownPoint.x) > 5) {

                }
                break;
            case PAN:
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int deltaX = e.getX() - mouseDownPoint.x;
                    int deltaY = e.getY() - mouseDownPoint.y;
                    double minX = -deltaX;
                    double minY = -deltaY;
                    double maxX = xyplot.getGraphArea().getWidth() - deltaX;
                    double maxY = xyplot.getGraphArea().getHeight() - deltaY;
                    xyplot.zoomToExtentScreen(minX, maxX, minY, maxY);
                    this.paintGraphics();
                }
                break;
        }
    }

    void onMouseDragged(MouseEvent e) {
        this.dragMode = true;
        int x = e.getX();
        int y = e.getY();
        switch (this.mouseMode) {
            case ZOOM_IN:
            case SELECT:
                this.repaint();
                break;
            case PAN:
                Plot plot = selPlot(e.getX(), e.getY());
                if (plot != null) {
                    Graphics2D g = (Graphics2D) this.getGraphics();
                    Rectangle2D mapRect = plot.getGraphArea();
                    g.setClip(mapRect);
                    g.setColor(Color.white);
                    int aX = e.getX() - mouseDownPoint.x;
                    int aY = e.getY() - mouseDownPoint.y;
                    if (aX > 0) {
                        if (mapRect.getX() >= 0) {
                            g.fillRect((int) mapRect.getX(), (int) mapRect.getY(), aX, (int) mapRect.getHeight());
                        } else {
                            g.fillRect(0, (int) mapRect.getY(), aX, (int) mapRect.getHeight());
                        }
                    } else if (mapRect.getX() <= this.getWidth()) {
                        g.fillRect((int) (mapRect.getX() + mapRect.getWidth() + aX), (int) mapRect.getY(), Math.abs(aX), (int) mapRect.getHeight());
                    } else {
                        g.fillRect(this.getWidth() + aX, (int) mapRect.getY(), Math.abs(aX), (int) mapRect.getHeight());
                    }
                    if (aY > 0) {
                        if (mapRect.getY() >= 0) {
                            g.fillRect((int) mapRect.getX(), (int) mapRect.getY(), (int) mapRect.getWidth(), aY);
                        } else {
                            g.fillRect((int) mapRect.getX(), 0, (int) mapRect.getWidth(), aY);
                        }
                    } else if (mapRect.getY() + mapRect.getHeight() <= this.getX() + this.getHeight()) {
                        g.fillRect((int) mapRect.getX(), (int) mapRect.getY() + (int) mapRect.getHeight() + aY, (int) mapRect.getWidth(), Math.abs(aY));
                    } else {
                        g.fillRect((int) mapRect.getX(), this.getY() + this.getHeight() + aY, (int) mapRect.getWidth(), Math.abs(aY));
                    }
                    int startX = (int) mapRect.getX() + aX;
                    int startY = (int) mapRect.getY() + aY;
                    g.drawImage(tempImage, startX, startY, this);
                    g.setColor(this.getForeground());
                    g.draw(mapRect);
                }
                break;
            case ROTATE:
                plot = selPlot(this.mouseDownPoint.x, this.mouseDownPoint.y);
                if (plot != null && plot.getPlotType() == PlotType.XYZ) {
                    Plot3D plot3d = (Plot3D) plot;
                    Projector projector = plot3d.getProjector();
                    float new_value = 0.0f;
                    // if (!thread.isAlive() || !data_available) {
                    if (e.isControlDown()) {
                        projector.set2D_xTranslation(projector.get2D_xTranslation() + (x - this.mouseLastPos.x));
                        projector.set2D_yTranslation(projector.get2D_yTranslation() + (y - this.mouseLastPos.y));
                    } else if (e.isShiftDown()) {
                        new_value = projector.getY2DScaling() + (y - this.mouseLastPos.y) * 0.5f;
                        if (new_value > 60.0f) {
                            new_value = 60.0f;
                        }
                        if (new_value < 2.0f) {
                            new_value = 2.0f;
                        }
                        projector.set2DScaling(new_value);
                    } else {
                        new_value = projector.getRotationAngle() + (x - this.mouseLastPos.x);
                        while (new_value > 360) {
                            new_value -= 360;
                        }
                        while (new_value < 0) {
                            new_value += 360;
                        }
                        projector.setRotationAngle(new_value);
                        new_value = projector.getElevationAngle() + (y - this.mouseLastPos.y);
                        if (new_value > 90) {
                            new_value = 90;
                        } else if (new_value < 0) {
                            new_value = 0;
                        }
                        projector.setElevationAngle(new_value);
                    }
                    //repaint();
                    this.paintGraphics();
//                    if (!model.isExpectDelay()) {
//                        repaint();
//                    } else {
//                        if (!dragged) {
//                            is_data_available = data_available;
//                            dragged = true;
//                        }
//                        data_available = false;
//                        repaint();
//                    }
                }
                break;
        }
        mouseLastPos.x = x;
        mouseLastPos.y = y;
    }

    void onMouseClicked(MouseEvent e) {
        int clickTimes = e.getClickCount();
        if (clickTimes == 1) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                switch (this.mouseMode) {
                    case IDENTIFER:

                        break;
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                popupMenu.show(this, e.getX(), e.getY());
            }
        }
    }

    void onMouseWheelMoved(MouseWheelEvent e) {
        Plot plt = selPlot(e.getX(), e.getY());
        if (!(plt instanceof AbstractPlot2D)) {
            return;
        }

        double minX, maxX, minY, maxY, lonRan, latRan, zoomF;
        double mouseLon, mouseLat;
        Extent drawExtent = ((AbstractPlot2D) plt).getDrawExtent();
        lonRan = drawExtent.maxX - drawExtent.minX;
        latRan = drawExtent.maxY - drawExtent.minY;
        mouseLon = drawExtent.minX + lonRan / 2;
        mouseLat = drawExtent.minY + latRan / 2;

        zoomF = 1 + e.getWheelRotation() / 10.0f;

        minX = mouseLon - (lonRan / 2 * zoomF);
        maxX = mouseLon + (lonRan / 2 * zoomF);
        minY = mouseLat - (latRan / 2 * zoomF);
        maxY = mouseLat + (latRan / 2 * zoomF);
        switch (this.mouseMode) {
            case PAN:
                ((AbstractPlot2D) plt).setDrawExtent(new Extent(minX, maxX, minY, maxY));
                this.paintGraphics();
                break;
        }
    }

    /**
     * Zoom back to full extent
     */
    public void onUndoZoomClick() {
        AbstractPlot2D xyplot;
        if (this.currentPlot == null) {
            xyplot = (AbstractPlot2D) this.chart.getPlots().get(0);
        } else {
            xyplot = (AbstractPlot2D) this.currentPlot;
        }
        xyplot.setDrawExtent((Extent) xyplot.getExtent().clone());
        this.paintGraphics();
    }

    private void onSaveFigureClick(ActionEvent e) {
        String path = System.getProperty("user.dir");
        File pathDir = new File(path);
        JFileChooser aDlg = new JFileChooser();
        aDlg.setCurrentDirectory(pathDir);
        String[] fileExts = new String[]{"png"};
        GenericFileFilter pngFileFilter = new GenericFileFilter(fileExts, "Png Image (*.png)");
        aDlg.addChoosableFileFilter(pngFileFilter);
        fileExts = new String[]{"gif"};
        GenericFileFilter mapFileFilter = new GenericFileFilter(fileExts, "Gif Image (*.gif)");
        aDlg.addChoosableFileFilter(mapFileFilter);
        fileExts = new String[]{"jpg"};
        mapFileFilter = new GenericFileFilter(fileExts, "Jpeg Image (*.jpg)");
        aDlg.addChoosableFileFilter(mapFileFilter);
        fileExts = new String[]{"eps"};
        mapFileFilter = new GenericFileFilter(fileExts, "EPS file (*.eps)");
        aDlg.addChoosableFileFilter(mapFileFilter);
        fileExts = new String[]{"pdf"};
        mapFileFilter = new GenericFileFilter(fileExts, "PDF file (*.pdf)");
        aDlg.addChoosableFileFilter(mapFileFilter);
        fileExts = new String[]{"emf"};
        mapFileFilter = new GenericFileFilter(fileExts, "EMF file (*.emf)");
        aDlg.addChoosableFileFilter(mapFileFilter);
        aDlg.setFileFilter(pngFileFilter);
        aDlg.setAcceptAllFileFilterUsed(false);
        if (JFileChooser.APPROVE_OPTION == aDlg.showSaveDialog(this)) {
            File aFile = aDlg.getSelectedFile();
            System.setProperty("user.dir", aFile.getParent());
            String extent = ((GenericFileFilter) aDlg.getFileFilter()).getFileExtent();
            String fileName = aFile.getAbsolutePath();
            if (!fileName.substring(fileName.length() - extent.length()).equals(extent)) {
                fileName = fileName + "." + extent;
            }

            try {
                this.saveImage(fileName);
            } catch (PrintException | FileNotFoundException | InterruptedException ex) {
                Logger.getLogger(ChartPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ChartPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Save image to a picture file
     *
     * @param aFile File path
     * @throws java.io.FileNotFoundException
     * @throws javax.print.PrintException
     * @throws java.lang.InterruptedException
     */
    public void saveImage(String aFile) throws FileNotFoundException, PrintException, IOException, InterruptedException {
        saveImage(aFile, null);
    }

    /**
     * Save image to a picture file
     *
     * @param aFile File path
     * @param sleep Sleep seconds for web map layer
     * @throws java.io.FileNotFoundException
     * @throws javax.print.PrintException
     * @throws java.lang.InterruptedException
     */
    public void saveImage(String aFile, Integer sleep) throws FileNotFoundException, PrintException, IOException, InterruptedException {
        int w, h;
        if (this.chartSize == null) {
            w = this.getWidth();
            h = this.getHeight();
        } else {
            w = this.chartSize.width;
            h = this.chartSize.height;
        }
        this.saveImage(aFile, w, h, sleep);
    }

    /**
     * Save image to a picture file
     *
     * @param aFile File path
     * @param width Width
     * @param height Height
     * @param sleep Sleep seconds for web map layer
     * @throws java.io.FileNotFoundException
     * @throws javax.print.PrintException
     * @throws java.lang.InterruptedException
     */
    public void saveImage(String aFile, int width, int height, Integer sleep) throws FileNotFoundException, PrintException, IOException, InterruptedException {
        if (aFile.endsWith(".ps")) {
            DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            String mimeType = "application/postscript";
            StreamPrintServiceFactory[] factories = StreamPrintServiceFactory.lookupStreamPrintServiceFactories(flavor, mimeType);
            FileOutputStream out = new FileOutputStream(aFile);
            if (factories.length > 0) {
                PrintService service = factories[0].getPrintService(out);
                SimpleDoc doc = new SimpleDoc(new Printable() {
                    @Override
                    public int print(Graphics g, PageFormat pf, int page) {
                        if (page >= 1) {
                            return Printable.NO_SUCH_PAGE;
                        } else {
                            double sf1 = pf.getImageableWidth() / (getWidth() + 1);
                            double sf2 = pf.getImageableHeight() / (getHeight() + 1);
                            double s = Math.min(sf1, sf2);
                            Graphics2D g2 = (Graphics2D) g;
                            g2.translate((pf.getWidth() - pf.getImageableWidth()) / 2, (pf.getHeight() - pf.getImageableHeight()) / 2);
                            g2.scale(s, s);

                            paintGraphics(g2);
                            return Printable.PAGE_EXISTS;
                        }
                    }
                }, flavor, null);
                DocPrintJob job = service.createPrintJob();
                PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
                job.print(doc, attributes);
                out.close();
            }
        } else if (aFile.endsWith(".eps")) {
            Properties p = new Properties();
            p.setProperty("PageSize", "A5");
            VectorGraphics g = new PSGraphics2D(new File(aFile), new Dimension(width, height));
            //g.setProperties(p);
            g.startExport();
            //this.paintGraphics(g);
            this.paintGraphics(g, width, height);
            g.endExport();
            g.dispose();
        } else if (aFile.endsWith(".pdf")) {
            try {
                Document document = new Document(new com.itextpdf.text.Rectangle(width, height));
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(aFile));
                document.open();
                PdfContentByte cb = writer.getDirectContent();
                PdfTemplate pdfTemp = cb.createTemplate(width, height); 
                Graphics2D g2 = new PdfGraphics2D(pdfTemp, width, height, true);
                this.paintGraphics(g2, width, height);
                g2.dispose(); 
                cb.addTemplate(pdfTemp, 0, 0);
                document.close();
            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (aFile.endsWith(".emf")) {
            VectorGraphics g = new EMFGraphics2D(new File(aFile), new Dimension(width, height));
            //g.setProperties(p);
            g.startExport();
            //this.paintGraphics(g);
            this.paintGraphics(g, width, height);
            g.endExport();
            g.dispose();
        } else {
            //String extension = aFile.substring(aFile.lastIndexOf('.') + 1);
            //ImageIO.write(this.mapBitmap, extension, new File(aFile));

            String extension = aFile.substring(aFile.lastIndexOf('.') + 1);
            BufferedImage aImage;
            if (extension.equalsIgnoreCase("bmp")) {
                aImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            } else {
                aImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            }
            Graphics2D g = aImage.createGraphics();
            paintGraphics(g, width, height);

            if (sleep != null) {
                Thread.sleep(sleep * 1000);
            }

            if (extension.equalsIgnoreCase("jpg")) {
                BufferedImage newImage = new BufferedImage(aImage.getWidth(), aImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                newImage.createGraphics().drawImage(aImage, 0, 0, Color.BLACK, null);
                ImageIO.write(newImage, extension, new File(aFile));
            } else {
                ImageIO.write(aImage, extension, new File(aFile));
            }
        }
    }

    /**
     * Save image to Jpeg file
     *
     * @param fileName File name
     * @param dpi DPI
     * @throws java.io.IOException
     */
    public void saveImage_Jpeg_old(String fileName, int dpi) throws IOException {
        BufferedImage image = this.mapBitmap;
        Iterator i = ImageIO.getImageWritersByFormatName("jpeg");
        //are there any jpeg encoders available?

        if (i.hasNext()) //there's at least one ImageWriter, just use the first one
        {
            ImageWriter imageWriter = (ImageWriter) i.next();
            //get the param
            ImageWriteParam param = imageWriter.getDefaultWriteParam();
            ImageTypeSpecifier its = new ImageTypeSpecifier(image.getColorModel(), image.getSampleModel());

            //get metadata
            IIOMetadata iomd = imageWriter.getDefaultImageMetadata(its,
                    param);

            String formatName = "javax_imageio_jpeg_image_1.0";//this is the DOCTYPE of the metadata we need

            Node node = iomd.getAsTree(formatName);
            //what are child nodes?
            NodeList nl = node.getChildNodes();
            for (int j = 0; j < nl.getLength(); j++) {
                Node n = nl.item(j);
                System.out.println("node from IOMetadata is : "
                        + n.getNodeName());

                if (n.getNodeName().equals("JPEGvariety")) {
                    NodeList childNodes = n.getChildNodes();

                    for (int k = 0; k < childNodes.getLength(); k++) {
                        System.out.println("node #" + k + " is "
                                + childNodes.item(k).getNodeName());
                        if (childNodes.item(k).getNodeName().equals("app0JFIF")) {
                            NamedNodeMap nnm = childNodes.item(k).getAttributes();
                            //get the resUnits, Xdensity, and Ydensity attribuutes 
                            Node resUnitsNode = getAttributeByName(childNodes.item(k), "resUnits");
                            Node XdensityNode = getAttributeByName(childNodes.item(k), "Xdensity");
                            Node YdensityNode = getAttributeByName(childNodes.item(k), "Ydensity");

                            //reset values for nodes
                            resUnitsNode.setNodeValue("1"); //indicate DPI mode 
                            XdensityNode.setNodeValue(String.valueOf(dpi));
                            YdensityNode.setNodeValue(String.valueOf(dpi));

                            System.out.println("name="
                                    + resUnitsNode.getNodeName() + ", value=" + resUnitsNode.getNodeValue());
                            System.out.println("name="
                                    + XdensityNode.getNodeName() + ", value=" + XdensityNode.getNodeValue());
                            System.out.println("name="
                                    + YdensityNode.getNodeName() + ", value=" + YdensityNode.getNodeValue());

                        } //end if (childNodes.item(k).getNodeName().equals("app0JFIF"))
                    } //end if (n.getNodeName().equals("JPEGvariety")
                    break; //we don't care about the rest of the children
                } //end if (n.getNodeName().equals("JPEGvariety"))

            } //end  for (int j = 0; j < nl.getLength(); j++)

            try {
                iomd.setFromTree(formatName, node);
            } catch (IIOInvalidTreeException e) {
                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            }
            //attach the metadata to an image
            IIOImage iioimage = new IIOImage(image, null, iomd);
            FileImageOutputStream stream = new FileImageOutputStream(new File(fileName));
            try {
                imageWriter.setOutput(stream);
                imageWriter.write(iioimage);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
        }  //end if (i.hasNext()) //there's at least one ImageWriter, just use the first one
    }

    /**
     * @param node
     * @param attributeName - name of child node to return
     * @return Node
     */
    private Node getAttributeByName(Node node, String attributeName) {
        if (node == null) {
            return null;
        }
        NamedNodeMap nnm = node.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Node n = nnm.item(i);
            if (n.getNodeName().equals(attributeName)) {
                return n;
            }
        }
        return null; // no such attribute was found
    }

    public boolean saveImage_Jpeg(String file, int dpi) {
        int w, h;
        if (this.chartSize == null) {
            w = this.getWidth();
            h = this.getHeight();
        } else {
            w = this.chartSize.width;
            h = this.chartSize.height;
        }
        return this.saveImage_Jpeg(file, w, h, dpi);
    }

    public boolean saveImage_Jpeg(String file, int width, int height, int dpi) {
        double scaleFactor = dpi / 72.0;
        BufferedImage bufferedImage = new BufferedImage((int)(width * scaleFactor), (int)(height * scaleFactor), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        AffineTransform at = g.getTransform();
        at.scale(scaleFactor, scaleFactor);
        g.setTransform(at);
        paintGraphics(g, width, height);

        try {
            // Image writer 
            ImageWriter imageWriter = ImageIO.getImageWritersBySuffix("jpeg").next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(new File(file));
            imageWriter.setOutput(ios);

            // Compression
            JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(0.85f);

            // Metadata (dpi)
            IIOMetadata data = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(bufferedImage), jpegParams);
            Element tree = (Element) data.getAsTree("javax_imageio_jpeg_image_1.0");
            Element jfif = (Element) tree.getElementsByTagName("app0JFIF").item(0);
            jfif.setAttribute("Xdensity", Integer.toString(dpi));
            jfif.setAttribute("Ydensity", Integer.toString(dpi));
            jfif.setAttribute("resUnits", "1"); // density is dots per inch	
            data.setFromTree("javax_imageio_jpeg_image_1.0", tree);

            // Write and clean up
            imageWriter.write(null, new IIOImage(bufferedImage, null, data), jpegParams);
            ios.close();
            imageWriter.dispose();
        } catch (Exception e) {
            return false;
        }
        g.dispose();

        return true;
    }

    /**
     * Save image
     *
     * @param fileName File name
     * @param dpi DPI
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    public void saveImage(String fileName, int dpi) throws IOException, InterruptedException {
        saveImage(fileName, dpi, null);
    }

    /**
     * Save image
     *
     * @param fileName File name
     * @param dpi DPI
     * @param sleep Sleep seconds for web map layer
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    public void saveImage(String fileName, int dpi, Integer sleep) throws IOException, InterruptedException {
        int width, height;
        if (this.chartSize != null) {
            height = this.chartSize.height;
            width = this.chartSize.width;
        } else {
            width = this.getWidth();
            height = this.getHeight();
        }
        
        this.saveImage(fileName,dpi, width, height, sleep);
    }

    /**
     * Save image
     *
     * @param fileName File name
     * @param dpi DPI
     * @param width Width
     * @param height Height
     * @param sleep Sleep seconds for web map layer
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    public void saveImage(String fileName, int dpi, int width, int height, Integer sleep) throws IOException, InterruptedException {
        File output = new File(fileName);
        output.delete();

        String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (formatName.equals("jpg")) {
            formatName = "jpeg";
            saveImage_Jpeg(fileName, width, height, dpi);
            return;
        }

        double scaleFactor = dpi / 72.0;
        BufferedImage image = new BufferedImage((int)(width * scaleFactor), (int)(height * scaleFactor), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        AffineTransform at = g.getTransform();
        at.scale(scaleFactor, scaleFactor);
        g.setTransform(at);
        paintGraphics(g, width, height);
        for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(formatName); iw.hasNext();) {
            ImageWriter writer = iw.next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
            IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
            if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
                continue;
            }

            ImageUtil.setDPI(metadata, dpi);

            if (sleep != null) {
                Thread.sleep(sleep * 1000);
            }
            final ImageOutputStream stream = ImageIO.createImageOutputStream(output);
            try {
                writer.setOutput(stream);
                writer.write(metadata, new IIOImage(image, null, metadata), writeParam);
            } finally {
                stream.close();
            }
            break;
        }
        g.dispose();
    }
    
    /**
     * Get view image
     *
     * @return View image
     */
    public BufferedImage getViewImage() {
        return this.mapBitmap;
    }

    /**
     * Paint view image
     *
     * @return View image
     */
    public BufferedImage paintViewImage() {
        BufferedImage aImage;
        int w, h;
        if (this.chartSize == null) {
            w = this.getWidth();
            h = this.getHeight();
        } else {
            w = this.chartSize.width;
            h = this.chartSize.height;
        }
        aImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = aImage.createGraphics();
        paintGraphics(g);

        return aImage;
    }

    // </editor-fold>
}
