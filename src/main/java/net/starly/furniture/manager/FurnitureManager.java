package net.starly.furniture.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.starly.furniture.Furniture;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FurnitureManager {

    private static FurnitureManager instance;

    @Getter
    private final static Map<String, Integer> furnitureMap = new HashMap<>();

    private FurnitureManager() {}

    public static FurnitureManager getInstance() {
        if (instance == null) instance = new FurnitureManager();
        return instance;
    }

    public void saveAll() {
        Set<String> furniture = furnitureMap.keySet();
        System.out.println(furniture);
        furniture.forEach(this::save);
    }

    public void save(String furniture) {
        Furniture plugin = Furniture.getInstance();
        File directory = new File(plugin.getDataFolder(), "furniture");
        File dataFile = new File(directory, furniture + ".json");

        if (!directory.exists()) directory.mkdir();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", furniture);
        jsonObject.addProperty("customModelData", furnitureMap.get(furniture));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAll() {
        Furniture plugin = Furniture.getInstance();
        File dataFolder = new File(plugin.getDataFolder(), "furniture");

        if (!dataFolder.exists()) dataFolder.mkdir();

        for (String fileName : dataFolder.list()) {
            String furniture = fileName.replace(".json", "");
            load(furniture);
        }
    }

    public void load(String furniture) {
        Furniture plugin = Furniture.getInstance();
        File directory = new File(plugin.getDataFolder(), "furniture");
        File dataFile = new File(directory, furniture + ".json");

        if (!directory.exists()) directory.mkdir();
        if (!dataFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            String name = json.get("name").getAsString();
            int customModelData = json.get("customModelData").getAsInt();

            furnitureMap.put(name, customModelData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
