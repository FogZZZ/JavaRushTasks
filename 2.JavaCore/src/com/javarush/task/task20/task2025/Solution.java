package com.javarush.task.task20.task2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*
Алгоритмы-числа
*/
public class Solution {

    public static long[][] degreeMatrix = new long[10][19];

    static {
        for (int i = 0; i < 10; i++) {
            for (int j = 1; j <= 19; j++) {
                long powered = i;
                for (int k = 0; k < j-1; k++) {
                    powered *= i;
                }

                degreeMatrix[i][j-1] = powered;
            }
        }
    }

    public static long[] getNumbers(long N) {
        long[] result = null;
        List<Long> resultList = new ArrayList<>();
        byte numDigitsOfN = digitsNum(N);

        //Первые 9 чисел (M = 1) точно полходят, добавляем
        for (long i = 1; i <= 9; i++) {
            if (i < N)
                resultList.add(i);
        }

        //Обрабатываем числа с разрядностью 2 и выше
        for (byte M = 2; M <= numDigitsOfN; M++) {
            byte[] digits = new byte[M];
            digits[0] = 1;
            byte pos = 0;

            while ((digits[0] != 9) || (digits[M-1] != 9)) {
                pos = (byte)((pos + 1) % M);

                //Если мы на последней позиции
                if (pos == M-1) {
                    if (digits[M-2] > digits[M-1]) {
                        digits[M-1]++;

                        //printArray(digits);
                        long num = getNumber(digits);
                        if ((num != 0) && (num < N))
                            resultList.add(num);

                        pos = (byte)(pos-1);
                        continue;
                    } else {
                        continue;
                    }
                }

                //Если на любой, кроме последней
                boolean flag = true;
                for (byte i = (byte)(pos+1); i < M; i++) {
                    if (digits[pos] != digits[i]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    digits[pos]++;
                    for (byte i = (byte)(pos+1); i < digits.length; i++) {
                        digits[i] = 0;
                    }

                    //printArray(digits);
                    long num = getNumber(digits);
                    if ((num != 0) && (num < N))
                        resultList.add(num);
                }
            }
        }

        result = new long[resultList.size()];
        for (int i = 0; i < resultList.size(); i++) {
            result[i] = resultList.get(i);
        }

        return result;
    }


    public static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
        }
        System.out.println();
    }


    public static byte digitsNum(long N) {
        long p = 10;

        for (byte i = 1; i < 19; i++) {
            if (N < p) {
                return i;
            }
            p *= 10;
        }

        return (byte)19;
    }


    public static long getNumber(byte[] array) {
        long sum = 0;
        for (byte i = 0; i < array.length; i++) {
            //sum += (long)Math.pow(array[i], array.length);
            /*long powered = array[i];
            for (int j = 0; j < array.length-1; j++) {
                powered *= array[i];
            }
            sum += powered;*/

            sum += degreeMatrix[array[i]][array.length-1];
        }
        long savedSum = sum;
        byte digitsNumOfSum = digitsNum(sum);
        byte[] digitsOfSum = new byte[digitsNumOfSum];
        for (byte i = 0; i < digitsOfSum.length; i++) {
            digitsOfSum[digitsOfSum.length-1-i] = (byte)(sum % 10);
            sum = sum / 10;
        }

        Arrays.sort(digitsOfSum);
        for(byte i = 0; i < digitsOfSum.length / 2; i++)
        {
            byte temp = digitsOfSum[i];
            digitsOfSum[i] = digitsOfSum[digitsOfSum.length - i - 1];
            digitsOfSum[digitsOfSum.length - i - 1] = temp;
        }

        boolean flag = true;
        if (digitsOfSum.length != array.length)
            flag = false;
        else {
            for (byte i = 0; i < digitsOfSum.length; i++) {
                if (digitsOfSum[i] != array[i]) {
                    flag = false;
                    break;
                }
            }
        }
        if (flag) {
            return savedSum;
        }
        return 0;
    }


    public static void main(String[] args) {

        /*System.out.println("Waiting 10 sec before start processing...");
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {

        }*/

        Date start = new Date();
        Runtime runtime = Runtime.getRuntime();
        long memStart = runtime.totalMemory() - runtime.freeMemory();

        long[] result = getNumbers(Long.MAX_VALUE);

        Date stop = new Date();
        long memStop = runtime.totalMemory() - runtime.freeMemory();

        int curDigitsNum = 0;
        int oldDigitsNum = 0;
        for (int i = 0; i < result.length; i++) {
            curDigitsNum = digitsNum(result[i]);
            if (curDigitsNum != oldDigitsNum) {
                System.out.println();
                System.out.println("number of digits = " + curDigitsNum + ":");
                oldDigitsNum = curDigitsNum;
            }
            System.out.println(result[i]);
        }
        System.out.println();

        System.out.println("total number of such numbers: " + result.length);


        for (int i = 0; i < result.length; i++) {
            long a = result[i];
            int digitsNum = digitsNum(a);
            int[] digits = new int[digitsNum];
            for (int j = 0; j < digits.length; j++) {
                digits[digits.length-1-j] = (int)(a % 10);
                a = a / 10;
            }
            long sum = 0;
            for (int j = 0; j < digits.length; j++) {
                //sum += Math.pow(digits[j], digits.length);
                long powered = digits[j];
                for (int k = 0; k < digits.length-1; k++) {
                    powered *= digits[j];
                }
                sum += powered;
            }
            if (sum != result[i])
                System.out.println("This number isn't correct: " + result[i]);
        }

        System.out.println("working time: " + (stop.getTime() - start.getTime()) + " ms");
        System.out.println("Used memory: " + (memStop - memStart));

    }
}
