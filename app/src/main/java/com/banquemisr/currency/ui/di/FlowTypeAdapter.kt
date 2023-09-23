package com.banquemisr.currency.ui.di

import kotlinx.coroutines.flow.asFlow

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import kotlinx.coroutines.flow.Flow


class FlowTypeAdapter<T>(private val adapter: TypeAdapter<T>) : TypeAdapter<Flow<T>>() {
    override fun write(out: JsonWriter, value: Flow<T>?) {
        throw UnsupportedOperationException("Writing a Flow is not supported")
    }

    override fun read(`in`: JsonReader): Flow<T> {
        if (`in`.peek() == JsonToken.NULL) {
            `in`.nextNull()
            return emptyList<T>().asFlow()
        }

        val list = mutableListOf<T>()
        `in`.beginArray()
        while (`in`.hasNext()) {
            list.add(adapter.read(`in`))
        }
        `in`.endArray()
        return list.asFlow()
    }
}
