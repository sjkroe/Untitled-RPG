package com.mygdx.game;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.util.ArrayList;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class NPC extends Entity{
	private final float MAX_SPEED=.175f;
	private final float ACCELERATION=.02f;
	private final float NORMALSPEEDSCALE=2/2.5f;
	private final float ROLLSPEEDSCALE=2f;
	private final float MAX_HEALTH=100f;
	private final float MAX_STAMINA=100f;
	public final int runCycleTime=15;
	private float runTime,drunTime,attackTime;
	boolean strafe,attack;
	int strafeTime,inactiveTime;
	private int mouseX,mouseY;
	private float initMouseX;
	private float angle;
	private float dx,dz;
	private int framesInAnim;
	private float scale;
	private float health,stamina;
	private State curState;
	Vector2 velocity,updir,sidedir,upv,sidev;
	
	float speedScale;
	
	public NPC(float x, float y, float z,int width,int length, int height,float scale) {
		super(x, y,z, width,length, height);
		framesInAnim=0;
		angle=0;
		initMouseX=getAngle();
		scale=scale;
		health=MAX_HEALTH;
		stamina=MAX_STAMINA;
		dx=0;
		dz=0;
		velocity=new Vector2(dx,dz);
		updir=new Vector2();
		sidedir=new Vector2();
		upv=new Vector2();
		sidev=new Vector2();
		strafe=false;
		inactiveTime=0;
		speedScale=NORMALSPEEDSCALE;
		runTime=0;
		drunTime=0;
		attackTime=0;
		curState=State.IDLE;
		
	}
	public void update(ArrayList<Rectangle2D.Float> e,Vector2 playerLoc){
		float vel=(float) Math.sqrt(dx*dx+dx*dz);
		velocity=new Vector2(dx,dz);
		updir=new Vector2(playerLoc.x-getX(),playerLoc.y-getZ());
		updir.setLength(1);
		//updir.set(updir.x/updir.len(),updir.y/updir.len());
		sidedir=new Vector2(updir.y,-updir.x);
		upv=new Vector2(velocity.dot(updir)*updir.x,velocity.dot(updir)*updir.y);
		sidev=new Vector2(upv.y,-upv.x);
		angle=playerLoc.angle(new Vector2(getX(),getZ()));
		//Vector2 upvdir=upv.
		//Vector2 sidevdir=new Vector2(upv.y,-upv.x);
		boolean tmp=false;
		if(attack){
			curState=State.ATTACK;
			attackTime++;
		}
		if(playerLoc.sub(new Vector2(getX(),getZ())).len()>4f){
			velocity=new Vector2(velocity.x+ACCELERATION*updir.x/updir.len(),velocity.y+ACCELERATION*updir.y/updir.len());
		}else{
			/*if(velocity.len()>ACCELERATION){
				velocity.sub(updir.scl(ACCELERATION));
			}else{
				velocity=new Vector2(0,0);
			}*/
			if(velocity.dot(updir)/updir.len()<.01f&&velocity.dot(updir)/updir.len()>-ACCELERATION){
				velocity=new Vector2(velocity.x-velocity.dot(updir)*updir.x/(updir.len()*updir.len()),velocity.y-velocity.dot(updir)*updir.y/(updir.len()*updir.len()));
			}else{
				if(velocity.dot(updir)>0f){
					velocity=new Vector2(velocity.x-ACCELERATION*updir.x/updir.len(),velocity.y-ACCELERATION*updir.y/updir.len());
				}else{
					velocity=new Vector2(velocity.x+ACCELERATION*updir.x/updir.len(),velocity.y+ACCELERATION*updir.y/updir.len());
				}
			}
			if(velocity.dot(sidedir)/sidedir.len()<.01f&&velocity.dot(sidedir)/sidedir.len()>-.01f){
				velocity=new Vector2(velocity.x-velocity.dot(sidedir)*sidedir.x/(sidedir.len()*sidedir.len()),velocity.y-velocity.dot(sidedir)*sidedir.y/(sidedir.len()*sidedir.len()));
			}else{
				if(velocity.dot(sidedir)>0f){
					velocity=new Vector2(velocity.x-ACCELERATION*sidedir.x/sidedir.len(),velocity.y-ACCELERATION*sidedir.y/sidedir.len());
				}else{
					velocity=new Vector2(velocity.x+ACCELERATION*sidedir.x/sidedir.len(),velocity.y+ACCELERATION*sidedir.y/sidedir.len());
				}
			}
		}
			
		if(velocity.len()>MAX_SPEED){
			velocity.setLength(MAX_SPEED);
		}
		drunTime=(velocity.len()/MAX_SPEED)/50f;
		if(playerLoc.sub(new Vector2(getX(),getZ())).len()>6f){
			runTime+=drunTime;
			if(runTime>1f){
				runTime-=1f;
			}
		}else{
			if(runTime>drunTime*2){
				if(runTime<.25f){
					runTime-=drunTime*2;
				}else if(runTime<.5f-drunTime*2){
					runTime+=drunTime*2;
				}else if(runTime<.5f){
					runTime=0f;
				}else if(runTime<.75f){
					if(runTime>.5f+drunTime*2){
						runTime-=drunTime*2;
					}else{
						runTime=0;
					}
				}else if(runTime<1f-drunTime*2){
					runTime+=drunTime*2;
				}else{
					runTime=0;
				}
				//runTime-=drunTime;
			}else{
				runTime=0;
			}
		}
		
		if(inactiveTime>0){
			inactiveTime--;
		}else{
			attack=false;
			strafe=false;
			curState=State.IDLE;
		}
		dx=velocity.x;
		dz=velocity.y;
		
		
		if(!collides(e,dx*speedScale,dz*speedScale)){
		setX(getX()+dx*speedScale);
		setZ(getZ()+dz*speedScale);
		}
		
	}
	public ArrayList<Matrix4> getTransforms(Quaternion q) {
		Matrix4 playerM4=new Matrix4();
		playerM4.set(new Vector3(getX(),getY(),getZ()),q);
		if(getRunTime()>.5f){
			playerM4.translate(0,(float)((8*(getRunTime()-.5f)-16*((getRunTime()-.5f)*(getRunTime()-.5f)))/5f),0).rotateRad(new Vector3(1,0,0),(float)(-Math.PI/24*getVelPer()));
		}else{
			playerM4.translate(0,(float)((8*(getRunTime())-16*((getRunTime())*(getRunTime())))/5f),0).rotateRad(new Vector3(1,0,0),(float)(-Math.PI/24*getVelPer()));
		}
		Matrix4 foot1M4=new Matrix4(playerM4);
		foot1M4.translate(5/16f, -3/4f, -1/8f);
		Matrix4 foot2M4=new Matrix4(playerM4);
		foot2M4.translate(-5/16f, -1/2f, -1/8f);
		foot1M4.rotateRad(new Vector3(1,0,0),(float)(Math.sin(Math.PI*2*getRunTime())*-Math.PI*3/8));
		foot1M4.translate(0,-1/2f,0);
		foot2M4.rotateRad(new Vector3(1,0,0),(float)(Math.sin(Math.PI*2*getRunTime())*Math.PI*3/8));
		foot2M4.translate(0,-1/2f,0);
		Matrix4 arm1M4,arm2M4,swordM4;
		arm2M4=new Matrix4(playerM4);
		arm2M4.translate(-5/8f, 7/8f, 0f);
		arm2M4.rotateRad(new Vector3(1,0,0),(float)(Math.sin(Math.PI*2*getRunTime())*Math.PI/6));
		arm2M4.translate(0f, -9/8f, 0f);
		if(getState()==State.ATTACK){
			arm1M4=new Matrix4(playerM4);
			arm1M4.translate(5/8f, 7/8f, 0f);
			arm1M4.rotateRad(new Vector3(-1,0,0),(float)(Math.PI/6-(5*Math.PI/6)*(1-(getAttackTime()+5)*(getAttackTime()+5)*(getAttackTime()+5)/42875f)));
			
			arm1M4.translate(0f, -9/8f, 0f);
			swordM4=new Matrix4(arm1M4);
			swordM4.translate(0f,-1/4f,-11/8f);
		}else{
			arm1M4=new Matrix4(playerM4);
			arm1M4.translate(5/8f, 7/8f, 0f);
			arm1M4.rotateRad(new Vector3(1,0,0),(float)(Math.sin(Math.PI*2*getRunTime())*-Math.PI/6));
			
			arm1M4.translate(0f, -9/8f, 0f);
			swordM4=new Matrix4(arm1M4);
			swordM4.translate(0f,-1/4f,-11/8f);
		}
		ArrayList<Matrix4> m4s=new ArrayList<Matrix4>();
		m4s.add(playerM4);
		m4s.add(foot1M4);
		m4s.add(foot2M4);
		m4s.add(arm1M4);
		m4s.add(arm2M4);
		m4s.add(swordM4);
		return m4s;
	}
	private boolean collides(ArrayList<Rectangle2D.Float> e, float dx2, float dz2) {
		boolean collides=false;
		boolean a=false;
		boolean b=false;
		Rectangle2D.Float p=new Rectangle2D.Float(getX()+dx2+1/2f,getZ()+dz2+1/2f,1,1);
		for(Rectangle2D.Float r:e){
			if(p.intersects(r)){
				collides=true;
				System.out.println(r.getX()+" "+r.getY()+" "+r.getWidth()+" "+r.getHeight());
				Rectangle2D.Float intersect=(Float) p.createIntersection(r);
				if(intersect.width>intersect.height){
					
					//Vector2 norm=new Vector2(1.0f,0f);
					if(dz2>0){
					setZ(getZ()-(intersect.height-dz2));
					}else{
						setZ(getZ()+(dz2+intersect.height));
					}
					//velocity.setLength(velocity.dot(norm));
					velocity.y=0f;
					dz=0f;
					a=true;
					
				}else{
					//Vector2 norm=new Vector2(0f,1.0f);
					if(dx2>0){
					setX(getX()-(intersect.width-dx2));
					}else{
						setX(getX()+(dx2+intersect.width));
						
					}
					//velocity.setLength(velocity.dot(norm));
					velocity.x=0f;
					dx=0f;
					b=true;
					
				}
			}
		}
		
		if(collides){
			if(!a){
				setZ(getZ()+dz2);
			}
			if(!b){
				setX(getX()+dx2);
			}
		}
		return collides;
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
	public Vector2 getSideDir(){
		return sidedir;
	}
	public float getRunTime(){
		return runTime;
	}
	public State getState() {
		return curState;
	}
	public int getAttackTime() {
		return (int)attackTime;
	}
	public float getVelPer(){
		return velocity.len()/MAX_SPEED;
	}
}
