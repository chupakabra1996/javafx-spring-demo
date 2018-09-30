package ru.kpfu.itis.service;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import ru.kpfu.itis.exception.ServerException;
import ru.kpfu.itis.model.Item;

import java.io.IOException;
import java.util.List;


public class ItemRestService implements RestService<Item> {

    private static final String URI = "http://localhost:8080/item/";

    private HttpClient client;

    private Header contentTypeHeader;

    private JsonItemConverter converter;

    public ItemRestService() {
        this.converter = new JsonItemConverter();
        contentTypeHeader = new BasicHeader("Content-Type", "application/json; charset=UTF-8");
        client = HttpClientBuilder.create().build();
    }

    @Override
    public Item create(Item object) throws ServerException {

        try {

            HttpPost post = new HttpPost(URI);

            post.addHeader(contentTypeHeader);

            post.setEntity(new StringEntity(converter.convert(object)));

            HttpResponse response = client.execute(post);

            throwServerException(response, HttpStatus.SC_CREATED);

            return converter.convert(response.getEntity().getContent());

        } catch (IOException e) {
            throw new ServerException(e.getMessage(), e, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Item update(int id, Item object) throws ServerException {
        try {

            HttpPut put = new HttpPut(URI + id);

            put.addHeader(contentTypeHeader);

            put.setEntity(new StringEntity(converter.convert(object)));

            HttpResponse response = client.execute(put);

            throwServerException(response, HttpStatus.SC_OK);

            return converter.convert(response.getEntity().getContent());

        } catch (IOException e) {
            throw new ServerException(e.getMessage(), e, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void delete(int id) throws ServerException {
        try {

            HttpClient client = HttpClientBuilder.create().build();
            HttpDelete delete = new HttpDelete(URI + id);
            delete.addHeader(contentTypeHeader);

            HttpResponse response = client.execute(delete);

            throwServerException(response, HttpStatus.SC_NO_CONTENT);

        } catch (IOException e) {
            throw new ServerException(e.getMessage(), e, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteAll() throws ServerException {
        try {

            HttpClient client = HttpClientBuilder.create().build();
            HttpDelete delete = new HttpDelete(URI);
            delete.addHeader(contentTypeHeader);

            HttpResponse response = client.execute(delete);

            throwServerException(response, HttpStatus.SC_NO_CONTENT);

        } catch (IOException e) {
            throw new ServerException(e.getMessage(), e, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Item> getAll() throws ServerException {

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet get = new HttpGet(URI);
            get.addHeader(contentTypeHeader);

            HttpResponse response = client.execute(get);

            throwServerException(response, HttpStatus.SC_OK);

            return converter.convertToList(response.getEntity().getContent());

        } catch (IOException e) {
            throw new ServerException(e.getMessage(), e, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Item getObject(int id) throws ServerException {
        try {

            HttpGet get = new HttpGet(URI + id);
            get.addHeader(contentTypeHeader);

            HttpResponse response = client.execute(get);

            throwServerException(response, HttpStatus.SC_OK);

            return converter.convert(response.getEntity().getContent());

        } catch (IOException e) {
            throw new ServerException(e.getMessage(), e, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }


    private void throwServerException(HttpResponse response, int goodCode) throws ServerException {

        if (response.getStatusLine().getStatusCode() == goodCode) return;

        switch (response.getStatusLine().getStatusCode()) {
            case 404:
                throw new ServerException("Item wasn't found!", HttpStatus.SC_NOT_FOUND);
            case 204:
                throw new ServerException("There is no content", HttpStatus.SC_NO_CONTENT);
            case 409:
                throw new ServerException("The item already exists", HttpStatus.SC_CONFLICT);
            default:
                throw new ServerException("Server error occurred!", response.getStatusLine().getStatusCode());
        }
    }

}
