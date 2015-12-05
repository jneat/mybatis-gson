package com.github.jneat.mybatis;

import com.google.gson.JsonElement;

import java.io.Serializable;

public class JsonEntity implements Serializable {

    private static final long serialVersionUID = 2361613838967425855L;

    private long id;

    private JsonElement jsonArray;

    private JsonElement jsonObject;

    public JsonEntity() {
    }

    public JsonEntity(long id, JsonElement jsonArray, JsonElement jsonObject) {
        this.id = id;
        this.jsonArray = jsonArray;
        this.jsonObject = jsonObject;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public JsonElement getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JsonElement jsonArray) {
        this.jsonArray = jsonArray;
    }

    public JsonElement getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonElement jsonObject) {
        this.jsonObject = jsonObject;
    }
}
