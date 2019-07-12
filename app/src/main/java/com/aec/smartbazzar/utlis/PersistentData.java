package com.aec.smartbazzar.utlis;

import android.support.annotation.NonNull;

import com.aec.smartbazzar.models.CartItem;
import com.aec.smartbazzar.models.Category;
import com.aec.smartbazzar.models.Item;
import com.aec.smartbazzar.models.OrderListItem;
import com.aec.smartbazzar.models.Users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PersistentData {

    static public Users myself = new Users();

    static List<Category> categoryList;

    static ArrayList<Item> itemList;

    static public HashMap<String, ArrayList<Item>> itemMap;

    static public ArrayList<CartItem> cartList;

    public static ArrayList<Item> getItemList() {
        return itemList;
    }

    public static void setItemList(ArrayList<Item> itemList) {
        PersistentData.itemList = itemList;
    }

    public static List<Category> getCategoryList() {
        return categoryList;
    }

    public static void setCategoryList(List<Category> categoryList) {
        PersistentData.categoryList = categoryList;
    }

    public static HashMap<String, ArrayList<Item>> setItemMap(List<Category> categoryList, List<Item> itemList){

        HashMap<String, ArrayList<Item>> itemMap = new HashMap<>();
        for (Category c: categoryList) {
            ArrayList<Item> items = new ArrayList<>();
            for (Item i: itemList) {
                if(c.getCat_id().equals(i.getCat_id())){
                    items.add(i);
                }
            }
            itemMap.put(c.getCat_name(), items);
        }
        return itemMap;
    }

    public static List<OrderListItem> orderListItems = new ArrayList<>();
}
