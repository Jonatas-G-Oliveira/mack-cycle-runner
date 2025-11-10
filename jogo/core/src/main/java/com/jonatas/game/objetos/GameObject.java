package com.jonatas.game.objetos;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject{
    protected Sprite sprite;
    protected Rectangle hitbox;  

    //Construindo um objeto estatico
    public GameObject(Texture textura, float x, float y, float largura, float altura){
        this.sprite = new Sprite(textura);
        this.sprite.setSize(largura, altura);
        this.sprite.setPosition(x, y);

        this.hitbox = new Rectangle(x, y, largura, altura);
    }

    //Construindo um objeto animado
    public GameObject(float x, float y, float width, float height) {
        this.sprite = new Sprite();
        this.sprite.setSize(width, height);
        this.sprite.setPosition(x, y);
        this.hitbox = new Rectangle(x, y, width, height);
    }
    
    //Esse é um contrato que deve ser implementado dentro da classe
    //que herdar essa aqui
    public abstract void update(float dt);

    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }

    public Rectangle getHitbox(){
        hitbox.setPosition(sprite.getX(), sprite.getY());
        return hitbox;
    }

    public void setPosition(float x, float y){
        sprite.setPosition(x, y);
    }

}



//Classe que define propriedades comuns para os objetos