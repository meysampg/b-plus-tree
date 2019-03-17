package com.riywo.ninja.bptree

import java.nio.ByteBuffer

abstract class Node(
    private val keyIO: AvroGenericRecord.IO,
    private val recordIO: AvroGenericRecord.IO,
    protected val page: Page
) {
    val id get() = page.id
    val type get() = page.nodeType
    val previousId get() = page.previousId
    val nextId get() = page.nextId
    val size get() = page.size
    val records get() = page.records
    val minRecord get() = page.records.first()

    fun dump() = page.dump()

    protected sealed class FindResult {
        data class ExactMatch(val index: Int, val byteBuffer: ByteBuffer) : FindResult()
        data class FirstGraterThanMatch(val index: Int) : FindResult()
    }

    protected fun find(key: AvroGenericRecord): FindResult? {
        if (page.records.isEmpty()) return null
        val keyBytes = keyIO.encode(key).toByteArray()
        page.records.forEachIndexed { index, byteBuffer ->
            when(compareKeys(byteBuffer, keyBytes)) {
                0 -> return FindResult.ExactMatch(index, byteBuffer)
                1 -> return FindResult.FirstGraterThanMatch(index)
            }
        }
        return FindResult.FirstGraterThanMatch(page.records.size)
    }

    protected fun compareKeys(aByteBuffer: ByteBuffer, bByteBuffer: ByteBuffer): Int {
        val aBytes = aByteBuffer.toByteArray()
        val bBytes = bByteBuffer.toByteArray()
        return keyIO.compare(aBytes, bBytes)
    }

    protected fun compareKeys(aBytes: ByteArray, bByteBuffer: ByteBuffer): Int {
        val bBytes = bByteBuffer.toByteArray()
        return keyIO.compare(aBytes, bBytes)
    }

    protected fun compareKeys(aByteBuffer: ByteBuffer, bBytes: ByteArray): Int {
        val aBytes = aByteBuffer.toByteArray()
        return keyIO.compare(aBytes, bBytes)
    }

    protected fun createRecord(byteBuffer: ByteBuffer): AvroGenericRecord {
        val record = AvroGenericRecord(recordIO)
        record.load(byteBuffer)
        return record
    }
}