package co.edu.icesi.tv.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 *
 * @author i2T
 */
public class ImageDictionary {
	
	/**
	 * Resources from Android project
	 */
	private Resources resources;

    /**
     * Array of all the images of the game
     */
	private Bitmap[] images;
    /**
     * Array of all the path of the images
     */
    public int[] imageNames;

    /**
     * Create a new resources center
     *
     * @param number numbers of total resources
     */
    public ImageDictionary(int number, Resources resources) {
    	this.resources = resources;
        this.images = new Bitmap[number];
        this.imageNames = new int[number];
    }

    /**
     * Associates the resource (identifier) to an index in the dictionary to import the Bitmap later when you invoke getImage(index)
     * @param position index in the dictionary
     * @param identifier the resource identifier, Ej: R.drawable.image01.png
     */
    public void setResource(int position, int identifier) {
    	this.imageNames[position] = identifier;
    }
    
    /**
     * Return the image according with its index
     *
     * @param pAncla index of the image in the array
     * @return The image in an specific index
     */
    public Bitmap getImage(int pAncla) {
        if (images[pAncla] == null) {
            images[pAncla] = BitmapFactory.decodeResource(resources, imageNames[pAncla]);
        }
        return images[pAncla];
    }

    /**
     * Load all the images to the array
     */
    public void preload() {
        for (int i = 0; i < imageNames.length; i++) {
            images[i] = getImage(imageNames[i]);
        }
    }

    /**
     * Clean the array of images
     */
    public void cleanImages() {
        images = new Bitmap[images.length];
    }

//    /**
//     * Flip an image horizontally
//     *
//     * @param pImage Image to flip
//     * @return the flipped image
//     */
//    public static Image horizontalFlip(Image pImage) {
//        System.out.println("<---Class: Resource.java--->   " + "horizontalFlip()");
//        int w = pImage.getWidth(null);
//        int h = pImage.getHeight(null);
//        Image imageFlip = Resource.createBufferImage(w, h);
//        Graphics g = imageFlip.getGraphics();
//        g.drawImage(pImage, 0, 0, w, h, w, 0, 0, h, null);
//        g.dispose();
//        return imageFlip;
//    }

//    /**
//     * Flip an image vertically
//     *
//     * @param pImage Image to flip
//     * @return the flipped image
//     */
//    public static Image verticalFlip(Image pImage) {
//        System.out.println("<---Class: Resource.java--->   " + "verticalFlip()");
//        int w = pImage.getWidth(null);
//        int h = pImage.getHeight(null);
//        Image dimg = Resource.createBufferImage(w, h);
//        Graphics g = dimg.getGraphics();
//        g.drawImage(pImage, 0, 0, w, h, 0, h, w, 0, null);
//        g.dispose();
//        return dimg;
//    }

//    /**
//     * Method that resize an Image
//     *
//     * @param pImage The image to resize
//     * @param newWidth New widht of the image
//     * @param newHeight New hight of the image
//     * @return The resized image
//     */
//    public static Image resize(Image pImage, int newWidth, int newHeight) {
//        int w = pImage.getWidth(null);
//        int h = pImage.getHeight(null);
//        Image dimg = Resource.createBufferImage(newWidth, newHeight);
//        Graphics2D g = (Graphics2D) dimg.getGraphics();
//        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        g.drawImage(pImage, 0, 0, newWidth, newHeight, 0, 0, w, h, null);
//        g.dispose();
//        return dimg;
//    }

//    /**
//     * Method that cut an image according to its secuence
//     *
//     * @param pImage - Image to cut in diferents tiles
//     * @param pTileColumns - Number of columns to cut the image gived by
//     * parameter
//     * @param pTileRows - Number of rows to cut the image gived by parameter
//     * @return An array with all the parts of the image
//     */
//    public Image[] getTiles(Image pImage, int pTileColumns, int pTileRows) {
//        // Image pImage = vImages[pos];
//
//        int imageWidth = pImage.getWidth(null);
//        int imageHeight = pImage.getHeight(null);
//
//        int tileWidth = imageWidth / pTileColumns;
//        int tileHeight = imageHeight / pTileRows;
//
//        // System.out.println("<---Class: Resource.java--->   " +   "Tama������������������o tile: " + tileWidth + "  -  " + tileHeight );
//        Image[] tiles = new Image[pTileColumns * pTileRows];
//
//        Image tileBuffer = null;
//
//        int count = 0;
//        for (int i = 0; i < pTileRows; i++) {
//            for (int j = 0; j < pTileColumns; j++) {
////                            if ( i == 0 && j == 0 )
////                                   ClimaCalido.report( "Ancho: " + tileWidth + " - Alto: " + tileHeight + " Image: " + pImage );
//
//                tileBuffer = Resource.createBufferImage(tileWidth, tileHeight);
//                tileBuffer.getGraphics().drawImage(pImage, 0 - (j * tileWidth), 0 - (i * tileHeight), null);
//                tiles[count] = tileBuffer;
//                count++;
//            }
//        }
//        return tiles;
//    }
    
//    public static Image rotate(Image pImage, int pAngle) {
//        int w = pImage.getWidth(null);
//        int h = pImage.getHeight(null);
//        Image dimg = dimg = Resource.createBufferImage(w, h);
//        Graphics2D g = (Graphics2D) dimg.getGraphics();
//        g.rotate(Math.toRadians(pAngle), w/2, h/2);
//        g.drawImage(pImage, 0, 0, null);
//        return dimg;
//    }
//    public Image loadTranslucentImage(Image pImage, float transperancy) {
//        // Create the image using the
//        Image aimg = new BufferedImage(pImage.getWidth(null), pImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
//        // Get the images graphics
//        Graphics2D g = (Graphics2D) aimg.getGraphics();
//        // Set the Graphics composite to Alpha
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transperancy));
//        // Draw the LOADED img into the prepared reciver image
//        g.drawImage(pImage, 0, 0, null);
//        // let go of all system resources in this Graphics
//        g.dispose();
//        // Return the image
//        return aimg;
//    }
}