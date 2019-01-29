package com.example.uftest1206182.docker

import com.spotify.docker.client.messages.PortBinding
import com.twitter.inject.Logging
import com.whisk.docker.testkit.scalatest.DockerTestKitForAll
import com.whisk.docker.testkit.{Container, ContainerSpec, DockerReadyChecker, ManagedContainers}
import org.scalatest.Suite

trait DockerMongodbService extends DockerTestKitForAll with Logging { self: Suite =>
  val DefaultMongodbPort: Int = 27017

  val mongodbContainer: Container = ContainerSpec("mongo:3.6.5")
    .withPortBindings(DefaultMongodbPort -> PortBinding.of("0.0.0.0", DefaultMongodbPort))
    .withReadyChecker(DockerReadyChecker.LogLineContains("waiting for connections on port"))
    .toContainer

  override val managedContainers: ManagedContainers = mongodbContainer.toManagedContainer
}
