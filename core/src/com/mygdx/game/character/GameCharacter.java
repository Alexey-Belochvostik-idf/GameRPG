package com.mygdx.game.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Weapon;

public abstract class GameCharacter {
    GameScreen gameScreen;
    Texture texture;
    Texture textureHp;
    Vector2 position;
    Vector2 direction;
    Vector2 temp;
    float speed;
    float hp, hpMax;

    float damageEffectTimer;
    float attackTimer;
    Weapon weapon;

    StringBuilder stringHelper;

    TextureRegion[] regions;
    float animationTimer;
    float secondsPerFrame;

    public boolean isAlive() {
        return hp > 0;
    }

    public Vector2 getPosition() {
        return position;
    }

    public abstract void update(float dt);

    public GameCharacter() {
        temp = new Vector2(0, 0);
        stringHelper = new StringBuilder();
    }

    public void render(SpriteBatch batch, BitmapFont font24) {
        if (damageEffectTimer > 0) {
            batch.setColor(1, 1 - damageEffectTimer, 1 - damageEffectTimer, 1);
        }
        int frameIndex = (int) (animationTimer / secondsPerFrame) % regions.length;
        batch.draw(regions[frameIndex], position.x - 40, position.y - 40);
        batch.setColor(1, 1, 1, 1);

        batch.setColor(0, 0, 0, 1);
        batch.draw(textureHp, position.x - 42, position.y + 80 - 42 + (int) (Math.sin(animationTimer * 10) * 15 * damageEffectTimer), 84, 16);
        batch.setColor(1, 0, 0, 1);

        batch.draw(textureHp, position.x - 40, position.y + 80 - 40 + (int) (Math.sin(animationTimer * 10) * 15 * damageEffectTimer), 0, 0, hp / hpMax * 80, 12, 1, 1, 0, 0, 0, 80, 20, false, false);
        batch.setColor(1, 1, 1, 1);

        stringHelper.setLength(0);
        stringHelper.append((int) hp);
        font24.draw(batch, stringHelper, position.x - 40, position.y + 80 - 22 + (int) (Math.sin(animationTimer * 10) * 15 * damageEffectTimer), 80, 1, false);
    }

    public void checkScreenBounds() {
        if (position.x > 1280) {
            position.x = 1280;
        }
        if (position.x < 0) {
            position.x = 0;
        }
        if (position.y > 720) {
            position.y = 720;
        }
        if (position.y < 0) {
            position.y = 0;
        }
    }

    public void takeDamage(float amount) {
        hp -= amount;
        damageEffectTimer += 0.5f;
        if (damageEffectTimer > 1) {
            damageEffectTimer = 1;
        }
    }

    public void moveForward(float dt) {
        if (gameScreen.getMap().isCellPassable(temp.set(position).mulAdd(direction, speed * dt))) {
            position.set(temp);
        } else if (gameScreen.getMap().isCellPassable(temp.set(position).mulAdd(direction, speed * dt).set(temp.x, position.y))) {
            position.set(temp);
        } else if (gameScreen.getMap().isCellPassable(temp.set(position).mulAdd(direction, speed * dt).set(position.x, temp.y))) {
            position.set(temp);
        }
    }
}
