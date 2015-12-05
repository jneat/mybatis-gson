# JSON support for Mybatis 3.x using Gson 2.x

Provide support for JSON like field types in any Database.
I'm developed this handler with PostgreSql in mind, 
but it looks like it can be used with any other database even without JSON support.

Artifact does not include direct gson dependencies - it is up to you to add them into your project.
Looks like you can use any Gson version compatible with API version >= 2.3

[![Release](https://jitpack.io/v/jneat/mybatis-gson.svg)](https://jitpack.io/#jneat/mybatis-gson)  
[API javadoc](https://jitpack.io/com/github/jneat/mybatis-gson/-SNAPSHOT/javadoc/)

##How does it work
Because JDBC does not support JSON types, it transfer JSON to/from database as a string.
It serialize JSON to string on save and deserialize from string on read.
This feature means that we are really do not care if our DB can support JSON or not.

###Lazy reading
Type handler returns TreeNode wrapper that actually does not parse JSON from string.
It is waiting for you to call any of its methods - only then it will read JSON into structure.
But this approach may lead to **unexpected runtime exception** in a case if your database will return
invalid JSON string.

##Add to your project

You can add this artifact to your project using [JitPack](https://jitpack.io/#jneat/mybatis-gson).  
All versions list, instructions for gradle, maven, ivy etc. can be found by link above.

To get latest commit use -SNAPSHOT instead version number.

## Configure
In result map configuration you should use ```javaType=com.google.gson.JsonElement"```

You should not configure anything if you want to use JsonElement types as arguments in your mapper
functions, but keep in mind that handler only expect objects of type JsonArray or JsonObject.

### Mybatis config
```
<!-- mybatis-config.xml -->
<typeHandlers>
  <typeHandler handler="com.github.jneat.mybatis.JsonElementTypeHandler"/>
</typeHandlers>
```

Or you can use package search

```
<!-- mybatis-config.xml -->
<typeHandlers>
  <package name="com.github.jneat.mybatis"/>
</typeHandlers>
```

### Mybatis via Spring
```
<bean id="SomeId" class="org.mybatis.spring.SqlSessionFactoryBean">
    <!-- your configuration -->
    <property name="typeHandlersPackage" value="com.github.jneat.mybatis" />
</bean>
```