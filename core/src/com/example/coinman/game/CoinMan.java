package com.example.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

import sun.rmi.runtime.Log;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
    Texture background;
	Texture[] man;
	int manState=0;
	int pause=0;
	int score=0;
	float gravity=0.2f;
	float velocity=0;
	int manY=0;
	int gameState=0;
	Rectangle manRectangle;
	Texture dizzy;
	ArrayList<Integer> coinsX=new ArrayList<Integer>();
	ArrayList<Integer> coinYs=new ArrayList<Integer>();
	ArrayList<Rectangle> coinsRectangle=new ArrayList<Rectangle>();
	int coinCount=0;
	Texture coin;
	ArrayList<Integer> bombX=new ArrayList<Integer>();
	ArrayList<Integer> bombY=new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangle=new ArrayList<Rectangle>();
	int bombCount=0;
	Texture bomb;
    Random random;
    BitmapFont bitmapFont;
	@Override
	public void create () {
		batch = new SpriteBatch();
        background=new Texture("bg.png");
        man=new Texture[4];
        man[0]=new Texture("frame1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		coin=new Texture("coin.png");
		bomb=new Texture("bomb.png");
		manY=Gdx.graphics.getHeight() / 2;
		random=new Random();
		bitmapFont=new BitmapFont();
		bitmapFont.setColor(Color.WHITE);
		bitmapFont.getData().setScale(10);
		dizzy=new Texture("dizzy-1.png");
	}
    public void makeCoin()
	{
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinsX.add(Gdx.graphics.getWidth());
	}
	public void makeBomb()
	{
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		bombY.add((int)height);
		bombX.add(Gdx.graphics.getWidth());
	}
	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if(gameState==0)
		{
        //about to start
			if(Gdx.input.justTouched())
				gameState=1;
		}
		else if(gameState==1)
		{
         //live
			//bomb
			if(bombCount<250)
			{
				bombCount++;
			}
			else
			{
				bombCount=0;
				makeBomb();
			}
			bombRectangle.clear();
			for(int i=0;i<bombX.size();i++)
			{
				batch.draw(bomb,bombX.get(i),bombY.get(i));
				bombX.set(i,bombX.get(i)-8);
				bombRectangle.add(new Rectangle(bombX.get(i),bombY.get(i),bomb.getWidth(),bomb.getHeight()));
			}
			//coin
			if(coinCount<100)
			{
				coinCount++;
			}
			else
			{
				coinCount=0;
				makeCoin();
			}
			coinsRectangle.clear();
			for(int i=0;i<coinsX.size();i++)
			{
				batch.draw(coin,coinsX.get(i),coinYs.get(i));
				coinsX.set(i,coinsX.get(i)-4);
				coinsRectangle.add(new Rectangle(coinsX.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
			}
			if(Gdx.input.justTouched())
			{
				velocity=-10;
			}
			if (pause < 2) {
				pause++;
			} else {
				pause=0;
				if (manState < 3) {
					manState++;
				} else {
					manState = 0;
				}
			}
			velocity+=gravity;
			manY-=velocity;
			if(manY<=0)
				manY=0;
		}
		else if(gameState==2)
		{
			//over
			if(Gdx.input.justTouched())
				gameState=1;
			manY=Gdx.graphics.getHeight() / 2;
			score=0;
			velocity=0;
			coinCount=0;
			coinYs.clear();
			coinsX.clear();
			coinsRectangle.clear();
			bombCount=0;
			bombY.clear();
			bombX.clear();
			bombRectangle.clear();
		}
		if(gameState==2)
		{
			batch.draw(dizzy, Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}
		else {
			batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}
		manRectangle=new Rectangle((Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2), manY,man[manState].getWidth(),man[manState].getHeight());
		for(int i=0;i<coinsRectangle.size();i++)
		{
			if(Intersector.overlaps(manRectangle,coinsRectangle.get(i)))
			{
				Gdx.app.log("Coin!","Collision!");
				score++;
				coinsRectangle.remove(i);
				coinsX.remove(i);
				coinYs.remove(i);
				break;
			}
		}
		for(int i=0;i<bombRectangle.size();i++)
		{
			if(Intersector.overlaps(manRectangle,bombRectangle.get(i)))
			{
				Gdx.app.log("Bomb!","Collision!");
				gameState=2;
			}
		}
		bitmapFont.draw(batch,String.valueOf(score),100,200);
    batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
