package com.example.cardslist.ui

import com.example.cardslist.data.managers.CardsManager
import com.example.cardslist.data.model.Card
import com.example.cardslist.data.model.Error
import com.example.cardslist.data.model.Result
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CardViewModelTest{

    @RelaxedMockK
    private lateinit var cardsManager: CardsManager

    @RelaxedMockK
    private lateinit var mockCardList: List<Card>

    private lateinit var dispatcher: TestDispatcher

    private lateinit var cardsViewModel: CardViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)

        mockkStatic(Dispatchers::class)
        every {
            Dispatchers.Default
        } returns dispatcher

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `when list is fetch successfully, then viewModel should contain the list and should not be in progress and error should be null`() =
        runTest {

            coEvery {
                cardsManager.getCardFilteredList()
            } returns flow { emit(Result.success(mockCardList)) }

            cardsViewModel = CardViewModel(cardsManager)

            advanceUntilIdle()

            cardsViewModel.state.value.apply {
                Assert.assertEquals(
                    cardList,
                    mockCardList
                )

                Assert.assertEquals(
                    inProgress,
                    false
                )

                Assert.assertEquals(
                    errorMessage,
                    null
                )
            }

        }

    @Test
    fun `when list is  not fetch successfully, then viewModel should contain error and should not be in progress and list should be null`() =
        runTest {

            coEvery {
                cardsManager.getCardFilteredList()
            } returns flow { emit(Result.error("any message", Error(404, "any message"))) }

            cardsViewModel = CardViewModel(cardsManager)

            advanceUntilIdle()

            cardsViewModel.state.value.apply {
                Assert.assertEquals(
                    cardList,
                    null
                )

                Assert.assertEquals(
                    inProgress,
                    false
                )

                Assert.assertEquals(
                    errorMessage,
                    "any message"
                )
            }

        }

    @Test
    fun `when fetch is in progress, then viewModel inprogress should be true and should contain both error and list as null`() =
        runTest {

            coEvery {
                cardsManager.getCardFilteredList()
            } returns flow { emit(Result.inProgress()) }

            cardsViewModel = CardViewModel(cardsManager)

            advanceUntilIdle()

            cardsViewModel.state.value.apply {
                Assert.assertEquals(
                    cardList,
                    null
                )

                Assert.assertEquals(
                    inProgress,
                    true
                )

                Assert.assertEquals(
                    errorMessage,
                    null
                )
            }

        }

}