package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
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
    private var batch: SpriteBatch = SpriteBatch()
    private val font: BitmapFont = BitmapFont()
    private var backgroundMusic: Music? = null
    private lateinit var backgroundTextures: Array<TextureRegion>
    private lateinit var backgroundAnimation: Animation<TextureRegion>
    private var stateTime = 0f

    init {
        val startTexture = Texture(Gdx.files.internal("Play@2x.png"))
        val vikTexture=Texture(Gdx.files.internal("Levels.png"))

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

        stage.addActor(start)
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("main_menu_theme.mp3"))
        backgroundMusic!!.isLooping = true // Повторять воспроизведение музыки
        backgroundMusic!!.volume = 0.5f // Установите громкость музыки (от 0.0 до 1.0)
        backgroundMusic!!.play() // Начните проигрывание музыки
        batch = SpriteBatch()

        // Загрузка текстур фона
        backgroundTextures = arrayOf(
            TextureRegion(Texture("frame0000.png")),
            TextureRegion(Texture("frame0001.png")),
            TextureRegion(Texture("frame0002.png")),
            TextureRegion(Texture("frame0003.png")),
            TextureRegion(Texture("frame0004.png")),
            TextureRegion(Texture("frame0005.png")),
            TextureRegion(Texture("frame0006.png")),
            TextureRegion(Texture("frame0007.png")),
            TextureRegion(Texture("frame0008.png")),
            TextureRegion(Texture("frame0009.png")),
            TextureRegion(Texture("frame0010.png")),
            TextureRegion(Texture("frame0011.png")),
            TextureRegion(Texture("frame0012.png")),
            TextureRegion(Texture("frame0013.png")),
            TextureRegion(Texture("frame0014.png")),
            TextureRegion(Texture("frame0015.png")),
            TextureRegion(Texture("frame0016.png")),
            TextureRegion(Texture("frame0017.png")),
            TextureRegion(Texture("frame0018.png")),
            TextureRegion(Texture("frame0019.png")),
            TextureRegion(Texture("frame0020.png")),
            TextureRegion(Texture("frame0021.png")),
            TextureRegion(Texture("frame0022.png")),
            TextureRegion(Texture("frame0023.png")),
            TextureRegion(Texture("frame0024.png")),
            TextureRegion(Texture("frame0025.png")),
            TextureRegion(Texture("frame0026.png")),
            TextureRegion(Texture("frame0027.png")),
            TextureRegion(Texture("frame0028.png")),
            TextureRegion(Texture("frame0029.png")),
            TextureRegion(Texture("frame0030.png")),
            TextureRegion(Texture("frame0031.png")),
            TextureRegion(Texture("frame0032.png")),
            TextureRegion(Texture("frame0033.png")),
            TextureRegion(Texture("frame0034.png")),
            TextureRegion(Texture("frame0035.png")),
            TextureRegion(Texture("frame0036.png")),
            TextureRegion(Texture("frame0037.png")),
            TextureRegion(Texture("frame0038.png")),
            TextureRegion(Texture("frame0039.png")),
            TextureRegion(Texture("frame0040.png")),
            TextureRegion(Texture("frame0041.png")),
            TextureRegion(Texture("frame0042.png")),
            TextureRegion(Texture("frame0043.png")),
            TextureRegion(Texture("frame0044.png")),
            TextureRegion(Texture("frame0045.png")),
            TextureRegion(Texture("frame0046.png")),
            TextureRegion(Texture("frame0047.png")),
            TextureRegion(Texture("frame0048.png")),
            TextureRegion(Texture("frame0049.png")),
            TextureRegion(Texture("frame0050.png")),
            TextureRegion(Texture("frame0051.png")),
            TextureRegion(Texture("frame0052.png")),
            TextureRegion(Texture("frame0053.png")),
            TextureRegion(Texture("frame0054.png")),
            TextureRegion(Texture("frame0055.png")),
            TextureRegion(Texture("frame0056.png")),
            TextureRegion(Texture("frame0057.png")),
            TextureRegion(Texture("frame0058.png")),
            TextureRegion(Texture("frame0059.png")),
            TextureRegion(Texture("frame0060.png")),
            TextureRegion(Texture("frame0061.png")),
            TextureRegion(Texture("frame0062.png")),
            TextureRegion(Texture("frame0063.png")),
            TextureRegion(Texture("frame0064.png")),
            TextureRegion(Texture("frame0065.png")),
            TextureRegion(Texture("frame0066.png")),
            TextureRegion(Texture("frame0067.png")),
            TextureRegion(Texture("frame0068.png")),
            TextureRegion(Texture("frame0069.png")),
            TextureRegion(Texture("frame0070.png")),
            TextureRegion(Texture("frame0071.png")),
            TextureRegion(Texture("frame0072.png")),
            TextureRegion(Texture("frame0073.png")),
            TextureRegion(Texture("frame0074.png")),
            TextureRegion(Texture("frame0075.png")),
            TextureRegion(Texture("frame0076.png")),
            TextureRegion(Texture("frame0077.png")),
            TextureRegion(Texture("frame0078.png")),
            TextureRegion(Texture("frame0079.png")),
            TextureRegion(Texture("frame0080.png")),
            TextureRegion(Texture("frame0081.png")),
            TextureRegion(Texture("frame0082.png")),
            TextureRegion(Texture("frame0083.png")),
            TextureRegion(Texture("frame0084.png")),
            TextureRegion(Texture("frame0085.png")),

            TextureRegion(Texture("frame0086.png")),

            TextureRegion(Texture("frame0087.png")),

            TextureRegion(Texture("frame0088.png")),

            TextureRegion(Texture("frame0089.png")),

            TextureRegion(Texture("frame0090.png")),

            TextureRegion(Texture("frame0091.png")),

            TextureRegion(Texture("frame0092.png")),

            TextureRegion(Texture("frame0093.png")),

            TextureRegion(Texture("frame0094.png")),

            TextureRegion(Texture("frame0095.png")),

            TextureRegion(Texture("frame0096.png")),

            TextureRegion(Texture("frame0097.png")),

            TextureRegion(Texture("frame0098.png")),

            TextureRegion(Texture("frame0099.png")),

            TextureRegion(Texture("frame0100.png")),

            TextureRegion(Texture("frame0101.png")),

            TextureRegion(Texture("frame0102.png")),

            TextureRegion(Texture("frame0103.png")),

            TextureRegion(Texture("frame0104.png")),

            TextureRegion(Texture("frame0105.png")),

            TextureRegion(Texture("frame0106.png")),

            TextureRegion(Texture("frame0107.png")),

            TextureRegion(Texture("frame0108.png")),

            TextureRegion(Texture("frame0109.png")),

            TextureRegion(Texture("frame0110.png")),

            TextureRegion(Texture("frame0111.png")),

            TextureRegion(Texture("frame0112.png")),

            TextureRegion(Texture("frame0113.png")),

            TextureRegion(Texture("frame0114.png")),

            TextureRegion(Texture("frame0115.png")),

            TextureRegion(Texture("frame0116.png")),

            TextureRegion(Texture("frame0117.png")),
            TextureRegion(Texture("frame0118.png")),
            TextureRegion(Texture("frame0119.png")),
            TextureRegion(Texture("frame0120.png")),
            TextureRegion(Texture("frame0121.png")),
            TextureRegion(Texture("frame0122.png")),
            TextureRegion(Texture("frame0123.png")),
            TextureRegion(Texture("frame0124.png")),
            TextureRegion(Texture("frame0125.png")),
            TextureRegion(Texture("frame0126.png")),
            TextureRegion(Texture("frame0127.png")),
            TextureRegion(Texture("frame0128.png")),
            TextureRegion(Texture("frame0129.png")),
            TextureRegion(Texture("frame0130.png")),
            TextureRegion(Texture("frame0131.png")),
            TextureRegion(Texture("frame0132.png")),
            TextureRegion(Texture("frame0133.png")),
            TextureRegion(Texture("frame0134.png")),
            TextureRegion(Texture("frame0135.png")),
            TextureRegion(Texture("frame0136.png")),
            TextureRegion(Texture("frame0137.png")),
            TextureRegion(Texture("frame0138.png")),
            TextureRegion(Texture("frame0139.png")),
            TextureRegion(Texture("frame0140.png")),
        )
        val frameDuration = 0.1f
        backgroundAnimation = Animation(frameDuration, *backgroundTextures)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stateTime += delta

        // Получение текущего кадра анимации
        val currentFrame = backgroundAnimation.getKeyFrame(stateTime, true)

        batch.begin()
        batch.draw(currentFrame, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        font.color = Color.BLACK
        font.data.setScale(4f)
        font.draw(batch, "Leap Legacy: 2D Adventure", 600f, 500f)
        batch.end()

        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun pause() {}

    override fun resume() {}

    override fun hide() { backgroundMusic?.stop() }

    override fun dispose() {
        backgroundMusic?.stop()
        backgroundMusic?.dispose()
    }
}