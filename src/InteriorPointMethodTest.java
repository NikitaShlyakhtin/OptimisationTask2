import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The type Interior point method test.
 */
public class InteriorPointMethodTest {
    /**
     * Test three variable problem.
     */
    @Test
    public void testThreeVariableProblem() {
        List<Double> c = Arrays.asList(2.0, 3.0, 4.0);
        List<List<Double>> a = Arrays.asList(
                Arrays.asList(3.0, 2.0, 1.0),
                Arrays.asList(2.0, 5.0, 3.0),
                Arrays.asList(4.0, 1.0, 2.0)
        );
        List<Double> b = new ArrayList<>(Arrays.asList(10.0, 15.0, 8.0)); // A vector of right-hand sides of constraints
        double eps = 0.0001;
        double alpha = 0.5;
        List<Double> xInterior = InteriorPointMethod.solveInteriorPointMethod(c, a, b, eps, alpha);
        double objInterior = InteriorPointMethod.calculateObjectiveFunction(c, xInterior);
        List<Double> expectedResult = Arrays.asList(0.0000, 0.8571, 3.5714);

        Assert.assertEquals(objInterior, 16.8571, eps); // comparing expected and received results
        Assert.assertNotNull(xInterior);
        Assert.assertEquals(expectedResult.size(), xInterior.size());
        for (int i = 0; i < expectedResult.size(); i ++) {
            Assert.assertEquals(expectedResult.get(i), xInterior.get(i), eps);
        }
    }

    /**
     * Test two variable problem.
     */
    @Test
    public void testTwoVariableProblem() {
        List<Double> c = Arrays.asList(-2.0, -3.0);
        List<List<Double>> a = Arrays.asList(
                Arrays.asList(1.0, 1.0),
                Arrays.asList(2.0, 3.0)
        );
        List<Double> b = Arrays.asList(4.0, 9.0); // A vector of right-hand sides of constraints
        double eps = 0.0001;
        double alpha = 0.4;
        List<Double> xInterior = InteriorPointMethod.solveInteriorPointMethod(c, a, b, eps, alpha);
        double objInterior = InteriorPointMethod.calculateObjectiveFunction(c, xInterior);
        List<Double> expectedResult = Arrays.asList(0.0, 0.0);

        Assert.assertEquals(objInterior, 0.0, eps); // comparing expected and received results
        Assert.assertNotNull(xInterior);
        Assert.assertEquals(expectedResult.size(), xInterior.size());
        for (int i = 0; i < expectedResult.size(); i ++) {
            Assert.assertEquals(expectedResult.get(i), xInterior.get(i), eps);
        }
    }

    /**
     * Test four variable problem.
     */
    @Test
    public void testFourVariableProblem() {
        List<Double> c = Arrays.asList(2.0, 3.0, -1.0, -4.0);
        List<List<Double>> a = Arrays.asList(
                Arrays.asList(1.0, 1.0, 2.0, 0.0),
                Arrays.asList(2.0, 3.0, 1.0, 0.0),
                Arrays.asList(1.0, 0.0, 0.0, 1.0)
        );
        List<Double> b = Arrays.asList(4.0, 9.0, 5.0); // A vector of right-hand sides of constraints
        double eps = 0.0001;
        double alpha = 0.4;
        List<Double> xInterior = InteriorPointMethod.solveInteriorPointMethod(c, a, b, eps, alpha);
        double objInterior = InteriorPointMethod.calculateObjectiveFunction(c, xInterior);
        List<Double> expectedResult = Arrays.asList(0.0, 3.0, 0.0, 0.0);

        Assert.assertEquals(objInterior, 9.0, eps); // comparing expected and received results
        Assert.assertNotNull(xInterior);
        Assert.assertEquals(expectedResult.size(), xInterior.size());
        for (int i = 0; i < expectedResult.size(); i ++) {
            Assert.assertEquals(expectedResult.get(i), xInterior.get(i), eps);
        }
    }
}
