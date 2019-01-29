package com.example.uftest1206182.services

import io.catbird.util.Rerunnable

trait RerunnableService[-Req, +Rep] extends (Req => Rerunnable[Rep])
