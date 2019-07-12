package com.aec.smartbazzar.utlis;

import com.aec.smartbazzar.models.Category;
import com.aec.smartbazzar.models.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtility {

    public static ArrayList<Category> parseCategoryJSON(JSONArray jsonArray){
        ArrayList<Category> categoryList = new ArrayList<>();

        JSONObject jsonobject = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            try{
                jsonobject = jsonArray.getJSONObject(i);
                String name = jsonobject.getString("cat_name");
                String id = jsonobject.getString("cat_id");
                categoryList.add(new Category(id, name));
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }
        return categoryList;
    }

    public static ArrayList<Item> parseItemJSON(JSONArray jsonArray){
        ArrayList<Item> itemList = new ArrayList<>();

        JSONObject jsonobject = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            try{
                jsonobject = jsonArray.getJSONObject(i);
                String id = jsonobject.getString("i_id");
                String name = jsonobject.getString("name");
                String price = jsonobject.getString("price");
                String cat_id = jsonobject.getString("cat_id");
                String imgurl = jsonobject.getString("img_url");
                String details = jsonobject.getString("details");
                itemList.add(new Item(id, name, price, imgurl, cat_id, details));
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }
        return itemList;
    }}
