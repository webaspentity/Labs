package com.evseev;

/**
 * Класс, представляющий комплексное число
 */
public class Complex {
    /**
     * Действительная часть комплексного числа
     */
    private double re;
    /**Мнимая часть комплексного числа*/
    private double im;

    /**
     * Конструктор инициализации
     */
    public Complex (double x, double y) {
        re = x;
        im = y;
    }

    /**
     * Конструктор по умолчанию
     */
    public Complex() {
        this(0.0, 0.0);
    }

    /**
     * Возвращает квадрат комплексного числа
     */
    public Complex square() {
        return new Complex(re * re - im * im, 2 * re * im);
    }

    /**
     * Возвращает квадрат модуля комплексного числа
     */
    public double moduleSquare() {
        return re * re + im * im;
    }

    /**
     * Возвращает сумму двух комплексных чисел
     */
    public Complex add(Complex complex) {
        return new Complex(this.re + complex.re, this.im + complex.im);
    }

    /**
     * Возвращает сопряженное комплексное число
     */
    public Complex getConjugate() {
        return new Complex(re, im * -1);
    }

    /**
     * Возвращает комплексное число, которое состоит из модулей
     действительной и мнимой частей
     */
    public Complex abs() {
        double r = this.re >= 0 ? this.re : Math.abs(this.re);
        double i = this.im >= 0 ? this.im : Math.abs(this.im);
        return new Complex(r, i);
    }

    /**
     * Возвращает произведение двух комплексных чисел в виде объекта Object
     */
    public Object multiply(Complex complex) {
        if (isConjugate(complex))
            return this.re * this.re + this.im * this.im;
        else
            return new Complex(this.re * complex.re - this.im * complex.im,
                               this.re * complex.im + this.im * complex.re);
    }

    public String toString() {
        String sign = (im >= 0) ? " + " : " - ";
        return "" + re + sign + Math.abs(im);
    }

    /**
     * Возвращает true, если два комплексных числа являются сопряженными
     */
    private boolean isConjugate(Complex complex) {
        return (this.re == complex.re && this.im == complex.im * -1) ? true : false;
    }

}
