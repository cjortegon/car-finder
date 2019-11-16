package co.edu.icesi.tv.game;

import java.util.Arrays;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


/**
 *
 * @author i2t
 */
public class TiledLayer extends Layer {

	/**
	 * Matrix of tiles
	 */
	private int[][] cells;

	private Bitmap images[];

	public int tileWidth;
	public int tileHeight;

	public TiledLayer(int[][] cells, Bitmap[] bitmaps) {
		this.images = bitmaps;
		this.cells = cells;
		this.tileWidth = images[0].getWidth();
		this.tileHeight = images[0].getHeight();
		this.width = (this.tileWidth * cells[0].length);
		this.height = (this.tileHeight * cells.length);
	}
	
	public TiledLayer(int rows, int columns, Bitmap[] bitmaps) {
		this.images = bitmaps;
		this.cells = new int[rows][columns];
		for (int i = 0; i < cells.length; i++) {
			Arrays.fill(cells[i], -1);
		}
		this.tileWidth = images[0].getWidth();
		this.tileHeight = images[0].getHeight();
		this.width = (this.tileWidth * cells[0].length);
		this.height = (this.tileHeight * cells.length);
	}
	
	public void setCell(int row, int column, int value) {
		this.cells[row][column] = value;
	}
	
	public void fillCells(int xStart, int yStart, int width, int height, int value) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.cells[i+xStart][j+yStart] = value;
			}
		}
	}

	@Override
	public void draw(Canvas canvas, int posX, int posY, int windowWidth, int windowHeight) {
		
//		for (int i = 0; i < 10; i++) {
//			canvas.drawBitmap(images[0], null, new Rect(tileWidth*i, 0, tileWidth*(i+1), tileHeight), null);
//		}
		
		this.x -= posX;
		this.y -= posY;
		for (int i = 0; i < this.cells.length; i++) {
			for (int j = 0; j < this.cells[0].length; j++) {
				if(cells[i][j] != -1) {
					canvas.drawBitmap(images[cells[i][j]], null, new Rect(this.x + tileWidth*j, this.y + tileHeight*i, this.x + tileWidth*(j+1), this.y + tileHeight*(i+1)), null);
				}
			}
		}
		this.x += posX;
		this.y += posY;
		
//		this.x -= posX;
//		this.y -= posY;
//		for (int i = 0; i < this.cells.length; i++) {
//			if (this.y + (i + 1) * this.tileHeight >= posY) {
//				if (this.y + i * this.tileHeight > posY + windowHeight) {
//					break;
//				} else {
//					for (int j = 0; j < this.cells[0].length; j++) {
//						if (this.x + (j + 1) * this.tileWidth >= posX) {
//							if (this.x + j * this.tileWidth > posX + windowWidth) {
//								break;
//							} else {
//								int imageIndex = this.cells[i][j];
//								if (imageIndex > -1) {
//									canvas.drawBitmap(images[imageIndex], null, new Rect(this.x + (j * this.tileWidth) + posX, this.y + (i * this.tileHeight) + posY, this.x + (j * (this.tileWidth+1)) + posX, this.y + (i * (this.tileHeight+1)) + posY), null);
//									//canvas.drawBitmap(images[imageIndex], null, new Rect(this.x + (j * this.tileWidth), this.y + (i * this.tileHeight), this.x + (j * (this.tileWidth+1)), this.y + (i * (this.tileHeight+1))), null);
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		this.x += posX;
//		this.y += posY;
	}

	public int getRows() {
		if(cells != null)
			return cells.length;
		return 0;
	}

	public int getColumns() {
		if(cells != null)
			return cells[0].length;
		return 0;
	}

	public int getCell(int i, int j) {
		if(cells != null)
			return cells[i][j];
		return -1;
	}

	
	
}