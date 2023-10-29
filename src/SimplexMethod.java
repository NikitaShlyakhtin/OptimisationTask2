import java.util.ArrayList;
import java.util.List;

/**
 * The type Simplex method.
 */
public class SimplexMethod {
    private static int n; // number of variables
    private static int m; // number of equation
    private static List<List<Double>> d; // matrix of coefficients of constraints
    private static List<Integer> basis; // basic variables
    private static double eps; // the approximation accuracy

    // Initialisation of SimplexMethod instance with given values
    private static void initializeFor(List<Double> c, List<List<Double>> a, List<Double> b, double eps) {
        SimplexMethod.n = c.size();
        SimplexMethod.m = b.size();
        SimplexMethod.eps = eps;
        SimplexMethod.d = new ArrayList<>();
        SimplexMethod.basis = new ArrayList<>();
        for (int i = 0; i < m + 1; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < n + m + 1; j++) {
                row.add(0.0);
            }
            SimplexMethod.d.add(row);
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                d.get(i).set(j, a.get(i).get(j));
            }
            d.get(i).set(n + i, 1.0);
            basis.add(n + i);
            d.get(i).set(n + m, b.get(i));
        }

        for (int j = 0; j < n; j++) {
            d.get(m).set(j, -c.get(j));
        }
    }

    // Find entering variable for current iteration
    private static int findIndexOfIteration() {
        int indexOfIteration = 0;
        for (int j = 1; j < n + m; j++) {
            if (d.get(m).get(j) < d.get(m).get(indexOfIteration)) {
                indexOfIteration = j;
            }
        }
        return indexOfIteration;
    }

    // Find leaving variable for current iteration
    private static int findLeavingIndex(int iteratingIndex) {
        int leavingIndex = -1;
        for (int i = 0; i < m; i++) {
            if (d.get(i).get(iteratingIndex) > eps &&
                    (leavingIndex == -1
                            || d.get(i).get(n + m) / d.get(i).get(iteratingIndex) < d.get(leavingIndex).get(n + m)
                                    / d.get(leavingIndex).get(iteratingIndex))) {
                leavingIndex = i;
            }
        }
        if (leavingIndex == -1) {
            throw new ArithmeticException("Result is unbounded");
        }
        return leavingIndex;
    }

    // Do pivot with leaving and entering variables
    private static void doPivot(int iteratingIndex, int leavingIndex) {
        double t = d.get(leavingIndex).get(iteratingIndex);
        for (int j = 0; j <= n + m; j++) {
            d.get(leavingIndex).set(j, d.get(leavingIndex).get(j) / t);
        }
        for (int i = 0; i <= m; i++) {
            if (i != leavingIndex) {
                t = d.get(i).get(iteratingIndex);
                for (int j = 0; j <= n + m; j++) {
                    d.get(i).set(j, d.get(i).get(j) - d.get(leavingIndex).get(j) * t);
                }
            }
        }
        basis.set(leavingIndex, iteratingIndex);
    }

    // Do one iteration with swapping leaving and entering variables
    private static void iterate() {
        while (true) {
            int iteratingIndex = findIndexOfIteration();
            if (d.get(m).get(iteratingIndex) >= -eps) {
                break;
            }
            doPivot(iteratingIndex, findLeavingIndex(iteratingIndex));
        }
    }

    // Derive the solution for x column
    private static List<Double> deriveSolution() {
        List<Double> derivedSolution = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            derivedSolution.add(0.0);
        }
        for (int i = 0; i < m; i++) {
            if (basis.get(i) < n) {
                derivedSolution.set(basis.get(i), d.get(i).get(n + m));
            }
        }
        return derivedSolution;
    }

    // Function that calculates the value of objective function, by multiplying x
    // and c columns
    private static Double calcObjectiveValue(List<Double> c, List<Double> x) {
        double obj = 0;
        for (int j = 0; j < n; j++) {
            obj += c.get(j) * x.get(j);
        }
        if (obj < -eps) {
            throw new ArithmeticException("Method cannot be used on provided inputs!");
        }
        return obj;
    }

    /**
     * Solution of simplex method.
     *
     * @param c   the vector of coefficients of objective function
     * @param a   the matrix of coefficients of constraints
     * @param b   the right side of constraints
     * @param eps the approximation accuracy
     * @return the simplex result
     */
    public static SimplexResult solve(List<Double> c, List<List<Double>> a, List<Double> b, double eps) {
        initializeFor(c, a, b, eps); // initialisation
        iterate(); // proceed with solution
        List<Double> x = deriveSolution(); // derivation of solution
        double obj = calcObjectiveValue(c, x); // derivation of objective function
        return new SimplexResult(obj, x); // print the result
    }
}
