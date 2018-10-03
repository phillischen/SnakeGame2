package com.packt.snake.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.packt.snake.MyAssetsManager;
import com.packt.snake.SnakeGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Food implements Disposable{
    private Texture food,background;
    private ArrayList<int[]> foodlist = new ArrayList<int[]>();
    private static final int padding = 500;
    private java.util.Random randomX = new java.util.Random(250);
    private java.util.Random randomY = new java.util.Random(3800);
    private int maxWidth;
    private int maxHeigh;
    private SnakeGame mygame;
    private MyAssetsManager myAm;

    public Food (SnakeGame game){
        mygame = game;
        myAm = mygame.getAm();
        myAm.loadMap();
        myAm.manager.finishLoading();
        background = myAm.manager.get(myAm.MAP1);
        maxWidth = background.getWidth() - 2*padding;
        maxHeigh = background.getHeight() - 2*padding;
        food = new Texture("apple.png");
        System.out.println("x: "+maxWidth);
        System.out.println("y: "+maxHeigh);
        placeFood();
    }

    public void placeFood(){ //randomly put food on screen
        if (foodlist.size()<50){ //no food no screen
            do{
                //need to change to unified canvas size
                int foodX = padding+randomX.nextInt(maxWidth);
                int foodY = padding+randomY.nextInt(maxHeigh);
                //need a method to avoid food on snake!!
                int[] x = {foodX,foodY};
                foodlist.add(x);
            }while (foodlist.size() < 20);
        }
    }

    public void placeFood(ArrayList<Array<Integer>> deadSnake){
        for(Array<Integer> pos : deadSnake){
            Random randomX = new Random();
            Random randomY = new Random();
            int[] foodPos = {pos.get(0)+randomX.nextInt(25),pos.get(1)+randomY.nextInt(25)};
            foodlist.add(foodPos);
        }
    }

    public void placeFoodAt(int x, int y){
        int[] pos = {x,y};
        foodlist.add(pos);
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
