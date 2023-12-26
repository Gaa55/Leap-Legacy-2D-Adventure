package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

class VictoryScreen(private val maingame: MainGame) : Screen {

    private var stage: Stage = Stage(ScreenViewport())
    private val font: BitmapFont = BitmapFont()

    override fun show() {
        val texture = Texture(Gdx.files.internal("Default.png"))
        val buttonStyle = ImageButton.ImageButtonStyle()
        buttonStyle.up = TextureRegionDrawable(texture)

        val button = ImageButton(buttonStyle)
        button.setPosition(300f, 200f)
        button.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                // Переход обратно в главное меню при нажатии кнопки "домой"
                maingame.screen = MainMenuScreen(maingame)
            }
        })

        val labelStyle = Label.LabelStyle(font, font.color) // Создание стиля для текста
        val label = Label("Вы победили!", labelStyle) // Создание объекта Label с текстом "Вы победили!"

        label.setPosition(300f, 400f) // Установка позиции надписи на экране

        stage.addActor(label) // Добавление надписи на сцену
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

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