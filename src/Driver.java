import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by robertseedorf on 3/11/16.
 */
public class Driver {

    private static Polygon subject;
    private static Polygon viewport;
    private static Polygon clipped;

    public static void main(String[] Args) {

        clipped = findClipped();
        renderWindow();
        test3();
    }

    private static void renderWindow() {
        JFrame frame = new JFrame(" Clipping Testing ");
        frame.setSize(500,500);
        Canvas canvas = new Canvas(viewport.getSides(), subject.getSides(),
                clipped.getSides(), frame.getWidth(), frame.getHeight());
        frame.getContentPane().add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void test3() {

        IntersectioniFinder IF = new IntersectioniFinder();

        //create subject
        Point A = new Point(3.0, 3.0);
        Point B = new Point(1.5, 1.75);
        Point C = new Point(0.0, 1.5);
        Point D = new Point(0.5, 0.5);
        Point E = new Point(3.0, 0.0);
        subject = new Polygon(Arrays.asList(A, B, C, D, E));

        //create viewport, square
        Point F = new Point(2.0, 3.0);
        Point G = new Point(-1.0, 3.0);
        Point H = new Point(-1.0, -1.0);
        Point I = new Point(2.0, -1.0);
        viewport = new Polygon(Arrays.asList(F, G, H, I));

        ArrayList<Point> P = new ArrayList<Point>();
        ArrayList<Point> Q = new ArrayList<Point>();
        ArrayList<Point> Ie = new ArrayList<Point>();
        int intersectionCount = 0;
        ArrayList<Line> subjectLines = new ArrayList<Line>(subject.getSides());
        ArrayList<Line> viewPortLines = new ArrayList<Line>(viewport.getSides());
        for(Line subjectLine: subjectLines) {
            P.add(subjectLine.start);
            for(Line viewPortLine: viewPortLines) {
                if(IF.checkIntersect(subjectLine, viewPortLine)) {
                    Point poi = IF.findPOI(subjectLine, viewPortLine);
                    P.add(poi);
                    if(intersectionCount % 2 == 0) {
                        Ie.add(poi);
                    }
                    intersectionCount++;
                }
            }
        }
        for(Line viewPortLine: viewPortLines) {
            Q.add(viewPortLine.start);
            for(Line subjectLine: subjectLines) {
                if(IF.checkIntersect(subjectLine, viewPortLine)) {
                    Point poi = IF.findPOI(subjectLine, viewPortLine);
                    Q.add(poi);
                }
            }

        }

        //Debug
        Collections.reverse(P);
        for(Point p: P) {
            System.out.println(p);
        }
        System.out.println("...........");
        for(Point p: Q) {
            System.out.println(p);
        }
        System.out.println("-----------");
        P = new ArrayList<Point>(Arrays.asList(A, IF.findPOI(new Line(A, B), new Line(F, I)), B, C, D,
                IF.findPOI(new Line(D, E), new Line(F, I)), E));
        for(Point p: P) {
            System.out.println(p.toString());
        }
        System.out.println("...........");
        Q = new ArrayList<Point>(Arrays.asList(F, G, H, I, IF.findPOI(new Line(D, E), new Line(F, I)),
                IF.findPOI(new Line(A, B), new Line(F, I))));
        for(Point p: Q) {
            System.out.println(p.toString());
        }
    }

    private static Polygon findClipped() {

        IntersectioniFinder IF = new IntersectioniFinder();

        //create subject
        Point A = new Point(3.0, 3.0);
        Point B = new Point(1.5, 1.75);
        Point C = new Point(0.0, 1.5);
        Point D = new Point(0.5, 0.5);
        Point E = new Point(3.0, 0.0);
        subject = new Polygon(Arrays.asList(A, B, C, D, E));

        //create viewport, square
        Point F = new Point(2.0, 3.0);
        Point G = new Point(-1.0, 3.0);
        Point H = new Point(-1.0, -1.0);
        Point I = new Point(2.0, -1.0);
        viewport = new Polygon(Arrays.asList(F, G, H, I));

        if(!IF.checkIntersect(A, B, F, I) || !IF.checkIntersect(D, E, F, I)) {
            return new Polygon();
        }
        Point K = IF.findPOI(new Line(A, B), new Line(F, I));
        Point J = IF.findPOI(new Line(D, E), new Line(F, I));
        ArrayList<Point> P = new ArrayList<Point>(Arrays.asList(A, K, B, C, D, J, E));
        ArrayList<Point> Q = new ArrayList<Point>(Arrays.asList(F, G, H, I, J, K));
        ArrayList<Point> Ie = new ArrayList<Point>(Arrays.asList(K));


        Stack<Point> result = new Stack<Point>();
        for (Point entry : Ie) {
            int index = P.indexOf(entry);
            int count = index;
            result.push(P.get(count));
            count = count + 1;
            while (!(P.get(count) instanceof PointOfIntersection)) {
                result.push(P.get(count));
                count++;
            }
            result.push(P.get(count));
            index = Q.indexOf(P.get(count));
            count = index;
            while (!(Q.get(count) instanceof PointOfIntersection)) {
                result.push(Q.get(count));
                count++;
            }

        }
        return new Polygon(result);
    }
}


