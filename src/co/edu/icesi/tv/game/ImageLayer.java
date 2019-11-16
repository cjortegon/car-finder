package co.edu.icesi.tv.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 *This class paint an image in the canvas
 * @author Juan Vicente Pradilla
 */
public class ImageLayer extends Layer {

	/**
	 * Image to paint in the canvas
	 */
	private Bitmap vImage;
	/**
	 * Number of repeats horizontally of the image
	 */
	private int vRepeatHorizontalImage;
	/**
	 * Number of repeats vertically of the image
	 */
	private int vRepeatVerticalImage;

	/**
	 * Define an ImageLayer object with an determinate image
	 * @param pImage the default image to initialize the ImageLayer object
	 */
	public ImageLayer(Bitmap pImage) {
		super();

		this.vImage = pImage;
		this.vRepeatHorizontalImage = 1;
		this.vRepeatVerticalImage = 1;
		this.height = this.vImage.getHeight();
		this.width = this.vImage.getWidth();
	}

	/**
	 * Define an ImageLayer object with a determinate image, number of  times to repeat the image vertically and horizontal
	 * @param pImage the default image to initialize the ImageLayer object
	 * @param pRepeatHorizontalImage number of times to repeat the image horizontal
	 * @param pRepeatVerticalImage number of times to repeat the image vertically
	 */
	public ImageLayer(Bitmap pImage, int pRepeatHorizontalImage, int pRepeatVerticalImage) {
		super();
		vImage = pImage;
		this.vRepeatHorizontalImage = pRepeatHorizontalImage;
		this.vRepeatVerticalImage = pRepeatVerticalImage;
		this.height = this.vImage.getHeight() * this.vRepeatVerticalImage;
		this.width = this.vImage.getWidth() * this.vRepeatHorizontalImage;
	}

	/**
	 * Draw a graphic object in the canvas
	 * @param canvas a graphic objetc to proceed to paint
	 */
	public void draw(Canvas canvas, int posX, int posY, int windowWidth, int windowHeight) {
		this.x -= posX;
		this.y -= posY;
		for (int j = 0; j < vRepeatHorizontalImage; j++) {
			for (int i = 0; i < vRepeatVerticalImage; i++) {
				canvas.drawBitmap(vImage, null, new Rect(x + i*width, y + j*height, x + (i+1)*width, y + (j+1)*height), null);
			}
		}
		this.x += posX;
		this.y += posY;
	}
}