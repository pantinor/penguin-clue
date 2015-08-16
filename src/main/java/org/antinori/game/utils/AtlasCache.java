package org.antinori.game.utils;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AtlasCache {

    private static Map<String, TextureAtlas> cache = new HashMap<>();

    public static void add(String name, String file) {
        TextureAtlas a = new TextureAtlas(Gdx.files.internal(file));
        cache.put(name, a);
    }

    public static TextureAtlas get(String name) {
        return cache.get(name);
    }
}