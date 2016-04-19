package com.mygdx.game;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.BaseAnimationController.Transform;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
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
	private final float MAX_SPEED=.2f;
	private final float ACCELERATION=.02f;
	private final float NORMALSPEEDSCALE=2/2.5f;
	private final float ROLLSPEEDSCALE=2f;
	private final float MAX_HEALTH=100f;
	private final float MAX_STAMINA=100f;
	public final int runCycleTime=15;
	private float runTime,drunTime,attackTime;
	private boolean up,down,left,right,space,rmb,lmb;
	boolean strafe,attack;
	int strafeTime,inactiveTime;
	private int mouseX,mouseY;
	private float initMouseX;
	private float angle;
	private float dx,dz;
	private int framesInAnim;
	private float scale;
	private float health,stamina,staminaRegen;
	private State curState;
	Vector2 velocity,updir,sidedir,upv,sidev;
	
	float speedScale;
	
	public Player(float x, float y, float z,int width,int length, int height,float scale) {
		super(x, y,z, width,length, height);
		up=false;
		down=false;
		left=false;
		right=false;
		rmb=false;
		lmb=false;
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
		speedScale=NORMALSPEEDSCALE;
		runTime=0;
		drunTime=0;
		attackTime=0;
		staminaRegen=1f;
		curState=State.IDLE;
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
		if(rmb&&inactiveTime==0&&!attack&&curState!=State.BLOCK&&stamina>=50f){
			attack=true;
			inactiveTime=30;
			stamina-=50;
			attackTime=0;
		}
		if(attack){
			curState=State.ATTACK;
			attackTime++;
		}
		if(up&&!down){
			velocity=new Vector2(velocity.x+ACCELERATION*updir.x,velocity.y+ACCELERATION*updir.y);
			if(!strafe&&space&&curState!=State.BLOCK&&inactiveTime==0&&stamina>=30f){
				strafe=true;
				strafeTime=10;
				stamina-=30f;
				speedScale=ROLLSPEEDSCALE;
				inactiveTime=15;
			}
		}else if(!up&&down){
			
			velocity=new Vector2(velocity.x-ACCELERATION*updir.x,velocity.y-ACCELERATION*updir.y);
			if(!strafe&&space&&curState!=State.BLOCK&&inactiveTime==0&&stamina>=30f){
				strafe=true;
				strafeTime=10;
				stamina-=30f;
				speedScale=ROLLSPEEDSCALE;
				inactiveTime=15;
			}
		}else{
			if(velocity.dot(updir)<.01f&&velocity.dot(updir)>-ACCELERATION){
				velocity=new Vector2(velocity.x-velocity.dot(updir)*updir.x,velocity.y-velocity.dot(updir)*updir.y);
			}else{
				if(velocity.dot(updir)>0f){
					velocity=new Vector2(velocity.x-ACCELERATION*updir.x,velocity.y-ACCELERATION*updir.y);
				}else{
					velocity=new Vector2(velocity.x+ACCELERATION*updir.x,velocity.y+ACCELERATION*updir.y);
				}
			}
		}
		if(!left&&right){
			velocity=new Vector2(velocity.x+ACCELERATION*sidedir.x,velocity.y+ACCELERATION*sidedir.y);
			if(!strafe&&space&&curState!=State.BLOCK&&inactiveTime==0&&stamina>=30f){
				strafe=true;
				strafeTime=10;
				stamina-=30;
				speedScale=ROLLSPEEDSCALE;
				inactiveTime=15;
			}
		}else if(left&&!right){
			velocity=new Vector2(velocity.x-ACCELERATION*sidedir.x,velocity.y-ACCELERATION*sidedir.y);
			if(!strafe&&space&&curState!=State.BLOCK&&inactiveTime==0&&stamina>=30f){
				strafe=true;
				strafeTime=10;
				stamina-=30;
				speedScale=ROLLSPEEDSCALE;
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
		if(curState!=State.BLOCK){
			drunTime=(velocity.len()/MAX_SPEED)/50f;
		}else{
			drunTime=(velocity.len()/MAX_SPEED)/200f;
		}
		if(right||left||up||down){
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
		if(strafe){
			strafeTime--;
			if(strafeTime<=0){
				strafe=false;
			}
			//speedScale=ROLLSPEEDSCALE;
			if(strafeTime<3){
				speedScale=(speedScale+NORMALSPEEDSCALE)/2;
			}
			//speedScale=ROLLSPEEDSCALE;
			velocity.scl(.2f/velocity.len());
		}else{
			speedScale=2/2.5f;
		}
		if(inactiveTime>0){
			inactiveTime--;
		}else{
			attack=false;
			strafe=false;
			if(right||left||up||down){
				curState=State.WALK;
			}else{
				curState=State.IDLE;
			}
		}
		if(lmb){
			curState=State.BLOCK;
			speedScale=4/10f;
			staminaRegen=.25f;
			drunTime/=2;
		}else{
			staminaRegen=1f;
			drunTime*=2;
		}
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
			stamina+=staminaRegen;
		if(!collides(e,dx*.001f,dz*.001f)){
			
		}
		if(!collides(e,dx*speedScale,dz*speedScale)){
			setX(getX()+dx*speedScale);
			setZ(getZ()+dz*speedScale);
		}
		Matrix4 m4=new Matrix4();
		m4.setToRotation(new Vector3(0,-1,0),((mouseX-initMouseX)-getAngle())%360);
		angle=(float) mouseX;
		return m4;
		
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
		foot1M4.translate(5/16f, -1/2f, -1/8f);
		Matrix4 foot2M4=new Matrix4(playerM4);
		foot2M4.translate(-5/16f, -1/2f, -1/8f);
		foot1M4.rotateRad(new Vector3(1,0,0),(float)(Math.sin(Math.PI*2*getRunTime())*-Math.PI*3/8));
		foot1M4.translate(0,-1/2f,0);
		foot2M4.rotateRad(new Vector3(1,0,0),(float)(Math.sin(Math.PI*2*getRunTime())*Math.PI*3/8));
		foot2M4.translate(0,-1/2f,0);
		Matrix4 arm1M4,arm2M4,swordM4,shieldM4;
		arm2M4=new Matrix4(playerM4);
		arm2M4.translate(-5/8f, 7/8f, 0f);
		arm2M4.rotateRad(new Vector3(1,0,0),(float)(Math.sin(Math.PI*2*getRunTime())*Math.PI/6));
		arm2M4.translate(0f, -9/8f, 0f);
		if(getState()==State.ATTACK){
			arm1M4=new Matrix4(playerM4);
			arm1M4.translate(5/8f, 7/8f, 0f);
			arm1M4.rotateRad(new Vector3(-1,0,0),(float)(Math.PI/6-(5*Math.PI/6)*(1-(Math.pow(getAttackTime()+5,5)/52521875f))));
			
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
		if(getState()==State.BLOCK){
			arm2M4=new Matrix4(playerM4);
			arm2M4.translate(-5/8f, 7/8f, 0f);
			arm2M4.rotateRad(new Vector3(1,0,0),(float)(Math.PI/2));
			arm2M4.rotateRad(new Vector3(0,0,-1),-(float)Math.PI/6);
			//arm2M4.translate(translation)
			arm2M4.translate(0f, -7/8f, 1/4f);
			shieldM4=new Matrix4(arm2M4);
			shieldM4.rotateRad(new Vector3(1,0,0),-(float)(Math.PI/2));
			shieldM4.rotateRad(new Vector3(0,-1,0),(float)(Math.PI/3));
			shieldM4.translate(-1/8f,-3/4f,1/8f);
		}else{
			arm2M4=new Matrix4(playerM4);
			arm2M4.translate(-5/8f, 7/8f, 0f);
			arm2M4.rotateRad(new Vector3(1,0,0),(float)(Math.sin(Math.PI*2*getRunTime())*Math.PI/6));
			arm2M4.translate(0f, -9/8f, 0f);
			
			shieldM4=new Matrix4(arm2M4);
			shieldM4.rotateRad(new Vector3(-1,0,0),(float)(Math.PI/2));
			shieldM4.translate(-1/4f,-1f,0f);
		}
		ArrayList<Matrix4> m4s=new ArrayList<Matrix4>();
		m4s.add(playerM4);
		m4s.add(foot1M4);
		m4s.add(foot2M4);
		m4s.add(arm1M4);
		m4s.add(arm2M4);
		m4s.add(swordM4);
		m4s.add(shieldM4);
		return m4s;
	}
	private boolean collides(ArrayList<Rectangle2D.Float> e, float dx2, float dz2) {
		boolean collideswithany=false;
		boolean a=false;
		boolean b=false;
		Rectangle2D.Float p=new Rectangle2D.Float(getX()+dx2+1/4f,getZ()+dz2+1/2f,1+1/2f,1);
		Point2D.Float pc=new Point2D.Float((float)p.getCenterX(),(float)p.getCenterY());
		Matrix3 rotation=new Matrix3();
		rotation.setToRotation(new Vector3(0,0,-1),((getAngle()-initMouseX)+315)%360);
		Vector2[] pPoints={new Vector2((float)p.getMinX(),(float)p.getMinY()),new Vector2((float)p.getMaxX(),(float)p.getMinY()),new Vector2((float)p.getMinX(),(float)p.getMaxY()),new Vector2((float)p.getMaxX(),(float)p.getMaxY())};
		for(int x=0;x<4;x++){
			Vector2 relative=new Vector2(pPoints[x].x-pc.x,pPoints[x].y-pc.y);
			pPoints[x]=relative.mul(rotation);
			pPoints[x]=new Vector2(pPoints[x].x+pc.x,pPoints[x].y+pc.y);
		}
		
		for(Rectangle2D.Float r:e){
			boolean collides=true;
			Vector2 sideA=new Vector2(0,1);
			Vector2 topA=new Vector2(1,0);
			
			Vector2 sideB=new Vector2(-(float)Math.cos(((getAngle()-initMouseX)+315)%360),(float)Math.sin(((getAngle()-initMouseX)+315)%360));
			Vector2 topB=new Vector2((float)Math.sin(((getAngle()-initMouseX)+315)%360),(float)Math.cos(((getAngle()-initMouseX)+315)%360));
			Vector2[] axes={sideA,topA,sideB,topB};
			Vector2[] rPoints={new Vector2((float)r.getMinX(),(float)r.getMinY()),new Vector2((float)r.getMaxX(),(float)r.getMinY()),new Vector2((float)r.getMinX(),(float)r.getMaxY()),new Vector2((float)r.getMaxX(),(float)r.getMaxY())};
			
			
			for(Vector2 axis:axes){
				float maxR=rPoints[0].dot(axis);
				float minR=rPoints[0].dot(axis);
				float maxP=pPoints[0].dot(axis);
				float minP=pPoints[0].dot(axis);
				for(int x=1;x<4;x++){
					if(rPoints[x].dot(axis)>maxR){
						maxR=rPoints[x].dot(axis);
					}
					if(rPoints[x].dot(axis)<minR){
						minR=rPoints[x].dot(axis);
					}
					if(pPoints[x].dot(axis)>maxP){
						maxP=pPoints[x].dot(axis);
					}
					if(pPoints[x].dot(axis)<minP){
						minP=pPoints[x].dot(axis);
					}
				}
				if(maxP<minR||minP>maxR){
					collides=false;
				}
			}
			if(collides){
				collideswithany=true;
				float minX=pPoints[0].x;
				float maxX=pPoints[0].x;
				float minY=pPoints[0].y;
				float maxY=pPoints[0].y;
				for(Vector2 v:pPoints){
					if(v.x>maxX){
						maxX=v.x;
					}
					if(v.x<minX){
						minX=v.x;
					}
					if(v.y>maxY){
						maxY=v.y;
					}
					if(v.y<minY){
						minY=v.y;
					}
				}
				
				Rectangle2D.Float playerAABB=new Rectangle2D.Float(minX,minY, maxX-minX, maxY-minY);
				
				Rectangle2D.Float intersect=(Float) playerAABB.createIntersection(r);
				
				
				if(intersect.width>intersect.height+.001f){
					//Vector2 norm=new Vector2(1.0f,0f);
					if(r.y>getZ()/*dz2>.001f*/){
						setZ(getZ()-(intersect.height-dz2));System.out.println("hi");
					}else/* if(intersect.y<playerAABB.y/*dz2<-.001f)*/{
						setZ(getZ()+(dz2+intersect.height));
					//}else{
						
					}
					//velocity.setLength(velocity.dot(norm));
					velocity.y=0f;
					dz=0f;
					a=true;
					
				}else if(intersect.width+.001f<intersect.height){
					
					//Vector2 norm=new Vector2(0f,1.0f);
					if(r.x>getX()/*dx2>.001f*/){
					setX(getX()-(intersect.width-dx2));
					}else{
						setX(getX()+(dx2+intersect.width));
					}
					//velocity.setLength(velocity.dot(norm));
					velocity.x=0f;
					dx=0f;
					b=true;
					
				}else{
					velocity.y=0f;
					dz=0f;
					a=true;
					velocity.x=0f;
					dx=0f;
					b=true;
				}
			}
		}
		if(collideswithany){
			if(!a){
				setZ(getZ()+dz2);
			}
			if(!b){
				setX(getX()+dx2);
			}
		}
		
		return collideswithany;
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
	public void mouseReleased(char c) {
		switch(c){
		case 'r':
			rmb=false;
			break;
		case 'l':
			lmb=false;
			break;
		}
		
	}
	public void mousePressed(char c) {
		switch(c){
		case 'r':
			rmb=true;
			break;
		case 'l':
			lmb=true;
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
