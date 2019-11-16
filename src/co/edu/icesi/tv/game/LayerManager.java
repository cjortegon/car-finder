package co.edu.icesi.tv.game;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * This class manage a serie of layers also provide several features that
 * control how the game's layers are rendered on the screen
 *
 * @author i2t
 */
public class LayerManager {

	
	public int colorBackground;
	
	/**
	 * Layers
	 */
	private ArrayList<Layer> layers;
	private ArrayList<Layer> windowLayers;
	
	/**
	 * The horizontal location of the view window
	 */
	public int vPositionViewWindowX = 0;
	public int vPositionViewWindowY = 0;
	
	/**
	 * The dimension of the world
	 */
	private int worldWidth = 0;
	private int worldHeight = 0;
	
	/**
	 * Dimension of the window
	 */
	private int vWidthViewWindow;
	private int vHeightViewWindow;
	
	/**
	 * Constantes del posicion de LayerManager
	 */
	public static final int VERTICAL = 1;
	public static final int HORIZONTAL = 2;
	public static final int BOTH = 3;
	
	/**
	 * Constructor
	 */
	public LayerManager(int width, int height) {
		this.colorBackground = Color.WHITE;
		this.vWidthViewWindow = width;
		this.vHeightViewWindow = height;
		this.layers = new ArrayList<Layer>();
		this.windowLayers = new ArrayList<Layer>();
	}
	
	/**
     * Recalcultes the maximum widht and hieght of the view windows of this layer manager
     */
    private void calculateMaximums() {
        for (int i = 0; i < layers.size(); i++) {
            Layer layer = (Layer) layers.get(i);
            int max = layer.x + layer.width;
            if (max > worldWidth) {
                worldWidth = max;
            }
        }
        for (int j = 0; j < layers.size(); j++) {
            Layer layer = (Layer) layers.get(j);
            int max = layer.y + layer.height;
            if (max > worldHeight) {
                worldHeight = max;
            }
        }
    }
    
    /**
     * Append a new layer to this layer manager
     *
     * @param pLayer the layer to be added
     */
    public void append(Layer pLayer) {
        this.layers.add(pLayer);
        this.calculateMaximums();
    }
    
    /**
     * Append a new layer to this the window of the layer manager
     *
     * @param pLayer the layer to be added
     */
    public void appendToWindow(Layer pLayer) {
        this.windowLayers.add(pLayer);
    }
    
    /**
     * Moves the window
     * @return
     */
    public void setWindowPosition(int x, int y) {
		this.vPositionViewWindowX = x;
		this.vPositionViewWindowY = y;
	}
    
    /**
     * Getters of the world dimension
     */
    public int getWorldWidth() {
		return worldWidth;
	}
	public void setWorldWidth(int worldWidth) {
		this.worldWidth = worldWidth;
	}
    
    /**
     * 
     */
    public void simulatePerspective() {

		for (int i = 0; i < this.layers.size(); i++) {
			Layer pi = this.layers.get(i);
			if ((pi.perspectiveKind & this.HORIZONTAL) == this.HORIZONTAL) {
				int rielMovilX = this.worldWidth - pi.width;
				int rielFijoX = this.worldWidth - this.vWidthViewWindow;
				if (rielFijoX > 0) {
					pi.x = ((rielMovilX * this.vPositionViewWindowX) / rielFijoX);
				}
			}
			if ((pi.perspectiveKind & this.VERTICAL) == this.VERTICAL) {
				int rielMovilY = this.worldHeight - pi.height;
				int rielFijoY = this.worldHeight - this.vHeightViewWindow;
				if (rielFijoY > 0) {
					pi.y = ((rielMovilY * this.vPositionViewWindowY) / rielFijoY);
				}
			}
		};
	}

	public int getWorldHeight() {
		return worldHeight;
	}

	public void setWorldHeight(int worldHeight) {
		this.worldHeight = worldHeight;
	}

	public void focusLayer(Layer layer, int focusKind) {

		if ((focusKind & this.HORIZONTAL) == this.HORIZONTAL) {
			int posX = (layer.x - ((this.vWidthViewWindow - layer.width) / 2));
			if ((posX + this.vWidthViewWindow) > this.worldWidth) {
				this.vPositionViewWindowX = (this.worldWidth - this.vWidthViewWindow);
			} else {
				if (posX < 0) {
					this.vPositionViewWindowX = 0;
				} else {
					this.vPositionViewWindowX = posX;
				}
			}
		}

		if ((focusKind & this.VERTICAL) == this.VERTICAL) {
			int posY = (layer.y - ((this.vHeightViewWindow - layer.height) / 2));
			if ((posY + this.vHeightViewWindow) > this.worldHeight) {
				this.vPositionViewWindowY = (this.worldHeight - this.vHeightViewWindow);
			} else {
				if (posY < 0) {
					this.vPositionViewWindowY = 0;
				} else {
					this.vPositionViewWindowY = posY;
				}
			}
		}
	}
	
	/**
	 * Paint the layer manager in the canvas
	 *
	 * @param canvas object for proceed to paint
	 */
	public void draw(Canvas canvas) {

		canvas.drawColor(this.colorBackground);

		this.simulatePerspective();
		for (int i = this.layers.size() - 1; i >= 0; i--) {
			if(this.layers.get(i).visible)
				this.layers.get(i).draw(canvas, this.vPositionViewWindowX, this.vPositionViewWindowY, this.vWidthViewWindow, this.vHeightViewWindow);
		};
		for (int i = this.windowLayers.size() - 1; i >= 0; i--) {
			if(this.windowLayers.get(i).visible)
				this.windowLayers.get(i).draw(canvas, 0, 0, this.vWidthViewWindow, this.vHeightViewWindow);
		};
		
	}

	/**
     * Append a new layer to this layer manager to the front
     *
     * @param pLayer the layer to be added
     */
    public void appendToFront(Layer pLayer) {
        this.layers.add(0, pLayer);
        this.calculateMaximums();
    }
}