package com.shuzhongchen.lab1.utils;

/**
 * Created by shuzhongchen on 3/3/18.
 */

public class CalUtils {

    private static final int N = 24;
    /**
     * @return the solution, or null if there are no solutions.
     */
    public static String getSolution(int a, int b, int c, int d) {
        int[] n = { a, b, c, d };
        char[] o = { '+', '-', '*', '/' };
        for (int w = 0; w < 4; w++) {
            for (int x = 0; x < 4; x++) {
                if (x == w)
                    continue;
                for (int y = 0; y < 4; y++) {
                    if (y == x || y == w)
                        continue;
                    for (int z = 0; z < 4; z++) {
                        if (z == w || z == x || z == y)
                            continue;
                        for (int i = 0; i < 4; i++) {
                            for (int j = 0; j < 4; j++) {
                                for (int k = 0; k < 4; k++) {
                                    String result = eval(n[w], n[x], n[y], n[z], o[i], o[j], o[k]);
                                    if (null != result) {
                                        return result;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static String eval(int a, int b, int c, int d, char x, char y, char z) {
        try {
            if (bingo(eval(eval(eval(a, x, b), y, c), z, d))) {
                return "((" + a + x + b + ")" + y + c + ")" + z + d;
            }
            if (bingo(eval(eval(a, x, eval(b, y, c)), z, d))) {
                return "(" + a + x + "(" + b + y + c + "))" + z + d;
            }
            if (bingo(eval(a, x, eval(eval(b, y, c), z, d)))) {
                return "" + a + x + "((" + b + y + c + ")" + z + d + ")";
            }
            if (bingo(eval(a, x, eval(b, y, eval(c, z, d))))) {
                return "" + a + x + "(" + b + y + "(" + c + z + d + ")" + ")";
            }
            if (bingo(eval(eval(a, x, b), y, eval(c, z, d)))) {
                return "((" + a + x + b + ")" + y + "(" + c + z + d + "))";
            }
        } catch (Throwable t) {
        }
        return null;
    }

    private static boolean bingo(double x) {
        return Math.abs(x - N) < 0.0000001;
    }

    private static double eval(double a, char x, double b) {
        switch (x) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            default:
                return a / b;
        }
    }
}
