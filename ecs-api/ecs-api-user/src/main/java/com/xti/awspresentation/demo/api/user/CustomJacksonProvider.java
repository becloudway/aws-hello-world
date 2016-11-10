package com.xti.awspresentation.demo.api.user;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class CustomJacksonProvider extends JacksonJaxbJsonProvider {

    public Object readFrom(Class<Object> arg0, Type arg1, Annotation[] arg2, MediaType arg3, MultivaluedMap<String,String> arg4, InputStream arg5) throws IOException {
        try {
            return super.readFrom(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (JsonParseException jpe) {
            System.out.println("Malformed json.");
            throw new WebApplicationException();
        }
    }
}