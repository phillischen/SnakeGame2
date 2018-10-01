package com.packt.snake.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Arrays;

import static sun.misc.Version.print;

public class Food implements Disposable{
    private Texture food;
    private ArrayList<int[]> foodlist = new ArrayList<int[]>();
    private int foodX, foodY;
    private static final int padding = 300;
    private java.util.Random randomX = new java.util.Random(1);
    private java.util.Random randomY = new java.util.Random(1);
    private static final int maxWidth = Gdx.graphics.getWidth() - padding;
    private static final int maxHeigh = Gdx.graphics.getHeight() - padding;

    public Food (){
        food = new Texture("apple.png");
        placeFood();
    }

    public void placeFood(){ //randomly put food on screen
        if (foodlist.size()<20){ //no food no screen
            do{
                //need to change to unified canvas size
//                foodX = randomX.nextInt() + padding;
//                foodY = randomY.nextInt() + padding;
                foodX = padding+randomX.nextInt(maxWidth);
                foodY = padding+randomY.nextInt(maxHeigh);
                System.out.print(foodX+"  ");
                System.out.print(foodY);
                System.out.println();
                //need a method to avoid food on snake!!
                int[] x = {foodX,foodY};
                foodlist.add(x);
            }while (foodlist.size() < 20);
        }
    }


    public ArrayList<int[]> getFoodlist() {
        return foodlist;
    }

    public Texture getFood() {
        return food;
    }

    public void removeFood(int[] headpostion){
        arraylistRemove(foodlist,headpostion);
    }

    private ArrayList<int[]> arraylistRemove(ArrayList<int[]> al, int[] lst){
        for (int[] x: al){
            if (Arrays.equals(x,lst)){
                al.remove(x);
                return al;
            }
        }
        return al;
    }

    @Override
    public void dispose() {
        food.dispose();
    }
}
