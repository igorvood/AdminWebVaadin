package ru.vood.amdWeb.util.abstraction

import java.util.*

interface RepositoryForView<T, ID> {
    //fun getOne(id: ID): T

    fun findById(id: ID): Optional<T>

    fun findAll(): List<T>

    fun delete(entity: T) //: Unit

    fun save(entity: T): T

}