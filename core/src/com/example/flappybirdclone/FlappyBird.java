package com.example.flappybirdclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture background2;
    Texture background3;
    Texture background4;
    Texture background5;
    Texture background6;
    Texture[] dragon;
    Texture[] tubeTop;
    Texture[] tubeDown;
    Texture gameOver;
    int flag = 0;
    int isBackground = 0;
    float flyHeight;
    float fallingSpeed = 0;
    int isTouch = 0;
    int spaceBetweenTubs = 500;
    Random random;
    float tubeSpeed = 3;
    int tubesNumber = 5;
    float tubeX[] = new float[tubesNumber];
    float shiftTube[] = new float[tubesNumber];
    float distanceBetweenTubes;
    ShapeRenderer shapeRenderer;
    Circle birdCircle;
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;
    int gameScore = 0;
    int passedTubeIndex = 0;
    BitmapFont scoreFont;


    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("background.png");
        background2 = new Texture("background2.png");
        background3 = new Texture("background3.png");
        background4 = new Texture("background4.png");
        dragon = new Texture[4];
        dragon[0] = new Texture("dragon_wings_up.png");
        dragon[1] = new Texture("dragon_wings_down.png");
        dragon[2] = new Texture("dragon_wings_dead.png");
        dragon[3] = new Texture("dragon_wings_dead_tube.png");
        tubeTop = new Texture[4];
        tubeDown = new Texture[4];
        tubeTop[0] = new Texture("top_tube.png");
        tubeDown[0] = new Texture("bottom_tube.png");
        tubeTop[1] = new Texture("top_tube2.png");
        tubeDown[1] = new Texture("bottom_tube2.png");
        tubeDown[2] = new Texture("bottom_tube3.png");
        tubeTop[2] = new Texture("top_tube3.png");
        tubeDown[3] = new Texture("bottom_tube3.png");
        tubeTop[3] = new Texture("top_tube3.png");
        gameOver = new Texture("game_over.png");
        shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        topTubeRectangles = new Rectangle[tubesNumber];
        bottomTubeRectangles = new Rectangle[tubesNumber];
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.RED);
        scoreFont.getData().setScale(10);

        random = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() / 2;

        initGame();
    }

    public void initGame() {
        flyHeight = Gdx.graphics.getHeight() / 2 - dragon[0].getHeight() / 2;
        for (int i = 0; i < tubesNumber; i++) {
            tubeX[i] = Gdx.graphics.getWidth() / 2 - tubeTop[isBackground].getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes * 1.5f;
            shiftTube[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - (spaceBetweenTubs * 2));
            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

    @Override
    public void render() {
        batch.begin();
        if (gameScore < 2) {
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            isBackground = 0;
        } else if (gameScore < 3) {
            batch.draw(background2, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            isBackground = 1;
        } else if (gameScore < 4) {
            batch.draw(background3, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            isBackground = 2;
        } else{
            batch.draw(background4, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            isBackground = 3;
        }
        if (isTouch == 1) {

            if (tubeX[passedTubeIndex] < Gdx.graphics.getWidth() / 2) {
                gameScore++;
                if (passedTubeIndex < tubesNumber - 1) {
                    passedTubeIndex++;
                } else {
                    passedTubeIndex = 0;
                }
                Gdx.app.log("gamescore", "Score: " + gameScore);
            }

            if (Gdx.input.justTouched()) {
                fallingSpeed = -20;
            }

            for (int i = 0; i < tubesNumber; i++) {

                if (tubeX[i] < -tubeTop[isBackground].getWidth()) {
                    tubeX[i] = tubesNumber * distanceBetweenTubes;
                } else {
                    tubeX[i] -= tubeSpeed;
                }

                batch.draw(tubeTop[isBackground], tubeX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubs / 2 + shiftTube[i]);
                batch.draw(tubeDown[isBackground], tubeX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubs / 2 - tubeDown[isBackground].getHeight() + shiftTube[i]);
                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubs / 2 + shiftTube[i], tubeTop[isBackground].getWidth(), tubeTop[isBackground].getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubs / 2 - tubeDown[isBackground].getHeight() + shiftTube[i], tubeDown[isBackground].getWidth(), tubeDown[isBackground].getHeight());
            }

            if (flyHeight > dragon[0].getHeight() / 2) {
                fallingSpeed++;
                flyHeight -= fallingSpeed;
            } else {
                isTouch = 2;
            }
        } else if (isTouch == 0) {
            if (Gdx.input.justTouched()) {
                Gdx.app.log("touch", "touch touch!!");
                isTouch = 1;
            }
        } else if (isTouch == 2) {
            batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            if (Gdx.input.justTouched()) {
                Gdx.app.log("touch", "touch touch!!");
                isTouch = 1;
                initGame();
                gameScore = 0;
                passedTubeIndex = 0;
                fallingSpeed = 0;
            }
        }


        if (flag == 0) {
            flag = 1;
        } else {
            flag = 0;
        }
        if (isTouch != 2) {
            if (flag == 0) {
                batch.draw(dragon[flag], Gdx.graphics.getWidth() / 2 - dragon[flag].getWidth() / 2, flyHeight + 23);
            } else {
                batch.draw(dragon[flag], Gdx.graphics.getWidth() / 2 - dragon[flag].getWidth() / 2, flyHeight);
            }
        }

        if (flyHeight < dragon[0].getHeight() / 2) {
            batch.draw(dragon[2], Gdx.graphics.getWidth() / 2 - dragon[2].getWidth() / 2, flyHeight);
        }


        scoreFont.draw(batch, String.valueOf("Score: " + gameScore), Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);


        birdCircle.set(Gdx.graphics.getWidth() / 2, flyHeight + dragon[flag].getHeight() / 2, dragon[flag].getWidth() / 2);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.CORAL);
//        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

        for (int i = 0; i < tubesNumber; i++) {

//            shapeRenderer.rect(topTubeRectangles[i].x, topTubeRectangles[i].y, topTubeRectangles[i].width, topTubeRectangles[i].height);
//            shapeRenderer.rect(bottomTubeRectangles[i].x, bottomTubeRectangles[i].y, bottomTubeRectangles[i].width, bottomTubeRectangles[i].height);

            if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
                Gdx.app.log("itersected", "Bump!");
                isTouch = 2;
                batch.draw(dragon[3], Gdx.graphics.getWidth() / 2 - dragon[3].getWidth() / 2, flyHeight);
            }
        }
//        shapeRenderer.end();
        batch.end();
    }

    @Override
    public void dispose() {

    }
}
