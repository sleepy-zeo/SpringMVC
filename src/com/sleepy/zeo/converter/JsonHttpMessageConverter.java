package com.sleepy.zeo.converter;

import com.google.gson.Gson;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.*;

public class JsonHttpMessageConverter<T> extends AbstractHttpMessageConverter<T> {

    public JsonHttpMessageConverter() {
    }

    @Override
    protected boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    protected T readInternal(Class<? extends T> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream inputStream = httpInputMessage.getBody();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);

        StringBuilder builder = new StringBuilder();
        while (true) {
            String data = bufferedReader.readLine();
            if (data == null) {
                break;
            }
            if (!data.isEmpty()) {
                builder.append(data);
            }
        }
        String json = builder.toString();
        return new Gson().fromJson(json, aClass);
    }

    @Override
    protected void writeInternal(T t, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        String json = new Gson().toJson(t);
        byte[] buffer = json.getBytes();
        httpOutputMessage.getBody().write(buffer);
    }

}
