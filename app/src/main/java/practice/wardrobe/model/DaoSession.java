package practice.wardrobe.model;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import practice.wardrobe.model.ClothItem;
import practice.wardrobe.model.FavPair;

import practice.wardrobe.model.ClothItemDao;
import practice.wardrobe.model.FavPairDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig clothItemDaoConfig;
    private final DaoConfig favPairDaoConfig;

    private final ClothItemDao clothItemDao;
    private final FavPairDao favPairDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        clothItemDaoConfig = daoConfigMap.get(ClothItemDao.class).clone();
        clothItemDaoConfig.initIdentityScope(type);

        favPairDaoConfig = daoConfigMap.get(FavPairDao.class).clone();
        favPairDaoConfig.initIdentityScope(type);

        clothItemDao = new ClothItemDao(clothItemDaoConfig, this);
        favPairDao = new FavPairDao(favPairDaoConfig, this);

        registerDao(ClothItem.class, clothItemDao);
        registerDao(FavPair.class, favPairDao);
    }
    
    public void clear() {
        clothItemDaoConfig.getIdentityScope().clear();
        favPairDaoConfig.getIdentityScope().clear();
    }

    public ClothItemDao getClothItemDao() {
        return clothItemDao;
    }

    public FavPairDao getFavPairDao() {
        return favPairDao;
    }

}