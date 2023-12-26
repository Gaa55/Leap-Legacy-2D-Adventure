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
import com.badlogic.gdx.maps.objects.RectangleMapObject
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
    private var jumpCount = 0
    private val maxJumps = 2 // Максимальное количество прыжков

    init {
        game = game
    }

    override fun show() {
        batch = SpriteBatch()
        playerTexture = Texture("Pink_Monster.png")
        // Создаем загрузчик карты
        val parameters = TmxMapLoader.Parameters()
        parameters.textureMinFilter = Texture.TextureFilter.Linear
        parameters.textureMagFilter = Texture.TextureFilter.Linear

        // Используем FileHandleResolver для разрешения путей к ресурсам
        val resolver =
            FileHandleResolver { fileName ->
                // Проверяем имя файла тайлсета и возвращаем соответствующий FileHandle
                if (fileName == "TX Village Props.tsx") {
                    return@FileHandleResolver Gdx.files.internal("TX Village Props.tsx")
                } else if (fileName == "TX Tileset Ground.tsx") {
                    return@FileHandleResolver Gdx.files.internal("TX Tileset Ground.tsx")
                } else if (fileName == "Background.png") {
                    return@FileHandleResolver Gdx.files.internal("Background.png")
                } else if (fileName == "GameMap.tmx") {
                    return@FileHandleResolver Gdx.files.internal("GameMap.tmx")
                }
                null // Возвращаем null, если не удалось разрешить путь
            }
        if (map == null) {
            map = TmxMapLoader(resolver).load("GameMap.tmx", parameters)
        }
        if (map != null) {
            collisionLayer = map!!.layers["Tile_1"] as TiledMapTileLayer
            if (collisionLayer == null) {
                collisionLayer = map!!.layers["Tile_1"] as TiledMapTileLayer
            }
        }
        mapObjects = map!!.layers["Trigger_final"].objects

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
        val playerBounds = Rectangle(
            playerPosition.x,
            playerPosition.y,
            playerTexture!!.width.toFloat(),
            playerTexture!!.height.toFloat()
        )

        mapObjects = map!!.layers["Trigger_final"].objects // Обновляем mapObjects

        if (mapObjects != null) {
            for (mapObject in mapObjects!!) {
                // Логика обработки столкновений
                if (mapObject is RectangleMapObject) {
                    val rectangle = mapObject.rectangle
                    val x = rectangle.x
                    val y = rectangle.y
                    val width = rectangle.width
                    val height = rectangle.height

                    val bounds = Rectangle(x, y, width, height)
                    if (playerBounds.overlaps(bounds)) {
                        game.setScreen(VictoryScreen(game))
                        break
                    }
                }
            }
        }
    }


    override fun render(delta: Float) {
        // Clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        for (layer in map!!.layers) {
            println(layer.name)
        }
        checkTriggers()
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
            if (playerVelocity.y == 0f) { // Если игрок не движется по оси Y (приземлился)
                jumpCount = 0 // Сброс счетчика прыжков
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

        // Ограничение, чтобы игрок не уходил за границы экрана
        if (playerPosition.x < 0) {
            playerPosition.x = 0f
        }
        val rightBoundary = (collisionLayer!!.width * collisionLayer!!.tileWidth).toFloat()
        val playerRightEdge = playerPosition.x + playerTexture!!.width
        if (playerRightEdge > rightBoundary) {
            playerPosition.x = rightBoundary - playerTexture!!.width
        }
    }

    private fun handleXCollision() {
        val newPositionX = playerPosition.x + playerVelocity.x
        val playerBounds = Rectangle(newPositionX, playerPosition.y, playerTexture!!.width.toFloat(), playerTexture!!.height.toFloat())

        for (x in 0 until collisionLayer!!.width) {
            for (y in 0 until collisionLayer!!.height) {
                val cell = collisionLayer!!.getCell(x, y)
                if (cell != null) {
                    val tileBounds = Rectangle(x * collisionLayer!!.tileWidth.toFloat(), y * collisionLayer!!.tileHeight.toFloat(), collisionLayer!!.tileWidth.toFloat(), collisionLayer!!.tileHeight.toFloat())

                    if (playerBounds.overlaps(tileBounds)) {
                        playerVelocity.x = 0f // Останавливаем движение по X
                        if (playerVelocity.x > 0) {
                            playerPosition.x = tileBounds.x - playerTexture!!.width
                        } else if (playerVelocity.x < 0) {
                            playerPosition.x = tileBounds.x + tileBounds.width
                        }
                    }
                }
            }
        }
        }

    private fun handleYCollision() {
        val newPositionY = playerPosition.y + playerVelocity.y
        val playerBounds = Rectangle(playerPosition.x, newPositionY, playerTexture!!.width.toFloat(), playerTexture!!.height.toFloat())

        for (x in 0 until collisionLayer!!.width) {
            for (y in 0 until collisionLayer!!.height) {
                val cell = collisionLayer!!.getCell(x, y)
                if (cell != null) {
                    val tileBounds = Rectangle(x * collisionLayer!!.tileWidth.toFloat(), y * collisionLayer!!.tileHeight.toFloat(), collisionLayer!!.tileWidth.toFloat(), collisionLayer!!.tileHeight.toFloat())

                    if (cell.tile.properties.containsKey("collision")) {
                        if (playerBounds.overlaps(tileBounds)) {
                            playerVelocity.y = 0f // Останавливаем движение по Y
                            if (playerVelocity.y < 0) {
                                playerPosition.y = tileBounds.y + tileBounds.height
                            } else if (playerVelocity.y > 0) {
                                playerPosition.y = tileBounds.y - playerTexture!!.height
                            }
                        }
                    }
                }
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
        if (jumpCount < maxJumps) {
            playerVelocity.y = jumpVelocity
            jumpCount++

        }
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