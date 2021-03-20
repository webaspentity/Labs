package com.evseev;

import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {

    /**Максимальное число итераций*/
    public static final int MAX_ITERATIONS = 2000;

    /**Определяет область комплексной плоскости для фрактала*/
    @Override
    public void getInitialRange (Rectangle2D.Double range) {
        range.setRect(-2.0, -1.5, 3.0, 3.0);
    }

    /**Возвращает количество итераций для заданного комплексного числа*/
    @Override
    public int numIterations(double x, double y) {
        Complex c = new Complex(x, y);
        Complex z = new Complex();
        int iterations = 0;

        while (z.moduleSquare() < 4) {
            z = z.square().add(c);
            iterations++;
            if (iterations == MAX_ITERATIONS) return -1;
        }
        return iterations;
    }

    public String toString() {
        return "Mandelbrot";
    }
}
