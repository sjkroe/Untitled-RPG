package com.mygdx.game;

public enum Block {
	STONE1("stone1"),
	STONE2("stone2"),
	STONE3("stone3"),
	STONE4("stone4"),
	STONE5("stone5"),
	STONE6("stone6"),
	STONE("stone"),
	STONEGRASS("grassStone"),
	TREE("tree"),
	STONECEILING("stoneCeiling"),
	GRASS("grass");
	private final String filename;
	Block(String fn){
		this.filename=fn;
	}
	Block(String fn, int x){
		this.filename=fn;
	}
	String getFile(){
		return filename;
	}
	boolean typeEqual(Block b){
		if(filename==b.getFile()){
			return true;
		}
		return false;
	}
}
