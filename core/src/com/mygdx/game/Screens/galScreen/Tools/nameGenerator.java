package com.mygdx.game.Screens.galScreen.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;

import java.io.BufferedReader;
import java.io.IOException;



public class nameGenerator {

    private String[] consonants;
    private String[] vowels;

    public nameGenerator(){
        String sCurrentLine;
        FileHandle handle = Gdx.files.internal("Arrays/LETTERS2.txt");
        BufferedReader reader = new BufferedReader(handle.reader());
        consonants = new String[21];
        vowels = new String[5];

        int i = 0;
        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                String[] arr = sCurrentLine.split("\\s+");
                //System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                if(i<5){
                    // vowels are first five
                    vowels[i] = arr[0];
                    //System.out.println("vowel "+vowels[i]);
                } else {
                    consonants[i-5] = arr[0];
                    //System.out.println("consonant "+consonants[i-5]);

                }
                //galNames[i] = (arr[2]);
                i++;
                //System.out.println("i is "+i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }

        int wordLength = 8;//(int) Math.ceil(Math.random()*14);
        System.out.println("wordlength "+wordLength);
        double vowelProb = 0.35;
        int multiplier = 1;
        String result[] = new String[100];
        double rando;

        for(int wordNo = 0;wordNo<result.length;wordNo++) {
            result[wordNo] = "";
            for (int letCount = 0; letCount < wordLength; letCount++) {
                System.out.println("counting " + letCount);
                rando = Math.random();
                System.out.println("rando " + rando + " vs " + Math.pow(vowelProb, multiplier));
                if (rando < Math.pow(vowelProb, multiplier)) {
                    // vowel
                    int vowelChoice = (int) (Math.random() * 4);
                    System.out.println("vowelchoice " + vowelChoice);
                    result[wordNo] = "" + result[wordNo] + "" + vowels[vowelChoice];
                    multiplier++;
                } else {
                    int consChoice = (int) (Math.random() * 20);
                    System.out.println("consChoice " + consChoice);
                    result[wordNo] = "" + result[wordNo] + "" + consonants[consChoice];
                    multiplier = 0;
                }
            }
        }

        for(int wordNo = 0;wordNo<result.length;wordNo++) {
            System.out.println(result[wordNo]);
        }

    }
}
