package com.example.mvvmcleantemplate.domain.repository

import com.example.mvvmcleantemplate.domain.model.entity.User
import kotlinx.coroutines.flow.Flow

interface AppDbRepository {

    fun getAllUsers(): Flow<List<User>>

    fun getUserById(id: String): Flow<User?>


    suspend fun insertUser(user: User): Long
    suspend fun updateUser(user: User)

//    suspend fun deleteUser(user: User)

    suspend fun executeRawQuery(query: String): Long

}