package co.edu.icesi.tv.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * This class represent a layer with motion
 * @author i2t
 */
public class Sprite extends Layer {

	private int sprite;
	public int animateMovementX;
	public int animateMovementY;
	private Bitmap img;
	public boolean automaticAnimation;
	private boolean active;
	public int[] animationSprites;

	public Sprite(Bitmap img, int x, int y, int sprites) {
		this.x = x;
		this.y = y;
		this.active = true;
		this.img = img;
		this.width = (int) (this.img.getWidth()/(double)sprites);
		this.height = this.img.getHeight();
		this.automaticAnimation = false;
		this.animationSprites = new int[sprites];
		for (int i = 0; i < animationSprites.length; i++) {
			animationSprites[i] = i;
		}
	}

	public void setAutomaticAnimation(boolean auto) {
		this.automaticAnimation = auto;
	}

	public void setAnimateSprites(int[] animationSprites) {
		if(active)
			this.animationSprites = animationSprites;
	}

	public boolean isActive() {
		return active;
	}

	public void animate() {
		x += animateMovementX;
		y += animateMovementY;
		sprite ++;
		active = sprite >= animationSprites.length;
		if(active)
			sprite = 0;
	}

	//	public boolean collide(Layer layer) {
	//		int w1 = this.width;
	//		int h1 = this.height;
	//		int w2 = layer.width;
	//		int h2 = layer.height;
	//		int x1 = this.x;
	//		int y1 = this.y;
	//		int x2 = layer.x;
	//		int y2 = layer.y;
	//		if (((x1 + w1) > x2) && ((y1 + h1) > y2) && ((x2 + w2) > x1) && ((y2 + h2) > y1)) {
	//			return true;
	//		} else {
	//			return false;
	//		}
	//	}

	public int[] collideTiledLayer(TiledLayer pTile) {

		// Funtional elements
		int[] collision = {
				-1, -1, -1, -1
		}; // Top, bottom, left, right
		int top = y;
		int bottom = y + height;
		int left = x;
		int right = x + width;

		for (int i = 0; i < pTile.getRows(); i++) {
			for (int j = 0; j < pTile.getColumns(); j++) {
				if (pTile.getCell(i, j) >= 0) {

					// Checking the range for top & bottom
					if ((left >= (pTile.x + pTile.tileWidth * j) && left <= (pTile.x + pTile.tileWidth * (j + 1))) || (right >= (pTile.x + pTile.tileWidth * j) && right <= (pTile.x + pTile.tileWidth * (j + 1))) || (right > (pTile.x + pTile.tileWidth * (j + 1)) && left < (pTile.x + pTile.tileWidth * j))) {

						int topIntercept = pTile.y + pTile.tileHeight * (i + 1) - top;
						if (topIntercept > collision[0] && topIntercept < pTile.tileHeight) {
							collision[0] = topIntercept;
						}

						int bottomIntercept = bottom - (pTile.y + pTile.tileHeight * i);
						if (bottomIntercept > collision[1] && bottomIntercept < pTile.tileHeight) {
							collision[1] = bottomIntercept;
						}
					}

					// Checking the range for left & right
					int middlePointY = top + (bottom - top) / 2;
					if (middlePointY >= (pTile.y + pTile.tileHeight * i) && middlePointY <= (pTile.y + pTile.tileHeight * (i + 1))) {

						int leftIntercept = pTile.x + pTile.tileWidth * (j + 1) - left;
						if (leftIntercept > collision[2] && leftIntercept < pTile.tileWidth) {
							collision[2] = leftIntercept;
						}

						int rightIntercept = right - (pTile.x + pTile.tileWidth * j);
						if (rightIntercept > collision[3] && rightIntercept < pTile.tileWidth) {
							collision[3] = rightIntercept;
						}
					}
				}
			}
		}
		return collision;
	}

	public void draw(Canvas canvas, int posX, int posY, int windowWidth, int windowHeight){

		//if(this.x < (posX + windowWidth) && (this.x + this.width) > posX && this.y < (posY + windowHeight) && (this.x + this.height) > posY) {
		if(this.x < (posX + windowWidth) && (this.x + this.width) > posX && this.y < (posY + windowHeight) && (this.x + this.height) > posY) {
			this.x -= posX;
			this.y -= posY;
			if(this.automaticAnimation){
				this.animate();
			}
			try{
				canvas.drawBitmap(img, new Rect(animationSprites[sprite]*this.width, 0, (animationSprites[sprite]+1)*this.width, this.height), new Rect(this.x, this.y, this.x + this.width, this.y + this.height), null);
			} catch(Exception e) {
				Paint p = new Paint();
				p.setTextSize(20);
				canvas.drawText("animationSprites.length = "+animationSprites.length+" sprite = "+sprite, 10, 10, p);
			}
			//canvas.drawRect(new Rect(x + left, y + top, x + width - right, y + height - bottom), new Paint());
			this.x += posX;
			this.y += posY;
		}


	}

}