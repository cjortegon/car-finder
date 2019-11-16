package co.edu.icesi.tv.game;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;

public class DrawingLayer extends Layer {

	private Bitmap bitmap;
	private Canvas canvas;
	private Rect rectSrc;

	public DrawingLayer(int width, int height) {
		this.width = width;
		this.height = height;
		bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		rectSrc = new Rect(0, 0, width, height);
	}

	public Canvas getCanvas() {
		return canvas;
	}

	@Override
	public void draw(Canvas pGraphics, int x, int y, int windowWidth, int windowHeight) {
		pGraphics.drawBitmap(bitmap, rectSrc, new Rect(x, y, x+width, y+height), null);
	}

}
