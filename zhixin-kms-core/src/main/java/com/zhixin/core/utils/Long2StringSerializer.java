package com.zhixin.core.utils;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

 
@JacksonStdImpl
public final class Long2StringSerializer
    extends StdScalarSerializer<Long>
{ 
	private static final long serialVersionUID = -5418998540647592585L;
	
	final static Long2StringSerializer instance = new Long2StringSerializer();

    public Long2StringSerializer() { super(Long.class); }
    
    @Override
    public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonGenerationException
    {
        jgen.writeString(value.toString());
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint)
    {
        return createSchemaNode("string", true);
    }
}

