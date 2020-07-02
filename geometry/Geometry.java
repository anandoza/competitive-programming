package library.geometry;

public class Geometry {

    public static void main(String[] args) {
//        Point a = new Point(0, 0);
//        Point b = new Point(5, 5);
//        Point c = new Point(1, 9);
//        Point d = new Point(-5, 5);
//        Point[] poly = {a,
//                        b,
//                        c,
//                        d};
//
//        System.out.println((new Point(2, 11)).classify(poly));

        double ans = 0;
        int n = 1000000;
        for (int i = 0; i < n; i++) {
            ans += f() ? 1.0 : 0.0;
        }
        ans /= n;
        System.out.println(ans);
    }

    static boolean f() {
        double[] angle = new double[3];
        for (int i = 0; i < 3; i++) {
            angle[i] = Math.random() * 2 * Math.PI;
        }
        PointF[] polygon = new PointF[3];
        for (int i = 0; i < 3; i++) {
            polygon[i] = new PointF(Math.cos(angle[i]), Math.sin(angle[i]));
        }

        Classification classification = new PointF(0, 0).classify(polygon);
        //System.out.println(classification + "\t" + Arrays.toString(polygon));
        return classification == Classification.IN;
    }

}
