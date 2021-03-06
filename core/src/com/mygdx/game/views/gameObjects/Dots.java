package com.mygdx.game.views.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Dots {
    private final List<Integer> colors;
    private final List<GameObject> dots;
    private final Texture lightGreyDot = new Texture(Gdx.files.internal("circle_lightgrey.png"));
    private final Texture darkGreyDot = new Texture(Gdx.files.internal("circle_darkgrey.png"));

    public Dots(List<Integer> colorList) {
        this.colors = colorList;
        this.dots = new ArrayList<>();
        this.makeList();
    }

    public void makeList() {
        int scale = 2;
        double move = 6.8/10.0;
        double screenWidth = Gdx.graphics.getWidth()-30;
        double space = ((screenWidth/8.0)*0.001 - 0.02);
        double width = this.lightGreyDot.getWidth()/1000.0;

        List<GameObject> tempDots = new ArrayList<>();
        for (int i = 0; i < colors.size()/2; i++) {
            double x = i + move;
            double xPos = screenWidth/2000.0 - x * space - width;
            GameObject newDot = new GameObject(this.lightGreyDot, xPos, 0.88, scale, false, false);
            tempDots.add(newDot);
        }

        for (int i = tempDots.size()-1; i >= 0; i--) {
            this.dots.add(tempDots.get(i));
        }

        for (int i = 0; i < colors.size()/2; i++) {
            double x = i + move;
            double xPos = screenWidth/2000.0 + x * space;
            GameObject newDot = new GameObject(this.lightGreyDot, xPos,0.88, scale,false,false);
            this.dots.add(newDot);
        }
        this.setDarkGreyDot(0);

    }

    public void drawDots(SpriteBatch sb) {
        for (GameObject dot : this.dots) {
            dot.drawGameObject(sb);
        }
    }

    public List<GameObject> getDots() {
        return this.dots;
    }

    public void setDarkGreyDot(int index) {
        this.dots.get(index).setImage(darkGreyDot);
    }

}
