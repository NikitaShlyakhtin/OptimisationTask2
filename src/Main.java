import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // maximize 2x1 + 3x2 + 4x3
        // subject to:
        // 3x1 + 2x2 + x3 <= 10
        // 2x1 + 5x2 + 3x3 <= 15
        // 4x1 + x2 + 2x3 <= 8
        // x1, x2, x3 >= 0

        List<Double> c = new ArrayList<>(Arrays.asList(2.0, 3.0, 4.0)); // A vector of coefficients of the objective
                                                                        // function
        List<List<Double>> a = Arrays.asList(
                Arrays.asList(3.0, 2.0, 1.0),
                Arrays.asList(2.0, 5.0, 3.0),
                Arrays.asList(4.0, 1.0, 2.0)); // A matrix of coefficients of constraints
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

        List<Double> xInterior1 = InteriorPointMethod.solve(c, a, b, eps, alpha1);
        double objInterior1 = InteriorPointMethod.calculateObjectiveFunction(c, xInterior1);
        System.out
                .println("x* by Interior-Point algorithm (alpha = 0.5): " + InteriorPointMethod.formatList(xInterior1));
        System.out.println(
                "Maximum value of the objective function (alpha = 0.5): " + String.format("%.4f", objInterior1));

        List<Double> xInterior2 = InteriorPointMethod.solve(c, a, b, eps, alpha2);
        double objInterior2 = InteriorPointMethod.calculateObjectiveFunction(c, xInterior2);
        System.out
                .println("x* by Interior-Point algorithm (alpha = 0.9): " + InteriorPointMethod.formatList(xInterior2));
        System.out.println(
                "Maximum value of the objective function (alpha = 0.9): " + String.format("%.4f", objInterior2));
    }
}