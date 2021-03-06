package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.ViewManager;
import com.mygdx.game.views.gameObjects.Button;
import com.mygdx.game.views.gameObjects.GameObject;
import com.mygdx.game.controllers.GameLobbyController;
import com.mygdx.game.models.GameInfo;
import com.mygdx.game.models.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GameLobbyView extends View {

    private final Button cancelButton;
    private Button startButton;
    private final BitmapFont font;
    private final GameLobbyController controller;
    private String diffRounds;

    private final List<GameObject> avatars = new ArrayList<>();
    private final List<Texture> avatarPics = new ArrayList<>(
            Arrays.asList(
                    new Texture(Gdx.files.internal("avatar_orange.png")), 
                    new Texture(Gdx.files.internal("avatar_green.png")),
                    new Texture(Gdx.files.internal("avatar_pink.png")),
                    new Texture(Gdx.files.internal("avatar_purple.png")),
                    new Texture(Gdx.files.internal("empty.png"))
            )
    );

    public GameLobbyView(ViewManager vm, boolean isHost) {
        super();
        controller = new GameLobbyController(vm);

        for (int i = 0; i < 4; i++) {
            avatars.add(new GameObject(avatarPics.get(4), 0.2, 0.6 - 0.12 * i, 1, false, false));
        }

        if (isHost) {
            boolean loading = true;
            while (loading) {
                loading = controller.isLoading();
            }
            for (Player player : controller.getGameInfo().getPlayers()) {
                avatars.get(player.getAvatarIndex()).setImage(avatarPics.get(player.getAvatarIndex()));
            }
            diffRounds = "Difficulty: "+controller.getGameInfo().getDifficulty()+"    Rounds: "+controller.getGameInfo().getRounds();
            startButton = new Button(new Texture("button_start.png"), 0.92, 0.08, 3,false, false, vm);

        }

        cancelButton = new Button(new Texture("button_cancel.png"), 0.08, 0.08, 3,false,false, vm);
        font = new BitmapFont(Gdx.files.internal("bebaskai.fnt"));
    }

    public GameLobbyView(ViewManager vm, boolean isHost, GameInfo gameInfo) {
        this(vm, isHost);
        this.controller.setGameInfo(gameInfo);
        for (Player player : this.controller.getGameInfo().getPlayers()) {
            avatars.get(player.getAvatarIndex()).setImage(avatarPics.get(player.getAvatarIndex()));
        }
        diffRounds = "Difficulty: "+controller.getGameInfo().getDifficulty()+"    Rounds: "+controller.getGameInfo().getRounds();
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            if (this.cancelButton.isObjectClicked()) {
                controller.leaveGame();
            }
            if (this.controller.isHost()) {
                if (this.startButton.isObjectClicked()) {
                    this.controller.startGame();
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        cancelButton.drawGameObject(sb);
        if (controller.isHost()){
            startButton.drawGameObject(sb);
        }
        font.getData().setScale(1);
        font.draw(sb, diffRounds,(float)avatars.get(0).getXPos()-30,(float)avatars.get(0).getYPos()+400);
        font.getData().setScale((float)1.5);
        this.drawPlayers(sb);

        font.draw(sb, "Game pin:", (float) avatars.get(0).getXPos() + 150, (float) avatars.get(0).getYPos() + 750);
        font.getData().setScale(3);
        font.draw(sb,String.valueOf(this.controller.getGameInfo().getGameId()), (float) avatars.get(0).getXPos()+110, (float) avatars.get(0).getYPos()+600);

        sb.end();
        super.renderStage();
    }

    @Override
    public void dispose() {
        super.dispose();
        cancelButton.getImage().dispose();
        if (this.controller.isHost()) {
            startButton.getImage().dispose();
        }
        for (int i = 0; i < this.avatars.size(); i++) {
            this.avatars.get(0).getImage().dispose();
        }
        this.font.dispose();
    }

    private void drawPlayers(SpriteBatch sb) {
        for (int i = 0; i < 4; i++) {
            try {
                for (Player player : controller.getGameInfo().getPlayers()) {
                    avatars.get(player.getAvatarIndex()).setImage(avatarPics.get(player.getAvatarIndex()));
                }
                font.draw(sb, this.controller.getGameInfo().getPlayers().get(i).getName(), (float) avatars.get(i).getXPos() + 250, (float) avatars.get(i).getYPos() + 130);
            } catch (Exception e) {
                avatars.get(i).setImage(avatarPics.get(4));
            }
            avatars.get(i).drawGameObject(sb);
        }
    }
}
