package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

class VictoryScreen(private val maingame: MainGame) : Screen {

    private var stage: Stage = Stage(ScreenViewport())
    private val batch: SpriteBatch = SpriteBatch()
    private val backgroundSprite: Sprite = Sprite(Texture(Gdx.files.internal("sky.png")))
    private val font: BitmapFont = BitmapFont()


    override fun show() {
        val texture = Texture(Gdx.files.internal("Default.png"))
        val buttonStyle = ImageButton.ImageButtonStyle()
        buttonStyle.up = TextureRegionDrawable(texture)

        val button = ImageButton(buttonStyle)
        button.setPosition(700f, 200f)
        button.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                // Переход обратно в главное меню при нажатии кнопки "домой"
                maingame.screen = MainMenuScreen(maingame)
            }
        })

        val labelStyle = Label.LabelStyle(font, font.color) // Создание стиля для текста
        val label = Label("you win!", labelStyle) // Создание объекта Label с текстом "Вы победили!"
        label.setPosition(300f, 400f) // Установка позиции надписи на экране
        stage.addActor(label) // Добавление надписи на сцену
        stage.addActor(button)
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()

        backgroundSprite.draw(batch)
        font.color = Color.BLACK
        font.data.setScale(4f)
        font.draw(batch, "You win!!!", 300f, 300f)
        batch.end()

        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        stage.dispose()
        font.dispose()
    }
}