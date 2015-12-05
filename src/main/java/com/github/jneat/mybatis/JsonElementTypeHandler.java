/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 rumatoest at github
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. 
 */
package com.github.jneat.mybatis;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonElementLazyWrapper;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Map PostgreSQL JSON type to Gson JsonElement does not return null (empty node instead).
 * Just use JSON string representation as intermediate data format.
 *
 * @see JsonElement
 */
@MappedTypes({JsonElement.class, JsonArray.class, JsonObject.class})
public class JsonElementTypeHandler extends NotNullResultTypeHandler<JsonElement> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JsonElement parameter, JdbcType jdbcType) throws SQLException {
        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setLenient(false);
            Streams.write(parameter, jsonWriter);
            ps.setString(i, sw.toString());
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public JsonElement getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonSource = rs.getString(columnName);
        if (jsonSource != null) {
            return fromString(jsonSource);
        }
        return JsonNull.INSTANCE;
    }

    @Override
    public JsonElement getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String jsonSource = rs.getString(columnIndex);
        if (jsonSource != null) {
            return fromString(jsonSource);
        }
        return JsonNull.INSTANCE;
    }

    @Override
    public JsonElement getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String jsonSource = cs.getString(columnIndex);
        if (jsonSource != null) {
            return fromString(jsonSource);
        }
        return JsonNull.INSTANCE;
    }

    private JsonElement fromString(String source) {
        return source == null ? null : new JsonElementLazyWrapper(source);
    }
}
