package com.mygdx.game;

public class Entity {
	private float x,y,z;
	private int width,length,height;
	public Entity(float x,float y,float z,int width, int length, int height){
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setWidth(width);
		this.setHeight(height);
		this.setLength(length);
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	public int getWidth() {
		return width;
	}
	private void setWidth(int width) {
		this.width = width;
	}
	public int getLength() {
		return length;
	}
	private void setLength(int length) {
		this.length = length;
	}
	public int getHeight() {
		return height;
	}
	private void setHeight(int height) {
		this.height = height;
	}
}
