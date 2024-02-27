package com.mygdx.game.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Weapon;

public class Monster extends GameCharacter {
    private float moveTimer;
    private final float activityRadius;

    public Monster(GameScreen gameScreen) {
        this.texture = new Texture("Skeleton.png");
        this.regions = new TextureRegion(texture).split(80,80)[0];
        this.textureHp = new Texture("Bar.png");
        this.position = new Vector2(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        while (!gameScreen.getMap().isCellPassable(position)) {
            this.position.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        }
        this.direction = new Vector2(0, 0);
        this.speed = 40;
        this.activityRadius = 200;
        this.gameScreen = gameScreen;
        this.hpMax = 20;
        this.hp = this.hpMax;
        this.weapon = new Weapon("Rusty Sword", 50, 0.8f, 5);
    }


    @Override
    public void update(float dt) {
        damageEffectTimer -= dt;
        animationTimer += dt;
        if (damageEffectTimer < 0) {
            damageEffectTimer = 0;
        }

        float dst = gameScreen.getHero().getPosition().dst(position);
        if (dst < activityRadius) {
            direction.set(gameScreen.getHero().getPosition()).sub(position).nor();
        } else {
            moveTimer -= dt;
            if (moveTimer < 0f) {
                moveTimer = MathUtils.random(1f, 4f);
                direction.set(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
                direction.nor();
            }
        }
        moveForward(dt);

        if (dst < weapon.getAttackRadius()) {
            attackTimer += dt;
            if (attackTimer >= weapon.getAttackPeriod()) {
                attackTimer = 0;
                gameScreen.getHero().takeDamage(weapon.getDamage());
            }
        }
        checkScreenBounds();
    }
}
