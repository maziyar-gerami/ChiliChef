package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.DesignCategoricalModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParserCategoricalDesign {

	private static final String KEY_ID 		= "Id_Design";
	private static final String KEY_Title 	= "Title";
	private static final String KEY_Pic    = "Pic_URL";




	public ArrayList<DesignCategoricalModel> parseJson(String jsonstring){
		ArrayList<DesignCategoricalModel> designCategoricalModels = new ArrayList<DesignCategoricalModel>();

		try {

			JSONArray jsonarray = new JSONArray(jsonstring);
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobject = jsonarray.getJSONObject(i);
				DesignCategoricalModel categoriesModel = new DesignCategoricalModel();
				categoriesModel.setId_Design(jsonobject.getInt(KEY_ID));
				categoriesModel.setTitle(jsonobject.getString(KEY_Title));
				categoriesModel.setPic_Url(jsonobject.getString(KEY_Pic)+".jpg");

				designCategoricalModels.add(categoriesModel);
			}
			return designCategoricalModels;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return designCategoricalModels;
	}

}
