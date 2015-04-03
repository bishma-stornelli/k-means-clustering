
import java.awt.Color;
import java.util.Random;

/**
 *
 * @author bstornelli
 */
public class Kmeans {

    private final static double CLUSTER_RADIO = 0.01;
    private final static double POINT_RADIO = 0.005;
    private final static double CIRCLE_RADIO = 0.002;
    private static double LINE_RADIO = 0.00002;
    private final static int SLEEP_TIME = 1;
    private static Color[] colors;
    private static Point2D[] clusters;
    private static Point2D[] points;
    private static int[] closestCluster;
    private static int n = 2000;
    private static int t = 20000;
    private static int k = 20;
    private static double[] clusterRadio;
    private static int iter = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {

        StdDraw.setXscale(0, t);
        StdDraw.setYscale(0, t * 1.1);

        points = new Point2D[n];
        for (int i = 0; i < n; ++i) {
            double x = random(t);
            double y = random(t);
            points[i] = new Point2D(x, y);
//            System.out.println(points[i]);
        }

        // Clustering:
        clusters = new Point2D[k];
        initializeColors(k);

        // Inicialization: select k points randomly
        for (int i = 0; i < k; ++i) {
            clusters[i] = points[i];
        }

        closestCluster = new int[n];
        clusterRadio = new double[k];
        double[] distance = new double[n]; // Distance to the closest cluster's center

        
        
        StdDraw.setPenColor();
        StdDraw.textLeft(0, t * 1.1, "K-Means Clustering");
        StdDraw.textLeft(0, t * 1.05, "Iteration " + iter);
        StdDraw.textRight(t, t * 1.1, "n = " + n);
        StdDraw.textRight(t, t * 1.05, "k = " + k);
        StdDraw.show(0);
        drawPoints(false);
        StdDraw.show(0);
//        Thread.sleep(5000);
        StdDraw.show(0);
        drawClusters();
        StdDraw.show(0);
//        Thread.sleep(1000);

        int changed;
        do {
            ++iter;
            changed = 0;
            double[] newX = new double[k];
            double[] newY = new double[k];
            int[] sizeOfCluster = new int[k];
            clusterRadio = new double[k];

            for (int p = 0; p < n; ++p) {
                distance[p] = Double.POSITIVE_INFINITY;

                for (int c = 0; c < k; ++c) {
                    double d = clusters[c].distanceTo(points[p]);
                    if (d < distance[p]) {
                        closestCluster[p] = c;
                        distance[p] = d;
                    }
                }
                newX[closestCluster[p]] += points[p].x();
                newY[closestCluster[p]] += points[p].y();
                if (distance[p] > clusterRadio[closestCluster[p]]) {
                    clusterRadio[closestCluster[p]] = distance[p];
                }
                ++sizeOfCluster[closestCluster[p]];
            }
            // Start the drawing
            draw();
            Thread.sleep(SLEEP_TIME);

//            StdArrayIO.print(newX);
//            StdArrayIO.print(newY);
//            StdArrayIO.print(sizeOfCluster);
            for (int i = 0; i < k; ++i) {
                newX[i] /= sizeOfCluster[i];
                newY[i] /= sizeOfCluster[i];
                Point2D newP;
                try {
                    newP = new Point2D(newX[i], newY[i]);
                } catch (IllegalArgumentException e) {
                    newP = clusters[i];
                }
                if (!newP.equals(clusters[i])) {
                    ++changed;
                }
                clusters[i] = newP;
            }
        } while (changed > 0);

        draw();
        Thread.sleep(SLEEP_TIME);

    }

    /**
     * Generates a random double greater than or equal to 0 and less than t
     *
     * @param t
     * @return
     */
    private static double random(int t) {
        return StdRandom.uniform() * t;
    }

    private static void draw() throws InterruptedException {

        StdDraw.show(0);
        StdDraw.clear();
        
        StdDraw.setPenColor();
        StdDraw.textLeft(0, t * 1.1, "K-Means Clustering");
        StdDraw.textLeft(0, t * 1.05, "Iteration " + iter);
        StdDraw.textRight(t, t * 1.1, "n = " + n);
        StdDraw.textRight(t, t * 1.05, "k = " + k);

        drawClusters();

        drawPoints(true);

        StdDraw.show(SLEEP_TIME / 2);
    }

    private static void initializeColors(int n) {
        colors = new Color[n];
        for (int i = 0; i < n; i++) {
            colors[i] = Color.getHSBColor((float)StdRandom.uniform(), 0.85f, 1.0f);
        }

    }

    private static void drawPoints(boolean colored) {
        
        for (int i = 0; i < points.length; ++i) {
            if (colored) {
                StdDraw.setPenColor(colors[closestCluster[i]]);
                StdDraw.setPenRadius(LINE_RADIO);
                points[i].drawTo(clusters[closestCluster[i]]);
            }
            StdDraw.setPenRadius(POINT_RADIO);
            points[i].draw();
        }
    }

    private static void drawClusters() {
        for (int i = 0; i < clusters.length; ++i) {
            StdDraw.setPenRadius(CLUSTER_RADIO);
            StdDraw.setPenColor(colors[i]);
            clusters[i].draw();
//            StdDraw.setPenRadius(CIRCLE_RADIO);
//            StdDraw.circle(clusters[i].x(), clusters[i].y(), clusterRadio[i]);
        }
    }

}
