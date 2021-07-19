package org.chaostocosmos.net.porta;

import java.util.concurrent.CountDownLatch;

public class ComplementTest {

    /*
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
    */
    static long count = 0;

    public static void main(String[] args) throws InterruptedException {        

        CountDownLatch latch = new CountDownLatch(20000);

        for(int i=0; i<20000; i++) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    count += 1;
                    latch.countDown();
                }
            });
            t.start();
        }
        latch.await();
        System.out.println(count);
    }
}
