package org.chaostocosmos.net.porta;

public class ComplementTest {

    public static void main(String[] args) {
        int a = 125;
        System.out.println(Integer.toBinaryString(a));        
        int val = a ^ -1;
        System.out.println(val+"   "+Integer.toBinaryString(val));
        int b = 100;
        System.out.println(Integer.toBinaryString(b));
        int f = Long.valueOf("FFFFFFFF", 16).intValue();        
        System.out.println(f);
        String bi = Integer.toHexString(-1);
        System.out.println(bi);
        int bc = (int)(b & (f+1));
        System.out.println(bc);
    }    
}
