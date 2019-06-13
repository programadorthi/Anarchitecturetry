package br.com.programadorthi.anarchtecturetry

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
abstract class BaseEspressoTest : KoinTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
}