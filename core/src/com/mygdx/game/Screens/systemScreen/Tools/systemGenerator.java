package com.mygdx.game.Screens.systemScreen.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.MyGdxGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

//import javax.xml.soap.Text;

public class systemGenerator {

    private float x;                                                    // current x coordinate
    private float y;                                                    // current y coorindate
    private double Rgal;                                                // distance to centre of galaxy
    private int nP;                                                     // number of planets in system
    private int X;                                                      // constant to determine number of planets in system
    private String Stype;                                               // type of star
    private double Sntype;                                              // number version of star type
    private int[] primes1;                                              // store for primes

    private double[] starData;                                             // store for star types
    private int score;
    private int starScore;
    private int starTemp;
    private int starRad;
    private int atmScore;
    private int tempScore;
    private int radScore;
    public float toteSize;

    private double[][] planetProbs;
    private double[][] planetData;

    public systemGenerator(MyGdxGame game){
        System.out.println("systGen constructor");
        x = game.galX;
        y = game.galY;
        Rgal = Math.sqrt(Math.pow((double) x,2)+Math.pow((double) y,2));
        X = 40;
        primes1 = new int[]{547,557,563,569,571,577,587,593,599,601};
        nP = (int) Math.round(X / Rgal + 2);                            // number of planets in system
        planetProbs = new double[10][3];
    }

    public void starGen(){
        System.out.println("starGen");
        //int primeChoice = (new BigDecimal((Rgal / nP) % 1)).intValueExact();
        String inter = Double.toString(Math.pow(Rgal, 1 / nP));                      // number to choose prime for star gen
        String num = ""+inter.charAt(2);
        int primeChoice = Integer.parseInt(num);//(new BigDecimal(temp3 % 1).intValueExact());
        double prime = primes1[primeChoice];
        prime = (new BigDecimal(Math.sqrt(prime) % 2)).doubleValue();
        starData = new double[4];
        if(prime < 100){
            if(prime < 99){
                if(prime < 98){
                    if(prime < 97){
                        if(prime < 96){
                            if(prime < 91){
                                if(prime < 86){
                                    if(prime < 60){
                                        if(prime < 40){
                                            if(prime < 20){
                                                Stype = "red";
                                                Sntype = 0;
                                                starScore = 3;
                                                starTemp = 2;
                                                starRad = 3;
                                            } else {
                                                Stype = "yellow";
                                                Sntype = 1;
                                                starScore = 5;
                                                starTemp = 4;
                                                starRad = 4;
                                            }
                                        } else {
                                            Stype = "blue";
                                            Sntype = 2;
                                            starScore = 7;
                                            starTemp = 6;
                                            starRad = 5;
                                        }
                                    } else {
                                        Stype = "white";
                                        Sntype = 3;
                                        starScore = 9;
                                        starTemp = 8;
                                        starRad = 6;
                                    }
                                } else {
                                    Stype = "giant";
                                    Sntype = 4;
                                    starScore = 1;
                                    starTemp = 6;
                                    starRad = 7;
                                }
                            } else {
                                Stype = "dwarf";
                                Sntype = 5;
                                starScore = 0;
                                starTemp = 1;
                                starRad = 3;
                            }
                        } else {
                            Stype = "binary";
                            Sntype = 6;
                            starScore = 10;
                            starTemp = 9;
                            starRad = 8;
                        }
                    } else {
                        Stype = "pulsar";
                        Sntype = 7;
                        starScore = 1;
                        starTemp = 1;
                        starRad = 9;
                    }
                } else {
                    Stype = "neutron";
                    Sntype = 8;
                    starScore = 1;
                    starTemp = 1;
                    starRad = 10;
                }
            } else {
                Stype = "dyson";
                Sntype = 9;
                starScore = 10;
                starTemp = 4;
                starRad = 2;
            }
        }

        starData[0] = Sntype;
        starData[1] = starScore;
        starData[2] = starTemp;
        starData[3] = 750;//starRad;
        //starData[4] = 5;

        // star type has been set

    }

    public void planetGen(){
        System.out.println("planetGen");
        float theta=0;
        // array for number of orbits: type, atmosphere, temperature & radiation
        // type:
        // 0 = terrestrial
        // 1 = gaseous terrestrial
        // 2 = gas giant

        // atmosphere:
        // 0 = danger
        // 1 = need equipment
        // 2 = ok

        // temperature:
        // double value based on above

        // radiaton:
        // double value based on above

        String sCurrentLine;
        FileHandle handle = Gdx.files.internal("Arrays/planetProbs.txt");
        BufferedReader reader = new BufferedReader(handle.reader());

        int i = 0;
        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                String[] arr = sCurrentLine.split("\\s+");
                //System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                planetProbs[i][0] = Integer.parseInt(arr[0]);
                planetProbs[i][1] = Integer.parseInt(arr[1]);
                planetProbs[i][2] = Integer.parseInt(arr[2]);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }

