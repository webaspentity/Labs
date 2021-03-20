package com.evseev;

/**Определяет, являются ли слова палиндромами*/
public class Palindrome {


    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String s = args[i];
            System.out.print(IsPalindrome(s) + " ");
        }
    }


    /**Возвращает строку, символы которой идут в обратном порядке
    относительно входной строки*/
    public static String ReverseString(String s) {
        StringBuilder sb = new StringBuilder();

        for (int i = s.length()-1; i >= 0; i--)
            sb.append(s.charAt(i));

        return sb.toString();
    }


    /** Возвращает true, если строка читается одинаково
    в прямом и обратном порядке, иначе возвращает false */
    public static boolean IsPalindrome(String s) {

        return s.equals(ReverseString(s));
    }


}

