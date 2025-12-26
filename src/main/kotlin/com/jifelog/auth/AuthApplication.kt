package com.jifelog.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.net.Socket

@SpringBootApplication
class AuthApplication

fun main(args: Array<String>) {
/*    println(java.lang.management.ManagementFactory.getRuntimeMXBean().inputArguments)

    Socket("192.168.0.52", 5432).use {
        println("TCP OK")
    }

    Socket("192.168.0.56", 6379).use {
        println("redis TCP OK")
    }*/



    runApplication<AuthApplication>(*args)
}
