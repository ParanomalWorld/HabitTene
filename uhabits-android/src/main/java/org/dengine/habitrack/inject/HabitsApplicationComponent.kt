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

import android.content.Context
import dagger.Component
import org.dengine.habitrack.core.AppScope
import org.dengine.habitrack.core.commands.CommandRunner
import org.dengine.habitrack.core.io.GenericImporter
import org.dengine.habitrack.core.io.Logging
import org.dengine.habitrack.core.models.HabitList
import org.dengine.habitrack.core.models.ModelFactory
import org.dengine.habitrack.core.preferences.Preferences
import org.dengine.habitrack.core.preferences.WidgetPreferences
import org.dengine.habitrack.core.reminders.ReminderScheduler
import org.dengine.habitrack.core.tasks.TaskRunner
import org.dengine.habitrack.core.ui.NotificationTray
import org.dengine.habitrack.core.ui.screens.habits.list.HabitCardListCache
import org.dengine.habitrack.core.utils.MidnightTimer
import org.dengine.habitrack.intents.IntentFactory
import org.dengine.habitrack.intents.IntentParser
import org.dengine.habitrack.intents.PendingIntentFactory
import org.dengine.habitrack.receivers.ReminderController
import org.dengine.habitrack.tasks.AndroidTaskRunner
import org.dengine.habitrack.widgets.WidgetUpdater

@AppScope
@Component(modules = [AppContextModule::class, HabitsModule::class, AndroidTaskRunner::class])
interface HabitsApplicationComponent {
    val commandRunner: CommandRunner

    @get:AppContext
    val context: Context
    val genericImporter: GenericImporter
    val habitCardListCache: HabitCardListCache
    val habitList: HabitList
    val intentFactory: IntentFactory
    val intentParser: IntentParser
    val logging: Logging
    val midnightTimer: MidnightTimer
    val modelFactory: ModelFactory
    val notificationTray: NotificationTray
    val pendingIntentFactory: PendingIntentFactory
    val preferences: Preferences
    val reminderScheduler: ReminderScheduler
    val reminderController: ReminderController
    val taskRunner: TaskRunner
    val widgetPreferences: WidgetPreferences
    val widgetUpdater: WidgetUpdater
}
