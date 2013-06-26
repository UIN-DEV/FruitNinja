package dev.emmaguy.fruitninja;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import dev.emmaguy.fruitninja.Projectile;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.util.SparseArray;

public class FruitProjectileManager implements ProjectileManager {

    private final Random random = new Random();
    private final List<Projectile> fruitProjectiles = new ArrayList<Projectile>();
    private final SparseArray<Bitmap> bitmapCache;
    private int maxWidth;
    private int maxHeight;

    public FruitProjectileManager(Resources r) {
	
	bitmapCache = new SparseArray<Bitmap>(FruitType.values().length);
	
	for(FruitType t : FruitType.values()){
	    bitmapCache.put(t.getResourceId(), BitmapFactory.decodeResource(r,t.getResourceId(), new Options()));
	}
    }

    public void draw(Canvas canvas) {
	synchronized (fruitProjectiles) {
	    for (Projectile f : fruitProjectiles) {
		f.draw(canvas);
	    }
	}
    }

    public void update() {

	if (maxWidth < 0 || maxHeight < 0) {
	    return;
	}

	if (random.nextInt(1000) <= 25) {
	    FruitProjectile createNewFruitProjectile = createNewFruitProjectile();
	    synchronized (fruitProjectiles) {
		fruitProjectiles.add(createNewFruitProjectile);
	    }
	}

	synchronized (fruitProjectiles) {
	    for (Iterator<Projectile> iter = fruitProjectiles.iterator(); iter.hasNext();) {

		Projectile f = iter.next();
		f.move();

		if (f.hasMovedOffScreen()) {
		    iter.remove();
		}
	    }
	}
    }

    private FruitProjectile createNewFruitProjectile() {
	int angle = random.nextInt(20) + 70;
	int speed = random.nextInt(30) + 120;
	float gravity = random.nextInt(6) + 14.0f;
	boolean rightToLeft = random.nextBoolean();

	FruitProjectile fruitProjectile = new FruitProjectile(bitmapCache.get(FruitType.randomFruit().getResourceId()), maxWidth, maxHeight, angle, speed, gravity, rightToLeft);
	return fruitProjectile;
    }

    public void setWidthAndHeight(int width, int height) {
	this.maxWidth = width;
	this.maxHeight = height;
    }

    @Override
    public List<Projectile> getProjectiles() {
	return fruitProjectiles;
    }
}