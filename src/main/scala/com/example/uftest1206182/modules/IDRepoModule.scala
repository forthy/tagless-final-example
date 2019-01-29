package com.example.uftest1206182.modules

import com.example.uftest1206182.functions.implementations.{RideIDRepo, UUIDRepo}
import com.google.inject.Provides
import com.twitter.inject.TwitterModule
import javax.inject.Singleton

object IDRepoModule extends TwitterModule {
  @Singleton
  @Provides
  def providesRideIDRepo: RideIDRepo = RideIDRepo

  @Singleton
  @Provides
  def providesUUIDRepo: UUIDRepo = UUIDRepo
}
