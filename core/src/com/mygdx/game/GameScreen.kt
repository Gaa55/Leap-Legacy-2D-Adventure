package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

class GameScreen(private var game: MainGame) : Screen {
    private var moveLeft = false
    private var moveRight = false
    var stage: Stage? = null
    private val skin: Skin? = null
    private var batch: SpriteBatch? = null
    private var playerTexture: Texture? = null
    var playerPosition = Vector2(0f, 0f)
    private var playerVelocity = Vector2(0f, 0f)
    private var groundRect: Rectangle? = null
    private var camera: OrthographicCamera? = null
    private var map: TiledMap? = null
    private var tiledMapRenderer: OrthogonalTiledMapRenderer? = null
    private var collisionLayer: TiledMapTileLayer? = null
    private val gameScreen: Screen? = null
    private val mainMenuScreen: Screen? = null
    private val isMainMenuActive = false
    var mapObjects: MapObjects? = null // Поле для хранения объектов карты
    private val gravity = -2f // Гравитация
    private val jumpVelocity = 30f // Скорость прыжка
    private val moveSpeed = 5f // Скорость движения

    init {
        game = game
    }

    override fun show() {
        batch = SpriteBatch()
        playerTexture = Texture("player.png")
        // Создаем загрузчик карты
        val parameters = TmxMapLoader.Parameters()
        parameters.textureMinFilter = Texture.TextureFilter.Linear
        parameters.textureMagFilter = Texture.TextureFilter.Linear

        // Используем FileHandleResolver для разрешения путей к ресурсам
        val resolver =
            FileHandleResolver { fileName ->
                // Проверяем имя файла тайлсета и возвращаем соответствующий FileHandle
                if (fileName == "TX Village Propss.tsx") {
                    return@FileHandleResolver Gdx.files.internal("TX Village Props.tsx")
                } else if (fileName == "TX Tileset Groundd.tsx") {
                    return@FileHandleResolver Gdx.files.internal("TX Tileset Ground.tsx")
                } else if (fileName == "Backgroundd.png") {
                    return@FileHandleResolver Gdx.files.internal("Background.png")
                } else if (fileName == "Leap_Legacy_2d_Adventure.tmx") {
                    return@FileHandleResolver Gdx.files.internal("Leap_Legacy_2d_Adventure.tmx")
                }
                null // Возвращаем null, если не удалось разрешить путь
            }
        if (map == null) {
            map = TmxMapLoader(resolver).load("Leap_Legacy_2d_Adventure.tmx", parameters)
        }
        if (map != null) {
            collisionLayer = map!!.layers["ground"] as TiledMapTileLayer
            if (collisionLayer == null) {
                collisionLayer = map!!.layers["ground"] as TiledMapTileLayer
            }
        }
        val mapObjects = map!!.layers["Trigger_final"].objects // получение объектов из слоя

        // Продолжайте работу с вашей загруженной картой здесь
        playerPosition = Vector2(700f, 350f)
        playerVelocity = Vector2(0f, 0f)
        groundRect = Rectangle(0f, 0f, Gdx.graphics.width.toFloat(), 20f)
        // Инициализация camera перед использованием
        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera!!.setToOrtho(false)

        // Другой ваш код...
        // Инициализация поля класса tiledMapRenderer
        tiledMapRenderer = OrthogonalTiledMapRenderer(map)
        tiledMapRenderer!!.setView(camera) // Установка камеры

        // Создание кнопки ImageButton вперед
        val forwardTexture = Texture(Gdx.files.internal("forward.png"))
        val forwardDrawable: Drawable = TextureRegionDrawable(TextureRegion(forwardTexture))
        val forward = ImageButton(forwardDrawable)
        forward.setSize(100f, 100f)
        forward.setPosition(200f, 30f)

        // Создание кнопки ImageButton назад
        val backwardTexture = Texture(Gdx.files.internal("backward.png"))
        val backwardDrawable: Drawable = TextureRegionDrawable(TextureRegion(backwardTexture))
        val backward = ImageButton(backwardDrawable)
        backward.setSize(100f, 100f)
        backward.setPosition(10f, 30f)

        // Создание кнопки ImageButton прыжок
        val jumpButtonTexture = Texture(Gdx.files.internal("jump.png"))
        val jumpButtonDrawable: Drawable = TextureRegionDrawable(TextureRegion(jumpButtonTexture))
        val jumpButton = ImageButton(jumpButtonDrawable)
        jumpButton.setSize(200f, 200f)
        jumpButton.setPosition(1300f, 30f)

        // Инициализация сцены и добавление кнопки на нее
        stage = Stage()
        Gdx.input.inputProcessor = stage
        stage!!.addActor(forward)
        stage!!.addActor(backward)
        stage!!.addActor(jumpButton)
        forward.addListener(object : ClickListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                moveRight = false
            }

            override fun touchDown(
                event: InputEvent,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                moveRight = true
                return true
            }
        })
        backward.addListener(object : ClickListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                moveLeft = false
            }

            override fun touchDown(
                event: InputEvent,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                moveLeft = true
                return true
            }
        })
        jumpButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                jump()
            }
        })
    }

    fun checkTriggers() {
        if (mapObjects != null) {
            for (mapObject in mapObjects!!) {
                if (mapObject.name != null && mapObject.name == "Trigger_final") {
                    game.setScreen(VictoryScreen(game)) // Переход на VictoryScreen
                    break // Если нужно выйти из цикла после обнаружения триггера
                }
            }
        } else {
            return
        }
    }

    override fun render(delta: Float) {
        // Clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        for (layer in map!!.layers) {
            println(layer.name)
        }
        handleInput() // Handle user input
        update() // Update game logic
        camera!!.position[playerPosition.x, playerPosition.y] = 0f
        camera!!.update()

        // Rendering the map
        tiledMapRenderer!!.setView(camera)
        tiledMapRenderer!!.render()

        // Rendering the player
        batch!!.projectionMatrix = camera!!.combined
        // Установка камеры для tiledMapRenderer перед его рендерингом
        tiledMapRenderer!!.setView(camera) // Обновляем вид камеры здесь
        tiledMapRenderer!!.render()
        batch!!.begin()
        batch!!.draw(playerTexture, playerPosition.x, playerPosition.y, 100f, 100f)
        batch!!.end()
        stage!!.act(delta) // Update the stage
        stage!!.draw() // Render the stage
    }

    private fun update() {
        // Add your game logic here
        playerVelocity.y += gravity // Применяем гравитацию
        playerPosition.add(playerVelocity) // Обновляем позицию игрока
        handleInput()
        checkTriggers()

        // Ограничение, чтобы игрок не уходил под землю
        if (playerPosition.y < 0) {
            playerPosition.y = 0f
            playerVelocity.y = 0f
        }
        // Обрабатываем коллизии по оси X
        handleXCollision()

        // Обрабатываем прыжки и коллизии по оси Y
        handleYCollision()

        // Обработка коллизий по оси Y при прыжке
        if (playerVelocity.y < 0) {
            val tileXBottomLeft = playerPosition.x.toInt() / collisionLayer!!.tileWidth
            val tileXBottomRight =
                (playerPosition.x + playerTexture!!.width).toInt() / collisionLayer!!.tileWidth
            val tileYBottom = (playerPosition.y - 1).toInt() / collisionLayer!!.tileHeight
            if (collisionLayer!!.getCell(
                    tileXBottomLeft,
                    tileYBottom
                ) != null || collisionLayer!!.getCell(tileXBottomRight, tileYBottom) != null
            ) {
                playerPosition.y = ((tileYBottom + 1) * collisionLayer!!.tileHeight).toFloat()
                playerVelocity.y = 0f
            }
        } else if (playerVelocity.y > 0) {
            val tileXTopLeft = playerPosition.x.toInt() / collisionLayer!!.tileWidth
            val tileXTopRight =
                (playerPosition.x + playerTexture!!.width).toInt() / collisionLayer!!.tileWidth
            val tileYTop =
                (playerPosition.y + playerTexture!!.height + 1).toInt() / collisionLayer!!.tileHeight
            if (collisionLayer!!.getCell(
                    tileXTopLeft,
                    tileYTop
                ) != null || collisionLayer!!.getCell(tileXTopRight, tileYTop) != null
            ) {
                playerPosition.y =
                    (tileYTop * collisionLayer!!.tileHeight - playerTexture!!.height).toFloat()
                playerVelocity.y = 0f
            }
        }

        // Проверка нажатия клавиш для движения
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerVelocity.x = -moveSpeed
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerVelocity.x = moveSpeed
        } else {
            playerVelocity.x = 0f
        }

        // Ограничение, чтобы игрок не уходил за границы экрана
        if (playerPosition.x < 0) {
            playerPosition.x = 0f
        }
    }

    private fun handleXCollision() {
        // Движение влево
    }

    private fun handleYCollision() {
        // Проверяем коллизии при падении вниз
        if (playerVelocity.y < 0) {
            val tileYBottom =
                (playerPosition.y + 1).toInt() / collisionLayer!!.tileHeight // Увеличиваем координату y, чтобы персонаж не застревал
            if (collisionLayer!!.getCell(
                    playerPosition.x.toInt() / collisionLayer!!.tileWidth,
                    tileYBottom
                ) != null ||
                collisionLayer!!.getCell(
                    (playerPosition.x + playerTexture!!.width - 1).toInt() / collisionLayer!!.tileWidth,
                    tileYBottom
                ) != null
            ) {
                playerPosition.y =
                    ((tileYBottom + 1) * collisionLayer!!.tileHeight).toFloat() // Устанавливаем игрока на уровень тайла
                playerVelocity.y = 0f
            }
        } else if (playerVelocity.y > 0) {
            val tileYTop =
                (playerPosition.y + playerTexture!!.height).toInt() / collisionLayer!!.tileHeight // Увеличиваем координату y, чтобы персонаж не застревал
            if (collisionLayer!!.getCell(
                    playerPosition.x.toInt() / collisionLayer!!.tileWidth,
                    tileYTop
                ) != null ||
                collisionLayer!!.getCell(
                    (playerPosition.x + playerTexture!!.width - 1).toInt() / collisionLayer!!.tileWidth,
                    tileYTop
                ) != null
            ) {
                playerPosition.y =
                    (tileYTop * collisionLayer!!.tileHeight - playerTexture!!.height - 1).toFloat() // Устанавливаем игрока на уровень тайла
                playerVelocity.y = 0f
            }
        }
    }

    private fun handleInput() {
        if (moveLeft) {
            if (!checkCollision(playerPosition.x - moveSpeed, playerPosition.y)) {
                playerPosition.x -= moveSpeed
            }
        } else if (moveRight) {
            if (!checkCollision(
                    playerPosition.x + moveSpeed + playerTexture!!.width,
                    playerPosition.y
                )
            ) {
                playerPosition.x += moveSpeed
            }
        }
    }

    private fun checkCollision(x: Float, y: Float): Boolean {
        val tileX = (x / collisionLayer!!.tileWidth).toInt()
        val tileY = (y / collisionLayer!!.tileHeight).toInt()
        return collisionLayer!!.getCell(tileX, tileY) != null
    }

    private fun jump() {
        playerVelocity.y = jumpVelocity
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {
        batch!!.dispose()
        playerTexture!!.dispose()
        map!!.dispose()
    }
}