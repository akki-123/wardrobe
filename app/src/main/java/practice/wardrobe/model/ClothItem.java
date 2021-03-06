package practice.wardrobe.model;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

import java.util.List;

import practice.wardrobe.DatabaseManager;

/**
 * Entity mapped to table "CLOTH_ITEM".
 */
public class ClothItem {

    private Long id;
    private String name;
    private String type;
    private String imageUrl;
    private Boolean isFavorite;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ClothItem() {
    }

    public ClothItem(Long id) {
        this.id = id;
    }

    public ClothItem(Long id, String name, String type, String imageUrl, Boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.imageUrl = imageUrl;
        this.isFavorite = isFavorite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    // KEEP METHODS - put your custom methods here
    public void attach() {
        DatabaseManager.getDaoSessionMain().getClothItemDao().attachEntity(this);
    }

    public static List<ClothItem> getItemsForType(String type) {
        ClothItemDao dao = DatabaseManager.getDaoSessionMain().getClothItemDao();
        return dao.queryBuilder().where(ClothItemDao.Properties.Type.eq(type)).orderAsc(ClothItemDao.Properties.Id).list();
    }
    // KEEP METHODS END

}
