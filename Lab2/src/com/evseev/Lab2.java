package com.evseev;

import java.util.Scanner;

public class Lab2 {


    public static void main(String[] args) {
        System.out.println("Enter the coordinates of the three points separated by a space.");
        Scanner in = new Scanner(System.in);
        System.out.print("First point: ");
        Point3d p1 = createPoint(in.nextLine());
        System.out.print("Second point: ");
        Point3d p2 = createPoint(in.nextLine());
        System.out.print("Third point: ");
        Point3d p3 = createPoint(in.nextLine());
        in.close();

        if (p1.equals(p2) || p1.equals(p3) || p2.equals(p3))
            System.out.println("---- It is not possible to build a triangle! ----");
        else {
            System.out.print("Area of the triangle: " );
            System.out.printf("%.2f", computeArea(p1, p2, p3));
        }
    }


    /** Возвращает площадь треугольника, вычисленную по формуле Герона **/
    public static double computeArea(Point3d p1, Point3d p2, Point3d p3) {
        double a = p1.distanceTo(p2);
        double b = p1.distanceTo(p3);
        double c = p2.distanceTo(p3);
        double p = (a + b + c) / 2;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }


    /** Создает точку с координатами, указанными во входной строке */
    public static Point3d createPoint(String coords) {
        Point3d point = new Point3d();
        String[] splitCoords = coords.split(" ");

        if (splitCoords.length == 3) {
            if (isDouble(splitCoords[0]))
                point.setX(Double.parseDouble(splitCoords[0]));
            if (isDouble(splitCoords[1]))
                point.setY(Double.parseDouble(splitCoords[1]));
            if (isDouble(splitCoords[2]))
                point.setZ(Double.parseDouble(splitCoords[2]));
        }
        if (splitCoords.length == 2) {
            if (isDouble(splitCoords[0]))
                point.setX(Double.parseDouble(splitCoords[0]));
            if (isDouble(splitCoords[1]))
                point.setY(Double.parseDouble(splitCoords[1]));
        }
        if (splitCoords.length == 1 && isDouble(splitCoords[0]))
            point.setX(Double.parseDouble(splitCoords[0]));
        return point;
    }


    /** Проверяет, возможно ли преобразование строки в значение типа double */
    private static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException exc) {return false;}
    }


}
