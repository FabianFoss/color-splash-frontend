package com.mygdx.game.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Models.Background;
import com.mygdx.game.Models.ErrorDialog;

public abstract class View {

    protected SpriteBatch sb;
    protected Background background;
    protected Stage stage;
    protected ErrorDialog error;

    protected View() {
        stage= new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
        sb = new SpriteBatch();
        background = new Background(new Texture(Gdx.files.internal("background_grey.png")),0,0, 5,true ,true);
    }

    public void setError(String message){
        System.out.println("HELLO ERROR MESSAGE SETTER");
        System.out.println(message);
        error = new ErrorDialog(message);
        error.getDialog().show(stage);
        renderStage();
    }

    protected void handleInput() {
        if(error!=null && error.getButton().isPressed()) {
            error.getDialog().hide();
        }
    };
    public abstract void update(float dt);
    public void render(SpriteBatch sb) {
        sb.begin();
        background.drawGameObject(sb);
    }
    public abstract void dispose();

    public void renderStage() {
        stage.draw();
        stage.act();
    }



}
