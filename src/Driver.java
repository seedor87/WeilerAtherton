import javax.swing.*;
import java.util.*;

/**
 * Created by robertseedorf on 3/11/16.
 */
public class Driver {

    private static Polygon subject;
    private static Polygon viewport;
    private static Polygon clipped;

    public static void main(String[] Args) {

        //create subject
        Point A = new Point(3.0, 1.0);
        Point B = new Point(1.0, 3.5);
        Point C = new Point(0.0, 0.0);
        Point D = new Point(0.0, -3.0);
        Point E = new Point(1.0, 0.0);
        subject = new Polygon(Arrays.asList(A, B, C, D, E));
        //subject = new Polygon(Arrays.asList(B, C, D, E, A));

        //create viewport, square
        Point F = new Point(2.0, 3.0);
        Point G = new Point(-1.0, 3.0);
        Point H = new Point(-1.0, -1.0);
        Point I = new Point(2.0, -1.0);
        viewport = new Polygon(Arrays.asList(F, G, H, I));
        //viewport = new Polygon(Arrays.asList(G, H, I, F));
        //viewport = new Polygon(Arrays.asList(H, I, F, G));

        clipped = findClipped();
        renderWindow();
        test5();
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

    private static void test5() {

        IntersectioniFinder IF = new IntersectioniFinder();

        ArrayList<Point> P = new ArrayList<Point>();
        ArrayList<Point> Q = new ArrayList<Point>();
        Stack<Point> Ie = new Stack<Point>();
        ArrayList<Line> subjectLines = new ArrayList<Line>(subject.getSides());
        ArrayList<Line> viewPortLines = new ArrayList<Line>(viewport.getSides());

        // Construct P from subject, and viewport
        for(Line subjectLine: subjectLines) {
            P.add(subjectLine.start);
            for(Line viewPortLine: viewPortLines) {
                if(IF.checkIntersect(subjectLine, viewPortLine)) {
                    Point poi = IF.findPOI(subjectLine, viewPortLine);
                    P.add(poi);
                    Ie.push(poi);        // build Ie in the mean while
                }
            }
        }
        Collections.reverse(Ie);

        /*
        Construct Q using new algorithm;
         */
        Point corner;                       // Point representing cardinal corners of viewPort polygon
        ArrayList<Point> storage ;          // ^emporary storage of only matching poi's

        // Top right corner first
        corner = viewPortLines.get(0).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.y.equals(p.y)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Collections.reverse(storage);
        Q.addAll(storage);

        // top left corner next
        corner = viewPortLines.get(1).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.x.equals(p.x)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Collections.reverse(storage);
        Q.addAll(storage);

        // bottom left corner next
        corner = viewPortLines.get(2).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.y.equals(p.y)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Q.addAll(storage);

        // bottom right corner last
        corner = viewPortLines.get(3).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.x.equals(p.x)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Q.addAll(storage);

        print(Q);
    }

    private static Polygon findClipped() {

        IntersectioniFinder IF = new IntersectioniFinder();

        ArrayList<Point> P = new ArrayList<Point>();        //Collection of points lying on perimeter of subject
        ArrayList<Point> Q = new ArrayList<Point>();        //Collection of points lying on perimeter of viewport
        Stack<Point> Ie = new Stack<Point>();       //Collection of all points of intersection

        ArrayList<Line> subjectLines = new ArrayList<Line>(subject.getSides());
        ArrayList<Line> viewPortLines = new ArrayList<Line>(viewport.getSides());

        //Build P
        for(Line subjectLine: subjectLines) {
            P.add(subjectLine.start);
            for(Line viewPortLine: viewPortLines) {
                if(IF.checkIntersect(subjectLine, viewPortLine)) {
                    Point poi = IF.findPOI(subjectLine, viewPortLine);
                    P.add(poi);
                    Ie.push(poi);        // build Ie
                }
            }
        }

        /*
        Construct Q using new algorithm;
         */
        Point corner;                       // Point representing cardinal corners of viewPort polygon
        ArrayList<Point> storage ;          // ^emporary storage of only matching poi's

        // Top right corner first
        corner = viewPortLines.get(0).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.y.equals(p.y)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Collections.reverse(storage);
        Q.addAll(storage);

        // top left corner next
        corner = viewPortLines.get(1).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.x.equals(p.x)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Collections.reverse(storage);
        Q.addAll(storage);

        // bottom left corner next
        corner = viewPortLines.get(2).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.y.equals(p.y)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Q.addAll(storage);

        // bottom right corner last
        corner = viewPortLines.get(3).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.x.equals(p.x)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Q.addAll(storage);

        //If no intersecting region is found then return polygon of zero dimension
        if(Ie.isEmpty()) {
            return new Polygon();
        }

        Stack<Point> result = new Stack<Point>();

        Collections.reverse(Ie);

        Point reserve, start, end, location;
        reserve = Ie.peek();
        location = reserve;
        int index;
        boolean flag = true;
        while(flag) {
            start = Ie.pop();
            end = Ie.peek();
            index = P.indexOf(location);
            while(!location.equals(end)) {
                result.push(location);
                index++;
                index = index % P.size();
                location = P.get(index);
            }
            start = Ie.pop();
            try {
                end = Ie.peek();
            }
            catch(EmptyStackException e) {
                end = reserve;
                flag = false;
            }
            finally {
                index = Q.indexOf(location);
                while (!location.equals(end)) {
                    result.push(location);
                    index++;
                    index = index % Q.size();
                    location = Q.get(index);
                }
            }
        }

        // For posterity we save commemorate this code
       /* for (Point entry : Ie) {
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
        }*/
        System.out.println(P);
        System.out.println(Q);
        System.out.println(result);
        return new Polygon(result);
    }

    private static void print(Object o) {
        System.out.println(o.toString());
    }
}

class CustomComparator implements Comparator<Point> {
    @Override
    public int compare(Point p1, Point p2) {
        return p1.compareTo(p2);
    }
}





