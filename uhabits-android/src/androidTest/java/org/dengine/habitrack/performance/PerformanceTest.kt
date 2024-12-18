/*
 * Copyright (C) 2016-2021 Álinson Santos Xavier <git@axavier.org>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dengine.habitrack.performance

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import org.dengine.habitrack.BaseAndroidTest
import org.dengine.habitrack.core.commands.CreateHabitCommand
import org.dengine.habitrack.core.commands.CreateRepetitionCommand
import org.dengine.habitrack.core.models.Habit
import org.dengine.habitrack.core.models.Timestamp
import org.dengine.habitrack.core.models.Timestamp.Companion.DAY_LENGTH
import org.dengine.habitrack.core.models.sqlite.SQLModelFactory
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class PerformanceTest : BaseAndroidTest() {
    private var habit: Habit? = null
    override fun setUp() {
        super.setUp()
        habit = fixtures.createLongHabit()
    }

    @Ignore
    @Test(timeout = 5000)
    fun benchmarkCreateHabitCommand() {
        val db = (modelFactory as SQLModelFactory).database
        db.beginTransaction()
        for (i in 0..999) {
            val model = modelFactory.buildHabit()
            CreateHabitCommand(modelFactory, habitList, model).run()
        }
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    @Ignore
    @Test(timeout = 5000)
    fun benchmarkCreateRepetitionCommand() {
        val db = (modelFactory as SQLModelFactory).database
        db.beginTransaction()
        val habit = fixtures.createEmptyHabit()
        for (i in 0..4999) {
            val timestamp: Timestamp = Timestamp(i * DAY_LENGTH)
            CreateRepetitionCommand(habitList, habit, timestamp, 1, "").run()
        }
        db.setTransactionSuccessful()
        db.endTransaction()
    }
}
