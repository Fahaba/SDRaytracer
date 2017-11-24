package task;

import color.RGB;
import geometrics.*;
import math.Matrix;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* Implementation of a very simple Raytracer
   Stephan Diehl, Universit�t Trier, 2010-2016
*/


public class SDRaytracer extends JFrame {
    private static final long serialVersionUID = 1L;
    private boolean profiling = false;
    static final int width = 1000;
    static final int height = 1000;

    private Future[] futureList = new Future[width];
    private final int nrOfProcessors = Runtime.getRuntime().availableProcessors();
    private final ExecutorService eservice = Executors.newFixedThreadPool(nrOfProcessors);

    public static int maxRec = 3;
    int rayPerPixel = 1;
    int startX, startY, startZ;

    public static List<Triangle> triangles;

    RGB[][] image = new RGB[width][height];

    private final float fovx = (float) 0.628;
    private final float fovy = (float) 0.628;

    private static int y_angle_factor = 4;
    private static int x_angle_factor = -4;


    public static void main(String argv[]) {
        long start = System.currentTimeMillis();
        SDRaytracer sdr = new SDRaytracer();
        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println("time: " + time + " ms");
        System.out.println("nrprocs=" + sdr.nrOfProcessors);
    }

    private void profileRenderImage() {
        long end, start, time;

        renderImage(); // initialisiere Datenstrukturen, erster Lauf verf�lscht sonst Messungen

        for (int procs = 1; procs < 6; procs++) {

            maxRec = procs - 1;
            System.out.print(procs);
            for (int i = 0; i < 10; i++) {
                start = System.currentTimeMillis();

                renderImage();

                end = System.currentTimeMillis();
                time = end - start;
                System.out.print(";" + time);
            }
            System.out.println("");
        }
    }

    private SDRaytracer() {
        createScene();

        if (!profiling) renderImage();
        else profileRenderImage();

        buildOverlay();
    }

    double tan_fovx;
    double tan_fovy;

    private void renderImage() {
        tan_fovx = Math.tan(fovx);
        tan_fovy = Math.tan(fovy);
        for (int i = 0; i < width; i++) {
            futureList[i] = (Future) eservice.submit(new RaytraceTask(this, i));
        }

        for (int i = 0; i < width; i++) {
            try {
                RGB[] col = (RGB[]) futureList[i].get();
                System.arraycopy(col, 0, image[i], 0, height);
            } catch (InterruptedException|ExecutionException e) {
            }
        }
    }

    private void createScene() {
        triangles = new ArrayList<>();

        triangles.addAll(Triangle.buildCube(0, 35, 0, 10, 10, 10, new RGB(0.3f, 0, 0), 0.4f));       //rot, klein
        triangles.addAll(Triangle.buildCube(-70, -20, -20, 20, 100, 100, new RGB(0f, 0, 0.3f), .4f));
        triangles.addAll(Triangle.buildCube(-30, 30, 40, 20, 20, 20, new RGB(0, 0.4f, 0), 0.2f));        // gr�n, klein
        triangles.addAll(Triangle.buildCube(50, -20, -40, 10, 80, 100, new RGB(.5f, .5f, .5f), 0.2f));
        triangles.addAll(Triangle.buildCube(-70, -26, -40, 130, 3, 40, new RGB(.5f, .5f, .5f), 0.2f));

        Matrix mRx = Matrix.createXRotation((float) (x_angle_factor * Math.PI / 16));
        Matrix mRy = Matrix.createYRotation((float) (y_angle_factor * Math.PI / 16));
        Matrix mT = Matrix.createTranslation(0, 0, 200);
        Matrix m = mT.mult(mRx).mult(mRy);
        m.print();
        m.apply(triangles);
    }

    private void buildOverlay() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel area = new JPanel() {
            @Override
            public void paint(Graphics g) {
                System.out.println("fovx=" + fovx + ", fovy=" + fovy + ", xangle=" + x_angle_factor + ", yangle=" + y_angle_factor);
                if (image == null) return;
                for (int i = 0; i < width; i++)
                    for (int j = 0; j < height; j++) {
                        g.setColor(image[i][j].color());
                        // zeichne einzelnen Pixel
                        g.drawLine(i, height - j, i, height - j);
                    }
            }
        };

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean redraw = true;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        x_angle_factor--;
                        break;
                    case KeyEvent.VK_UP:
                        x_angle_factor++;
                        break;
                    case KeyEvent.VK_LEFT:
                        y_angle_factor--;
                        break;
                    case KeyEvent.VK_RIGHT:
                        y_angle_factor++;
                        break;
                    default:
                        redraw = false;
                        break;
                }
                if (redraw) {
                    createScene();
                    renderImage();
                    repaint();
                }
            }
        });

        area.setPreferredSize(new Dimension(width, height));
        contentPane.add(area);
        this.pack();
        this.setVisible(true);

    }
}






