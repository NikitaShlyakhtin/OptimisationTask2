import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InteriorPointMethod {
    public static void main(String[] args) {
        List<Double> c = new ArrayList<>(Arrays.asList(2.0, 3.0, 4.0)); // A vector of coefficients of the objective function
        List<List<Double>> a = new ArrayList<>(Arrays.asList(
                Arrays.asList(3.0, 2.0, 1.0),
                Arrays.asList(2.0, 5.0, 3.0),
                Arrays.asList(4.0, 1.0, 2.0)
        )); // A matrix of coefficients of constraints
        List<Double> b = new ArrayList<>(Arrays.asList(10.0, 15.0, 8.0)); // A vector of right-hand sides of constraints
        double eps = 0.0001;
        double alpha1 = 0.5;
        double alpha2 = 0.9;

        System.out.println("\n--- Interior-Point Method ---\n");

        SimplexResult simplexResult = SimplexMethod.solve(c, a, b, eps);
        if (simplexResult.getObj() == Double.POSITIVE_INFINITY) {
            System.out.println("The problem does not have a solution!");
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

    private static List<Double> solveInteriorPointMethod(List<Double> c, List<List<Double>> a, List<Double> b, double eps, double alpha) {
        int n = c.size();
        int m = b.size();
        List<Double> x = initializeX(n, m);
        List<Double> s = initializeS(m);
        double t = 1.0;

        while (true) {
            List<List<Double>> A = formMatrixA(a, s, m, n);
            List<Double> bNew = formVectorB(b);
            List<Double> cNew = formVectorC(c, n, m, t);

            SimplexResult result = SimplexMethod.solve(cNew, A, bNew, eps);
            if (result.getObj() == Double.POSITIVE_INFINITY) {
                System.out.println("The method is not applicable!");
                return null;
            }

            List<Double> y = result.getX();
            double mu = calculateMu(s, b, m);
            double tNew = updateT(t, alpha);
            double sigma = calculateSigma(t, tNew, mu);

            boolean done = adjustXandS(x, y, s, b, sigma, eps, n, m);

            if (done) {
                return x;
            }

            t = tNew;
        }
    }

    private static List<Double> initializeX(int n, int m) {
        List<Double> x = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            x.add(1.0);
        }
        return x;
    }

    private static List<Double> initializeS(int m) {
        List<Double> s = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            s.add(1.0);
        }
        return s;
    }

    private static List<List<Double>> formMatrixA(List<List<Double>> a, List<Double> s, int m, int n) {
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

        return A;
    }

    private static List<Double> formVectorB(List<Double> b) {
        List<Double> bNew = new ArrayList<>(b);
        bNew.add(0.0);
        return bNew;
    }

    private static List<Double> formVectorC(List<Double> c, int n, int m, double t) {
        List<Double> cNew = new ArrayList<>(n + m + 1);
        for (int i = 0; i < n; i++) {
            cNew.add(c.get(i));
        }
        for (int i = 0; i < m; i++) {
            cNew.add(0.0);
        }
        cNew.add(-t);
        return cNew;
    }

    private static double calculateMu(List<Double> s, List<Double> b, int m) {
        double mu = 0.0;
        for (int i = 0; i < m; i++) {
            mu += s.get(i) / b.get(i);
        }
        mu /= m;
        return mu;
    }

    private static double updateT(double t, double alpha) {
        double tNew = alpha * t;
        return tNew;
    }

    private static double calculateSigma(double t, double tNew, double mu) {
        double sigma = (t / tNew) * mu;
        return sigma;
    }

    private static boolean adjustXandS(List<Double> x, List<Double> y, List<Double> s, List<Double> b, double sigma, double eps, int n, int m) {
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
        return done;
    }

    private static double calculateObjectiveFunction(List<Double> c, List<Double> x) {
        double obj = 0.0;
        for (int i = 0; i < c.size(); i++) {
            obj += c.get(i) * x.get(i);
        }
        return obj;
    }
}
