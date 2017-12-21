package com.easedroid.code.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.easedroid.code.R;
import com.easedroid.code.bitmap.ImageCache;
import com.easedroid.code.bitmap.ImageResizer;
import com.easedroid.code.provider.Images;
import com.easedroid.code.utils.L;

import java.util.List;

/**
 * Created by bin.jing on 2017/12/20.
 */

public class BitmapReuseFragment extends BaseFragment implements AdapterView.OnItemClickListener {


    private static final boolean D = true;
    private static final String TAG = "display";
    private static final String IMAGE_CACHE_DIR = "thumbs";

    private ImageAdapter mAdapter;
    private ImageResizer mImageResizer;
    private int mImageThumbSize;
    private int mImageThumbSpacing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (D) L.d(TAG, "ImageGridFragment onCreate");
        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f);// Set memory cache to 25% of app memory

        mImageResizer = new ImageResizer(getActivity(), 100) {
        };
        mImageResizer.setLoadingImage(R.drawable.image);
        mImageResizer.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);

        mAdapter = new ImageAdapter(getActivity());
        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (D) L.d(TAG, "ImageGridFragment onCreateView");
        final View view = inflater.inflate(R.layout.image_grid_fragment, container, false);
        final GridView mGridView = (GridView) view.findViewById(R.id.gridview);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause fetcher to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Before Honeycomb pause image loading on scroll to help with performance
                    mImageResizer.setPauseWork(true);
                } else {
                    mImageResizer.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        // This listener is used to get the final width of the GridView and then calculate the
        // number of columns and the width of each column. The width of each column is variable
        // as the GridView has stretchMode=columnWidth. The column width is used to set the height
        // of each view so we get nice square thumbnails.
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        if (mAdapter.getNumColumns() == 0) {
                            final int numColumns = (int) Math.floor(
                                    mGridView.getWidth() / (mImageThumbSize + mImageThumbSpacing));
                            if (numColumns > 0) {
                                final int columnWidth =
                                        (mGridView.getWidth() / numColumns) - mImageThumbSpacing;
                                if (D) L.d(TAG, "columnWidth = " + columnWidth
                                        + "\nmImamgeThumbSize = " + mImageThumbSize
                                        + "\nnumColumns = " + numColumns);
                                mAdapter.setNumColumns(numColumns);
                                mAdapter.setItemHeight(columnWidth);
                                mGridView.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                            }
                        }
                    }
                });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (D) L.d(TAG, "ImageGridFragment onResume");
        mImageResizer.setExitTasksEarly(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (D) L.d(TAG, "ImageGridFragment onPause");
        mImageResizer.setPauseWork(false);
        mImageResizer.setExitTasksEarly(true);
        mImageResizer.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (D) L.d(TAG, "ImageGridFragment onDestroy");
        mImageResizer.closeCache();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        final Intent i = new Intent(getActivity(), ImageDetailActivity.class);
//        i.putExtra(ImageDetailActivity.EXTRA_IMAGE, (int) id);
//        startActivity(i);
    }

    /**
     * The main adapter that backs the GridView.
     */
    private class ImageAdapter extends BaseAdapter {

        private Context mContext;
        private int mItemHeight = 0;
        private int mNumColumns = 0;
        private GridView.LayoutParams mImageViewLayoutParams;
        private List<String> mImagesList = null;
        private Images mImages = null;

        public ImageAdapter(Context context) {
            mContext = context;
            mImageViewLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
            mImages = new Images(context);
            mImagesList = mImages.readImages(null);

        }

        @Override
        public int getCount() {
            return mImagesList != null ? mImagesList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return mImagesList != null ? mImagesList.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.image_grid_item, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.mImageView);
                viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                viewHolder.imageView.setLayoutParams(mImageViewLayoutParams);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Check the height matches our calculated column width
            if (viewHolder.imageView.getLayoutParams().height != mItemHeight) {
                viewHolder.imageView.setLayoutParams(mImageViewLayoutParams);
            }
            mImageResizer.loadImage(mImagesList.get(position), viewHolder.imageView);
            return convertView;
        }

        class ViewHolder {
            public ImageView imageView;
        }

        /**
         * Sets the item height. Useful for when we know the column width so the height can be set
         * to match.
         *
         * @param height
         */
        public void setItemHeight(int height) {
            if (height == mItemHeight) {
                return;
            }
            mItemHeight = height;
            mImageViewLayoutParams =
                    new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, mItemHeight);
            mImageResizer.setImageSize(mItemHeight);
            notifyDataSetChanged();
        }

        public void setNumColumns(int numColumns) {
            mNumColumns = numColumns;
        }

        public int getNumColumns() {
            return mNumColumns;
        }
    }
}