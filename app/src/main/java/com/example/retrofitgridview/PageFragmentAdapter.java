package com.example.retrofitgridview;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.retrofitgridview.fragments.PageFragment;

import java.util.ArrayList;
import java.util.List;

public class PageFragmentAdapter extends FragmentPagerAdapter {
    private int charMax;
    private String content;
    private List<String> parts;
    private String newPageSpacing = "\n\n\n";
    private int txtSize = 12;
    private List<PageFragment> pageFragmentList = new ArrayList<>();

    // private Long totalTime = 0L;
    public PageFragmentAdapter(FragmentManager fragmentManager, int charMax, String content) {
        super(fragmentManager);
        this.charMax = charMax;
        this.content = content;
        this.parts = getParts(content, charMax);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return parts.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        Log.d("getItem", position + "");
        PageFragment pageFragment = PageFragment.newInstance(position, parts.get(position), txtSize);
        pageFragmentList.add(pageFragment);
        return pageFragment;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }


    private List<String> getParts(String string, int partitionSize) {
        List<String> parts = new ArrayList<String>();
        int len = string.length();
        String verifiedContent;
        int addPositions = 0;
        for (int i = 0; i < len; i += partitionSize) {
            verifiedContent = string.substring(i, Math.min(len, i + partitionSize));
            addPositions = 0;

            while (!verifiedContent.endsWith(" ")) {
                verifiedContent = verifiedContent.substring(0, verifiedContent.length() - 1);
                addPositions++;
            }

            for (String newLine : verifiedContent.split(newPageSpacing)) {
                if (newLine.length() > newPageSpacing.length())
                    parts.add(newLine);
            }

            i = i - addPositions;

        }
        return parts;
    }

    public void setNewSize(int spSize) {
        txtSize = spSize;
        for (PageFragment page : pageFragmentList) {
            page.setNewSize(spSize);
        }
        notifyDataSetChanged();
    }

}
