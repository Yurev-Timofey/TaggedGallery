package com.neuron.taggedgallery;

public class Image {
    private String name;
    private String id;
    private int image;

    public Image(String name, String id, int image) {
        this.name = name;
        this.id = id;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getImage() {
        return image;
    }
}
