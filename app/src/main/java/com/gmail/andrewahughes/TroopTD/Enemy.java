
package com.gmail.andrewahughes.TroopTD;

import java.util.ArrayList;
import java.util.List;

import com.gmail.andrewahughes.framework.Graphics;
import com.gmail.andrewahughes.framework.Image;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

public class Enemy {


	List<Destination> destination = new ArrayList<Destination>();
	PointF position, prevPos,offSet,offSet2;
	boolean alive = false;
	float speed;
	double length;
	PointF direction;
	Rect rectangle;
	Image image;
	int margin=5,health =10,colour=255,pathType,delayTimer,enemyType,currentWaypoint=0;
	public Enemy()
	{
		image = Assets.menu;
		rectangle = new Rect(500,300,500+image.getWidth(),300+image.getHeight());
	}
	public Enemy(int path,int delay, int enemy,int x, int y)
	{
		pathType = path;
		delayTimer=delay;
		enemyType = enemy;
		position=new PointF(x,y);
		prevPos=new PointF(x, y);
		offSet = new PointF(x,y);
		offSet2 =new PointF(0,0);
		speed = 2;
		image = Assets.menu;
		rectangle = new Rect(x-margin,y-margin,x+image.getWidth()+margin,y+image.getHeight()+margin);
		alive=false;
	}
	public Enemy(int posX,int posY)
	{
		position=new PointF(posX, posY);
		prevPos=new PointF(posX, posY);
		offSet = new PointF(posX,posY);
		offSet2 =new PointF(0,0);
		speed = 2;
		image = Assets.menu;
		rectangle = new Rect(posX-margin,posY-margin,posX+image.getWidth()+margin,posY+image.getHeight()+margin);
		alive=false;
		
	}
	public void hit()
	{
		health--;
		colour=colour-25;
		if (health<=0)
		{
			alive=false;
		}
	}
	public void addDestination(int posX,int posY)
	{
		destination.add(new Destination(posX,posY));//set destination coordinates
		setDirection();  
	}
	public void editDestination( int destID, float positionX, float positionY){
		destination.get(destID).changeDest(positionX, positionY);
		setDirection();
	}
	public void setDirection()
	{
		if(destination.size()>0){
		direction = new PointF(destination.get(0).pointF.x-position.x,destination.get(0).pointF.y-position.y);//set the direction vector to head in
		//direction = new PointF(Data.pathList.get(pathType).pointList.get(currentWaypoint).x-position.x,Data.pathList.get(pathType).pointList.get(currentWaypoint).y-position.y);//set the direction vector to head in
		length = Math.sqrt((direction.x*direction.x)+(direction.y*direction.y));//find the length of the vector

			direction.x=(float) (direction.x/length);//normalise the direction
			direction.y=(float) (direction.y/length);
		}
	}
	public void moveTo(float dt)
	{
		
		position.x+= direction.x*speed*dt;//increase position by direction
		position.y+= direction.y*speed*dt;
		offSet2.x+= direction.x*speed*dt;
		offSet2.y+= direction.y*speed*dt;

		if(direction.x<0)//if the direction is to the left...
		{
			if(position.x<destination.get(0).pointF.x)//...and we go further left than the destination ...
			{
				position.x=destination.get(0).pointF.x;//...then stop!
				direction.x=0;//set direction x to zero, if y is zero too then destination will be deleted
			}
		}
		else if(direction.x>0)//if the direction is to the right...
		{
			if(position.x>destination.get(0).pointF.x)//...and we go further left than it ...
			{
				position.x=destination.get(0).pointF.x;//...then stop!
				direction.x=0;
			}
		}
		if(direction.y<0)
		{
			if(position.y<destination.get(0).pointF.y)
			{
				position.y=destination.get(0).pointF.y;
				direction.y=0;
			}
		}
		else if(direction.y>0)
		{
			if(position.y>destination.get(0).pointF.y)
			{
				position.y=destination.get(0).pointF.y;
				direction.y=0;
			}
		}

		if(destination.get(0).rectangle.intersect(rectangle))//if the troop has reached the destination 
		{
			destination.remove(0);
				setDirection();
			
		}
		
		updateRect((int)position.x,(int)position.y);

		
	}
	public void updateRect(int x,int y)
	{
		rectangle.left = x-margin;
		rectangle.top = y-margin;
		rectangle.right = x+image.getWidth()+margin;
		rectangle.bottom = y+image.getHeight()+margin;
	}

	public void removeDirection()
	{
		destination.remove(0);
	}

	public void paint(Graphics graphics,Point camera)
	{
        int len = destination.size();
        for (int i = 0; i < len; i++) {
    		graphics.drawRect(new Rect(destination.get(i).rectangle.left+camera.x,destination.get(i).rectangle.top+camera.y,
    				destination.get(i).rectangle.right+camera.x,destination.get(i).rectangle.bottom+camera.y), Color.argb(100,0,255,0));
        }
	}
}
