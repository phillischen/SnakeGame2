package com.packt.snake.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Arrays;

public class Food implements Disposable{
    private Texture food;
    private ArrayList<int[]> foodlist = new ArrayList<int[]>();
    private int foodX, foodY;

    public Food (){
        food = new Texture("apple.png");
        placeFood();
    }

    public void placeFood(){ //randomly put food on screen
        if (foodlist.size()<20){ //no food no screen
            do{
                //need to change to unified canvas size
                foodX = 50 + MathUtils.random(Gdx.graphics.getWidth()-50);
                foodY = 50 + MathUtils.random(Gdx.graphics.getHeight()-50);
                //need a method to avoid food on snake!!
                int[] x = {foodX,foodY};
                foodlist.add(x);
                //Gdx.app.log("MYTAG", Arrays.toString(x));
            }while (foodlist.size() < 10);
            //for (int[] xx: foodlist){
            //    Gdx.app.log("MYTAG", Arrays.toString(xx));
            //}
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
