/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consumer.producer;

import java.util.*;

/**
 *
 * @author admin
 */
public class ConsumerProducer {
    
    private final List<Integer> data = new ArrayList<Integer>();
    private final static int MAX_LENGTH = 10;
    public Boolean probe = false;

    public void produce() throws Exception {
        while(!probe) {
            synchronized(this) {
                while(data.size() >= MAX_LENGTH)
                    this.wait();
                Random a = new Random();
                int num = a.nextInt();

                data.add(num);
                System.out.println("Adding " + num);

                this.notify();
            }
        }
    }

    public void consume() throws Exception {
        while(!probe) {
            synchronized(this) {            
                while(data.isEmpty())
                    this.wait();
                Integer a = data.remove(0);
                System.out.println("Removing " + a);

                this.notify();
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ConsumerProducer con = new ConsumerProducer();
        
        try {
            Thread a = new Thread(new Runnable() {
                public void run() {
                    try {
                        con.produce();
                    } catch(Exception e) {
                        System.out.println(e);
                    }
                }
            });

            Thread b = new Thread(new Runnable() {
                public void run() {
                    try {
                        con.consume();
                    } catch(Exception e) {
                        System.out.println(e);
                    }
                }
            });
            
            a.start();
            b.start();
           
            Thread.sleep(10000);
            con.probe = true;

            a.join();
            b.join();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    
}
