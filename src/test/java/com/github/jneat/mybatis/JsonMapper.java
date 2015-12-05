package com.github.jneat.mybatis;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.ibatis.annotations.Param;

public interface JsonMapper {

    JsonEntity get(@Param("id") long id);

    int insert(JsonEntity entity);

    int insertValues(
        @Param("id") long id,
        @Param("jsonArray") JsonArray jArray,
        @Param("jsonObject") JsonObject jObj
    );
}
