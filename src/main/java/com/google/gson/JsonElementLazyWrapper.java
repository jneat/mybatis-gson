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
package com.google.gson;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Lazy JSON node wrapper, that will create generate real JsonElement after first call to it's methods.
 * Note, that in a case if input JSON string is invalid it may throw runtime exception from any method.
 * This is really awkward, but I can extend JsonElement only from com.google.gson package.
 */
public class JsonElementLazyWrapper extends JsonElement implements Serializable {

    private static final JsonParser PARSER = new JsonParser();

    private static final long serialVersionUID = -4882634013737740905L;

    private final String json;

    private JsonElement node;

    public JsonElementLazyWrapper(String json) {
        this.json = json;
    }

    /**
     * This will return source JSON string passed as argument into constructor.
     */
    public String getJsonSource() {
        return this.json;
    }

    private JsonElement element() {
        if (this.node == null) {
            synchronized (this) {
                if (this.node == null) {
                    node = PARSER.parse(json);
                }
            }
        }
        return this.node;
    }

    @Override
    JsonElement deepCopy() {
        return element().deepCopy();
    }

    @Override
    public boolean isJsonArray() {
        return element().isJsonArray();
    }

    @Override
    public boolean isJsonObject() {
        return element().isJsonObject();
    }

    @Override
    public boolean isJsonPrimitive() {
        return element().isJsonPrimitive();
    }

    @Override
    public boolean isJsonNull() {
        return element().isJsonNull();
    }

    @Override
    public JsonObject getAsJsonObject() {
        return element().getAsJsonObject();
    }

    @Override
    public JsonArray getAsJsonArray() {
        return element().getAsJsonArray();
    }

    @Override
    public JsonPrimitive getAsJsonPrimitive() {
        return element().getAsJsonPrimitive();
    }

    @Override
    public JsonNull getAsJsonNull() {
        return element().getAsJsonNull();
    }

    @Override
    public boolean getAsBoolean() {
        return element().getAsBoolean();
    }

    @Override
    public Number getAsNumber() {
        return element().getAsNumber();
    }

    @Override
    public String getAsString() {
        return element().getAsString();
    }

    @Override
    public double getAsDouble() {
        return element().getAsDouble();
    }

    @Override
    public float getAsFloat() {
        return element().getAsFloat();
    }

    @Override
    public long getAsLong() {
        return element().getAsLong();
    }

    @Override
    public int getAsInt() {
        return element().getAsInt();
    }

    @Override
    public byte getAsByte() {
        return element().getAsByte();
    }

    @Override
    public char getAsCharacter() {
        return element().getAsCharacter();
    }

    @Override
    public BigDecimal getAsBigDecimal() {
        return element().getAsBigDecimal();
    }

    @Override
    public BigInteger getAsBigInteger() {
        return element().getAsBigInteger();
    }

    @Override
    public short getAsShort() {
        return element().getAsShort();
    }

    @Override
    public String toString() {
        return element().toString();
    }

    @Override
    public int hashCode() {
        return element().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return element().equals(o);
    }
}
