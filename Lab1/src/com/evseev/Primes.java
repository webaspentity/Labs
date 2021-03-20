package com.evseev;

public class Primes
{


    public static void main(String[] args) {
        for (int i = 2; i < 100; i++)
            if (IsPrime(i)) System.out.print(i + "; ");
    }


    public static boolean IsPrime(int n) {
        for (int i = 2; i <= Math.sqrt(n); i++)
            if (n % i == 0) return false;

        return true;
    }


}
