package com.androidizate.clase9.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.androidizate.clase9.model.Tag;

import java.util.List;

/**
 * @author Andres Oller
 */
@Dao
public interface TagDao {

    @Query("SELECT * FROM tag")
    List<Tag> getAll();

    @Query("SELECT * FROM tag WHERE id IN (:tagIds)")
    List<Tag> loadAllByIds(int[] tagIds);

    @Query("SELECT * FROM tag WHERE id LIKE :id AND")
    Tag findById(int id);

    @Query("SELECT  * FROM tag td, tag tg, tag_tag tt WHERE tg.tag_name = :tagName AND tg.id = tt.tad_id AND td.id = tt.tag_id")
    List<Tag> getTodosByTag(String tagName);

    @Insert
    void insertAll(Tag... users);

    @Delete
    void delete(Tag user);

    @Update
    void updateTags(Tag... tags);
}
