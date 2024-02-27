package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.character.GameCharacter;
import com.mygdx.game.character.Hero;
import com.mygdx.game.character.Monster;

import java.util.*;

public class GameScreen {
    private final SpriteBatch batch;
    private Stage stage;
    private BitmapFont font24;
    private Map map;
    private ItemsEmitter itemsEmitter;
    private TextEmitter textEmitter;
    private Hero hero;
    private List<GameCharacter> allCharacters;
    private List<Monster> allMonsters;
    private Comparator<GameCharacter> drowOrderComparator;
    private boolean paused;
    private float spawnTimer;


    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public Hero getHero() {
        return hero;
    }

    public List<Monster> getAllMonsters() {
        return allMonsters;
    }

    public Map getMap() {
        return map;
    }

    public TextEmitter getTextEmitter() {
        return textEmitter;
    }

    public void create() {
        map = new Map();
        allCharacters = new ArrayList<>();
        allMonsters = new ArrayList<>();
        hero = new Hero(this);
        itemsEmitter = new ItemsEmitter();
        textEmitter = new TextEmitter();
        allCharacters.addAll(Arrays.asList(
                hero,
                new Monster(this),
                new Monster(this),
                new Monster(this),
                new Monster(this),
                new Monster(this),
                new Monster(this)
        ));
        for (int i = 0; i < allCharacters.size(); i++) {
            if (allCharacters.get(i) instanceof Monster) {
                allMonsters.add(((Monster) allCharacters.get(i)));
            }
        }
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        stage = new Stage();

        Skin skin = new Skin();
        skin.add("simpleButton", new Texture("SimpleButton.png"));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;

        TextButton pauseButton = new TextButton("Pause", textButtonStyle);
        TextButton exitButton = new TextButton("Exit", textButtonStyle);
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                paused = !paused;
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });
        Group menuGroup = new Group();
        menuGroup.addActor(pauseButton);
        menuGroup.addActor(exitButton);
        exitButton.setPosition(150,0);
        menuGroup.setPosition(1000,680);
        stage.addActor(menuGroup);
        Gdx.input.setInputProcessor(stage);

        drowOrderComparator = (o1, o2) -> (int) (o2.getPosition().y - o1.getPosition().y);
    }

    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        map.render(batch);
        allCharacters.sort(drowOrderComparator);
        for (int i = 0; i < allCharacters.size(); i++) {
            allCharacters.get(i).render(batch, font24);
        }
        itemsEmitter.render(batch);
        textEmitter.render(batch, font24);
        hero.renderHUD(batch, font24);
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        if (!paused) {
            spawnTimer += dt;
            if (spawnTimer > 3){
                Monster monster = new Monster(this);
                allCharacters.add(monster);
                allMonsters.add(monster);
                spawnTimer = 0;
            }
            for (int i = 0; i < allCharacters.size(); i++) {
                allCharacters.get(i).update(dt);
            }
            for (int i = 0; i < allMonsters.size(); i++) {
                Monster currentMonster = allMonsters.get(i);
                if (!currentMonster.isAlive()) {
                    allMonsters.remove(currentMonster);
                    allCharacters.remove(currentMonster);
                    itemsEmitter.generateRandomItem(currentMonster.getPosition().x, currentMonster.getPosition().y, 5, 0.6f);
                    hero.killMonster(currentMonster);
                }
            }
            for (int i = 0; i < itemsEmitter.getItems().length; i++) {
                Item it = itemsEmitter.getItems()[i];
                if (it.isActive()) {
                    float dst = hero.getPosition().dst(it.getPosition());
                    if (dst < 24) {
                        hero.useItem(it);
                    }
                }
            }
            textEmitter.update(dt);
            itemsEmitter.update(dt);
        }
        stage.act(dt);
    }
}
