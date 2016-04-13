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
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
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
	ModelBatch mbatch;
	SpriteBatch sbatch;
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
	Model play,road,fireball,grass,stone1,stone2,sword;
    ModelInstance roadInstance,playerInstance,grassInstance,stone1Instance,stone2Instance,swordInstance;
    Model foot,body,arm;
    ModelInstance foot1I,foot2I,bodyI,arm1I,arm2I;
    Player player;
    boolean did911happen=false;
    private Stage stage;
    private Skin skin;
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
    
	@Override
	public void create () {
		Gdx.input.setInputProcessor(stage);
        Gdx.input.setCursorCatched(true);
		initMouseX=Gdx.input.getX();
		sbatch=new SpriteBatch();
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
		env.add(new PointLight().set(10f,10f,10f,5f,5f,5f, 15f));
		mbatch=new ModelBatch();
		//camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
		am = new AssetManager();
        am.load("models/Level.obj", Model.class);
        am.load("models/chr_old.obj", Model.class);
        am.load("models/fireball.obj", Model.class);
        am.load("models/stone1.obj", Model.class);
        am.load("models/stone2.obj", Model.class);
        am.load("models/stone3.obj", Model.class);
        am.load("models/stone4.obj", Model.class);
        am.load("models/stone5.obj", Model.class);
        am.load("models/stone6.obj", Model.class);
        am.load("models/stone.obj", Model.class);
        am.load("models/grassStone.obj", Model.class);
        am.load("models/grass.obj", Model.class);
        am.load("models/sword.obj", Model.class);
        am.load("models/tree.obj", Model.class);
        am.load("models/arm.obj", Model.class);
        am.load("models/foot.obj", Model.class);
        am.load("models/body.obj", Model.class);
        loading = true;
        player=new Player(5f,0f,5f,16,16,13,1/4f);
        e.add(new Entity(-12f,1f,-13f,4,26,10));
        e.add(new Entity(-8f,1f,-13f,23,4,10));
        e.add(new Entity(11f,1f,-12f,4,25,10));
        e.add(new Entity(-8f,1f,9f,5,4,10));
        e.add(new Entity(3f,1f,9f,5,4,10));
        
        
        stage=new Stage(new StretchViewport(1280,960));
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
        
        Rectangle2D.Float rect1=new Rectangle2D.Float(0,0,11/8f,1f);
        
        //Rectangle2D.Float rect2=new Rectangle2D.Float(0,11/8f,11/8f,7*2f);
        collisions.add(rect1);
        //collisions.add(rect2);
        
        mainLevel=new Level();

	}
	private void doneLoading() {
		play=am.get("models/chr_old.obj", Model.class);
        road = am.get("models/Level.obj", Model.class);
        fireball=am.get("models/fireball.obj", Model.class);
        stone1=am.get("models/stone1.obj", Model.class);
        stone2=am.get("models/stone2.obj", Model.class);
        grass=am.get("models/grass.obj", Model.class);
        sword=am.get("models/sword.obj",Model.class);
        body=am.get("models/body.obj",Model.class);
        foot=am.get("models/foot.obj",Model.class);
        arm=am.get("models/arm.obj",Model.class);
        arm1I=new ModelInstance(arm);
        arm2I=new ModelInstance(arm);
        foot1I=new ModelInstance(foot);
        foot2I=new ModelInstance(foot);
        bodyI=new ModelInstance(body);
        swordInstance=new ModelInstance(sword);
        roadInstance = new ModelInstance(road); 
        //instances.add(roadInstance);
        playerInstance = new ModelInstance(play); 
        stone1Instance=new ModelInstance(stone1);
        stone2Instance=new ModelInstance(stone2);
        grassInstance=new ModelInstance(grass);
        for(Node n:playerInstance.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        for(Node n:playerInstance.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
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
        for(Node n:stone2Instance.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        for(Node n:grassInstance.nodes){
        	n.scale.set(1/8f,1/8f,1/8f);
        }
        for(Node n:fireball.nodes){
        	n.scale.set(1/2f,1/2f,1/2f);
        }
        for(Node n:swordInstance.nodes){
        	n.scale.set(1/2f,1/2f,1/2f);
        }
        swordInstance.calculateTransforms();
        swordInstance.transform.setToTranslation(0, 0f, 0f);
        bodyI.calculateTransforms();
        bodyI.transform.setToTranslation(0, 0f, 0f);
        playerInstance.calculateTransforms();
        playerInstance.transform.setToTranslation(0, 0f, 0f);
        playerInstance.calculateTransforms();
        playerInstance.transform.setToTranslation(0, 0f, 0f);
        playerInstance.calculateTransforms();
        playerInstance.transform.setToTranslation(0, 0f, 0f);
        playerInstance.calculateTransforms();
        playerInstance.transform.setToTranslation(0, 0f, 0f);
        grassInstance.calculateTransforms();
        grassInstance.transform.setToTranslation(0, 0f, 0f);
        stone1Instance.calculateTransforms();
        stone1Instance.transform.setToTranslation(0, 0f, 0f);
        stone2Instance.calculateTransforms();
        stone2Instance.transform.setToTranslation(0, 0f, 0f);
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
        instances.add(playerInstance);
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
				player.setMousePos((Gdx.input.getX()-initMouseX)/2,Gdx.input.getY());
				Matrix4 m4=player.update(collisions);
				if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
					if(timeUntilFireball==0){
						float x6=(float)(Math.cos(Math.toRadians(((player.getAngle()-initMouseX)+225)%360)))*player.getWidth();
						float y6=(float)(Math.sin(Math.toRadians(((player.getAngle()-initMouseX)+225)%360)))*player.getWidth();
						System.out.println("X:"+x6+" Y:"+y6);
						projectiles.add(new Projectile((float)(player.getX()+Math.cos(Math.toRadians(((player.getAngle()-initMouseX)+225)%360)))*player.getWidth()/8,player.getY(),(float)(player.getZ()+Math.sin(Math.toRadians(((player.getAngle()-initMouseX)+225)%360)))*player.getWidth()/8,5,5,8,player.getAngle(),2f,fireball));
						timeUntilFireball=15;
					}else if(timeUntilFireball>0){
						timeUntilFireball--;
					}
				}
				
				for(Projectile p2:projectiles){
					p2.update();
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
					playerQ.setFromAxis(new Vector3(0,-1,0),((player.getAngle()-initMouseX)+135)%360);
					playerM4.set(new Vector3(player.getX(), player.getY(), player.getZ()),playerQ);
					instances.get(0).transform.set(playerM4);//setToTranslation(player.getX(), player.getY(), player.getZ());
					//instances.get(1).transform.
					cam.position.set((float)(3.75f*Math.cos(Math.toRadians(((player.getAngle()-initMouseX)+45)%360))+player.getX()/*-player.getWidth()/16*/),5f+player.getY()+player.getHeight()/8/*+player.getHeight()/8*/,(float)(3.75f*Math.sin(Math.toRadians(((player.getAngle()-initMouseX)+45)%360))+player.getZ()/*-player.getLength()/16*/));
					//cam.lookAt(player.getX(),player.getY(),player.getZ());
					cam.rotate(m4);
					//instances.get(1).transform.setFromEulerAngles(((player.getAngle()-initMouseX)+45)%360,0f,0f);
					//instances.get(1).transform.setToRotation(new Vector3(0,-1,0),((player.getAngle()-initMouseX)+135)%360);
					cam.update();
				}
				for(Projectile p:projectiles){
					p.update();
				}
				if(player.getRect().intersects(cave)&&light>.3f){
					light-=.01f;
				}else if(!player.getRect().intersects(cave)&&light<.5f){
					light+=.01f;
				}
				env.set(new ColorAttribute(ColorAttribute.AmbientLight, light, light, light, 10f));
				
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
		sbatch.setProjectionMatrix(cam.combined);
		sbatch.enableBlending();
		
		mbatch.begin(cam);
		mbatch.render(instances,env);
		mbatch.render(Blocks,env);
		mbatch.render(fireballInstances,env);
		/*if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
			float x6=(float)(Math.cos(Math.toRadians(((player.getAngle()-initMouseX)+225)%360)))*player.getWidth();
			float y6=(float)(Math.sin(Math.toRadians(((player.getAngle()-initMouseX)+225)%360)))*player.getWidth();
			System.out.println("X:"+x6+" Y:"+y6);
			swordInstance=new Projectile((float)(player.getX()+Math.cos(Math.toRadians(((player.getAngle()-initMouseX)+225)%360)))*player.getWidth()/8,player.getY(),(float)(player.getZ()+Math.sin(Math.toRadians(((player.getAngle()-initMouseX)+225)%360)))*player.getWidth()/8,5,5,8,player.getAngle(),2f,fireball));
		}*/
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
		//TextureRegionDrawable trd=new TextureRegionDrawable(tr);
		Image stamI=new Image(tr);
		Image backgroundI=new Image(new Texture(Gdx.files.internal("images/health_empty.png")));
		stamI.setScale(2f);
		stamI.setX(0);
		stamI.setY(960-44);
		backgroundI.setScale(2f);
		backgroundI.setX(0);
		backgroundI.setY(960-44);
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
