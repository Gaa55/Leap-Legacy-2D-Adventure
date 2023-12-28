package com.mygdx.game

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
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
    private val maxJumps = 2
    private var backgroundMusic: Music? = null
    private lateinit var jumpAnimation: Animation<TextureRegion>
    private lateinit var walkAnimation: Animation<TextureRegion>
    private var elapsedTime = 0f

    init {
        game = game

        val jumpFrame1Texture = Texture(Gdx.files.internal("Pink_Monster_Jump_1_1.png"))
        val jumpFrame2Texture = Texture(Gdx.files.internal("Pink_Monster_Jump_1_2.png"))
        val jumpFrame3Texture = Texture(Gdx.files.internal("Pink_Monster_Jump_1_3.png"))
        val jumpFrame4Texture = Texture(Gdx.files.internal("Pink_Monster_Jump_1_4.png"))
        val jumpFrame5Texture = Texture(Gdx.files.internal("Pink_Monster_Jump_1_5.png"))
        val jumpFrame6Texture = Texture(Gdx.files.internal("Pink_Monster_Jump_1_6.png"))
        val jumpFrame7Texture = Texture(Gdx.files.internal("Pink_Monster_Jump_1_7.png"))
        val jumpFrame8Texture = Texture(Gdx.files.internal("Pink_Monster_Jump_1_8.png"))
        val jumpFrame1 = TextureRegion(jumpFrame1Texture)
        val jumpFrame2 = TextureRegion(jumpFrame2Texture)
        val jumpFrame3 = TextureRegion(jumpFrame3Texture)
        val jumpFrame4 = TextureRegion(jumpFrame4Texture)
        val jumpFrame5 = TextureRegion(jumpFrame5Texture)
        val jumpFrame6 = TextureRegion(jumpFrame6Texture)
        val jumpFrame7 = TextureRegion(jumpFrame7Texture)
        val jumpFrame8 = TextureRegion(jumpFrame8Texture)
        val jumpFrames = arrayOf(jumpFrame1, jumpFrame2, jumpFrame3, jumpFrame4, jumpFrame5, jumpFrame6, jumpFrame7, jumpFrame8)
        jumpAnimation = Animation(0.1f, *jumpFrames)
        jumpAnimation?.playMode = Animation.PlayMode.NORMAL

        val walkFrame1Texture = Texture(Gdx.files.internal("Pink_Monster_Run_1_1.png"))
        val walkFrame2Texture = Texture(Gdx.files.internal("Pink_Monster_Run_1_2.png"))
        val walkFrame3Texture = Texture(Gdx.files.internal("Pink_Monster_Run_1_3.png"))
        val walkFrame4Texture = Texture(Gdx.files.internal("Pink_Monster_Run_1_4.png"))
        val walkFrame5Texture = Texture(Gdx.files.internal("Pink_Monster_Run_1_5.png"))
        val walkFrame6Texture = Texture(Gdx.files.internal("Pink_Monster_Run_1_6.png"))

        val walkFrame1 = TextureRegion(walkFrame1Texture)
        val walkFrame2 = TextureRegion(walkFrame2Texture)
        val walkFrame3 = TextureRegion(walkFrame3Texture)
        val walkFrame4 = TextureRegion(walkFrame4Texture)
        val walkFrame5 = TextureRegion(walkFrame5Texture)
        val walkFrame6 = TextureRegion(walkFrame6Texture)

        val walkFrames = arrayOf(walkFrame1, walkFrame2, walkFrame3, walkFrame4, walkFrame5, walkFrame6)
        walkAnimation = Animation(0.1f, *walkFrames)
        walkAnimation?.playMode = Animation.PlayMode.NORMAL

    }

    override fun show() {
        Gdx.app.setLogLevel(Application.LOG_INFO);
        System.out.println(mapObjects)
        batch = SpriteBatch()
        playerTexture = Texture("Pink_Monster_Jump_1_1.png")
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
                }
                else if (fileName == "tileset.tsx") {
                    return@FileHandleResolver Gdx.files.internal("tileset.tsx")}
                else if (fileName == "GameMap.tmx") {
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


        playerPosition = Vector2(700f, 900f)
        playerVelocity = Vector2(0f, 0f)
        groundRect = Rectangle(0f, 0f, Gdx.graphics.width.toFloat(), 20f)
        // Инициализация camera перед использованием
        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera!!.setToOrtho(false)


        // Инициализация поля класса tiledMapRenderer
        tiledMapRenderer = OrthogonalTiledMapRenderer(map)
        tiledMapRenderer!!.setView(camera) // Установка камеры

        // Создание кнопки ImageButton вперед
        val forwardTexture = Texture(Gdx.files.internal("forward.png"))
        val forwardDrawable: Drawable = TextureRegionDrawable(TextureRegion(forwardTexture))
        val forward = ImageButton(forwardDrawable)
        forward.setSize(150f, 150f)
        forward.setPosition(200f, 30f)

        // Создание кнопки ImageButton назад
        val backwardTexture = Texture(Gdx.files.internal("backward.png"))
        val backwardDrawable: Drawable = TextureRegionDrawable(TextureRegion(backwardTexture))
        val backward = ImageButton(backwardDrawable)
        backward.setSize(150f, 150f)
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
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("game_theme.mp3"))
        backgroundMusic!!.isLooping = true
        backgroundMusic!!.volume = 0.5f
        backgroundMusic!!.play()
    }

    override fun render(delta: Float) {
        // Clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        elapsedTime += delta // Обновление времени прошедшего для анимации
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

        batch!!.projectionMatrix = camera!!.combined
        // Установка камеры для tiledMapRenderer перед его рендерингом
        tiledMapRenderer!!.setView(camera) // Обновляем вид камеры здесь
        tiledMapRenderer!!.render()

        stage!!.act(delta)
        stage!!.draw()


        var showCharacter = true // Флаг для отображения спрайта персонажа
        val isWalking = moveLeft || moveRight
        val isJumping = playerVelocity.y != 0f

        val scale = 2.7f // Коэффициент масштабирования

        val currentFrame = if (isWalking) {
            showCharacter = false
            walkAnimation?.getKeyFrame(elapsedTime)
        } else if (isJumping) {
            showCharacter = false
            jumpAnimation?.getKeyFrame(elapsedTime)
        } else {
            null // Другие случаи (если персонаж не движется и не прыгает)
        }

        batch!!.begin()

        if (currentFrame == null) {
            showCharacter = true
        }

        if (showCharacter) {
            // Отобразить спрайт персонажа только если флаг установлен в true
            batch!!.draw(
                playerTexture,
                playerPosition.x,
                playerPosition.y,
                100f,
                100f
            )
        }

        currentFrame?.let {
            val animationFrameWidth = it.regionWidth.toFloat() * scale
            val animationFrameHeight = it.regionHeight.toFloat() * scale

            val playerCenterX = playerPosition.x + 100 / 2
            val playerCenterY = playerPosition.y + 100 / 2

            val animationX = playerCenterX - animationFrameWidth / 2
            val animationY = playerCenterY - animationFrameHeight / 2

            batch!!.draw(
                it,
                animationX,
                animationY,
                animationFrameWidth,
                animationFrameHeight
            )
        }
        batch!!.end()
    }

    private fun update() {
        playerVelocity.y += gravity // Применяем гравитацию
        playerPosition.add(playerVelocity) // Обновляем позицию игрока
        handleInput()
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
        if (playerPosition.x>4300)
        {
            backgroundMusic?.stop()
            game.setScreen(VictoryScreen(game))
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

            elapsedTime = 0f // Сбросить прошедшее время анимации
        }
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {backgroundMusic?.stop() }
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {
        batch!!.dispose()
        playerTexture!!.dispose()
        map!!.dispose()
    }
}