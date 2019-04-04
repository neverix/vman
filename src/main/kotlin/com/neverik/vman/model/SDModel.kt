package com.neverik.vman.model

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ObjectProperty
import java.io.*
import java.lang.NullPointerException
import java.net.SocketException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


abstract class SDModel {
    val disks = mapOf(
            "Alpine Linux" to Pair("3.8.2", URL("http://dl-cdn.alpinelinux.org/alpine/v3.8/releases/x86_64/alpine-virt-3.8.2-x86_64.iso")),
            "Debian" to Pair("9.8", URL("https://cdimage.debian.org/debian-cd/current/amd64/iso-cd/debian-9.8.0-amd64-netinst.iso"))
    )
    abstract fun getFileForDisk(name: String, version: String): String
    abstract val basePath: String

    fun initialize() {
        for(disk in disks.keys) {
            Paths.get(basePath, disk).toFile().mkdirs()
        }
    }

    fun cleanup(users: List<String>) {
        val allOSes = File(basePath).listFiles()
        val allVersions = allOSes.flatMap { it.listFiles().asIterable() }
        val downloadedDisks = allVersions.map { Pair(it.parentFile.name, it.name) }

        for((disk, version) in downloadedDisks) {
            val path = getFileForDisk(disk, version)
            if(!users.contains(path)) {
                File(path).delete()
            }
        }
    }

    fun download(name: String, progress: DoubleProperty, canceller: ObjectProperty<() -> Unit>): String {
        val (version, url) = disks[name] ?: throw NullPointerException()
        val path = Paths.get(getFileForDisk(name, version))
        if(path.toFile().exists()) return path.toString()

        val conn = url.openConnection()
        val stream = conn.getInputStream()

        ProcessInputStream(stream, conn.contentLength.toDouble(), progress).use {
            canceller.set {
                stream.close()
                path.toFile().delete()
            }
            try {
                Files.copy(it, path, StandardCopyOption.REPLACE_EXISTING)
            } catch (e: SocketException) {
                return ""
            }
        }
        return path.toString()
    }

    class ProcessInputStream(private val input: InputStream, private val length: Double, private val listener: DoubleProperty): InputStream() {
        private var sumRead: Int = 0
        private var percent: Double = 0.toDouble()

        init {
            sumRead = 0
        }

        override fun read(b: ByteArray): Int {
            val readCount = input.read(b)
            evaluatePercent(readCount.toLong())
            return readCount
        }

        override fun read(b: ByteArray, off: Int, len: Int): Int {
            val readCount = input.read(b, off, len)
            evaluatePercent(readCount.toLong())
            return readCount
        }

        override fun skip(n: Long): Long {
            val skip = input.skip(n)
            evaluatePercent(skip)
            return skip
        }

        override fun read(): Int {
            val read = input.read()
            if (read != -1) {
                evaluatePercent(1)
            }
            return read
        }

        private fun evaluatePercent(readCount: Long) {
            if (readCount != -1L) {
                sumRead += readCount.toInt()
                percent = sumRead * 1.0 / if(length == -1.0) Double.MAX_VALUE else length
            }
            notifyListener()
        }

        private fun notifyListener() {
            listener.set(percent)
        }
    }
}