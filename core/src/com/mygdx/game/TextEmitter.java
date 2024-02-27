package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextEmitter {
    private final FlyingText[] items;

    public TextEmitter() {
        items = new FlyingText[50];
        for (int i = 0; i < items.length; i++) {
            items[i] = new FlyingText();
        }
    }

    public void render(SpriteBatch batch, BitmapFont font24) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].isActive()) {
                font24.draw(batch, items[i].getText(), items[i].getPosition().x, items[i].getPosition().y);
            }
        }
    }

    public void setup(float x, float y, StringBuilder text) {
        for (int i = 0; i < items.length; i++) {
            if (!items[i].isActive()) {
                items[i].setup(x, y, text);
                break;
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].isActive()) {
                items[i].update(dt);
            }
        }
    }
}
