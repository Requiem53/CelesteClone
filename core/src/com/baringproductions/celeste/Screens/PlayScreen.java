    package com.baringproductions.celeste.Screens;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.Screen;
    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.GL20;
    import com.badlogic.gdx.graphics.OrthographicCamera;
    import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
    import com.badlogic.gdx.maps.tiled.TiledMap;
    import com.badlogic.gdx.maps.tiled.TmxMapLoader;
    import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
    import com.badlogic.gdx.math.Rectangle;
    import com.badlogic.gdx.math.Vector2;
    import com.badlogic.gdx.physics.box2d.*;
    import com.badlogic.gdx.utils.ScreenUtils;
    import com.badlogic.gdx.utils.viewport.FitViewport;
    import com.badlogic.gdx.utils.viewport.Viewport;
    import com.baringproductions.celeste.Statics.Constants;
    import com.baringproductions.celeste.Tiles.*;
    import com.baringproductions.celeste.Utils.WorldCreator;
    import com.baringproductions.celeste.CelesteGame;
    import com.baringproductions.celeste.Player;
    import com.baringproductions.celeste.Utils.WorldListener;

    import java.util.ArrayList;

    public class PlayScreen implements Screen {

        // play screen vars
        private final CelesteGame game;
        private final OrthographicCamera camera;
        private final Viewport viewport;

        //Tiled map variables
        private TmxMapLoader maploader;
        private final TiledMap map;
        private final OrthogonalTiledMapRenderer renderer;

        //Box2d variables
        private final World world;
        private final Box2DDebugRenderer b2dr;
        private WorldCreator creator;

        public static Player player;
        public static Rectangle trackedPoint;
        public ShapeRenderer trackedPointDebug;
        public static ArrayList<SpawnPoint> spawnPoints;
        public int currSpawnPoint;

        public static Body trackedBody;
        public static Fixture trackedFixture;

        public static float trackedBodyWidth = CelesteGame.V_WIDTH/96f;
        public static float trackedBodyHeight = CelesteGame.V_HEIGHT/96f;

        public static ArrayList<MovingPlatform> movingPlatforms;
        public static ArrayList<CollapsingPlatform> collapsingPlatforms;
        public static ArrayList<DashGem> dashGems;
        public static ArrayList<Spring> springs;


        public PlayScreen(CelesteGame game) {
            currSpawnPoint = 0;
            spawnPoints = new ArrayList<>();
            movingPlatforms = new ArrayList<>();
            collapsingPlatforms = new ArrayList<>();
            dashGems = new ArrayList<>();
            springs = new ArrayList<>();

            this.game = game;

            camera = new OrthographicCamera();
            viewport = new FitViewport(
                    CelesteGame.V_WIDTH/48f,
                    CelesteGame.V_HEIGHT/48f, camera);

            maploader = new TmxMapLoader();
//            map = maploader.load("demo.tmx");
            map = maploader.load("map.tmx");
            renderer = new OrthogonalTiledMapRenderer(map, 1 / CelesteGame.PPM);
            camera.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);

            world = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, CelesteGame.GRAVITY), true);
            b2dr = new Box2DDebugRenderer();

            creator = new WorldCreator(this);

            trackedPoint = new Rectangle();
    //        trackedPoint.setPosition(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2);
            trackedPoint.setPosition(0, 0);
            trackedPoint.setWidth(CelesteGame.V_WIDTH/48f);
            trackedPoint.setHeight(CelesteGame.V_HEIGHT/48f);
            System.out.println("SIZE: " + trackedPoint.getWidth() + " " + trackedPoint.getHeight());

            BodyDef trackDef = new BodyDef();
            trackDef.type = BodyDef.BodyType.KinematicBody;
            trackDef.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2);

            trackedBody = world.createBody(trackDef);

            PolygonShape trackPoly = new PolygonShape();
            trackPoly.setAsBox(CelesteGame.V_WIDTH/96f ,CelesteGame.V_HEIGHT/96f);

            FixtureDef trackFixtureDef = new FixtureDef();
            trackFixtureDef.shape = trackPoly;
            trackFixtureDef.isSensor = true;

            trackedFixture = trackedBody.createFixture(trackFixtureDef);
            trackedFixture.setUserData("trackedBody");

            trackedBody = world.createBody(trackDef);
    //        trackedBody.setFixedRotation(true);

            trackedPointDebug = new ShapeRenderer();
            player = new Player(world);

            world.setContactListener(new WorldListener(player));

            //para mospawn sa spawn point, dili sa 0, 0
            spawnPoints.get(currSpawnPoint).respawnPlayer(player);
        }


    public void update(float dt) {
        player.allProcesses(dt);

            if (player.isDead) {
                resetLevelInteractiveTiles();
                spawnPoints.get(currSpawnPoint).respawnPlayer(player);
            }

            world.step(1/60f, 6, 2);

            player.update(dt);
    //        player.body.setLinearVelocity(0, player.body.getLinearVelocity().y);

    //        camera.position.x = player.body.getPosition().x;
    //        camera.position.y = player.body.getPosition().y;

            camera.position.x = trackedBody.getPosition().x;
            camera.position.y = trackedBody.getPosition().y;

            //WIDTH AND HEIGHT OF TRACKED POINT
    //        camera.position.x = trackedPoint.getWidth() / 2;
    //        camera.position.y = trackedPoint.getHeight() / 2;

    //
    //        System.out.println("SIZE: " + trackedPoint.getWidth() + " " + trackedPoint.getHeight());
    //        System.out.println("SIZE: " + trackedPoint.x + " " + trackedPoint.y);
    //
            camera.update();
            renderer.setView(camera);
        }

        public static void resetLevelInteractiveTiles(){
            for(MovingPlatform mplatform : movingPlatforms){
                mplatform.resetPosition();
            }
            for(CollapsingPlatform cPlatform : collapsingPlatforms){
                cPlatform.forceRespawn();
            }
            for(DashGem dashGem : dashGems){
                dashGem.forceRespawn();
            }
        }

        @Override
        public void show() {

        }

        @Override
        public void render(float dt) {

            update(dt);

            ScreenUtils.clear(0,0,0,1);

            renderer.render();
            b2dr.render(world, camera.combined);

            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();
    //        Gdx.gl.glEnable(GL20.GL_BLEND);
    //        trackedPointDebug.setProjectionMatrix(camera.combined);
    //        trackedPointDebug.begin(ShapeRenderer.ShapeType.Filled);
    //        trackedPointDebug.setColor(new Color(0, 1, 0, 0.5f));
    //        trackedPointDebug.rect(trackedPoint.x, trackedPoint.y, trackedPoint.width, trackedPoint.height);
    //        trackedPointDebug.end();

            for (MovingPlatform mplatform : movingPlatforms) {
                mplatform.update(dt);
                mplatform.sprite.draw(game.batch);
            }
            for(CollapsingPlatform cplatform : collapsingPlatforms){
                if(cplatform.collapsed) continue;
                cplatform.sprite.draw(game.batch);
                if(cplatform.isShaking) {
                    cplatform.updateShaking(dt);
                }
            }

            for(DashGem dashGem : dashGems){
                if(dashGem.isActive) dashGem.sprite.draw(game.batch);
            }

            for (Spring spring : springs) {
                spring.update(dt);
                spring.drawSpring(game.batch);
            }

            player.draw(game.batch);

            // draw stuff here
            game.batch.end();
        }

        public World getWorld() { return world; }
        public TiledMap getMap() { return map; }

        @Override
        public void resize(int width, int height) {
            viewport.update(width, height);
        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {

        }

        @Override
        public void hide() {

        }

        @Override
        public void dispose() {
            map.dispose();
            renderer.dispose();
            world.dispose();
            b2dr.dispose();
        }
    }
