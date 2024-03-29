package com.mygdx.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Item;
import com.mygdx.game.Weapon;

public class Hero extends GameCharacter {
    private final String name;
    private int coins;
    private int level;
    private int exp;
    private final int[] expTo = {0, 0, 100, 300, 600, 1000, 5000};

    public Hero(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.name = "Alexey";
        this.level = 1;
        this.texture = new Texture("Knight_anim.png");
        this.regions = new TextureRegion(texture).split(80, 80)[0];
        this.textureHp = new Texture("Bar.png");
        this.position = new Vector2(200, 200);
        this.direction = new Vector2(0, 0);
        this.hpMax = 100;
        this.hp = this.hpMax;
        this.speed = 100;
        this.weapon = new Weapon("Spear", 150, 1, 5);
        this.secondsPerFrame = 0.2f;
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font24) {
        stringHelper.setLength(0);
        stringHelper.append("Knight: ").append(name).append('\n')
                .append("Level: ").append(level).append('\n')
                .append("Exp: ").append(exp).append(" / ").append(expTo[level + 1]).append('\n')
                .append("Coins: ").append(coins);
        font24.draw(batch, stringHelper, 20, 700);
    }

    @Override
    public void update(float dt) {
        damageEffectTimer -= dt;
        animationTimer += dt;
        if (damageEffectTimer < 0) {
            damageEffectTimer = 0;
        }

        Monster nearstMonster = null;
        float minDist = Float.MAX_VALUE;
        for (int i = 0; i < gameScreen.getAllMonsters().size(); i++) {
            float dst = gameScreen.getAllMonsters().get(i).getPosition().dst(position);
            if (dst < minDist) {
                minDist = dst;
                nearstMonster = gameScreen.getAllMonsters().get(i);
            }
        }
        if (nearstMonster != null && minDist < weapon.getAttackRadius()) {
            attackTimer += dt;
            if (attackTimer > weapon.getAttackPeriod()) {
                attackTimer = 0;
                nearstMonster.takeDamage(weapon.getDamage());
            }
        }

        direction.set(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction.x = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction.x = -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction.y = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction.y = -1;
        }
        moveForward(dt);
        checkScreenBounds();
    }

    public void killMonster(Monster monster) {
        exp += (int) (monster.hpMax * 5);
        if (exp > expTo[level + 1]) {
            level++;
            exp -= expTo[level];
            hpMax += 10;
            hp = hpMax;
        }
    }
    public void useItem(Item it) {
        switch (it.getType()) {
            case COINS:
                int amount = MathUtils.random(3, 5);
                coins += amount;
                stringHelper.setLength(0);
                stringHelper.append("coins").append("+").append(amount);
                gameScreen.getTextEmitter().setup(it.getPosition().x, it.getPosition().y, stringHelper);
                break;
            case MEDKIT:
                hp += 5;
                stringHelper.setLength(0);
                stringHelper.append("hp").append("+5");
                gameScreen.getTextEmitter().setup(it.getPosition().x, it.getPosition().y, stringHelper);
                if (hp > hpMax) {
                    hp = hpMax;
                }
                break;
        }
        it.deactivate();
    }
}
