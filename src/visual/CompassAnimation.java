package visual;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import co.edu.icesi.tv.game.DrawingLayer;
import co.edu.icesi.tv.game.Game;
import co.edu.icesi.tv.game.ImageDictionary;

public class CompassAnimation extends Game {

	// Constantes
	private final Rect carRectSrc, radarRectSrc;
	private final int HALF_WIDTH, HALF_HEIGHT, CAR_SIZE;

	// Recursos
	private ImageDictionary resources;

	// Configuracion
	private float north, carPointing, radar = 1f, aproach = 1f;
	private boolean gpsOn;

	// Objetos visuales
	private DrawingLayer circle;
	private Paint paint;

	public CompassAnimation(int windowWidth, int windowHeight, ImageDictionary resources) {
		super(windowWidth, windowHeight);

		this.resources = resources;

		this.HALF_WIDTH = width/2;
		this.HALF_HEIGHT = (int) (height/2.25);
		this.CAR_SIZE = HALF_WIDTH/4;
		this.carRectSrc = new Rect(0, 0, resources.getImage(0).getWidth(), resources.getImage(0).getHeight());
		this.radarRectSrc = new Rect(0, 0, resources.getImage(1).getWidth(), resources.getImage(1).getHeight());

		startVisualComponents();
	}

	public void startVisualComponents() {

		paint = new Paint();
		paint.setFakeBoldText(true);
		paint.setTextSize(30);
		paint.setStrokeMiter(3);

		circle = new DrawingLayer(width, width);
		paint.setColor(Color.CYAN);
		circle.getCanvas().drawCircle(HALF_WIDTH, HALF_WIDTH, HALF_WIDTH, paint);
		paint.setColor(Color.GREEN);
		circle.getCanvas().drawCircle(HALF_WIDTH, HALF_WIDTH, HALF_WIDTH-5, paint);
		paint.setColor(Color.DKGRAY);
		circle.getCanvas().drawCircle(HALF_WIDTH, HALF_WIDTH, HALF_WIDTH-10, paint);

	}

	@Override
	protected void refresh() {
		if(gpsOn) {
			radar += 0.015625;
			if(radar > 1f)
				radar = 0;
		}else
			radar = 0;
	}

	@Override
	protected void move(Object[] interactive) {
		this.north = (float)Math.PI - ((Float)interactive[0] - (float)(Math.PI/2));
		this.carPointing = (float)Math.PI - ((Float)interactive[1] - (float)(Math.PI/2));
		float distance = (Float) interactive[2];
		this.aproach = (distance > 50 ? 1 : (distance/50));
		this.gpsOn = (Float)interactive[3] == 1f;
	}

	@Override
	protected void collision() {}

	@Override
	protected void render(Canvas canvas) {

		// Dibujar circulo
		circle.draw(canvas, 0, HALF_HEIGHT-(circle.width/2), width, height);

		// Dibujar norte
		paint.setColor(Color.YELLOW);
		paint.setStyle(Paint.Style.FILL);
		int xNorth = HALF_WIDTH + (int) ((HALF_WIDTH-25)*Math.cos(north));
		int yNorth = HALF_HEIGHT + (int) ((HALF_WIDTH-25)*Math.sin(north));
		canvas.drawText("N", xNorth-12, yNorth+12, paint);

		if(gpsOn) {

			// Dibujar aguja
			int xCarPointing = HALF_WIDTH + (int) ((HALF_WIDTH-25)*Math.cos(carPointing));
			int yCarPointing = HALF_HEIGHT + (int) ((HALF_WIDTH-25)*Math.sin(carPointing));
			paint.setStrokeWidth(5);
			paint.setColor(Color.CYAN);
			canvas.drawLine(HALF_WIDTH, HALF_HEIGHT, xCarPointing, yCarPointing, paint);

			// Dibujar carro
			int xCar = HALF_WIDTH + (int) ((HALF_WIDTH-40)*Math.cos(carPointing)*aproach - (CAR_SIZE/2));
			int yCar = HALF_HEIGHT + (int) ((HALF_WIDTH-40)*Math.sin(carPointing)*aproach - (CAR_SIZE/2));
			canvas.drawBitmap(resources.getImage(0), carRectSrc, new Rect(xCar, yCar, xCar + CAR_SIZE, yCar + CAR_SIZE), null);

			// Dibujar el radar
			paint.setStrokeWidth(3);
			paint.setColor(Color.WHITE);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle(HALF_WIDTH, HALF_HEIGHT, (HALF_WIDTH-20)*radar, paint);
		}

		// Radar en degrade
		rotation += 5;
		rotation %= 360;
		canvas.rotate(rotation, HALF_WIDTH, HALF_HEIGHT);
		canvas.drawBitmap(resources.getImage(1), radarRectSrc, new Rect(0, HALF_HEIGHT-HALF_WIDTH, HALF_WIDTH, HALF_HEIGHT), null);
	}

	private int rotation = 0;

}
