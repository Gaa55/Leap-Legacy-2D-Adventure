package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.ScreenViewport

class MainMenuScreen(private val maingame:MainGame) : Screen {

    private val stage: Stage = Stage(ScreenViewport())
    private val batch: SpriteBatch = SpriteBatch()
    private val backgroundSprite: Sprite
    private val font: BitmapFont = BitmapFont()

    init {
        val startTexture = Texture(Gdx.files.internal("Play@2x.png"))
        val vikTexture=Texture(Gdx.files.internal("Levels.png"))
        val backgroundTexture = Texture(Gdx.files.internal("sky.png"))
        backgroundSprite = Sprite(backgroundTexture)

        val startDrawable: Drawable = TextureRegionDrawable(TextureRegion(startTexture))
        val start = ImageButton(startDrawable)
        start.setSize(200f, 200f)
        start.setPosition(640f, 30f)

        start.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val gameScreen = GameScreen(maingame)
                maingame.setScreen(gameScreen)
            }
        })
        val vikDrawable: Drawable = TextureRegionDrawable(TextureRegion(vikTexture))
        val vik = ImageButton(vikDrawable)
        vik.setSize(200f, 200f)
        vik.setPosition(940f, 30f)

        vik.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val victoryScreen = VictoryScreen(maingame)
                maingame.setScreen(victoryScreen)
            }
        })

        stage.addActor(start)
        stage.addActor(vik)
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        backgroundSprite.draw(batch)
        font.color = Color.BLACK
        font.data.setScale(4f)
        font.draw(batch, "Leap Legacy: 2D Adventure", 300f, 300f)
        batch.end()

        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}
}