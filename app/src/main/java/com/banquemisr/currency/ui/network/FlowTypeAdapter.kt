package com.banquemisr.currency.ui.network

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import java.io.IOException

class FlowTypeAdapter<T>(private val adapter: TypeAdapter<T>) : TypeAdapter<Flow<T>>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Flow<T>?) {
        throw UnsupportedOperationException("Writing Flow is not supported")
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Flow<T> {
        val list = mutableListOf<T>()
        `in`.beginArray()
        while (`in`.hasNext()) {
            val item = adapter.read(`in`)
            list.add(item)
        }
        `in`.endArray()
        return list.asFlow()
    }
}