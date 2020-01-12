package com.mygdx.game.Screens.galScreen.Tools.dijkstra;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class serialTest implements Serializable {

    public serialTest(){

    }

    public void writeSerial(Object obj){

        System.out.println("Starting serial test out");

        try {
            FileOutputStream fileOut =
                    new FileOutputStream("testSerial.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(obj);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in testSerial.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    public dijkstra readSerial(){
        dijkstra e = null;
        try {
            FileInputStream fileIn = new FileInputStream("testSerial.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (dijkstra) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("dijkistra class not found");
            c.printStackTrace();
        }
        return e;
    }
}
