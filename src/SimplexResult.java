import java.util.List;

/**
 * The type Simplex result.
 */
public class SimplexResult {
    private final double obj;
    private final List<Double> x;

    /**
     * Instantiates a new Simplex result.
     *
     * @param obj the maximum / minimum value of objective function
     * @param x   the x
     */
    public SimplexResult(double obj, List<Double> x) {
        this.obj = obj;
        this.x = x;
    }

    /**
     * Gets the value of obj.
     *
     * @return the obj
     */
    public double getObj() {
        return obj;
    }

    /**
     * Gets the value of x.
     *
     * @return the x
     */
    public List<Double> getX() {
        return x;
    }

    public String toString() {
        return "x* = " + x + "\nobj = " + obj;
    }
}