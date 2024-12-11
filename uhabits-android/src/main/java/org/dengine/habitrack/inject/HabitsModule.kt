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

package org.dengine.habitrack.inject

import dagger.Module
import dagger.Provides
import org.dengine.habitrack.core.AppScope
import org.dengine.habitrack.core.commands.CommandRunner
import org.dengine.habitrack.core.database.Database
import org.dengine.habitrack.core.database.DatabaseOpener
import org.dengine.habitrack.core.io.Logging
import org.dengine.habitrack.core.models.HabitList
import org.dengine.habitrack.core.models.ModelFactory
import org.dengine.habitrack.core.models.sqlite.SQLModelFactory
import org.dengine.habitrack.core.models.sqlite.SQLiteHabitList
import org.dengine.habitrack.core.preferences.Preferences
import org.dengine.habitrack.core.preferences.WidgetPreferences
import org.dengine.habitrack.core.reminders.ReminderScheduler
import org.dengine.habitrack.core.tasks.TaskRunner
import org.dengine.habitrack.core.ui.NotificationTray
import org.dengine.habitrack.database.AndroidDatabase
import org.dengine.habitrack.database.AndroidDatabaseOpener
import org.dengine.habitrack.intents.IntentScheduler
import org.dengine.habitrack.io.AndroidLogging
import org.dengine.habitrack.notifications.AndroidNotificationTray
import org.dengine.habitrack.preferences.SharedPreferencesStorage
import org.dengine.habitrack.utils.DatabaseUtils
import java.io.File

@Module
class HabitsModule(dbFile: File) {

    val db: Database = AndroidDatabase(DatabaseUtils.openDatabase(), dbFile)

    @Provides
    @AppScope
    fun getPreferences(storage: SharedPreferencesStorage): Preferences {
        return Preferences(storage)
    }

    @Provides
    @AppScope
    fun getReminderScheduler(
        sys: IntentScheduler,
        commandRunner: CommandRunner,
        habitList: HabitList,
        widgetPreferences: WidgetPreferences
    ): ReminderScheduler {
        return ReminderScheduler(commandRunner, habitList, sys, widgetPreferences)
    }

    @Provides
    @AppScope
    fun getTray(
        taskRunner: TaskRunner,
        commandRunner: CommandRunner,
        preferences: Preferences,
        screen: AndroidNotificationTray
    ): NotificationTray {
        return NotificationTray(taskRunner, commandRunner, preferences, screen)
    }

    @Provides
    @AppScope
    fun getWidgetPreferences(
        storage: SharedPreferencesStorage
    ): WidgetPreferences {
        return WidgetPreferences(storage)
    }

    @Provides
    @AppScope
    fun getModelFactory(): ModelFactory {
        return SQLModelFactory(db)
    }

    @Provides
    @AppScope
    fun getHabitList(list: SQLiteHabitList): HabitList {
        return list
    }

    @Provides
    @AppScope
    fun getDatabaseOpener(opener: AndroidDatabaseOpener): DatabaseOpener {
        return opener
    }

    @Provides
    @AppScope
    fun getLogging(): Logging {
        return AndroidLogging()
    }

    @Provides
    @AppScope
    fun getDatabase(): Database {
        return db
    }
}
