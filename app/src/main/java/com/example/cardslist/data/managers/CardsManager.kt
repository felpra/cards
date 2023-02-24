package com.example.cardslist.data.managers

import com.example.cardslist.data.model.Card
import com.example.cardslist.data.model.Result
import com.example.cardslist.data.repositories.LocalRepository
import com.example.cardslist.data.repositories.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.reflect.full.memberProperties

class CardsManager @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {

    suspend fun getCardFilteredList(): Flow<Result<Any>> {
        return flow {
            emit(Result.inProgress())
            emit(Result.success(localRepository.getCardsCache()))
            val remote = remoteRepository.fetchCardList()

            if (remote.status == Result.Status.SUCCESS) {
                remote.data?.let { it ->
                    val filteredList = withContext(Dispatchers.Default) {
                        filterList(it)
                    }
                    localRepository.deleteAll()
                    localRepository.saveCards(filteredList)
                    emit(Result.success(filteredList))
                }
            } else
                emit(remote)

        }.flowOn(Dispatchers.IO)
    }

    private fun filterList(list: List<Card>): List<Card> {
        val filter = list.filter {
            !hasNullMembers(it)
        }
        return filter.distinctBy { it.name }
    }

    private fun hasNullMembers(card: Card): Boolean {
        Card::class.memberProperties.forEach { member ->
            val value = member.get(card) ?: return true
        }
        return false
    }

}