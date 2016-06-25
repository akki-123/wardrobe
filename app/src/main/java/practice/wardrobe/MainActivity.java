package practice.wardrobe;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import practice.wardrobe.helper.AlarmHelper;
import practice.wardrobe.helper.ImageHelper;
import practice.wardrobe.helper.ImagePicker;
import practice.wardrobe.helper.Utility;
import practice.wardrobe.model.ClothItem;
import practice.wardrobe.model.ClothItemDao;
import practice.wardrobe.model.FavPair;
import practice.wardrobe.model.FavPairDao;
import practice.wardrobe.model.Image;
import practice.wardrobe.wardrobe.WardrobePagerAdapter;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private static final int PICK_IMAGE_ID_UPPER = 101; // any random number
    private static final int PICK_IMAGE_ID_LOWER = 102;

    @Bind(R.id.vp_lower)
    ViewPager lowerVP;

    @Bind(R.id.vp_tshirt)
    ViewPager upperVP;

    @Bind(R.id.ib_favourite)
    ImageButton favIB;

    private List<ClothItem> mUpperItemList;
    private List<ClothItem> mLowerItemList;
    WardrobePagerAdapter upperWardrobePagerAdapter;
    WardrobePagerAdapter lowerWardrobePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViewPagers();

        AlarmHelper.setAlarm(this);

    }

    @OnClick(R.id.ib_add_upper)
    public void onAddUpperClicked() {
        boolean isGranted = Utility.checkExternalStoragePermission(this);
        if (isGranted) {
            onPickImage(PICK_IMAGE_ID_UPPER);
        }
    }

    @OnClick(R.id.ib_add_lower)
    public void onAddLowerClicked() {
        boolean isGranted = Utility.checkExternalStoragePermission(this);
        if (isGranted) {
            onPickImage(PICK_IMAGE_ID_LOWER);
        }
    }

    @OnClick(R.id.ib_randomize)
    public void onRandomizeClick() {
        selectRandomPair();
    }

    @OnClick(R.id.ib_favourite)
    public void onFavClick() {
        onFavClicked();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initViewPagers() {
        mUpperItemList = ClothItem.getItemsForType("Upper");
        upperWardrobePagerAdapter = new WardrobePagerAdapter(this, mUpperItemList, R.layout.wardrobe_item);
        upperVP.setAdapter(upperWardrobePagerAdapter);

        mLowerItemList = ClothItem.getItemsForType("Lower");
        lowerWardrobePagerAdapter = new WardrobePagerAdapter(this, mLowerItemList, R.layout.wardrobe_item);
        lowerVP.setAdapter(lowerWardrobePagerAdapter);

        lowerVP.setOnPageChangeListener(this);
        upperVP.setOnPageChangeListener(this);
        onRefreshFavourites();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE_ID_LOWER:
                case PICK_IMAGE_ID_UPPER:
                    Image image = ImagePicker.getImageFromResult(this, resultCode, data);
                    String path = ImageHelper.saveImage(getApplicationContext(), image);
                    if (path != null) {
                        long count = DatabaseManager.getDaoSessionMain().getClothItemDao().queryBuilder().where(ClothItemDao.Properties.ImageUrl.eq(path)).count();
                        if (count == 0) {
                            ClothItem item = new ClothItem();
                            if (requestCode == PICK_IMAGE_ID_UPPER) {
                                item.setType("Upper");
                                item.setImageUrl(path);
                                item.setName(path);
                                upperWardrobePagerAdapter.addItem(item);
                                DatabaseManager.getDaoSessionMain().getClothItemDao().insertOrReplace(item);
                                moveUpperToItem(item);
                            } else {
                                item.setType("Lower");
                                item.setImageUrl(path);
                                item.setName(path);
                                lowerWardrobePagerAdapter.addItem(item);
                                DatabaseManager.getDaoSessionMain().getClothItemDao().insertOrReplace(item);
                                moveLowerToItem(item);
                            }
                        } else {
                            Toast.makeText(this, R.string.err_image_already_added, Toast.LENGTH_LONG).show();
                        }

                    }
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                //code for deny
                }
                break;
        }
    }

    public void onPickImage(int reqCode) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, reqCode);
    }

    private void moveUpperToItem(ClothItem item) {
        if (mUpperItemList != null && mUpperItemList.size() > 0 && mUpperItemList.indexOf(item) != -1) {
            upperVP.setCurrentItem(mUpperItemList.indexOf(item));
        }
    }

    private void moveLowerToItem(ClothItem item) {
        if (mLowerItemList != null && mLowerItemList.size() > 0 && mLowerItemList.indexOf(item) != -1) {
            lowerVP.setCurrentItem(mLowerItemList.indexOf(item));
        }
    }

    private void selectRandomPair() {
        if (lowerVP.getAdapter() != null && lowerVP.getAdapter().getCount() > 1 &&
                upperVP.getAdapter() != null && upperVP.getAdapter().getCount() > 1) {

            Pair<Integer, Integer> clothesPair;
            ClothItem currentUpperItem = mUpperItemList.get(upperVP.getCurrentItem());
            ClothItem currentLowerItem = mLowerItemList.get(lowerVP.getCurrentItem());
            List<FavPair> favPairList = DatabaseManager.getDaoSessionMain().getFavPairDao().queryBuilder().whereOr(FavPairDao.Properties.UpperId.notEq(currentUpperItem.getId()),
                    FavPairDao.Properties.LowerId.notEq(currentLowerItem.getId())).list();

            if (new Random().nextBoolean() && favPairList.size() > 0) {
                FavPair favPair = favPairList.get(new Random().nextInt(favPairList.size()));
                clothesPair = new Pair<>(upperVP.getAdapter().getItemPosition(favPair.getUpper()),
                        lowerVP.getAdapter().getItemPosition(favPair.getLower()));
            } else {
                clothesPair = new Pair<>(Utility.getRandomWithExclusion(0, upperVP.getAdapter().getCount() - 1, upperVP.getCurrentItem()),
                        Utility.getRandomWithExclusion(0, lowerVP.getAdapter().getCount() - 1, lowerVP.getCurrentItem()));
            }

            upperVP.setCurrentItem(clothesPair.first, true);
            lowerVP.setCurrentItem(clothesPair.second, true);
            Log.e("POSITIONS", "First : " + clothesPair.first + "   Second: " + clothesPair.second);
        } else {
            Toast.makeText(this, R.string.err_not_enough_combinations, Toast.LENGTH_LONG).show();
        }
    }

    private void onFavClicked() {

        ClothItem currentUpperItem = null;
        if (mUpperItemList != null && upperVP.getCurrentItem() < mUpperItemList.size()) {
            currentUpperItem = mUpperItemList.get(upperVP.getCurrentItem());
        }
        ClothItem currentLowerItem = null;
        if (mLowerItemList != null && lowerVP.getCurrentItem() < mLowerItemList.size())
            currentLowerItem = mLowerItemList.get(lowerVP.getCurrentItem());

        if (currentLowerItem != null && currentUpperItem != null) {
            FavPair pair = FavPair.getUniquePair(currentLowerItem.getId(), currentUpperItem.getId());
            if (pair == null) {
                favIB.setSelected(true);
                pair = new FavPair();
                pair.setLowerId(currentLowerItem.getId());
                pair.setUpperId(currentUpperItem.getId());
                DatabaseManager.getDaoSessionMain().getFavPairDao().insertOrReplace(pair);
            } else {
                pair.delete();
                pair.update();
                favIB.setSelected(false);
            }
        } else {
            favIB.setSelected(false);
        }
    }

    private void onRefreshFavourites() {

        ClothItem currentUpperItem = null;
        if (mUpperItemList != null && upperVP.getCurrentItem() < mUpperItemList.size()) {
            currentUpperItem = mUpperItemList.get(upperVP.getCurrentItem());
        }
        ClothItem currentLowerItem = null;
        if (mLowerItemList != null && lowerVP.getCurrentItem() < mLowerItemList.size())
            currentLowerItem = mLowerItemList.get(lowerVP.getCurrentItem());

        if (currentLowerItem != null && currentUpperItem != null) {
            FavPair pair = FavPair.getUniquePair(currentLowerItem.getId(), currentUpperItem.getId());
            favIB.setSelected(pair != null);
        } else {
            favIB.setSelected(false);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        onRefreshFavourites();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
