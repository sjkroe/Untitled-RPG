package com.mygdx.game;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Entity{
	private boolean up,down,left,right,space;
	boolean strafe;
	int strafeTime,inactiveTime;
	private int mouseX,mouseY;
	private float initMouseX;
	private float angle;
	private float dx,dz;
	private int framesInAnim;
	private float scale;
	private float health,stamina;
	private final float MAX_HEALTH=100f;
	private final float MAX_STAMINA=100f;
	Vector2 velocity,updir,sidedir,upv,sidev;
	private final float MAX_SPEED=.2f;
	private final float ACCELERATION=.02f;
	float speedScale;
	
	public Player(float x, float y, float z,int width,int length, int height,float scale) {
		super(x, y,z, width,length, height);
		up=false;
		down=false;
		left=false;
		right=false;
		framesInAnim=0;
		angle=0;
		initMouseX=getAngle();
		scale=scale;
		health=MAX_HEALTH;
		stamina=MAX_STAMINA;
		Vector2 velocity=new Vector2();
		Vector2 updir=new Vector2();
		Vector2 sidedir=new Vector2();
		Vector2 upv=new Vector2();
		Vector2 sidev=new Vector2();
		strafe=false;
		inactiveTime=0;
		speedScale=speedScale=1/2.5f;
	}
	public Matrix4 update(ArrayList<Rectangle2D.Float> e){
		float vel=(float) Math.sqrt(dx*dx+dx*dz);
		velocity=new Vector2(dx,dz);
		updir=new Vector2((float) Math.cos(Math.toRadians(((getAngle()-initMouseX)+45)%360)),(float) Math.sin(Math.toRadians(((getAngle()-initMouseX)+45)%360)));
		sidedir=new Vector2(updir.y,-updir.x);
		upv=new Vector2(velocity.dot(updir)*updir.x,velocity.dot(updir)*updir.y);
		sidev=new Vector2(upv.y,-upv.x);
		//Vector2 upvdir=upv.
		//Vector2 sidevdir=new Vector2(upv.y,-upv.x);
		boolean tmp=false;
			//delete
		if(up&&!down){
			velocity=new Vector2(velocity.x+ACCELERATION*updir.x,velocity.y+ACCELERATION*updir.y);
			if(!strafe&&space&&inactiveTime==0){
				strafe=true;
				strafeTime=10;
				stamina-=30f;
				speedScale=1/1.25f;
				inactiveTime=15;
			}
		}else if(!up&&down){
			velocity=new Vector2(velocity.x-ACCELERATION*updir.x,velocity.y-ACCELERATION*updir.y);
			if(!strafe&&space&&inactiveTime==0){
				strafe=true;
				strafeTime=10;
				stamina-=30f;
				speedScale=1/1.25f;
				inactiveTime=15;
			}
		}else{
			if(velocity.dot(updir)<.01f&&velocity.dot(updir)>-ACCELERATION){
				velocity=new Vector2(velocity.x-velocity.dot(updir)*updir.x,velocity.y-velocity.dot(updir)*updir.y);
			}else{
				if(velocity.dot(updir)>0f){
					velocity=new Vector2(velocity.x-ACCELERATION*updir.x,velocity.y-ACCELERATION*updir.y);
				}else{
					velocity=new Vector2(velocity.x+.01f*updir.x,velocity.y+ACCELERATION*updir.y);
				}
			}
		}
		if(!left&&right){
			velocity=new Vector2(velocity.x+ACCELERATION*sidedir.x,velocity.y+ACCELERATION*sidedir.y);
			if(!strafe&&space&&inactiveTime==0){
				strafe=true;
				strafeTime=10;
				stamina-=30;
				speedScale=1/1.25f;
				inactiveTime=15;
			}
		}else if(left&&!right){
			velocity=new Vector2(velocity.x-ACCELERATION*sidedir.x,velocity.y-ACCELERATION*sidedir.y);
			if(!strafe&&space&&inactiveTime==0){
				strafe=true;
				strafeTime=10;
				stamina-=30;
				speedScale=1/1.25f;
				inactiveTime=15;
			}
		}else{
			if(velocity.dot(sidedir)<.01f&&velocity.dot(sidedir)>-.01f){
				velocity=new Vector2(velocity.x-velocity.dot(sidedir)*sidedir.x,velocity.y-velocity.dot(sidedir)*sidedir.y);
			}else{
				if(velocity.dot(sidedir)>0f){
					velocity=new Vector2(velocity.x-ACCELERATION*sidedir.x,velocity.y-ACCELERATION*sidedir.y);
				}else{
					velocity=new Vector2(velocity.x+ACCELERATION*sidedir.x,velocity.y+ACCELERATION*sidedir.y);
				}
			}
		}
		//delete
		if(velocity.len()>MAX_SPEED){
			velocity.setLength(MAX_SPEED);
		}
		if(strafe){
			strafeTime--;
			if(strafeTime<=0){
				strafe=false;
			}
			speedScale=1/1.25f;
			if(strafeTime<3){
				speedScale=(speedScale+1/2.5f)/2;
			}
			speedScale=1/1.25f;
			velocity.scl(.2f/velocity.len());
		}else{
			speedScale=1/2.5f;
		}
		if(inactiveTime>0)
			inactiveTime--;
		dx=velocity.x;
		dz=velocity.y;
		
		
		if(stamina<0){
			stamina=0;
		}else if(stamina>MAX_STAMINA){
			stamina=MAX_STAMINA;
		}
		if(stamina>MAX_STAMINA)
			stamina=MAX_STAMINA;
		else if(stamina<0)
			stamina=0;
		else if(stamina<MAX_STAMINA-1f&&inactiveTime==0)
			stamina+=1f;
		if(!collides(e,velocity.x*speedScale+dx*speedScale,velocity.y*speedScale+dz*speedScale)){
			setX(getX()+velocity.x*speedScale+dx*speedScale);
			setZ(getZ()+velocity.y*speedScale+dz*speedScale);
		}else{
			if(!collides(e,0f,dz*speedScale)){
				setZ(getZ()+dz*speedScale);
			}else if(!collides(e,dx*speedScale,0f)){
				setX(getX()+dx*speedScale);
			}
		}
		
		Matrix4 m4=new Matrix4();
		m4.setToRotation(new Vector3(0,-1,0),((mouseX-initMouseX)-getAngle())%360);
		angle=(float) mouseX;
		
		System.out.println(stamina);
		return m4;
		//b.setLinearVelocity((float)dx*200,(float)dy*200);
		//b.applyLinearImpulse(1.0f, dx ,dy ,true);
		
	}
	private boolean collides(ArrayList<Rectangle2D.Float> e, float dx2, float dz2) {
		boolean collides=false;
		System.out.print((getX()+dx2)+" ");
		System.out.println(getZ()+dz2);
		Rectangle2D.Float p=new Rectangle2D.Float(getX()+dx2+1/2f,getY()+dz2+1/2f,1,1);
		for(Rectangle2D.Float r:e){
			if(p.intersects(r)){
				collides=true;
				System.out.println(r.getX()+" "+r.getY()+" "+r.getWidth()+" "+r.getHeight());
			}
		}
		
		//if()
		return collides;
	}
	public void keyReleased(char c) {
		switch(c){
		case 'W':
			down=false;
			break;
		case 'A':
			left=false;
			break;
		case 'S':
			up=false;
			
			break;
		case 'D':
			right=false;
			break;
		case ' ':
			space=false;
			break;
		}
		
	}

	public void keyPressed(char c) {
		switch(c){
		case 'W':
			down=true;
			break;
		case 'A':
			left=true;
			break;
		case 'S':
			up=true;
			break;
		case 'D':
			right=true;
			break;
		case ' ':
			space=true;
			break;
		case 'E':
			System.exit(0);
			break;
		}
		
	}
	public Vector2 getUpDir(){
		return updir;
	}
	public void setMousePos(int x, int y) {
		mouseX=x;
		mouseY=y;
	}
	public float getAngle() {
		return angle;
	}
	public float getScale(){
		return scale;
	}
	public void setScale(float scale){
		scale=scale;
	}
	public Rectangle2D.Float getRect() {
		return new Rectangle2D.Float(getX()+1/2f,getY()+1/2f,1f,1f);
	}
	public float getStamina(){
		return stamina;
	}
}
