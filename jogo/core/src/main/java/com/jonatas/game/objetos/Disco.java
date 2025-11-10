package com.jonatas.game.objetos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Disco extends GameObject{
    private float velocidade;
    private Viewport viewport;
    
    public Disco(Texture textura,float x, float y, float largura, float altura, Viewport viewport){
        super(textura, x, y, largura, altura);
        this.viewport = viewport;
        this.velocidade = 4f;
    }

    @Override
    public void update(float dt){
        sprite.translateX(-velocidade * dt);
        hitbox.setPosition(sprite.getX(), sprite.getY()); //Talvez seja desnecesário
    }


}
