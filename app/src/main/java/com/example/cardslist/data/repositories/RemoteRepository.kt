package com.example.cardslist.data.repositories

import com.example.cardslist.data.model.Card
import retrofit2.Response
import com.example.cardslist.data.model.Result
import com.example.cardslist.data.model.Error
import com.example.cardslist.data.repositories.api.CardsInterface
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepository @Inject constructor(private val retrofit: Retrofit) {
    val cardService = retrofit.create(CardsInterface::class.java)

    suspend fun fetchCardList(): Result<List<Card>> {
        return getResponse(
            request = {
                cardService.getCardsList()
                      },
            defaultErrorMessage = "Error fetching Card List")
    }


    private suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Result<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = parseError(result, retrofit)
                Result.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Result.error("Unknown Error", null)
        }
    }


    private fun parseError(response: Response<*>, retrofit: Retrofit): Error? {
        val converter = retrofit.responseBodyConverter<Error>(Error::class.java, arrayOfNulls(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            Error()
        }
    }

}