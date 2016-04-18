package com.mygdx.game;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.BaseAnimationController.Transform;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MyGdxGame extends ApplicationAdapter implements ApplicationListener{
	public static final int WIDTH=1280;
	public static final int HEIGHT=960;
	ModelBatch mbatch;
	PerspectiveCamera cam;
	Model model;
	ArrayList<ModelInstance> instances=new ArrayList<ModelInstance>();
	ArrayList<Entity> e=new ArrayList<Entity>();
	ArrayList<Projectile> projectiles=new ArrayList<Projectile>();
	ModelInstance mi;
	Environment env;
	AssetManager am;
	boolean loading;
	Texture t;
	CameraInputController camController;
	Model play,fireball,sword;
    ModelInstance playerInstance,swordInstance;
    Model foot,body,arm,arm2;
    ModelInstance foot1I,foot2I,bodyI,arm1I,arm2I;
    Model Sfoot,Sbody,Sarm,Sarm2;
    ModelInstance Sfoot1I,Sfoot2I,SbodyI,Sarm1I,Sarm2I,SswordI;
    Player player;
    NPC skeleton1;
    boolean did911happen=false;
    private Stage stage;
    int initMouseX;
    int curStam;
    Texture[] fullstamina;
    Image[] fullhealth;
    int timeUntilFireball;
    ArrayList<ModelInstance> fireballInstances=new ArrayList<ModelInstance>();
    ArrayList<ModelInstance> Blocks=new ArrayList<ModelInstance>();
    ArrayList<Rectangle2D.Float> collisions=new ArrayList<Rectangle2D.Float>();
    Rectangle2D.Float cave=new Rectangle2D.Float(0,0,18f,20f);
    Level mainLevel;
    float light;
    String vertex,fragment;
	@Override
	public void create () {
		Gdx.input.setInputProcessor(stage);
        Gdx.input.setCursorCatched(true);
		initMouseX=Gdx.input.getX();
		cam=new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		cam.position.set(5f,10f,5f);
		cam.lookAt(0,0,0);
		cam.near=1f;
		cam.far=300f;
		cam.update();
		ModelBuilder mb=new ModelBuilder();
		env=new Environment();
		env.add();   
		light=.3f;
		env.set(new ColorAttribute(ColorAttribute.AmbientLight, light, light, light, 10f));
		env.add(new PointLight().set(.7f,.6f,.6f,5f,5f,5f, 30f));
		mbatch=new ModelBatch();
		//camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
		am = new AssetManager();
        am.load("models/Level.obj", Model.class);
        //am.load("models/chr_old.obj", Model.class);
        am.load("models/fireball.obj", Model.class);
        am.load("models/stone1.obj", Model.class);
        am.load("models/stone2.obj", Model.class);
        am.load("models/stone3.obj", Model.class);
        am.load("models/stone4.obj", Model.class);
        am.load("models/stone5.obj", Model.class);
        am.load("models/stone6.obj", Model.class);
        am.load("models/stone.obj", Model.class);
        am.load("models/stoneCeiling.obj", Model.class);
        am.load("models/grassStone.obj", Model.class);
        am.load("models/grass.obj", Model.class);
        am.load("models/dagger.obj", Model.class);
        am.load("models/tree.obj", Model.class);
        am.load("models/arm.obj", Model.class);
        am.load("models/arm2.obj", Model.class);
        am.load("models/foot.obj", Model.class);
        am.load("models/body2.obj", Model.class);
        am.load("models/SkeletonArm.obj", Model.class);
        am.load("models/SkeletonArm2.obj", Model.class);
        am.load("models/SkeletonFoot.obj", Model.class);
        am.load("models/SkeletonBody.obj", Model.class);
        loading = true;
        skeleton1=new NPC(5f,1f,5f,16,16,13,1/4f);
        player=new Player(5f,1f,5f,16,16,13,1/4f);
        
        e.add(new Entity(-12f,1f,-13f,4,26,10));
        e.add(new Entity(-8f,1f,-13f,23,4,10));
        e.add(new Entity(11f,1f,-12f,4,25,10));
        e.add(new Entity(-8f,1f,9f,5,4,10));
        e.add(new Entity(3f,1f,9f,5,4,10));
        
        
        stage=new Stage(new StretchViewport(WIDTH,HEIGHT));
        curStam=0;
        fullstamina=new Texture[6];
        fullhealth=new Image[6];
        for(int x=1;x<=6;x++){
        	fullstamina[x-1]=new Texture(Gdx.files.internal("images/stamina_full"+x+".png"));
        	//fullstamina[x-1].setScale(2);
            //fullstamina[x-1].setPosition(0, 960-2*fullstamina[x-1].getHeight());
            fullhealth[x-1]=new Image(new Texture(Gdx.files.internal("images/health_full"+x+".png")));
        	fullhealth[x-1].setScale(2);
            fullhealth[x-1].setPosition(0, 960-fullhealth[x-1].getHeight()*fullhealth[x-1].getScaleY());
        }
        
        //stage.addActor(fullstamina[curStam]);
        stage.addActor(fullhealth[curStam]);
        timeUntilFireball=0;
        
        Rectangle2D.Float rect1=new Rectangle2D.Float(0,0,11/8f,20f);
        Rectangle2D.Float rect2=new Rectangle2D.Float(0,0,16f,11/8f);
        Rectangle2D.Float rect3=new Rectangle2D.Float(0,20-11/8f,16f,11/8f);
        Rectangle2D.Float rect4=new Rectangle2D.Float(14f+5/8f,0,22/8f+4f,8f+11/8f);
        Rectangle2D.Float rect5=new Rectangle2D.Float(14f+5/8f,12f+5/8f,22/8f+4f,6f+11/8f);
        
        //Rectangle2D.Float rect2=new Rectangle2D.Float(0,11/8f,11/8f,7*2f);
        collisions.add(rect1);
        collisions.add(rect2);
        collisions.add(rect3);
        collisions.add(rect4);
        collisions.add(rect5);
        
        mainLevel=new Level();

	}
	private void doneLoading() {
        fireball=am.get("models/fireball.obj", Model.class);
        sword=am.get("models/dagger.obj",Model.class);
        body=am.get("models/body2.obj",Model.class);
        foot=am.get("models/foot.obj",Model.class);
        arm=am.get("models/arm.obj",Model.class);
        arm2=am.get("models/arm2.obj",Model.class);
        Sbody=am.get("models/SkeletonBody.obj",Model.class);
        Sfoot=am.get("models/SkeletonFoot.obj",Model.class);
        Sarm=am.get("models/SkeletonArm.obj",Model.class);
        Sarm2=am.get("models/SkeletonArm2.obj",Model.class);
        arm1I=new ModelInstance(arm);
        arm2I=new ModelInstance(arm2);
        foot1I=new ModelInstance(foot);
        foot2I=new ModelInstance(foot);
        bodyI=new ModelInstance(body);
        Sarm1I=new ModelInstance(Sarm);
        Sarm2I=new ModelInstance(Sarm2);
        Sfoot1I=new ModelInstance(Sfoot);
        Sfoot2I=new ModelInstance(Sfoot);
        SbodyI=new ModelInstance(Sbody);
        SswordI=new ModelInstance(sword);
        swordInstance=new ModelInstance(sword);
        for(Node n:bodyI.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        for(Node n:foot1I.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        for(Node n:foot2I.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        for(Node n:arm1I.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        for(Node n:arm2I.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        for(Node n:SbodyI.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        for(Node n:Sfoot1I.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        for(Node n:Sfoot2I.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        for(Node n:Sarm1I.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        for(Node n:Sarm2I.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        for(Node n:fireball.nodes){
        	n.scale.set(1/2f,1/2f,1/2f);
        }
        for(Node n:swordInstance.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        for(Node n:SswordI.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        swordInstance.calculateTransforms();
        swordInstance.transform.setToTranslation(0, 0f, 0f);
        SswordI.calculateTransforms();
        SswordI.transform.setToTranslation(0, 0f, 0f);
        bodyI.calculateTransforms();
        bodyI.transform.setToTranslation(0, 0f, 0f);
        foot1I.calculateTransforms();
        foot1I.transform.setToTranslation(0, 0f, 0f);
        foot2I.calculateTransforms();
        foot2I.transform.setToTranslation(0, 0f, 0f);
        arm1I.calculateTransforms();
        arm1I.transform.setToTranslation(0, 0f, 0f);
        arm2I.calculateTransforms();
        arm2I.transform.setToTranslation(0, 0f, 0f);
        SbodyI.calculateTransforms();
        SbodyI.transform.setToTranslation(0, 0f, 0f);
        Sfoot1I.calculateTransforms();
        Sfoot1I.transform.setToTranslation(0, 0f, 0f);
        Sfoot2I.calculateTransforms();
        Sfoot2I.transform.setToTranslation(0, 0f, 0f);
        Sarm1I.calculateTransforms();
        Sarm1I.transform.setToTranslation(0, 0f, 0f);
        Sarm2I.calculateTransforms();
        Sarm2I.transform.setToTranslation(0, 0f, 0f);
        int[] lengths={mainLevel.lay1.length,mainLevel.lay2.length,mainLevel.lay3.length};
        for(int x=0;x<3;x++){
        	for(int y=0;y<mainLevel.getLayer(x).length;y++){
        		for(int z=0;z<mainLevel.getLayer(x)[y].length;z++){
        			//System.out.println(mainLevel.lay1[y].length);
        			if(mainLevel.getBlock(x, y, z)!=null){
        				Model tempM=am.get("models/"+mainLevel.getBlock(x, y, z).getFile()+".obj",Model.class);
        				ModelInstance temp=new ModelInstance(tempM);
        				temp.transform.setToTranslation(y*2, x*2-2, z*2);
        				for(Node n:temp.nodes){
        					n.scale.set(1/8f,1/8f,1/8f);
        				}
        				temp.calculateTransforms();
        				Quaternion tempQ=new Quaternion();
        				tempQ.setFromAxis(new Vector3(0,-1,0),90*mainLevel.getRotation(x, y, z));
        				Matrix4 tempM4=new Matrix4();
        				tempM4.set(new Vector3(y*2f, x*2f-2f, z*2f),tempQ);
        				temp.transform.set(tempM4);
    		        
        				Blocks.add(temp);
        			}
        		}
        	}
        }
        instances.add(bodyI);
        instances.add(foot1I);
        instances.add(foot2I);
        instances.add(arm1I);
        instances.add(arm2I);
        instances.add(swordInstance);
        instances.add(SbodyI);
        instances.add(Sfoot1I);
        instances.add(Sfoot2I);
        instances.add(Sarm1I);
        instances.add(Sarm2I);
        instances.add(SswordI);
        loading = false;
    }

	@Override
	public void render () {
		if(loading&&am.update()){
			doneLoading();
		}
		
		//controls
				if(Gdx.input.isKeyPressed(Input.Keys.W)){
					player.keyPressed('W');
				}else{
					player.keyReleased('W');
				}
				if(Gdx.input.isKeyPressed(Input.Keys.A)){
					player.keyPressed('A');
				}else{
					player.keyReleased('A');
				}
				if(Gdx.input.isKeyPressed(Input.Keys.S)){
					player.keyPressed('S');
				}else{
					player.keyReleased('S');
				}
				if(Gdx.input.isKeyPressed(Input.Keys.D)){
					player.keyPressed('D');
				}else{
					player.keyReleased('D');
				}
				if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
					player.keyPressed('E');
				}else{
					player.keyReleased('E');
				}
				if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
					player.keyPressed(' ');
				}else{
					player.keyReleased(' ');
				}
				if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
					player.mousePressed('r');
				}else{
					player.mouseReleased('r');
				}
				player.setMousePos((Gdx.input.getX()-initMouseX)/2,Gdx.input.getY());
				Matrix4 m4=player.update(collisions);
				skeleton1.update(collisions, new Vector2(player.getX(),player.getZ()));
				if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
					if(timeUntilFireball==0){
						float x6=(float)(Math.cos(Math.toRadians(((player.getAngle()-initMouseX)+225)%360)))*player.getWidth();
						float y6=(float)(Math.sin(Math.toRadians(((player.getAngle()-initMouseX)+225)%360)))*player.getWidth();
						projectiles.add(new Projectile((float)(player.getX()+Math.cos(Math.toRadians(((player.getAngle()-initMouseX)+225)%360)))*player.getWidth()/8,player.getY(),(float)(player.getZ()+Math.sin(Math.toRadians(((player.getAngle()-initMouseX)+225)%360)))*player.getWidth()/8,5,5,8,(float)(((player.getAngle()-initMouseX)+225)%360),.5f,fireball));
						timeUntilFireball=15;
						System.out.println("fire");
					}
				}
				if(timeUntilFireball>0){
					timeUntilFireball--;
				}
				for(Projectile p2:projectiles){
					if(!fireballInstances.contains(p2.getModelInstance())){
						float x6=(float)(Math.cos(Math.toRadians(((player.getAngle()-initMouseX)+225)%360)))*player.getWidth()/8;
						float y6=(float)(Math.sin(Math.toRadians(((player.getAngle()-initMouseX)+225)%360)))*player.getWidth()/8;
						Matrix4 playerM4=new Matrix4();
						Quaternion playerQ=new Quaternion();
						playerQ.setFromAxis(new Vector3(0,-1,0),((player.getAngle()-initMouseX)+135)%360);
						playerM4.set(new Vector3(player.getX()+x6, player.getY(), player.getZ()+y6),playerQ);
						ModelInstance m=p2.getModelInstance();
						m.transform.set(playerM4);
						for(Node n:m.nodes){
				        	n.scale.set(1/4f,1/4f,1/4f);
				        }
						m.calculateTransforms();
						fireballInstances.add(m);
					}
				}
				if(instances.size()>0){
					Matrix4 playerM4=new Matrix4();
					Quaternion playerQ=new Quaternion();
					playerQ.setFromAxis(new Vector3(0,-1,0),((player.getAngle()-initMouseX)+315)%360);
					Quaternion skeletonQ=new Quaternion();
					skeletonQ.setFromAxis(new Vector3(0,-1,0),90);
					//System.out.println(arg0);
					ArrayList<Matrix4> playerm4s=player.getTransforms(playerQ);
					ArrayList<Matrix4> skeletonm4s=skeleton1.getTransforms(skeletonQ);
					for(int x=0;x<playerm4s.size();x++){
						instances.get(x).transform.set(playerm4s.get(x));
					}
					for(int x=0;x<skeletonm4s.size();x++){
						instances.get(x+playerm4s.size()).transform.set(skeletonm4s.get(x));
					}
					cam.position.set((float)(5f*Math.cos(Math.toRadians(((player.getAngle()-initMouseX)+45)%360))+player.getX()/*-player.getWidth()/16*/),7f+player.getY()+player.getHeight()/8+(float)(Math.sin(Math.PI*4*player.getRunTime())*-Math.PI/6)/10,(float)(5f*Math.sin(Math.toRadians(((player.getAngle()-initMouseX)+45)%360))+player.getZ()/*-player.getLength()/16*/));
					//cam.lookAt(player.getX(),player.getY(),player.getZ());
					cam.rotate(m4);
					//instances.get(1).transform.setFromEulerAngles(((player.getAngle()-initMouseX)+45)%360,0f,0f);
					//instances.get(1).transform.setToRotation(new Vector3(0,-1,0),((player.getAngle()-initMouseX)+135)%360);
					cam.update();
				}
				//for(ModelInstance p:fireballInstances){
					//System.out.println("hi");
					//p.transform.translate(0f,0f,projectiles.get(0).getSpeed());
				//}
				for(int x=0;x<projectiles.size();x++){
					if(projectiles.get(x).update(collisions)){
						fireballInstances.get(x).transform.translate(0f,0f,projectiles.get(x).getSpeed());
					}else{
						fireballInstances.remove(x);
						projectiles.remove(x);
						x--;
					}
					
				}
				if(player.getRect().intersects(cave)&&light>.3f){
					light-=.01f;
				}else if(!player.getRect().intersects(cave)&&light<.5f){
					light+=.01f;
				}
				env.set(new ColorAttribute(ColorAttribute.AmbientLight, light, light, light, 10f));
				env.set(new ColorAttribute(ColorAttribute.Fog));
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
		
		mbatch.begin(cam);
		mbatch.render(instances,env);
		mbatch.render(Blocks,env);
		mbatch.render(fireballInstances,env);
		stage.act();
	    
		mbatch.end();
		updateStage();
		stage.draw();
		
	}
	private void updateStage() {
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		if(curStam<35){
			curStam++;
		}else{
			curStam=0;
		}
		stage.clear();
		TextureRegion tr=new TextureRegion(fullstamina[curStam/6]);
		tr.setRegion(0, 0, (int)(160f*player.getStamina()/100f),22);
		Image stamI=new Image(tr);
		Image backgroundI=new Image(new Texture(Gdx.files.internal("images/health_empty.png")));
		stamI.setScale(2f);
		stamI.setX(0);
		stamI.setY(HEIGHT-44);
		backgroundI.setScale(2f);
		backgroundI.setX(0);
		backgroundI.setY(HEIGHT-44);
		stage.addActor(backgroundI);
        stage.addActor(stamI);
	}
	public void dispose(){
		mbatch.dispose();
		stage.dispose();
		
	}
	public void resize(int width,int height){
		stage.getViewport().update(width, height, true);
	}

}