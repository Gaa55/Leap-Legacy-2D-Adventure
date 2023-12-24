package com.mygdx.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.graphics.Color

class VictoryScreen(private val game: Game) : Screen {

    private var stage: Stage = Stage(ScreenViewport())

    override fun show() {
        stage = Stage(ScreenViewport())

        val yourBitmapFont = BitmapFont(Gdx.files.internal("com/badlogic/gdx/utils/default.fnt"), false)
        val labelStyle = Label.LabelStyle()
        labelStyle.font = yourBitmapFont
        labelStyle.fontColor = Color.WHITE

        val label = Label("Вы победили!!!!!", labelStyle)
        label.setPosition(640f, 640f)

        val textButtonStyle = TextButton.TextButtonStyle()
        textButtonStyle.font = yourBitmapFont
        textButtonStyle.fontColor = Color.WHITE

        val backButton = TextButton("В меню", textButtonStyle)
        backButton.setPosition(300f, 200f)
        backButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                // Действия по нажатию кнопки "В меню"
            }
        })

        stage.addActor(label)
        stage.addActor(backButton)

        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}
}