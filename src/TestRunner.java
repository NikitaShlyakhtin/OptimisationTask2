import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestRunner {
    /**
     * The entry point of application.
     * Prints result of passed tests.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(InteriorPointMethodTest.class);
        System.out.println("Total number of tests " + result.getRunCount()); // all number of tests
        System.out.println("Total number of tests failed " + result.getFailureCount()); // number of failed test
    }

}