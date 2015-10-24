package com.gmail.andrewahughes.TroopTD;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.gmail.andrewahughes.framework.Game;
import com.gmail.andrewahughes.framework.Graphics;
import com.gmail.andrewahughes.framework.Screen;
import com.gmail.andrewahughes.framework.Input.TouchEvent;

public class GameScreen extends Screen {
	enum GameState {
		Ready, Running, Paused, GameOver
	}

	GameState state = GameState.Ready;

	// Variable Setup
	// You would create game objects here.

	int livesLeft = 1;
	Paint paint;
	Paint blackText;
	String text;
	String text1;
	String text2;
	Bullet bullet;
	Command command;
	EnemyUpdate enemyUpdate;
	boolean commandState = true;
	boolean cameraMode = false;
	boolean disableControl = false;//variable to stop input commands while moving camera
	//boolean cameraMode = true;
	int no = 0;
	Point cameraOrigin;
	Point cameraDrag;
	PointF zoomOrigin, zoomDrag, zoomDrag2, finger1, finger2;
	float zoomScaleInitial = 1, zoomPinchDistanceInitial, zoomPinchDistance,
			zoomIncrease=1, zoomScale = 1,zoomScale2=1;
	//Button selectBtn;//don't need cos marquee select can also tap select


	// Rect b,c;
	public GameScreen(Game game) {
		super(game);

		// Initialize game objects here

		// Defining a paint object
		paint = new Paint();
		paint.setTextSize(30);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);

		blackText = new Paint();
		blackText.setColor(Color.BLACK);
		blackText.setTextSize(20);

		text = new String();
		text1 = new String();
		text2 = new String();

		bullet = new Bullet();
		command = new Command();
		enemyUpdate = new EnemyUpdate();
		cameraOrigin = new Point();
		cameraDrag = new Point(0, 0);
		finger1 = new PointF(0, 0);
		finger2 = new PointF(0, 0);

		zoomOrigin = new PointF(1,1); //zoomDrag, zoomDrag2, finger1, finger2;
		//selectBtn = new Button(1200, 10, "Select", "Marquee Select", true);
		//cameraBtn = new Button(1200, 90, "Camera", "No camera", true);
		// b = new Rect(100,100,100,100);
		// c = new Rect();

		Data.save(game.getFileIO());
		Data.loadPath(game.getFileIO());
		Data.load(game.getFileIO());

	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		// We have four separate update methods in this example.
		// Depending on the state of the game, we call different update methods.
		// Refer to Unit 3's code. We did a similar thing without separating the
		// update methods.

