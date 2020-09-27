package com.neuron.taggedgallery;

public class Image {
    private String name;
    private String id;
    private String imagePath;

    public Image(String name, String id, String imagePath) {
        this.name = name;
        this.id = id;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }
}
