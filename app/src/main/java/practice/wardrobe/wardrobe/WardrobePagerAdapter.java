package practice.wardrobe.wardrobe;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import practice.wardrobe.R;
import practice.wardrobe.helper.ImageHelper;
import practice.wardrobe.model.ClothItem;

/**
 * Created by akhil on 24/11/15.
 */
public class WardrobePagerAdapter extends PagerAdapter {

    private final int mResId;
    private List<ClothItem> mClothItemList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public WardrobePagerAdapter(Context context, List<ClothItem> clothItemList, int resId) {
        mLayoutInflater = LayoutInflater.from(context);
        mClothItemList = clothItemList;
        mContext = context;
        mResId = resId;
    }

    @Override
    public int getCount() {
        return mClothItemList != null ? mClothItemList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(mResId, container, false);
        ImageView wardrobeItemIV = (ImageView) view.findViewById(R.id.iv_wardrobe_item);
        ClothItem clothItem = mClothItemList.get(position);
        if (clothItem != null) {
            wardrobeItemIV.setImageBitmap(ImageHelper.getImageFromPath(mContext, clothItem.getImageUrl()));
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void addItem(ClothItem clothItem) {
        mClothItemList.add(clothItem);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return mClothItemList.indexOf(object);
    }
}
