package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class WardrobeDaoGenerator {
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(2, "practice.wardrobe.model");
        schema.enableKeepSectionsByDefault();

        createWardrobeItem(schema);
        new DaoGenerator().generateAll(schema, "../Wardrobe/app/src/main/java");

    }

    private static void createWardrobeItem(Schema schema) {
        Entity clothItem = schema.addEntity("ClothItem");
        clothItem.addIdProperty().autoincrement();
        clothItem.addStringProperty("name");
        clothItem.addStringProperty("type");
        clothItem.addStringProperty("imageUrl");
        clothItem.addBooleanProperty("isFavorite");


        Entity favPair = schema.addEntity("FavPair");
        favPair.addIdProperty().autoincrement();
        favPair.addToOne(clothItem, favPair.addLongProperty("upperId").getProperty(), "upper");
        favPair.addToOne(clothItem, favPair.addLongProperty("lowerId").getProperty(), "lower");
    }

}