        // Loaded stats for planet generation by orbit
        i=0;
        int j=0;
        boolean scanning = true;
        //BigDecimal temp1 = new BigDecimal(Rgal).setScale(10, RoundingMode.HALF_DOWN);
        //BigDecimal temp2 = new BigDecimal(nP).setScale(10, RoundingMode.HALF_DOWN);
        String inter = Double.toString(Rgal / nP);
        String num = ""+inter.charAt(2);

        //BigDecimal temp3 = temp1.divide(temp2);//(new BigDecimal(inter % 1).setScale(10, RoundingMode.HALF_DOWN)).intValueExact();
        int primeChoice = Integer.parseInt(num);//(new BigDecimal(temp3 % 1).intValueExact());

        planetData = new double[10][nP];
        // 0    - Numeric type
        // 1    - Size
        // 2    - xPosition
        // 3    - yPosition
        // 4    - Atmosphere double
        // 5    - Temperature double
        // 6    - Radiation double

        int orbRad;
        int firstOrbRad = 2000;
        int planSpace=4000;

        while(i<nP){
            System.out.println("i "+i);
            if(primeChoice>9){
                primeChoice = 0;
            }
            double prime = primes1[primeChoice];
            double sqrt = Math.sqrt(prime);
            String decimals = (new BigDecimal(sqrt % 6)).toString();
            String ecimals = decimals.substring(2);
            System.out.println(ecimals);
            System.out.println("sqrt "+sqrt+" bigdec "+decimals);
            int atmChoice = (int) Double.parseDouble(ecimals.substring(1,3));
            int tChoice = (int) Double.parseDouble(ecimals.substring(3,4))-50;
            int rChoice = (int) Double.parseDouble(ecimals.substring(5,6));
            double nPd = (double) nP;
            toteSize = (750*(nP+1)+500)/2;       // position of sun from origin

            while(scanning) {
                System.out.println("i "+i+" and j "+j);
                System.out.println("atmChoice "+atmChoice+" and planetProb "+planetProbs[i][j]+" and toteSize "+toteSize);
                if (atmChoice >= planetProbs[i][j]) {
                    System.out.println("setting planetData");
                    planetData[0][i] = j;                                                           // Type
                    planetData[1][i] = (j+1)*10;                                                   // size
                    orbRad = firstOrbRad+planSpace*i;
                    planetData[2][i] = toteSize + orbRad* MathUtils.sin(theta);// - ((j+1)*100/2);     // xPosition
                    planetData[3][i] = toteSize + orbRad*MathUtils.cos(theta);// - ((j+1)*100/2);      // yPosition - OLD CODE: (Math.pow(-1,i))*((i)*375+750))
                    System.out.println("xPos "+orbRad* MathUtils.sin(theta)+" and yPos "+orbRad*MathUtils.cos(theta));
                    planetData[4][i] = atmChoice;
                    planetData[5][i] = tChoice*(5/(Math.pow(nPd,0.3)));                             // 5 at 1 and tends to 2
                    planetData[6][i] = rChoice*(100/(Math.pow(nPd,0.3)));                           // 5 at 1 and tends to 2
                    planetData[7][i] = theta;
                    planetData[8][i] = orbRad;
                    planetData[9][i] = 1;
                    theta = theta + MathUtils.PI/4;
                    j=0;
                    scanning = false;
                } else {
                    System.out.println("Moving on");
                    j++;
                }
            }
            primeChoice = primeChoice + i;
            scanning = true;
            i++;
        }
        System.out.println("Completed planet generation");
        System.out.println("Example: Planet 1");
        System.out.println("Type "+planetData[0][0]+" size "+planetData[1][0]+" xPos "+planetData[2][0]+" yPos "+planetData[3][0]);
        System.out.println("Example: Planet 5");
        System.out.println("Type "+planetData[0][3]+" size "+planetData[1][3]+" xPos "+planetData[2][3]+" yPos "+planetData[3][3]);
    }

    public void spacingGen(){
        System.out.println("spacingGen");
    }

    public void buildingGen(){
        System.out.println("buildingGen");
    }

    public double[][] getPlanetData(){
        return planetData;
    }

    public double[] getStarData() { return starData; }

}
