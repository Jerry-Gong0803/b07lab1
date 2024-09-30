import java.io.*;
import java.util.*;

public class Polynomial {
    private double[] coefficients; // Non-zero coefficients
    private int[] exponents; // Corresponding exponents

    // No-argument constructor that sets the polynomial to zero
    public Polynomial() {
        this.coefficients = new double[]{0.0};
        this.exponents = new int[]{0};
    }

    // Constructor that takes arrays for coefficients and exponents
    public Polynomial(double[] coefficients, int[] exponents) {
        this.coefficients = coefficients;
        this.exponents = exponents;
    }

    // Constructor that reads from a file
    public Polynomial(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String polynomial = br.readLine();
        br.close();
        parsePolynomial(polynomial);
    }

    // Parse the polynomial from a string representation
    public void parsePolynomial(String polynomial) {
        List<Double> coefList = new ArrayList<>();
        List<Integer> expList = new ArrayList<>();

        String[] terms = polynomial.split("(?=\\+|-)");
        for (String term : terms) {
            double coef = 1.0;
            int exp = 0;

            if (term.contains("x")) {
                String[] parts = term.split("x");
                coef = parts[0].isEmpty() || parts[0].equals("+") ? 1 : parts[0].equals("-") ? -1 : Double.parseDouble(parts[0]);
                exp = parts.length == 2 && !parts[1].isEmpty() ? Integer.parseInt(parts[1].substring(1)) : 1;
            } else {
                coef = Double.parseDouble(term);
            }

            coefList.add(coef);
            expList.add(exp);
        }

        coefficients = coefList.stream().mapToDouble(Double::doubleValue).toArray();
        exponents = expList.stream().mapToInt(Integer::intValue).toArray();
    }

    // Method to add two polynomials
    public Polynomial add(Polynomial other) {
        Map<Integer, Double> resultMap = new TreeMap<>(Collections.reverseOrder());

        for (int i = 0; i < this.coefficients.length; i++) {
            resultMap.put(this.exponents[i], this.coefficients[i]);
        }

        for (int i = 0; i < other.coefficients.length; i++) {
            resultMap.put(other.exponents[i], resultMap.getOrDefault(other.exponents[i], 0.0) + other.coefficients[i]);
        }

        List<Double> resultCoefficients = new ArrayList<>();
        List<Integer> resultExponents = new ArrayList<>();

        for (Map.Entry<Integer, Double> entry : resultMap.entrySet()) {
            if (entry.getValue() != 0.0) {
                resultCoefficients.add(entry.getValue());
                resultExponents.add(entry.getKey());
            }
        }

        return new Polynomial(resultCoefficients.stream().mapToDouble(Double::doubleValue).toArray(),
                              resultExponents.stream().mapToInt(Integer::intValue).toArray());
    }

    // Method to multiply two polynomials
    public Polynomial multiply(Polynomial other) {
        Map<Integer, Double> resultMap = new TreeMap<>(Collections.reverseOrder());

        for (int i = 0; i < this.coefficients.length; i++) {
            for (int j = 0; j < other.coefficients.length; j++) {
                int expSum = this.exponents[i] + other.exponents[j];
                double coefProduct = this.coefficients[i] * other.coefficients[j];
                resultMap.put(expSum, resultMap.getOrDefault(expSum, 0.0) + coefProduct);
            }
        }

        List<Double> resultCoefficients = new ArrayList<>();
        List<Integer> resultExponents = new ArrayList<>();

        for (Map.Entry<Integer, Double> entry : resultMap.entrySet()) {
            if (entry.getValue() != 0.0) {
                resultCoefficients.add(entry.getValue());
                resultExponents.add(entry.getKey());
            }
        }

        return new Polynomial(resultCoefficients.stream().mapToDouble(Double::doubleValue).toArray(),
                              resultExponents.stream().mapToInt(Integer::intValue).toArray());
    }

    // Method to evaluate the polynomial at a given value of x
    public double evaluate(double x) {
        double result = 0.0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, exponents[i]);
        }
        return result;
    }

    // Method to check if a given value of x is a root of the polynomial
    public boolean hasRoot(double x) {
        return evaluate(x) == 0.0;
    }

    // Method to save the polynomial to a file
    public void saveToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < coefficients.length; i++) {
            if (coefficients[i] != 0) {
                sb.append(coefficients[i] < 0 || sb.length() == 0 ? "" : "+").append(coefficients[i]);
                if (exponents[i] != 0) {
                    sb.append("x");
                    if (exponents[i] != 1) {
                        sb.append("^").append(exponents[i]);
                    }
                }
            }
        }

        writer.write(sb.toString());
        writer.close();
    }
}