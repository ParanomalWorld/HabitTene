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
package org.dengine.habitrack.core.io

import org.dengine.habitrack.core.database.Cursor
import org.dengine.habitrack.core.database.Database
import org.dengine.habitrack.core.database.DatabaseOpener
import org.dengine.habitrack.core.models.Entry
import org.dengine.habitrack.core.models.Frequency
import org.dengine.habitrack.core.models.Habit
import org.dengine.habitrack.core.models.HabitList
import org.dengine.habitrack.core.models.ModelFactory
import org.dengine.habitrack.core.models.Timestamp
import org.dengine.habitrack.core.utils.DateUtils
import org.dengine.habitrack.core.utils.isSQLite3File
import java.io.File
import javax.inject.Inject

/**
 * Class that imports data from database files exported by Tickmate.
 */
class TickmateDBImporter @Inject constructor(
    private val habitList: HabitList,
    private val modelFactory: ModelFactory,
    private val opener: DatabaseOpener
) : AbstractImporter() {

    override fun canHandle(file: File): Boolean {
        if (!file.isSQLite3File()) return false
        val db = opener.open(file)
        val c = db.query(
            "select count(*) from SQLITE_MASTER " +
                "where name='tracks' or name='track2groups'"
        )
        val result = c.moveToNext() && c.getInt(0) == 2
        c.close()
        db.close()
        return result
    }

    override fun importHabitsFromFile(file: File) {
        val db = opener.open(file)
        db.beginTransaction()
        createHabits(db)
        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    private fun createCheckmarks(
        db: Database,
        habit: Habit,
        tickmateTrackId: Int
    ) {
        var c: Cursor? = null
        try {
            c = db.query(
                "select distinct year, month, day from ticks where _track_id=?",
                tickmateTrackId.toString()
            )
            if (!c.moveToNext()) return
            do {
                val year = c.getInt(0)!!
                val month = c.getInt(1)!!
                val day = c.getInt(2)!!
                val cal = DateUtils.getStartOfTodayCalendar()
                cal[year, month] = day
                habit.originalEntries.add(Entry(Timestamp(cal), Entry.YES_MANUAL))
            } while (c.moveToNext())
        } finally {
            c?.close()
        }
    }

    private fun createHabits(db: Database) {
        var c: Cursor? = null
        try {
            c = db.query("select _id, name, description from tracks")
            if (!c.moveToNext()) return
            do {
                val id = c.getInt(0)!!
                val name = c.getString(1)
                val description = c.getString(2)
                val habit = modelFactory.buildHabit()
                habit.name = name!!
                habit.description = description ?: ""
                habit.frequency = Frequency.DAILY
                habitList.add(habit)
                createCheckmarks(db, habit, id)
            } while (c.moveToNext())
        } finally {
            c?.close()
        }
    }
}
