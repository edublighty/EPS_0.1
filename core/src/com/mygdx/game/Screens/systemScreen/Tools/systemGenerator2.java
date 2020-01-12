package com.mygdx.game.Screens.systemScreen.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.galScreen.galaxyScreen;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

//import javax.xml.soap.Text;

public class systemGenerator2 {

    private float x;                                                    // current x coordinate
    private float y;                                                    // current y coorindate
    private double Rgal;                                                // distance to centre of galaxy
    private int nP;                                                     // number of planets in system
    private int X;                                                      // constant to determine number of planets in system
    private String Stype;                                               // type of star
    private double Sntype;                                              // number version of star type
    private int[] primes1;                                              // store for primes
    private String[] systemNames;                                       // store for system names

    private double[] starData;                                             // store for star types
    private int score;
    private int starScore;
    /*private int starTemp;
    private int starRad;*/
    private int atmScore;
    private int tempScore;
    private int radScore;
    public float toteSize;

    private double[][] planetProbs;
    private double[][] planetData;
    private String[][] systemData;
    private int[][] systemPositions;
    private int numSystems;

    public systemGenerator2(MyGdxGame game, galaxyScreen screen){
        System.out.println("systGen constructor");
        x = game.galX;
        y = game.galY;
        Rgal = Math.sqrt(Math.pow((double) x,2)+Math.pow((double) y,2));
        X = 40;
        primes1 = new int[1160];
        systemNames = new String[900];
        String sCurrentLine;
        FileHandle handle = Gdx.files.internal("Arrays/1160primes.txt");
        BufferedReader reader = new BufferedReader(handle.reader());

        int i = 0;
        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                String[] arr = sCurrentLine.split("\\s+");
                //System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                primes1[i] = Integer.parseInt(arr[0]);
                System.out.println(primes1[i]);
                i++;

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }

        handle = Gdx.files.internal("Arrays/galNamesSon.txt");
        reader = new BufferedReader(handle.reader());

