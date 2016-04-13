package com.mygdx.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Level {
	
	Block [][] lay1=new Block[13][10];
	int [][] rotationLay1=new int[13][10];
	Block [][] lay2=new Block[13][10];
	int [][] rotationLay2=new int[13][10];
	Block [][] lay3=new Block[11][10];
	int [][] rotationLay3=new int[11][10];
	final char idChars[]={'1','2','3','4','5','6','7','s','g','t'};
	final Block idBlocks[]={Block.GRASS,Block.STONE1,Block.STONE2,Block.STONE3,Block.STONE4,Block.STONE5,Block.STONE6,Block.STONE,Block.STONEGRASS,Block.TREE};
	public Level(){
		File lvl1=new File("lvl 1.txt");
		File lvl2=new File("lvl 2.txt");
		File lvl3=new File("lvl 3.txt");
		File[] files=new File[3];
		files[0]=lvl1;
		files[1]=lvl2;
		files[2]=lvl3;
		for(int x=0;x<3;x++){
			try {

		        Scanner sc = new Scanner(files[x]);
		        int y=0;
		        while (sc.hasNextLine()) {
		        	String z=sc.nextLine();
		        	Block[] temp=new Block[z.length()/2];
		        	int[] tempI=new int[z.length()/2];
		            for(int a=0;a<z.length();a+=2){
		            	for(int i=0;i<idChars.length;i++){
		            		if(z.charAt(a)==idChars[i]){
		            			temp[a/2]=idBlocks[i];
		            			tempI[a/2]=Character.getNumericValue(z.charAt(a+1));
		            		}
		            	}
		            }
		            if(x==0){
		            	lay1[y]=temp;
		            	rotationLay1[y]=tempI;
		            }else if(x==1){
		            	lay2[y]=temp;
		            	rotationLay2[y]=tempI;
		            }else{
		            	lay3[y]=temp;
		            	rotationLay3[y]=tempI;
		            }
		            y++;
		        }
		        sc.close();
		    } 
		    catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }
		}
	}
	public Block getlay1Block(int x,int y){
		return lay1[x][y];
	}
	public Block getlay2Block(int x,int y){
		return lay2[x][y];
	}
	public Block getlay3Block(int x,int y){
		return lay3[x][y];
	}
	public Block getBlock(int layer,int x,int y){
		if(layer==0){
			return lay1[x][y];
		}else if(layer==1){
			return lay2[x][y];
		}else{
			return lay3[x][y];
		}
	}
	public int getRotation(int layer,int x,int y){
		if(layer==0){
			return rotationLay1[x][y];
		}else if(layer==1){
			return rotationLay2[x][y];
		}else{
			return rotationLay3[x][y];
		}
	}
	public Block[][] getLayer(int x){
		switch(x){
			case 0:
				return lay1;
			case 1:
				return lay2;
			case 2:
				return lay3;
		}
		return null;
	}
}
