package com.dicoding.habitapp.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.sqlite.db.SupportSQLiteQuery

//TODO 2 : Define data access object (DAO)
interface HabitDao {

    fun getHabits(query: SupportSQLiteQuery): PagingSource<Int, Habit>

    fun getHabitById(habitId: Int): LiveData<Habit>

    fun insertHabit(habit: Habit): Long

    fun insertAll(vararg habits: Habit)

    fun deleteHabit(habits: Habit)

    fun getRandomHabitByPriorityLevel(level: String): LiveData<Habit>
}
