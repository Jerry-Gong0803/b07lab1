import java.io.File;
import java.io.IOException;

public class Driver {
    public static void main(String[] args) {
        try {
            // Testing polynomial creation from coefficients and exponents
            double[] c1 = {6, -2, 5};
            int[] e1 = {0, 1, 3};
            Polynomial p1 = new Polynomial(c1, e1);

            double[] c2 = {3, -1};
            int[] e2 = {2, 1};
            Polynomial p2 = new Polynomial(c2, e2);

            // Adding polynomials
            Polynomial sum = p1.add(p2);
            System.out.println("Sum evaluated at x=1: " + sum.evaluate(1));

            // Multiplying polynomials
            Polynomial product = p1.multiply(p2);
            System.out.println("Product evaluated at x=1: " + product.evaluate(1));

            // Checking roots
            if (sum.hasRoot(1)) {
                System.out.println("1 is a root of the sum.");
            } else {
                System.out.println("1 is not a root of the sum.");
            }

            // Testing polynomial creation from a file
            Polynomial pFromFile = new Polynomial(new File("polynomial.txt"));
            System.out.println("Polynomial from file evaluated at x=2: " + pFromFile.evaluate(2));

            // Saving polynomial to file
            p1.saveToFile("outputPolynomial.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}