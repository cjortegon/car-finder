package co.edu.icesi.tv.game;

import android.graphics.Canvas;

/**
 * This class provide the estructure to make the game with layers, and make it easy the disposition of the
 * elements on the canvas
 * @author i2t
 */
public abstract class Layer {

    /**
     * Heigth of the object layer
     */
    public int height;
    /**
     * Width of the object layer
     */
    public int width;
    /**
     * The X position of the object layer
     */
    public int x;
    /**
     * The Y position of the object layer
     */
    public int y;
    /**
     * Collide margin
     */
    public int top, bottom, left, right;
    /**
     * Determinate if the object layer is visible or not
     */
    public boolean visible;
    /**
     * Determinate if the object layer is autohide or not
     */
    public boolean autoHide;
    /**
     * Kind of perspective of the layer
     */
    public int perspectiveKind;
    
    /**
     * Reflection of the layers
     */
    public boolean horizontalReflection, verticalReflection;

    /**
     * Move the object layer on the x-axis acording pMoveX and the y-axis acording pMoveY
     * @param pMoveX value to move the object layer on the x-axis
     * @param pMoveY value to move the object layer on the  y-axis
     */
    public void move(int pMoveX, int pMoveY) {
        this.x += pMoveX;
        this.y += pMoveY;
    }
    
    /**
     * Default collision
     */
    public boolean collide(Layer layer) {
    	//return (((x + width) > layer.x) && ((y + height) > layer.y) && ((layer.x + layer.width) > x) && ((layer.y + layer.height) > y));
    	return (((x + width - right) > layer.x + layer.left) && ((y + height - bottom) > layer.y + top) && ((layer.x + layer.width - layer.right) > x + left) && ((layer.y + layer.height - layer.bottom) > y + top));
    }

    /**
     * Draw the layer in the canvas
     * @param pGraphics object to proceed to paint
     */
    public abstract void draw(Canvas pGraphics, int x, int y, int windowWidth, int windowHeight);
}