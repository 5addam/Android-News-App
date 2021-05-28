package com.newsapp.db

import androidx.room.TypeConverter
import com.newsapp.data.Article

class Converters {

    @TypeConverter
    fun fromSource(source: Article.ArticleSource): String{
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Article.ArticleSource{
        return Article.ArticleSource(name, name)
    }
}