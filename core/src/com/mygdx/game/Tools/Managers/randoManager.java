package com.mygdx.game.Tools.Managers;

import java.math.BigDecimal;

public class randoManager {

    private int baseCount;
    private int currentDigit;

    public randoManager(){
        baseCount = 2;
        currentDigit = 1;
    }

    public void setBaseCounts(int baseCount){
        this.baseCount = baseCount;
        currentDigit = 1;
    }

    public void incrementBaseCount(){
        baseCount++;
        currentDigit=1;
        boolean searching = true;
        double root;
        double temp1;
        int int1;
        while(searching){
            root = Math.sqrt(baseCount);
            int1 = (int) Math.floor(root);
            temp1 = root - int1;
            if(temp1==0){
                // increase baseCount and keep looking
                baseCount++;
            } else {
                // happy with number
                searching = false;
            }
        }
    }

    public double getRandomNo(int digits){

        // maximum of 16 decimal places in double
        // checks if need to go to next basecount
        double root;
        double temp1;
        double int1;
        int margin = 16 - currentDigit - digits;// + 1;
        if(margin<=0){
            incrementBaseCount();
        }

        //System.out.println("baseCount "+baseCount);
        //System.out.println("currentDigit "+currentDigit);
        root = Math.sqrt(baseCount);
        //System.out.println("root "+root);
        temp1 = root*Math.pow(10,(currentDigit-1));
        //System.out.println("temp1 "+temp1);
        int1 = Math.floor(temp1);
        //System.out.println("int1 "+int1);//
        // get rid of integer part
        double temp2 = temp1 - int1;
        //System.out.println("temp2 "+temp2);
        // get digits wanted as integer part
        double temp3 = temp2*Math.pow(10,digits);
        //System.out.println("temp3 "+temp3);
        // keep integer part only
        double int3 = Math.floor(temp3);
        //System.out.println("randomNo "+int3);
        currentDigit += digits;

        return int3;
    }

}
