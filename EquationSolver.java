package equationsolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EquationSolver {
    
    private static final String LIMITERS = "(?=[=\\-+])";
    
    private final String equation;
    private final List<Integer> xTags;
    private final List<Integer> cTags;
    private final double sumX;
    private final double sumC;
    private String result;

    private EquationSolver(String equation) {
        this.equation = equation.toLowerCase().replaceAll(" ", "")
                .replaceAll("[a-z]", "x");
        this.xTags = new ArrayList<>();
        this.cTags = new ArrayList<>();
        this.findParams();
        if (cTags.isEmpty()) {cTags.add(0);}
        this.sumX = ListUtils.sum(xTags);
        this.sumC = ListUtils.sum(cTags);
        this.result = "x = ";
        this.evaluate();
    }
    
    public static EquationSolver create(String equation) {
        return new EquationSolver(equation);
    }

    public static void main(String[] args) {
        EquationSolver eq = EquationSolver.create(input());
        System.out.println(eq);
    }
    
    private static String input() {
        System.out.print("Enter the equation you looking for:\n");
        return new Scanner(System.in).nextLine();
    }

    private void findParams() {
        String[] array = equation.split(LIMITERS);
        boolean leftSide = true;
        for (String str : array) {
            if (str.startsWith("=")) {
                str = str.substring(1);
                leftSide = false;
            }
            if (str.endsWith("x")) {
                str = str.substring(0, str.length() - 1) +
                        (str.length() <= 2 && !Character.isDigit(str.charAt(0)) ?
                        "1" : "");
                xTags.add(leftSide ? Integer.parseInt(str) : -Integer.parseInt(str));
            } else {
                cTags.add(leftSide ? -Integer.parseInt(str) : Integer.parseInt(str));
            }
        }
    }

    private void evaluate() {
        if (!(xTags.isEmpty() || sumX == 0)) {
            double res = sumC / sumX;
            if (res == 0) {
                result += "0";
            }
            else if (sumC % sumX == 0) {
                result += String.format("%.0f", res);
            } else {
                result += (res < 0 ? "-" : "") + 
                        String.format("%.0f", Math.abs(sumC)) + "/" + 
                        String.format("%.0f", Math.abs(sumX));
            }
        } else {
            result = "No real solution for this equation!";
        }
    }

    @Override
    public String toString() {
        return equation + "\n" +
                ListUtils.toString(xTags, "x") + "=" +
                ListUtils.toString(cTags, "") + "\n" +
                (sumX == 1 ? "" : sumX == -1 ? "-" : String.format("%.0f", sumX)) + "x=" + 
                String.format("%.0f", sumC) + "\n" +
                result;
    }

    private static class ListUtils {
        private static double sum(List<Integer> list) {
            return  list.isEmpty() ? Double.NaN : 
                    list.stream().mapToInt(Integer::intValue).sum();
        }

        private static String toString(List<Integer> list, String str) {
            Integer elem = list.get(0);
            String result = list.isEmpty() ? "NaN" : 
                    (elem == 1 ? "" : elem == -1 ? "-" : elem) + str;
            for (Integer i = 1; i < list.size(); i++) {
                elem = list.get(i);
                result += (Math.abs(elem) != 1 ?
                        (elem > 0 ? "+" + elem : elem) :
                        (elem == 1 ? "+" : "-")) + str;
            }
            return result;
        }
    }
}