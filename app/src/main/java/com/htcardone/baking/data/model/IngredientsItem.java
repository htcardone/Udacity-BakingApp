package com.htcardone.baking.data.model;

import com.google.gson.annotations.SerializedName;

public class IngredientsItem {

	@SerializedName("quantity")
	private float quantity;

	@SerializedName("measure")
	private String measure;

	@SerializedName("ingredient")
	private String ingredient;

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public float getQuantity(){
		return quantity;
	}

	public void setMeasure(String measure){
		this.measure = measure;
	}

	public String getMeasure(){
		return measure;
	}

	public void setIngredient(String ingredient){
		this.ingredient = ingredient;
	}

	public String getIngredient(){
		return ingredient;
	}

    public IngredientsItem(float quantity, String measure, String ingredient) {
		this.quantity = quantity;
		this.measure = measure;
		this.ingredient = ingredient;
	}

    @Override
	public String toString() {
		return "IngredientsItem{" +
				"quantity=" + quantity +
				", measure='" + measure + '\'' +
				", ingredient='" + ingredient + '\'' +
				'}';
	}
}