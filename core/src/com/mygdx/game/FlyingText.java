package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class FlyingText {

    private final Vector2 position;
    private final Vector2 velocity;
    private final StringBuilder text;
    private float time;
    private final float timeMax;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public StringBuilder getText() {
        return text;
    }

    public FlyingText() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.text = new StringBuilder();
        this.timeMax = 5;
        this.time = 0;
        this.active = false;
    }

    public void setup(float x, float y, StringBuilder text) {
        this.position.set(x, y);
        this.velocity.set(10, 40);
        this.text.setLength(0);
        this.text.append(text);
        this.time = 0;
        this.active = true;
    }

    public void update(float dt) {
        time += dt;
        position.mulAdd(velocity, dt);
        if (time > timeMax) {
            deactivate();
        }

    }
}
