package com.mygdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen

class MainGame: Game() {

    private lateinit var gameScreen: Screen
    private lateinit var mainMenuScreen: Screen
    private lateinit var victoryScreen: Screen

    private var isMainMenuActive: Boolean = false

    override fun create() {
        gameScreen = GameScreen(this) // Создание экрана игры
        mainMenuScreen = MainMenuScreen(this) // Создание экрана главного меню
        setScreenToMainMenu()
        victoryScreen = VictoryScreen(this)
    }

    fun setScreenToMainMenu() {
        val mainMenuScreen = MainMenuScreen(this) // Создание экземпляра MainMenuScreen
        setScreen(mainMenuScreen) // Установка MainMenuScreen в качестве текущего экрана
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {

    }
}