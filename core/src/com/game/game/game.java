package com.game.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.Iterator;

public class game extends ApplicationAdapter {
	// SpriteBatch batch;
	Texture img;

	private Texture dropImage;
	private Texture bucketImage;

	private Sound dropSound;

	private Music rainMusic;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Rectangle bucket;

	private Array<Rectangle> raindrops;

	private long lastDropTime;

//	@Override
//	public void create () {
//		batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");
//	}

	@Override
	public void create(){
		dropImage = new Texture(Gdx.files.internal("drop.png"));
		bucketImage =  new Texture(Gdx.files.internal("bucket.png"));

//		dropSound = Gdx.audio.newSound(Gdx.files.internal(("drop.wav")));
//		rainMusic = (Music) Gdx.audio.newSound(Gdx.files.internal(("rain.mp3")));

//		rainMusic.setLooping(true);
//		rainMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);

		batch = new SpriteBatch();

		bucket = new Rectangle();
		bucket.x = 800/2 - 64/2;
		bucket.y = 20;
		bucket.width = 16;
		bucket.height = 16;

		raindrops = new Array<Rectangle>();
		spawnRaindrop();
	}

	@Override
	public void render () {
		// background color
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle raindrop: raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.end();

		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = Math.round(touchPos.x - 64/2);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800-64) bucket.x = 800 - 64;

		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

		for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0) iter.remove();
			if (raindrop.overlaps(bucket)) {
//				dropSound.play();
				iter.remove();
			}
		}
	}
	
	@Override
	public void dispose () {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
		batch.dispose();
		img.dispose();
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-54);
		raindrop.y = 480;
		raindrop.width = 16;
		raindrop.height = 16;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}
}
