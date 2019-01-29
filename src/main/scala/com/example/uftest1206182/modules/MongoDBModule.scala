package com.example.uftest1206182.modules

import com.example.uftest1206182.datastore.Database
import com.example.uftest1206182.models.Order
import com.github.benmanes.caffeine.cache.Caffeine
import com.google.inject.Provides
import com.twitter.inject.TwitterModule
import javax.inject.Singleton
import scalacache._
import scalacache.caffeine._
import com.example.uftest1206182.util.AppConfigLib._

object MongoDBModule extends TwitterModule {
  @Singleton
  @Provides
  def providesMongoDB: Database = new Database

  @Singleton
  @Provides
  def providesOrderCache: Cache[Order] =
    CaffeineCache(
      Caffeine
        .newBuilder()
        .maximumSize(getConfig[Long]("MAXIMUM_CACHE_ENTRIES").getOrElse(1000L))
        .build[String, Entry[Order]]
    )
}
