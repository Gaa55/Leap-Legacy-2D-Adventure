package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

class GameScreen(private val game: MainGame) : Screen {

    private var moveLeft = false
    private var moveRight = false
    private val stage: Stage = Stage()
    private val batch: SpriteBatch = SpriteBatch()
    private val playerTexture: Texture = Texture("player.png")
    private var playerPosition = Vector2(0f, 0f)
    private var playerVelocity = Vector2(0f, 0f)
    private val groundRect: Rectangle = Rectangle(0f, 0f, Gdx.graphics.width.toFloat(), 20f)
    private val camera: OrthographicCamera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private var map: TiledMap? = null
    private var tiledMapRenderer: OrthogonalTiledMapRenderer? = null
    private var collisionLayer: TiledMapTileLayer? = null
    private var mapObjects: MapObjects? = null

    private val gravity = -2f
    private val jumpVelocity = 30f
    private val moveSpeed = 5f

    init {
        if (map == null) {
            val resolver = FileHandleResolver { fileName ->
                when (fileName) {
                    "TX Village Props.tsx" -> Gdx.files.internal("TX Village Props.tsx")
                    "TX Tileset Ground.tsx" -> Gdx.files.internal("TX Tileset Ground.tsx")
                    "Background.png" -> Gdx.files.internal("Background.png")
                    "GameMap.tmx" -> Gdx.files.internal("GameMap.tmx")
                    else -> null
                }
            }

            val parameters = TmxMapLoader.Parameters()
            parameters.textureMinFilter = Texture.TextureFilter.Linear
            parameters.textureMagFilter = Texture.TextureFilter.Linear

            map = TmxMapLoader(resolver).load("GameMap.tmx", parameters)
        }

        if (map != null) {
            collisionLayer = map!!.layers.get("Tlie_1") as? TiledMapTileLayer
            if (collisionLayer == null) {
                collisionLayer = map!!.layers.get("Tile_1") as? TiledMapTileLayer
            }
            mapObjects = map!!.layers.get("Trigger_final").objects
        }

        playerPosition.set(700f, 350f)
        playerVelocity.set(0f, 0f)
        groundRect.set(0f, 0f, Gdx.graphics.width.toFloat(), 20f)

        camera.setToOrtho(false)
        tiledMapRenderer = OrthogonalTiledMapRenderer(map)
        tiledMapRenderer?.setView(camera)

        val forwardTexture = Texture(Gdx.files.internal("forward.png"))
        val forwardDrawable: Drawable = TextureRegionDrawable(TextureRegion(forwardTexture))
        val forward = ImageButton(forwardDrawable)
        forward.setSize(100f, 100f)
        forward.setPosition(200f, 30f)

        val backwardTexture = Texture(Gdx.files.internal("backward.png"))
        val backwardDrawable: Drawable = TextureRegionDrawable(TextureRegion(backwardTexture))
        val backward = ImageButton(backwardDrawable)
        backward.setSize(100f, 100f)
        backward.setPosition(10f, 30f)

        val jumpButtonTexture = Texture(Gdx.files.internal("jump.png"))
        val jumpButtonDrawable: Drawable = TextureRegionDrawable(TextureRegion(jumpButtonTexture))
        val jumpButton = ImageButton(jumpButtonDrawable)
        jumpButton.setSize(200f, 200f)
        jumpButton.setPosition(1300f, 30f)

        stage.addActor(forward)
        stage.addActor(backward)
        stage.addActor(jumpButton)

        forward.addListener(object : ClickListener() {
            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                moveRight = false
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                moveRight = true
                return true
            }
        })

        backward.addListener(object : ClickListener() {
            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                moveLeft = false
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                moveLeft = true
                return true
            }
        })

        jumpButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                jump()
            }
        })
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        tiledMapRenderer?.setView(camera)
        tiledMapRenderer?.render()

        batch.projectionMatrix = camera.combined
        batch.begin()
        batch.draw(playerTexture, playerPosition.x, playerPosition.y, 100f, 100f)
        batch.end()

        stage.act(delta)
        stage.draw()

        handleInput()
        update()
    }

    private fun update() {
        // Update logic here
    }

    private fun handleInput() {
        // Handle input here
    }

    private fun jump() {
        // Jump logic here
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        batch.dispose()
        playerTexture.dispose()
        map?.dispose()
    }
}