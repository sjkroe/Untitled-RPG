package com.mygdx.game;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.util.ArrayList;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Projectile extends Entity{
	private float angle;
	private float dx,dy;
	private float speed;
	private Vector2 velocity;
	private Model m;
	private ModelInstance mi;
	public Projectile(float x,float y,float z,int width, int length, int height, float angle2, float speed, Model m) {
		super(x,y,z,width,length,height);
		angle=angle2;
		dx=(float)Math.cos(Math.toRadians(angle))*speed;
		dy=(float)Math.sin(Math.toRadians(angle))*speed;
		velocity=new Vector2((float)dx,(float)dy);
		this.m=m;
		this.speed=speed;
		mi=new ModelInstance(m);
		for(Node n:mi.nodes){
        	n.scale.set(1/2f,1/2f,1/2f);
        }
		mi.calculateTransforms();
        mi.transform.setToTranslation(x, y, z);
	}
	public boolean update(ArrayList<Rectangle2D.Float> e) {
		if(!collides(e,dx,dy)){
			setX((float)(getX()+dx));
			setY((float)(getY()+dy));
			return true;
		}
		return false;
	}
	public ModelInstance getModelInstance(){
		return mi;
	}
	public float getSpeed() {
		return speed;
	}
	public float getAngle() {
		return angle;
	}
	private boolean collides(ArrayList<Rectangle2D.Float> e, float dx2, float dz2) {
		Rectangle2D.Float p=new Rectangle2D.Float(getX()+dx2,getZ()+dz2,1,1);
		Point2D.Float pc=new Point2D.Float((float)p.getCenterX(),(float)p.getCenterY());
		Matrix3 rotation=new Matrix3();
		rotation.setToRotation(new Vector3(0,0,-1),angle);
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
			
			Vector2 sideB=new Vector2(-(float)Math.cos(angle),(float)Math.sin(angle));
			Vector2 topB=new Vector2((float)Math.sin(angle),(float)Math.cos(angle));
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
				return true;
			}
		}
		return false;
	}
}
