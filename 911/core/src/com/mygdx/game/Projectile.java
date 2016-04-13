package com.mygdx.game;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
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
	private double dx,dy;
	private float speed;
	private Model m;
	private ModelInstance mi;
	public Projectile(float x,float y,float z,int width, int length, int height, float angle2, float speed, Model m) {
		super(x,y,z,width,length,height);
		angle=angle2;
		dx=Math.cos(Math.toRadians(angle))*speed;
		dy=Math.sin(Math.toRadians(angle))*speed;
		m=m;
		speed=speed;
		mi=new ModelInstance(m);
		for(Node n:mi.nodes){
        	n.scale.set(1/2f,1/2f,1/2f);
        }
		mi.calculateTransforms();
        mi.transform.setToTranslation(x, y, z);
	}
	public void update() {
		setX((float)(getX()+dx));
		setY((float)(getY()+dy));
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
}
