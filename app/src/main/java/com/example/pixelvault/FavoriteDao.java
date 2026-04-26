package com.example.pixelvault;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteGame game);

    @Delete
    void delete(FavoriteGame game);

    @Query("SELECT * FROM favorites ORDER BY name ASC")
    LiveData<List<FavoriteGame>> getAllFavorites();

    @Query("SELECT * FROM favorites WHERE id = :gameId LIMIT 1")
    FavoriteGame getById(int gameId);
}