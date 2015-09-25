package org.antinori.game.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationSelection extends Game implements InputProcessor {

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 768;
    
    OrthographicCamera camera;
    SpriteBatch batch;

    BamAnimationStore store;
    float frameCounter = 0;
    BitmapFont font;
    int section = 0;

    public static final String BAMDIR = "C:\\Users\\Paul\\Desktop\\BAMS";

    public static final String[] prefixes = {//"MAIR","MAIS","MAKH","MASG","MASL","MBAS","MBEG",
        //"MBEH","MBER","MBES","MCAR","MDJI","MDJL","MDKN","MDKS","MDLI","MDOG",
        "MDOP", "MDRO", "MDSW", "MEAE", "MEAS", "METN", "METT", "MEYE", "MFDR", "MFIE", "MFIG", "MFIS",
        //"MGCL","MGCP","MGHL","MGIB","MGIT","MGLC","MGNL","MGO1","MGO2","MGO3","MGO4","MGWE",
        //"MHOB","MIGO","MIMP","MINO","MKOB","MLER","MLIC","MLIZ","MMAG","MMEL","MMIN","MMIS",
        //"MMST","MMUM","MMY2","MMYC","MNO1","MNO2","MNO3","MOGH","MOGM","MOGN","MOGR","MOR1",
        //"MOR2","MOR3","MOR4","MOR5","MOTY","MOVE","MRAK","MRAV","MSA2","MSAH","MSAI","MSAL",
        //"MSAT","MSHD","MSHR","MSIR","MSKA","MSKB","MSKL","MSKT","MSLI","MSLY","MSNK","MSOG",
        //"MMUM","MSOL","MSPI","MSPL","MSPS","MTAN","MTAS","MTRO","MTRS","MUMB","MVAF","MVAM","MWER",
        "MWFM", "MWLF", "MWLS", "MWYV", "MXVT", "MYU1", "MYU2", "MYU3", "MZOM"};

    public static int currentPrefixIndex = 0;

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "AnimationSelection";
        cfg.width = SCREEN_WIDTH;
        cfg.height = SCREEN_HEIGHT;
        new LwjglApplication(new AnimationSelection(), cfg);
    }

    public void create() {
        
        font = new BitmapFont();

        String prefix = "MDJL";
        store = new BamAnimationStore(BAMDIR, prefix);
        store.init();
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        batch = new SpriteBatch();
        
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();

        font.draw(batch, store.sheetName, 10, 10);
        
        frameCounter += Gdx.graphics.getDeltaTime();
        
        int y = 1;
        int count = 0;
        int index = 0;
        
        for (String key : store.getAnimations().keySet()) {
            Animation anim = store.getAnimations().get(key);

            index++;
            if (section == 0 && index >= 24) {
                continue;
            }
            if (section == 1 && (index < 24 || index >=48) ) {
                continue;
            }
            if (section == 2 && (index < 48 || index >=72) ) {
                continue;
            }
            if (section == 3 && (index < 72 || index >=96) ) {
                continue;
            }

            count++;

            int rX = (count * store.maxWidth) - (store.maxWidth / 2);
            int rY = (y * store.maxHeight) - (store.maxHeight / 2);
            int centerRectX = rX + (store.maxWidth / 2);
            int centerRectY = rY + (store.maxHeight / 2);

            //centerize the image on the rectangle
            TextureRegion frame = anim.getKeyFrame(frameCounter);
            int width = frame.getRegionWidth();
            int height = frame.getRegionHeight();
            batch.draw(frame, centerRectX - width / 2, centerRectY - height / 2, width, height);

            font.draw(batch, key, rX, rY);
            
            if (count > 7) {
                y++;
            }
            if (count > 7) {
                count = 0;
            }
        }

        batch.end();
        
    }

    @Override
    public boolean keyDown(int key) {
        if (key == Keys.SPACE) {
            section++;
        }
        if (section > 3) {
            section = 0;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean keyUp(int i) {
        return false;
    }

    public boolean keyTyped(char c) {
        return false;
    }

    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    public boolean scrolled(int i) {
        return false;
    }

}
