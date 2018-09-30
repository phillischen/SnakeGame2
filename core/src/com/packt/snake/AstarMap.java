package com.packt.snake;

import com.badlogic.gdx.math.MathUtils;

public class AstarMap {
    private int x;
    private int y;
    private double F;
    private double G;
    private double H;
    private AstarMap previous;

    public AstarMap(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setH(AstarMap end){
        int deltaX = end.getX() - x;
        int deltaY = end.getY() - y;
        this.H = Math.sqrt(deltaX* deltaX + deltaY * deltaY);
    }

    public double getH(){
        return H;
    }

    public void setG(double G){
        this.G = G;
    }

    public double getG(){
        return G;
    }

    public void setF(double F){
        this.F = F;
    }

    public void setF(){
        this.F = this.G + this.H;
    }

    public double getF(){
        return F;
    }

    public void setPrevious(AstarMap center){
        this.previous = previous;
    }

    public AstarMap getPrevious(){
        return previous;
    }

}