		if (state == GameState.Ready)
			updateReady(touchEvents);
		if (state == GameState.Running)
			updateRunning(touchEvents, deltaTime);
		if (state == GameState.Paused)
			updatePaused(touchEvents);
		if (state == GameState.GameOver)
			updateGameOver(touchEvents);
	}

	private void updateReady(List<TouchEvent> touchEvents) {

		// This example starts with a "Ready" screen.
		// When the user touches the screen, the game begins.
		// state now becomes GameState.Running.
		// Now the updateRunning() method will be called!

		if (touchEvents.size() > 0)
			state = GameState.Running;
	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {

		//Data.load(game.getFileIO());
		command.update(deltaTime);
		enemyUpdate.update(deltaTime);
		troopInteractionUpdate(command.troops,Data.enemies,deltaTime);
		//interaction between enemy and troop
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			//pointerPos = "touch "+event.x+","+event.y+"zoomOrigin "+zoomOrigin+"zoomDrag"+zoomDrag+"zoomScale"+zoomScale+"distinit"+zoomPinchDistanceInitial+"dist"+zoomPinchDistance;

			//pointerPos = "f1 "+finger1+"f2 "+finger2+"pointer "+event.pointer+"type "+event.type+"initial dist "+zoomPinchDistanceInitial+"dist "+zoomPinchDistance+"scale "+zoomScale+"increase "+zoomIncrease;
			//pointerPos="pos "+event.x+" "+event.y+" fin "+finger1+" fin2 "+finger2+" dist init "+zoomPinchDistanceInitial+" dist "+zoomPinchDistance+" "+zoomIncrease+" "+zoomScale+" "+zoomOrigin;
			//pointerPos="dist "+zoomPinchDistance+" distinit "+zoomPinchDistanceInitial+" z1 "+zoomScale+" z2 "+zoomScale2+" p1 "+finger1+" p2 "+finger2;
			/*text="offset "+command.troops.get(0).offSet+"\t offset2 "+command.troops.get(0).offSet2+" \tpos "+command.troops.get(0).position;
			text1="offset "+command.troops.get(1).offSet+"\t offset2 "+command.troops.get(1).offSet2+"\t pos "+command.troops.get(1).position;
			text2=" origin "+zoomOrigin+" zoom "+zoomScale+"  Pos"+command.troops.get(0).position+" zoomincrease " +zoomIncrease;
			*/
			//text="disabled? "+disableControl+ " pointers "+event.pointer+"marquee alive "+command.marqueeAlive;

			if (event.type == TouchEvent.TOUCH_DOWN) {
				// button logic
				// if we touch a button do nothing, but if touch up event is
				// also on button then press button
				/*if (selectBtn.rectangle.contains(event.x, event.y)) {

				} else if (cameraBtn.rectangle.contains(event.x, event.y)) {

				} else */// if no button is pressed
				if (event.pointer>0) //if two fingers down
				{
					command.marqueeAlive=false;//stop drawing marquee
				}
				else
				{
				
			if(disableControl==false){
					command.startMarquee((int)((event.x - cameraDrag.x)/zoomScale), (int)((event.y
							- cameraDrag.y)/zoomScale));
			}
					/*
					 * if(no<3) { command.createTroop(event.x,event.y); no++; }
					 * else {
					 */
					// }

				}
			}

			if (event.type == TouchEvent.TOUCH_UP) {

				// button logic
				// if we touch a button do nothing, but if touch up event is
				// also on button then press button
				/*if (selectBtn.rectangle.contains(event.x, event.y)) {
					selectBtn.toggle();
					command.toggleSelState();
				} else if (cameraBtn.rectangle.contains(event.x, event.y)) {
					cameraBtn.toggle();
					if (cameraMode) {
						cameraMode = false;
					} else if (cameraMode == false) {
						cameraMode = true;
					}
				} else*/ {
					if(disableControl==false)
					{
						command.evaluateTouch((int)((event.x - cameraDrag.x)/zoomScale), (int)((event.y
							- cameraDrag.y)/zoomScale));
					}
				}
				zoomScaleInitial=zoomScale;
				
				cameraMode=false;
				if(event.noOfPointers==1)//if you lift the last finger up, we no longer control the camera and so ...
				{
					disableControl=false;//allow touch controls
				}
				

			}
			if (event.type == TouchEvent.TOUCH_DRAGGED) {
				// c.left=event.x;
				// c.right=event.x;
				// c.top=event.y;
				// c.bottom=event.y;
				cameraControl1(event);
				
				if (event.pointer>0) {
					disableControl=true;//if more than one finger down disable controls since we are moving the camera

						command.marqueeAlive=false;//stop drawing marquee
					
					if(cameraMode==false)
					{
						cameraControlInitiate(event);	
					}
					cameraControl(event);
				} else {

					command.updateMarquee((int)((event.x - cameraDrag.x)/zoomScale), (int)((event.y
							- cameraDrag.y)/zoomScale));
				}
			}

		}

		// 2. Check miscellaneous events like death:

		if (livesLeft == 0) {
			state = GameState.GameOver;
		}

		// 3. Call individual update() methods here.
		// This is where all the game updates happen.
		// For example, robot.update();
	}

	private void troopInteractionUpdate(List<Troop>troop,List<Enemy>enemy ,float deltaTime) {
		for(int i = 0;i<troop.size();i++)//for each troop
		{
			if(troop.get(i).alive&&!troop.get(i).targetAcquired)//if troop is alive and is not already shooting something
			{
				for(int j = 0;j<enemy.size();j++)//for each enemy
				{
					if(enemy.get(j).alive&&distanceBetween(troop.get(i).position,enemy.get(j).position)<troop.get(i).range*troop.get(i).range) {//if an alive enemy is in range
						if(distanceBetween(troop.get(i).position,enemy.get(j).position)<=//if enemy is the closest
								troop.get(i).closestEnemy)
						{
							troop.get(i).target=j;
							troop.get(i).closestEnemy=distanceBetween(troop.get(i).position,enemy.get(j).position);
							troop.get(i).targetAcquired=true;
						}
					}
				}
			}
			if(troop.get(i).targetAcquired)
			{
				if (troop.get(i).fireTimer<0) {
					troop.get(i).fireTimer=50;
					troop.get(i).fire();
					enemy.get(troop.get(i).target).hit();
				}
				if (distanceBetween(troop.get(i).position, enemy.get(troop.get(i).target).position) > troop.get(i).range*troop.get(i).range ||
						enemy.get(troop.get(i).target).health <= 0)
				{
					troop.get(i).targetAcquired=false;
					troop.get(i).closestEnemy=troop.get(i).range*troop.get(i).range;
				}

			}
		}
	}
	private float distanceBetween(PointF p1,PointF p2)
	{
		float a,b;
		a =p1.x-p2.x;
		b =p1.y-p2.y;
		float c = a*a+b*b;
		return c;
	}
	private void updatePaused(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				state = GameState.Running;
			}
		}
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x > 300 && event.x < 980 && event.y > 100
						&& event.y < 500) {
					nullify();
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}

	}

	@Override
	public void paint(float deltaTime) {
		// First draw the game elements.

		// Example:
		// g.drawImage(Assets.background, 0, 0);
		// g.drawImage(Assets.character, characterX, characterY);

		Graphics g = game.getGraphics();
		// g.drawRect(0, 0, 1280, 800, Color.argb(255, 153, 217,
		// 234));//cornflower blue :)
		g.drawARGB(255, 153, 217, 234);// another way to draw a blue background

		command.paint(g, cameraDrag, zoomScale);
		enemyUpdate.paint(g, cameraDrag, zoomScale);

		g.drawString(text, 10, 30, blackText);
		g.drawString(text1, 10, 60, blackText);
		//g.drawString(" "+ Data.highscores[0]+"  "+ Data.currentLevel, 50, 90, blackText);
		// Secondly, draw the UI above the game elements.
		if (state == GameState.Ready)
			drawReadyUI();
		if (state == GameState.Running)
			drawRunningUI();
		if (state == GameState.Paused)
			drawPausedUI();
		if (state == GameState.GameOver)
			drawGameOverUI();

	}

	private void nullify() {

		// Set all variables to null. You will be recreating them in the
		// constructor.
		paint = null;

		// Call garbage collector to clean up memory.
		System.gc();
	}

	private void drawReadyUI() {
		Graphics g = game.getGraphics();

		g.drawARGB(155, 0, 0, 0);
		g.drawString("Tap each side of the screen to move in that direction.",
				640, 300, paint);

	}

	private void drawRunningUI() {
		Graphics g = game.getGraphics();

		command.drawMarquee(g, cameraDrag,zoomScale);
		//selectBtn.paint(g, paint);
		//cameraBtn.paint(g, paint);
	}

	private void drawPausedUI() {
		Graphics g = game.getGraphics();
		// Darken the entire screen so you can display the Paused screen.
		g.drawARGB(100, 0, 0, 0);
		g.drawString("Tap to resume", 640, 600, paint);

	}

	private void drawGameOverUI() {
		Graphics g = game.getGraphics();
		g.drawRect(0, 0, 1281, 801, Color.BLACK);
		g.drawString("GAME OVER.", 640, 300, paint);

	}

	@Override
	public void pause() {
		if (state == GameState.Running) {
			state = GameState.Paused;
		}

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {
		// pause();
		command.commandStateToggle();
		enemyUpdate.commandStateToggle();
		//Data.currentLevel++;
	}
	public void cameraControlInitiate(TouchEvent event)
	{
			//zoomOrigin = new PointF(event.x, event.y);
	
		cameraMode=true;
		if(event.pointer==0)
			{
				finger1=new PointF(event.x,event.y);
			}
		if(event.pointer==1)
		{
			finger2=new PointF(event.x,event.y);
		}

		cameraOrigin = new Point((int)(finger1.x+finger2.x)/2 - cameraDrag.x,(int) (finger1.y+finger2.y)/2
				- cameraDrag.y);
		zoomOrigin=new PointF((finger1.x+finger2.x)/2,(finger1.y+finger2.y)/2);
		zoomPinchDistanceInitial=0;
		if(event.pointer>0)
		{

			zoomScale2=1;
			zoomPinchDistance = 		(float) Math.sqrt(((finger1.x-finger2.x)*(finger1.x-finger2.x))+((finger1.y-finger2.y)*(finger1.y-finger2.y)));//find the length of the vector
			zoomPinchDistanceInitial = 	(float) Math.sqrt(((finger1.x-finger2.x)*(finger1.x-finger2.x))+((finger1.y-finger2.y)*(finger1.y-finger2.y)));//find the length of the vector
		}
		
	}
	public void cameraControl1(TouchEvent event)
	{
		if(event.pointer==0)
		{
			finger1 = new PointF(event.x,event.y);
		}
	}
	public void cameraControl(TouchEvent event)
	{
	if(event.pointer==1)
	{
		finger2 = new PointF(event.x,event.y);
	}
	if(event.pointer>0)
	{
		zoomPinchDistance = (float) Math.sqrt(((finger1.x-finger2.x)*(finger1.x-finger2.x))+((finger1.y-finger2.y)*(finger1.y-finger2.y)));//find the length of the vector
		if(zoomPinchDistanceInitial!=0)
		{

			if(zoomScale>=10)//zoomscale is the total zoom
			{
				zoomScale=10;
			}
			else
			{
				zoomIncrease=zoomScaleInitial*(zoomPinchDistance/zoomPinchDistanceInitial);			
				zoomScale = zoomIncrease;
				zoomScale2=zoomPinchDistance/zoomPinchDistanceInitial;
			}
			if(zoomScale2>=10)//zoomscale2is the current zoom increase of this gesture
			{
				zoomScale2=10;
			}
		}
	}
	
	cameraDrag = new Point(	(int) ((finger1.x+finger2.x)/2 - cameraOrigin.x),
							(int) ((finger1.y+finger2.y)/2 - cameraOrigin.y));
	}
}