        i = 0;
        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                String[] arr = sCurrentLine.split("\\s+");
                //System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                systemNames[i] = arr[0];
                System.out.println(systemNames[i]);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }

        handle = Gdx.files.internal("Arrays/systemPositions.txt");
        reader = new BufferedReader(handle.reader());
        int lineNum=0;

        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                lineNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }

        numSystems = lineNum / 2;

        systemPositions = new int[numSystems][2];
        i = 0;
        int j = 0;
        handle = Gdx.files.internal("Arrays/systemPositions.txt");
        reader = new BufferedReader(handle.reader());
        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                String[] arr = sCurrentLine.split("\\s+");
                //System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                System.out.println("number line says "+arr[0]);
                systemPositions[i][j] = Integer.parseInt(arr[0]);
                if(j<1){
                    // still on same line
                    j++;
                } else {
                    // finished line, move to next
                    j=0;
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }

        for(int count=0;count<systemPositions.length;count++){
            System.out.println("system i "+systemPositions[count][0]+" and j "+systemPositions[count][1]);
        }

        //nP = (int) Math.round(X / Rgal + 2);                            // number of planets in system
        planetProbs = new double[10][3];

        systemGen();

        screen.setSystemData(systemData);
    }

    public void systemGen(){
        String alphabets[] = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L"};
        String races[] = new String[]{"TERRAN","PIRATES","ROBOTS","ALIENS","FROGS"};
        systemData = new String[numSystems][250];
        System.out.println("starGen");
        int primeChoice = 0;//(new BigDecimal((Rgal / nP) % 1)).intValueExact();
        String tempDecimals;
        for(int systNo=0; systNo<numSystems;systNo++){
            // Start calculating system variables for each system
            systemData[systNo][0] = systemNames[systNo];                    // system name
            System.out.println("X "+systemPositions[systNo][0]+" Y "+systemPositions[systNo][1]);
            systemData[systNo][1] = Double.toString(systemPositions[systNo][0]); // x-coord in galaxy view - random coordinate for now
            systemData[systNo][2] = Double.toString(systemPositions[systNo][1]); // y-coord in galaxy view - random coordinate for now
            systemData[systNo][3] = Double.toString(Math.sqrt(Math.pow((Double.parseDouble(systemData[systNo][1])-50),2)+Math.pow((Double.parseDouble(systemData[systNo][1])-50),2))).substring(0,6);  // radius from galactic centre
            tempDecimals = (new BigDecimal(Math.sqrt(primes1[primeChoice]) % 1)).toString().substring(2);
            systemData[systNo][4] = Double.toString(200 + Double.parseDouble(tempDecimals.substring(0,2)) - 50);    // star diamter
            systemData[systNo][5] = Double.toString(Double.parseDouble(tempDecimals.substring(3,5))*100);//.substring(1,6);       // star mass
            String starClass = "YELLOW";
            int starTemp=5;
            int starRad=5;
            if(Double.parseDouble(systemData[systNo][4])<225){
                if(Double.parseDouble(systemData[systNo][4])<200){
                    if(Double.parseDouble(systemData[systNo][4])<175){
                        if(Double.parseDouble(systemData[systNo][5])<6667) {
                            if(Double.parseDouble(systemData[systNo][5])<3333){
                                starClass = "DWARF";
                                starTemp = 1;
                                starRad = 1;
                            } else {
                                starClass = "NEUTRON";
                                starTemp = 1;
                                starRad = 9;
                            }
                        } else {
                            starClass = "BLACKHOLE";
                            starTemp = 1;
                            starRad = 8;
                        }
                    } else {
                        if(Double.parseDouble(systemData[systNo][5])<6667) {
                            if(Double.parseDouble(systemData[systNo][5])<3333){
                                starClass = "RED";
                                starTemp = 2;
                                starRad = 3;
                            } else {
                                starClass = "YELLOW";
                                starTemp = 4;
                                starRad = 5;
                            }
                        } else {
                            starClass = "BLUE";
                            starTemp = 7;
                            starRad = 6;
                        }
                    }
                } else {
                    if(Double.parseDouble(systemData[systNo][5])<6667) {
                        if(Double.parseDouble(systemData[systNo][5])<3333){
                            starClass = "PULSAR";
                            starTemp = 3;
                            starRad = 10;
                        } else {
                            starClass = "YELLOW";
                            starTemp = 5;
                            starRad = 5;
                        }
                    } else {
                        starClass = "WHITE";
                        starTemp = 9;
                        starRad = 7;
                    }
                }
            } else {
                if(Double.parseDouble(systemData[systNo][5])<6667) {
                    if(Double.parseDouble(systemData[systNo][5])<3333){
                        // GIANT
                        starClass = "GIANT";
                        starTemp = 6;
                        starRad = 7;
                    } else {
                        // dyson
                        starClass = "DYSON";
                        starTemp = 0;
                        starRad = 0;
                    }
                } else {
                    // BINARY SYSTEM
                    starClass = "BINARY";
                    starTemp = 7;
                    starRad = 7;
                }
            }
            systemData[systNo][6] = starClass;      // star class
            systemData[systNo][7] = Double.toString(starTemp*1000 + Double.parseDouble(tempDecimals.substring(6,7))*1000 - 500);        // base temperature
            systemData[systNo][8] = Double.toString(starRad*1000 + Double.parseDouble(tempDecimals.substring(9,10))*1000 - 500);         // base radiation
            primeChoice++;
            if(primeChoice>primes1.length-1){
                primeChoice = 0;
            }
            tempDecimals = (new BigDecimal(Math.sqrt(primes1[primeChoice]) % 1)).toString().substring(2);
            systemData[systNo][9] = tempDecimals.substring(1,3);    // System stability by percentage
            systemData[systNo][10] = Double.toString(Double.parseDouble(tempDecimals.substring(4,6))*Double.parseDouble(tempDecimals.substring(7,9))*1000);    // system GDP
            String race;
            String specialism;
            int alignment;
            primeChoice++;
            if(primeChoice>primes1.length-1){
                primeChoice = 0;
            }
            tempDecimals = (new BigDecimal(Math.sqrt(primes1[primeChoice]) % 1)).toString().substring(2);
            if(Double.parseDouble(tempDecimals.substring(1,3))<80){
                if(Double.parseDouble(tempDecimals.substring(1,3))<60){
                    if(Double.parseDouble(tempDecimals.substring(1,3))<40){
                        if(Double.parseDouble(tempDecimals.substring(1,3))<20){
                            race = "FROGS";
                            specialism = "MINING";
                            alignment = -1;
                        } else {
                            race = "ALIENS";
                            specialism = "MANUFACTURE";
                            alignment = 4;
                        }
                    } else {
                        race = "ROBOTS";
                        specialism = "UPGRADES";
                        alignment = 0;
                    }
                } else {
                    race = "PIRATES";
                    specialism = "SMUGGLING";
                    alignment = -5;
                }
            } else {
                race = "TERRAN";
                specialism = "MERCANTILE";
                alignment = 2;
            }
            systemData[systNo][11] = race;
            if(Double.parseDouble(systemData[systNo][10])>(Math.pow(60,2)*1000)) { // if gdp is high enoough, allow specialism in system
                systemData[systNo][12] = specialism;
            }
            systemData[systNo][13] = Double.toString(alignment);
            systemData[systNo][9] = Double.toString(Double.parseDouble(systemData[systNo][9]) + alignment*10);      // alteration of System stability due to alignment
            systemData[systNo][14] = Double.toString((int) (50 / Double.parseDouble(systemData[systNo][3]) + 2));                            // number of planets in system
            if(Double.parseDouble(systemData[systNo][14])>10){
                systemData[systNo][14] = Double.toString(10);
            }
            primeChoice++;
            if(primeChoice>primes1.length-1){
                primeChoice = 0;
            }
            tempDecimals = (new BigDecimal(Math.sqrt(primes1[primeChoice]) % 1)).toString().substring(2);
            systemData[systNo][15] = Double.toString(Double.parseDouble(tempDecimals.substring(1,2))*Double.parseDouble(tempDecimals.substring(3,4))*Double.parseDouble(tempDecimals.substring(5,6))*Double.parseDouble(tempDecimals.substring(7,8))*1000);    // system population

            // Now begin planet generation
            int planCols = 16;
            double remGDP = Double.parseDouble(systemData[systNo][10]);
            double remPop = Double.parseDouble(systemData[systNo][15]);
            double systemPop = remPop;
            int nP = (int) Double.parseDouble(systemData[systNo][14]);

            for(int j=0;j<nP;j++){
                System.out.println("systNo "+systNo+" j "+j+" column "+(16+j*planCols));
                systemData[systNo][16+j*planCols] = systemData[systNo][0] + " - "+alphabets[j];       // planet name
                primeChoice++;
                if(primeChoice>primes1.length-1){
                    primeChoice = 0;
                }
                tempDecimals = (new BigDecimal(Math.sqrt(primes1[primeChoice]) % 1)).toString().substring(2);
                String planetType;
                int atmosMod;
                int magField;
                int size;
                if(Double.parseDouble(tempDecimals.substring(1,3))<80){
                    if(Double.parseDouble(tempDecimals.substring(1,3))<30){
                        planetType = "terr1";
                        atmosMod = 5;
                        magField = 2;
                        size = 1;
                    } else {
                        planetType = "terGas1";
                        atmosMod = 2;
                        magField = 3;
                        size = 2;
                    }
                } else {
                    planetType = "gasGi3";
                    atmosMod = 1;
                    magField = 8;
                    size = 4;
                }
                systemData[systNo][17+j*planCols] = planetType;     // planet type - GAS GIANT, TERRESTRIAL OR TERRESTRIAL BUT TOXIC FOR SOME REASON
                systemData[systNo][18+j*planCols] = Double.toString(100*Math.random()).substring(0,6); // x-coord in system view - random coordinate for now
                String temp = Double.toString(100*Math.random()).substring(0,6);
                System.out.println("j "+j);
                planCols=planCols;
                systemData[systNo][19+j*planCols] = Double.toString(100*Math.random()).substring(0,6); // y-coord in system view - random coordinate for now
                systemData[systNo][20+j*planCols] = Double.toString(Math.sqrt(Math.pow((Double.parseDouble(systemData[systNo][18+j*planCols])-50),2)+Math.pow((Double.parseDouble(systemData[systNo][19+j*planCols])-50),2)));
                systemData[systNo][21+j*planCols] = Double.toString((50 + Double.parseDouble(tempDecimals.substring(4,5)) - 40)*size);     // planet diameter
                systemData[systNo][22+j*planCols] = Double.toString(atmosMod);  // atmosphere - to consider later
                systemData[systNo][23+j*planCols] = Double.toString(magField);  // magnetic field
                systemData[systNo][24+j*planCols] = Double.toString(Double.parseDouble(systemData[systNo][7])*atmosMod/Math.pow(Double.parseDouble(systemData[systNo][20+j*planCols]),2));      // planet temp
                systemData[systNo][25+j*planCols] = Double.toString(Double.parseDouble(systemData[systNo][8])*magField/Math.pow(Double.parseDouble(systemData[systNo][20+j*planCols]),2));      // planet rad
                String planetRace = "UNINHABITED";
                if(Double.parseDouble(tempDecimals.substring(6,8)) < 40){
                    // allegiance is same as system
                    planetRace = systemData[systNo][11];
                } else {
                    if(Double.parseDouble(tempDecimals.substring(6,8)) < 65){
                        planetRace = "UNINHABITED";
                    } else {
                        boolean searching = true;
                        int raceCount = 0;
                        while (searching) {
                            if (races[raceCount]==systemData[systNo][11]){
                                raceCount++;
                            } else {
                                planetRace = races[raceCount];
                                searching = false;
                            }
                        }
                    }
                }
                systemData[systNo][26+j*planCols] = planetRace;     // ALLEGIANCE OF PLANET
                if(planetRace==systemData[systNo][11]){
                    systemData[systNo][27+j*planCols] = systemData[systNo][9];              // STABILITY OF PLANET
                } else {
                    systemData[systNo][27+j*planCols] = Double.toString(Double.parseDouble(systemData[systNo][9]) - 50);
                }
                double planetPop = remPop*(Double.parseDouble(tempDecimals.substring(9,11)))/100;
                systemData[systNo][28+j*planCols] = Double.toString(planetPop);
                double planetGDP = remGDP*remPop/systemPop;
                systemData[systNo][29+j*planCols] = Double.toString(planetGDP);
                remGDP -= planetGDP;
                if(planetGDP>500000){
                    systemData[systNo][30+j*planCols] = systemData[systNo][12];
                }
                if(planetGDP>1000000) {
                    systemData[systNo][31 + j*planCols] = Double.toString(1);
                }
            }
        }

        final String FNAME = "systemData.txt";
        ArrayList<String> list_copy = new ArrayList<>();

        list_copy.add ("Line 1");
        list_copy.add ("Line 2");

        String line1 = " ";

        try ( BufferedWriter bw =
                      new BufferedWriter (new FileWriter(FNAME)) )
        {
            for (int i=0;i<systemData.length-1;i++) {
                //System.out.println("systemData line "+i);
                for (int j = 0; j < systemData[0].length-1; j++) {
                    //System.out.println("systemData column "+j);
                    line1 = line1 + String.valueOf(systemData[i][j]);
                    if(String.valueOf(systemData[i][j])==null){

                    } else {
                        bw.write(line1 + "\n");
                    }
                    line1 = " ";
                }
                bw.write("\r\n");
                line1 = " ";
            }


            bw.close ();

        } catch (IOException e) {
            e.printStackTrace ();
        }

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
