package com.mygdx.game.Screens.systemScreen.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.IOException;

public class testPlanetCreator {

    public testPlanetCreator(){

        int earthMass = 1;
        int sunMass = earthMass*333000;
        double rDMass = sunMass*5/100;
        int groundP = 1;
        int groundT = 25;

        double allPlanetsMass = sunMass/100;

        int nPlanets = 2;//(int) Math.random()*10;
        int nElements = 45;

        String[] elements = new String[nElements];
        double[] elementsAbund = new double[nElements];

        String sCurrentLine;
        FileHandle handle = Gdx.files.internal("elements.txt");
        BufferedReader reader = new BufferedReader(handle.reader());

        int iLine = 0;
        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                if(iLine<nElements) {
                    String[] arr = sCurrentLine.split("\\s+");
                    //System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                    elements[iLine] = (arr[0]);
                    //System.out.println("element "+elements[iLine]);
                    Double tempAbundD = Double.parseDouble(arr[1]);
                    elementsAbund[iLine] = tempAbundD;
                    iLine++;
                    //System.out.println(" | abundance: "+tempAbund+" as double "+tempAbundD+" again "+elements[iLine-1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }

        double[][] planetElements = new double[1][nElements];
        double totalMassPercent = 10000;
        double remMassPercent = totalMassPercent;
        int eCount=0;

        for(int i=0;i<nElements;i++){
            planetElements[0][i] = 0;
        }

        while(remMassPercent>10){
            // while above 0.1%
            System.out.println("eCount "+eCount);
            double tempMassPercent = Math.random()*remMassPercent/1000+1*Math.random()*elementsAbund[eCount];
            planetElements[0][eCount] += tempMassPercent;
            remMassPercent -= tempMassPercent*100;
            eCount++;
            if(eCount>nElements-1){
                eCount = 0;
            }
        }

        double tempDoub = 0;

        for(int i=0;i<nElements;i++){
            //System.out.println("ELEMENT: "+elements[i]+" | MASS: "+planetElements[0][i]);
            tempDoub = tempDoub + planetElements[0][i];
        }

        System.out.println("total mass % = "+tempDoub);

        eCount = 0;

        while(tempDoub>100.1){
            double massRemoval = Math.random()*100/1000;
            planetElements[0][eCount] -= massRemoval;
            double tempDoub2 = planetElements[0][eCount];
            if(planetElements[0][eCount]<0){
                planetElements[0][eCount] = 0;
                tempDoub = tempDoub - massRemoval - tempDoub2;
            } else {
                tempDoub -= massRemoval;
            }
            eCount++;
            if(eCount>nElements-1){
                eCount=0;
            }
        }

        tempDoub = 0;

        for(int i=0;i<nElements;i++){
            System.out.println("ELEMENT: "+elements[i]+" | MASS: "+planetElements[0][i]);
            tempDoub = tempDoub + planetElements[0][i];
        }

        System.out.println("total mass % again = "+tempDoub);

        while(tempDoub<99.9){
            double massRemoval = Math.random()*100/1000;
            planetElements[0][eCount] += massRemoval;
            double tempDoub2 = planetElements[0][eCount];
            if(planetElements[0][eCount]<0){
                planetElements[0][eCount] = 0;
                tempDoub = tempDoub + massRemoval + tempDoub2;
            } else {
                tempDoub += massRemoval;
            }
            eCount++;
            if(eCount>nElements-1){
                eCount=0;
            }
        }

        tempDoub = 0;

        for(int i=0;i<nElements;i++){
            System.out.println("ELEMENT: "+elements[i]+" | MASS: "+planetElements[0][i]);
            tempDoub = tempDoub + planetElements[0][i];
        }

        System.out.println("total mass % again = "+tempDoub);

        int nCompoundsPref = 45;
        int nCompoundsSuff = 11;
        int nCompounds = nCompoundsPref*nCompoundsSuff;

        String[] compoundsPref = new String[nCompoundsPref];
        String[] compoundsSuff = new String[nCompoundsSuff];
        String[] compounds = new String[nCompounds];
        double[][] compoundBools = new double[2][nCompounds];
        double[] compoundsAbund = new double[nCompounds];
        String[] compoundStates = new String[nCompounds];

        handle = Gdx.files.internal("compunds1.txt");
        reader = new BufferedReader(handle.reader());

        iLine = 0;
        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                if(iLine<nCompoundsPref) {
                    String[] arr = sCurrentLine.split("\\s+");
                    //System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                    compoundsPref[iLine] = (arr[0]);
                    iLine++;
                    //System.out.println(" | abundance: "+tempAbund+" as double "+tempAbundD+" again "+elements[iLine-1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }

        handle = Gdx.files.internal("compunds2.txt");
        reader = new BufferedReader(handle.reader());

        iLine = 0;
        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                if(iLine<nCompoundsSuff) {
                    String[] arr = sCurrentLine.split("\\s+");
                    //System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                    compoundsSuff[iLine] = (arr[0]);
                    iLine++;
                    //System.out.println(" | abundance: "+tempAbund+" as double "+tempAbundD+" again "+elements[iLine-1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }

        handle = Gdx.files.internal("compunds3.txt");
        reader = new BufferedReader(handle.reader());

        iLine = 0;
        eCount = 0;
        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                if(iLine<nCompounds) {
                    String[] arr = sCurrentLine.split("\\s+");
                    System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                    for(int i2 = 0; i2 < nCompoundsSuff; i2++){
                        int tempInt = iLine+i2;
                        System.out.println("the boolean is "+arr[i2]+" also known as "+Double.parseDouble(arr[i2])+" and at "+tempInt);
                        compoundBools[0][tempInt] = eCount;
                        compoundBools[1][tempInt] = Double.parseDouble(arr[i2]);
                    }
                    eCount++;
                    iLine += nCompoundsSuff;
                    //iLine++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }

        iLine = 0;
        for(int i=0;i<nCompoundsPref;i++){
            for(int j=0;j<nCompoundsSuff;j++){
                compounds[iLine] = compoundsPref[i] + " " + compoundsSuff[j];
                System.out.println("example of compound "+compounds[iLine]+" and boolean "+compoundBools[1][iLine]);
                iLine++;
            }
        }

        for(int i=0;i<nCompounds;i++){
            if(compoundBools[1][i]>0){
                // the combination is allowed
                System.out.println(""+(int) compoundBools[0][i]);
                compoundsAbund[i] = elementsAbund[(int) compoundBools[0][i]]*Math.random();
            }
        }

        for(int i=0;i<compoundsAbund.length;i++){
            System.out.println("compound "+compounds[i]+" and abundance "+compoundsAbund[i]);
        }

        tempDoub = 0;

        for(int i=0;i<compoundsAbund.length;i++){
            //System.out.println("ELEMENT: "+elements[i]+" | MASS: "+planetElements[0][i]);
            tempDoub = tempDoub + compoundsAbund[i];
        }

        System.out.println("total mass % of compounds = "+tempDoub);

        eCount = 0;

        while(tempDoub>100.1){
            double massRemoval = Math.random()*100/1000;
            compoundsAbund[eCount] -= massRemoval;
            double tempDoub2 = compoundsAbund[eCount];
            if(compoundsAbund[eCount]<0){
                compoundsAbund[eCount] = 0;
                tempDoub = tempDoub - massRemoval - tempDoub2;
            } else {
                tempDoub -= massRemoval;
            }
            eCount++;
            if(eCount>nCompounds-1){
                eCount=0;
            }
        }

        tempDoub = 0;

        for(int i=0;i<compoundsAbund.length;i++){
            //System.out.println("ELEMENT: "+elements[i]+" | MASS: "+planetElements[0][i]);
            tempDoub = tempDoub + compoundsAbund[i];
        }

        System.out.println("total mass % of compounds again = "+tempDoub);

        eCount = 0;

        while(tempDoub<99.9){
            double massRemoval = Math.random()*100/1000;
            compoundsAbund[eCount] += massRemoval;
            double tempDoub2 = compoundsAbund[eCount];
            if(compoundsAbund[eCount]<0){
                compoundsAbund[eCount] = 0;
                tempDoub = tempDoub - massRemoval - tempDoub2;
            } else {
                tempDoub += massRemoval;
            }
            eCount++;
            if(eCount>nCompounds-1){
                eCount=0;
            }
        }

        tempDoub = 0;

        for(int i=0;i<compoundsAbund.length;i++){
            //System.out.println("ELEMENT: "+elements[i]+" | MASS: "+planetElements[0][i]);
            tempDoub = tempDoub + compoundsAbund[i];
        }

        System.out.println("total mass % of compounds last time = "+tempDoub);




/*
        for(int i=0;i<nPlanets;i++){

            double currentPlanetMass;

            if(i<nPlanets){
                currentPlanetMass = Math.random()*65;
                allPlanetsMass -= currentPlanetMass;
            } else {
                currentPlanetMass = allPlanetsMass;
            }

        }*/

    }

}
