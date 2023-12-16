package com.example.rhythmix.models;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    List<Track> data;

    public List<Track> getData() {
        return data;
}
}