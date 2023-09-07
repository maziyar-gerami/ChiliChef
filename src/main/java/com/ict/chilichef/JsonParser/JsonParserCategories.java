package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.CategoriesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParserCategories {

	private static final String KEY_ID 		= "Id_Cat";
	private static final String KEY_NAME 	= "Name";
	private static final String KEY_PAR	= "Parent";
	private static final String KEY_Picname	= "id_cat";
	private static final String KEY_isLeaf	= "isLeaf";
	private static final String KEY_DES	= "Description";



	public ArrayList<CategoriesModel> parseJson(String jsonstring){
		ArrayList<CategoriesModel> categoriesModels = new ArrayList<CategoriesModel>();

		try {

			JSONArray jsonarray = new JSONArray(jsonstring);
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobject = jsonarray.getJSONObject(i);
				CategoriesModel categoriesModel = new CategoriesModel();
				categoriesModel.setId_Cat(jsonobject.getInt(KEY_ID));
				categoriesModel.setName(jsonobject.getString(KEY_NAME));
				categoriesModel.setParent(jsonobject.getInt(KEY_PAR));
				categoriesModel.setLeaf(jsonobject.getBoolean(KEY_isLeaf));
				categoriesModel.setPic_url(KEY_Picname+jsonobject.getInt(KEY_ID)+".jpg");
				categoriesModel.setDescription(jsonobject.getString(KEY_DES));

				categoriesModels.add(categoriesModel);
			}
			return categoriesModels;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return categoriesModels;
	}

}
