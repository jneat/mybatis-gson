package com.github.jneat.mybatis;

import static org.assertj.core.api.Assertions.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PostgresqlTest extends JsonHandlersTestApi {

    private static final String PG_URL = "jdbc:postgresql://jneat/jneat?user=jneat&password=jneat";

    private static final String PG_SQL = "/postgresql.sql";

    private static final JsonParser PARSER = new JsonParser();

    private static SqlSessionFactory sessionFactory;

    private static JsonArray aNode;

    private static JsonObject oNode;

    @BeforeClass
    public static void init() throws SQLException, IOException {
        PGSimpleDataSource pgDs = new PGSimpleDataSource();
        pgDs.setUrl(PG_URL);
        sessionFactory = JsonHandlersTestApi.setUpDb(pgDs, PG_SQL);
    }

    JsonElement readJson(String json) throws IOException {
        return PARSER.parse(json);
    }

    @Test
    public void test1InsertNulls() {
        try (SqlSession sess = sessionFactory.openSession()) {
            JsonMapper mapper = sess.getMapper(JsonMapper.class);
            mapper.insert(new JsonEntity(1, null, null));
            mapper.insertValues(2, null, null);
            sess.commit();
        }
    }

    @Test
    public void test2InsertValues() throws IOException {
        aNode = (JsonArray)readJson("[1,2,3,7,8]");
        oNode = (JsonObject)readJson("{a:12, b:12.12, c: \"some name\"}");
        try (SqlSession sess = sessionFactory.openSession()) {
            JsonMapper mapper = sess.getMapper(JsonMapper.class);

            mapper.insert(new JsonEntity(3, aNode, oNode));
            mapper.insertValues(4, aNode, oNode);
            sess.commit();
        }
    }

    @Test
    public void test3ReadNulls() {
        try (SqlSession sess = sessionFactory.openSession()) {
            JsonMapper mapper = sess.getMapper(JsonMapper.class);
            JsonEntity e1 = mapper.get(1);
            assertThat(e1.getJsonArray().isJsonNull()).isTrue();
            assertThat(e1.getJsonObject().isJsonNull()).isTrue();

            JsonEntity e2 = mapper.get(2);
            assertThat(e2.getJsonArray().isJsonNull()).isTrue();
            assertThat(e2.getJsonObject().isJsonNull()).isTrue();
        }
    }

    @Test
    public void test4ReadValues() {
        try (SqlSession sess = sessionFactory.openSession()) {
            JsonMapper mapper = sess.getMapper(JsonMapper.class);
            JsonEntity e1 = mapper.get(3);

//            assertThat(aNode).isEqualTo(e1.getJsonArray());
//            assertThat(oNode).isEqualTo(e1.getJsonObject());
            compareArrays(aNode, e1.getJsonArray());
            compareObjects(oNode, e1.getJsonObject());

            JsonEntity e2 = mapper.get(4);
//            assertThat(aNode).isEqualTo(e2.getJsonArray());
//            assertThat(oNode).isEqualTo(e2.getJsonObject());
            compareArrays(aNode, e2.getJsonArray());
            compareObjects(oNode, e2.getJsonObject());
        }
    }

    protected void compareArrays(JsonElement a, JsonElement b) {
        assertThat(a.isJsonArray()).isEqualTo(b.isJsonArray());
        assertThat(a.getAsJsonArray().size()).isEqualTo(b.getAsJsonArray().size());
        for (int i = 0; i < a.getAsJsonArray().size(); i++) {
            assertThat(a.getAsJsonArray().get(i)).isEqualTo(b.getAsJsonArray().get(i));
        }
    }

    protected void compareObjects(JsonElement a, JsonElement b) {
        assertThat(a.isJsonObject()).isEqualTo(b.isJsonObject());

        for (Map.Entry<String, JsonElement> e : a.getAsJsonObject().entrySet()) {
            assertThat(b.getAsJsonObject().get(e.getKey())).isEqualTo(e.getValue());
        }
    }

}
