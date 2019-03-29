package ir.acharkit.android.component.tabPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

import androidx.appcompat.widget.LinearLayoutCompat;
import ir.acharkit.android.component.badge.BadgeView;
import ir.acharkit.android.util.Font;
import ir.acharkit.android.util.helper.StringHelper;
import ir.acharkit.android.util.helper.ViewHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    3/24/18
 * Email:   alirezat775@gmail.com
 */
public class TabPagerView extends TabLayout {

    private static final int DEFAULT_PADDING = 12;
    private static final int TEXT_SIZE = 10;

    private HashMap<Integer, BadgeView> badgeList = new HashMap<>();
    private int typeface;
    private String pathFont;

    public TabPagerView(Context context) {
        this(context, null);
    }

    public TabPagerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabPagerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        post(new Runnable() {
            @Override
            public void run() {
                containerTabLayout();
            }
        });
    }

    /**
     * @param pathFont
     * @param typeface
     */
    public void setFont(String pathFont, int typeface) {
        this.typeface = typeface;
        this.pathFont = pathFont;
    }

    /**
     * @return
     */
    private String getPathFont() {
        return pathFont;
    }

    /**
     * @return
     */
    private int getTypeface() {
        return typeface;
    }

    /**
     * @param tab for which custom icon is created
     */
    private ImageView addIcon(Tab tab) {
        if (tab.getIcon() == null)
            return null;
        ImageView icon = new ImageView(getContext());
        LinearLayoutCompat.LayoutParams param = new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        icon.setLayoutParams(param);
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        icon.setImageDrawable(tab.getIcon());
        return icon;
    }

    /**
     * @param tab for which custom title is created
     */
    @SuppressLint("WrongConstant")
    private TextView addTitle(Tab tab) {
        String titleText = (String) tab.getText();
        if (StringHelper.isEmpty(titleText))
            return null;
        TextView title = new TextView(getContext());
        LinearLayoutCompat.LayoutParams param = new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        title.setLayoutParams(param);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getTabTextColors());
        if (getPathFont() != null)
            Font.fromAsset(getContext(), getPathFont(), getTypeface(), title);

        title.setText(titleText);
        title.setSingleLine(false);
        title.setEllipsize(TextUtils.TruncateAt.END);
        title.setLines(1);
        title.setTextSize(TEXT_SIZE);
        return title;
    }

    /**
     * container items TabLayout
     */
    private void containerTabLayout() {
        for (int i = 0; i < getTabCount(); i++) {
            Tab tab = getTabAt(i);
            if (tab != null) {
                LinearLayoutCompat item = layoutItem(tab);
                tab.setCustomView(item);
            }
        }
    }

    /**
     * created item TabLayout
     */
    private LinearLayoutCompat layoutItem(Tab tab) {
        LinearLayoutCompat layout = new LinearLayoutCompat(getContext());
        LinearLayoutCompat.LayoutParams param = new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        layout.setLayoutParams(param);
        layout.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);
        layout.setOrientation(LinearLayoutCompat.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        if (addIcon(tab) != null)
            layout.addView(addIcon(tab));
        if (addTitle(tab) != null)
            layout.addView(addTitle(tab));

        return layout;
    }


    /**
     * @param index           add badge to index of tabPagerView
     * @param backgroundColor change badge backgroundColor
     * @param textColor       change badge textColor
     * @param number          change badge number
     */
    public void addBadge(final int index, final int backgroundColor, final int textColor, final int number) {
        post(new Runnable() {
            @Override
            public void run() {
                if (badgeList.get(index) == null) {
                    TabLayout.Tab tab = getTabAt(index);
                    View tabView = tab.getCustomView();
                    if (tabView != null) {
                        BadgeView badgeView = new BadgeView(getContext());
                        badgeView.setParentView(tabView);
                        badgeView.setFont(getPathFont(), getTypeface());
                        badgeView.setBadgeColor(backgroundColor, textColor);
                        badgeView.setNumber(number);
                        badgeView.setBadgePosition(BadgeView.POSITION_CENTER_TOP);
                        badgeView.show();
                        ViewHelper.setMargins(getContext(), badgeView, (8), (2), (0), (0));
                        badgeList.put(index, badgeView);
                    }
                }

            }
        });

    }

    /**
     * @param index remove badge from index of tabPagerView
     */
    public void removeBadge(int index) {
        if (badgeList.get(index) != null) {
            badgeList.get(index).hide();
            badgeList.remove(index);
        }
    }

}
