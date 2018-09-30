package ru.kpfu.itis.service;

import java.util.List;

public interface RestService<T> {

    /**
     * Save object
     *
     * @param object - any object to be saved
     * @return saved entity
     */
    T create(T object) throws Exception;

    /**
     * Update already existed object
     *
     * @param object - any object to be updated
     * @param id     - object's id
     * @return - successfully updated object
     */
    T update(int id, T object) throws Exception;


    /**
     * Delete object
     *
     * @param id - object's id
     */
    void delete(int id) throws Exception;


    /**
     * Delete all objects
     */
    void deleteAll() throws Exception;


    /**
     * Get all objects
     *
     * @return Collection of objects
     */
    List<T> getAll() throws Exception;


    /**
     * Get the object by id
     *
     * @param id - object's id
     * @return object with an appropriate id
     */
    T getObject(int id) throws Exception;


}
