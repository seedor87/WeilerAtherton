import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

/**
 * Created by robertseedorf on 3/11/16.
 */
public class Driver {

    private static Polygon subject;
    private static Polygon viewport;
    private static Polygon clipped;

    public static void main(String[] Args) {

        //create subject
        Point A = new Point(3.0, 3.5);
        Point B = new Point(-2.0, 3.5);
        Point C = new Point(-0.5, 1.5);
        Point D = new Point(0.5, 0.5);
        Point E = new Point(3.0, 0.0);
        subject = new Polygon(Arrays.asList(A, B, C, D, E));

        //create viewport, square
        Point F = new Point(2.0, 3.0);
        Point G = new Point(-1.0, 3.0);
        Point H = new Point(-1.0, -1.0);
        Point I = new Point(2.0, -1.0);
        viewport = new Polygon(Arrays.asList(F, G, H, I));

        clipped = findClipped();
        renderWindow();
        test4();
    }

    private static void renderWindow() {
        JFrame frame = new JFrame(" Clipping Testing ");
        frame.setSize(500,500);

        Canvas canvas = new Canvas(viewport.getSides(), subject.getSides(),
                clipped.getSides(), frame.getWidth(), frame.getHeight());
        frame.getContentPane().add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        System.out.println("Success");
    }

    private static void test4() {

        IntersectioniFinder IF = new IntersectioniFinder();

        ArrayList<Point> P = new ArrayList<Point>();
        ArrayList<Point> Q = new ArrayList<Point>();
        ArrayList<Point> Ie = new ArrayList<Point>();
        int intersectionindex = 0;
        ArrayList<Line> subjectLines = new ArrayList<Line>(subject.getSides());
        ArrayList<Line> viewPortLines = new ArrayList<Line>(viewport.getSides());
        for(Line subjectLine: subjectLines) {
            P.add(subjectLine.start);
            for(Line viewPortLine: viewPortLines) {
                if(IF.checkIntersect(subjectLine, viewPortLine)) {
                    Point poi = IF.findPOI(subjectLine, viewPortLine);
                    P.add(poi);
                    if(intersectionindex % 2 == 0) {
                        Ie.add(poi);
                    }
                    intersectionindex++;
                }
            }
        }
        Collections.reverse(subjectLines);          //Be sure to iterate over the collection of subject lines in reverse
        for(Line viewPortLine: viewPortLines) {
            Q.add(viewPortLine.start);
            for(Line subjectLine: subjectLines) {
                if(IF.checkIntersect(subjectLine, viewPortLine)) {
                    Point poi = IF.findPOI(subjectLine, viewPortLine);
                    Q.add(poi);
                }
            }
        }
        Collections.reverse(subjectLines);

        for(Line l: subjectLines) {
            System.out.println(l);
        }
        System.out.println("=====");
        int index = 2;
        int sentinel = index;
        do {
            index++;
            index = index % subjectLines.size();
            System.out.println(subjectLines.get(index));
        }
        while(index != sentinel);
    }

    private static Polygon findClipped() {

        IntersectioniFinder IF = new IntersectioniFinder();

        ArrayList<Point> P = new ArrayList<Point>();        //Collection of points lying on perimeter of subject
        ArrayList<Point> Q = new ArrayList<Point>();        //Collection of points lying on perimeter of viewport
        ArrayList<Point> Ie = new ArrayList<Point>();       //Collection of *entry* points of intersection
        int intersectionCount = 0;

        ArrayList<Line> subjectLines = new ArrayList<Line>(subject.getSides());
        ArrayList<Line> viewPortLines = new ArrayList<Line>(viewport.getSides());

        //Build P
        for(Line subjectLine: subjectLines) {
            P.add(subjectLine.start);
            for(Line viewPortLine: viewPortLines) {
                if(IF.checkIntersect(subjectLine, viewPortLine)) {
                    Point poi = IF.findPOI(subjectLine, viewPortLine);
                    P.add(poi);
                    if(intersectionCount % 2 == 0) {    //Build Ie
                        Ie.add(poi);
                    }
                    intersectionCount++;
                }
            }
        }

        //Build Q
        Collections.reverse(subjectLines);          //Be sure to iterate over the collection of subject lines in reverse
        for(Line viewPortLine: viewPortLines) {
            Q.add(viewPortLine.start);
            for(Line subjectLine: subjectLines) {
                if(IF.checkIntersect(subjectLine, viewPortLine)) {
                    Point poi = IF.findPOI(subjectLine, viewPortLine);
                    Q.add(poi);
                }
            }
        }

        //If no intersecting region is found then return polygon of zero dimension
        if(Ie.isEmpty()) {
            return new Polygon();
        }

        Stack<Point> result = new Stack<Point>();
        for (Point entry : Ie) {
            int index = P.indexOf(entry);
            int sentinel = index;
            result.push(P.get(index));
            do {
                index++;
                index = index % P.size();
                result.push(P.get(index));
            }
            while (index != sentinel && !(P.get(index) instanceof PointOfIntersection));
            index = Q.indexOf(P.get(index));
            sentinel = index;
            do {
                index++;
                index = index % Q.size();
                result.push(Q.get(index));
            }
            while (index != sentinel && !(Q.get(index) instanceof PointOfIntersection));
        }
        return new Polygon(result);
    }
}

    /*Stack<Point> result = new Stack<Point>();
        for (Point entry : Ie) {
        int index = P.indexOf(entry);
        result.push(P.get(index));
        do {
        index++;
        result.push(P.get(index));
        }
        while (!(P.get(index) instanceof PointOfIntersection));
        index = Q.indexOf(P.get(index));
        do {
        index++;
        result.push(Q.get(index));
        }
        while (!(Q.get(index) instanceof PointOfIntersection));
        }
        return new Polygon(result);*/





