import java.util.ArrayList;
import java.util.List;

public class InteriorPointMethod {
    public static void main(String[] args) {
        List<Double> c = new ArrayList<>();
        List<List<Double>> a = new ArrayList<>();
        List<Double> b = new ArrayList<>();
        double eps = 0.0001;
        double alpha1 = 0.5;
        double alpha2 = 0.9;

        // maximize 2x1 + 3x2 + 4x3
        // subject to:
        // 3x1 + 2x2 + x3 <= 10
        // 2x1 + 5x2 + 3x3 <= 15
        // 4x1 + x2 + 2x3 <= 8
        // x1, x2, x3 >= 0

        c.add(2.0);
        c.add(3.0);
        c.add(4.0);

        List<Double> a1 = new ArrayList<>();
        a1.add(3.0);
        a1.add(2.0);
        a1.add(1.0);
        a.add(a1);

        List<Double> a2 = new ArrayList<>();
        a2.add(2.0);
        a2.add(5.0);
        a2.add(3.0);
        a.add(a2);

        List<Double> a3 = new ArrayList<>();
        a3.add(4.0);
        a3.add(1.0);
        a3.add(2.0);
        a.add(a3);

        b.add(10.0);
        b.add(15.0);
        b.add(8.0);

        System.out.println("\n--- Interior-Point Method ---\n");

        SimplexResult simplexResult = SimplexMethod.solve(c, a, b, eps);
        if (simplexResult.getObj() == Double.POSITIVE_INFINITY) {
            System.out.println("The problem does not have solution!");
            return;
        }

        List<Double> xInterior1 = solveInteriorPointMethod(c, a, b, eps, alpha1);
        double objInterior1 = calculateObjectiveFunction(c, xInterior1);
        System.out.println("x* by Interior-Point algorithm (alpha = 0.5): " + formatList(xInterior1));
        System.out.println(
                "Maximum value of the objective function (alpha = 0.5): " + String.format("%.4f", objInterior1));

        List<Double> xInterior2 = solveInteriorPointMethod(c, a, b, eps, alpha2);
        double objInterior2 = calculateObjectiveFunction(c, xInterior2);
        System.out.println("x* by Interior-Point algorithm (alpha = 0.9): " + formatList(xInterior2));
        System.out.println(
                "Maximum value of the objective function (alpha = 0.9): " + String.format("%.4f", objInterior2));
    }

    private static String formatList(List<Double> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(String.format("%.4f", list.get(i)));
            if (i < list.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private static List<Double> solveInteriorPointMethod(List<Double> c, List<List<Double>> a, List<Double> b,
            double eps, double alpha) {
        int n = c.size();
        int m = b.size();
        List<Double> x = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            x.add(1.0);
        }
        List<Double> s = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            s.add(1.0);
        }
        double t = 1.0;

        while (true) {
            List<List<Double>> A = new ArrayList<>(m + 1);
            for (int i = 0; i < m; i++) {
                List<Double> row = new ArrayList<>(n + m + 1);
                for (int j = 0; j < n; j++) {
                    row.add(a.get(i).get(j));
                }
                for (int j = 0; j < m; j++) {
                    row.add(0.0);
                }
                row.add(s.get(i));
                A.add(row);
            }
            List<Double> row = new ArrayList<>(n + m + 1);
            for (int i = 0; i < n; i++) {
                row.add(0.0);
            }
            for (int i = 0; i < m; i++) {
                row.add(a.get(i).get(n - 1));
            }
            row.add(0.0);
            A.add(row);

            List<Double> bNew = new ArrayList<>(b);
            bNew.add(0.0);

            List<Double> cNew = new ArrayList<>(n + m + 1);
            for (int i = 0; i < n; i++) {
                cNew.add(c.get(i));
            }
            for (int i = 0; i < m; i++) {
                cNew.add(0.0);
            }
            cNew.add(-t);

            SimplexResult result = SimplexMethod.solve(cNew, A, bNew, eps);
            if (result.getObj() == Double.POSITIVE_INFINITY) {
                System.out.println("The method is not applicable!");
                return null;
            }

            List<Double> y = result.getX();
            double mu = 0.0;
            for (int i = 0; i < m; i++) {
                mu += s.get(i) / b.get(i);
            }
            mu /= m;

            double tNew = alpha * t;
            double sigma = (t / tNew) * mu;

            boolean done = true;
            for (int i = 0; i < n; i++) {
                double xi = y.get(i);
                double si = y.get(n + i);
                double delta = sigma - si * xi;
                if (delta > 0) {
                    done = false;
                    x.set(i, xi + delta / (si + eps));
                }
            }
            for (int i = 0; i < m; i++) {
                double si = y.get(n + i);
                double delta = sigma - si * b.get(i);
                if (delta > 0) {
                    done = false;
                    s.set(i, si + delta / (b.get(i) + eps));
                }
            }

            if (done) {
                return x;
            }

            t = tNew;
        }
    }

    private static double calculateObjectiveFunction(List<Double> c, List<Double> x) {
        double obj = 0.0;
        for (int i = 0; i < c.size(); i++) {
            obj += c.get(i) * x.get(i);
        }
        return obj;
    }
}