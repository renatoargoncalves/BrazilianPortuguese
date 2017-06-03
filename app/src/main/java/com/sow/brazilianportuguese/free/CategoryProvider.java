package com.sow.brazilianportuguese.free;

import android.content.Context;

import java.util.ArrayList;

public class CategoryProvider {

    private Context context;
    private ArrayList<Category> categoryArrayList = new ArrayList<>();

    public CategoryProvider(Context context) {
        this.context = context;
        String[] arrayCategories = context.getResources().getStringArray(R.array.categories);
        createCategoryArray(arrayCategories);
    }

    private void createCategoryArray(String[] categories) {
        categoryArrayList.clear();
        for (int i = 0; i < categories.length; i++) {
            Category category = new Category();
            category.setIdentifier(String.valueOf(categories[i].split("#")[0]));
            category.setTitle(String.valueOf(categories[i].split("#")[1]));
            category.setSubtitle(String.valueOf(categories[i].split("#")[2]));
            category.setImage_id(context.getResources().getIdentifier(String.valueOf(categories[i].split("#")[0]), "drawable", context.getPackageName()));
            categoryArrayList.add(category);
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Category> getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }

    public class Category {

        int image_id;
        String identifier;
        String title;
        String subtitle;

        public int getImage_id() {
            return image_id;
        }

        public void setImage_id(int image_id) {
            this.image_id = image_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }
    }

}
