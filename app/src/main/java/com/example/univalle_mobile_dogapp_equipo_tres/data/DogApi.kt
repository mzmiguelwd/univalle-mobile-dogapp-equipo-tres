package com.example.univalle_mobile_dogapp_equipo_tres.data

import com.example.univalle_mobile_dogapp_equipo_tres.model.BreedsResponse
import com.example.univalle_mobile_dogapp_equipo_tres.model.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApi {

    @GET("breeds/list/all")
    suspend fun getAllBreeds(): BreedsResponse

    @GET("breed/{breed}/images/random")
    suspend fun getRandomImageByBreed(@Path("breed") breed: String): ImageResponse

    @GET("breeds/image/random")
    suspend fun getRandomImage(): ImageResponse
}