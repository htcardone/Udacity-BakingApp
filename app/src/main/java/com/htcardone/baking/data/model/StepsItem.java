package com.htcardone.baking.data.model;

import com.google.gson.annotations.SerializedName;

public class StepsItem{

	@SerializedName("videoURL")
	private String videoURL;

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private int id;

	@SerializedName("shortDescription")
	private String shortDescription;

	@SerializedName("thumbnailURL")
	private String thumbnailURL;

	public void setVideoURL(String videoURL){
		this.videoURL = videoURL;
	}

	public String getVideoURL(){
		return videoURL;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setShortDescription(String shortDescription){
		this.shortDescription = shortDescription;
	}

	public String getShortDescription(){
		return shortDescription;
	}

	public void setThumbnailURL(String thumbnailURL){
		this.thumbnailURL = thumbnailURL;
	}

	public String getThumbnailURL(){
		return thumbnailURL;
	}

	public StepsItem(String videoURL, String description, int id, String shortDescription, String thumbnailURL) {
		this.videoURL = videoURL;
		this.description = description;
		this.id = id;
		this.shortDescription = shortDescription;
		this.thumbnailURL = thumbnailURL;
	}

	@Override
	public String toString() {
		return "StepsItem{" +
				"videoURL='" + videoURL + '\'' +
				", description='" + description + '\'' +
				", id=" + id +
				", shortDescription='" + shortDescription + '\'' +
				", thumbnailURL='" + thumbnailURL + '\'' +
				'}';
	}
}