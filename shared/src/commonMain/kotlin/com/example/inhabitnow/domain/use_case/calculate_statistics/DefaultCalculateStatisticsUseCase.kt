package com.example.inhabitnow.domain.use_case.calculate_statistics

import com.example.inhabitnow.data.model.record.RecordEntity
import com.example.inhabitnow.data.model.record.content.RecordContentEntity
import com.example.inhabitnow.data.model.task.content.ArchiveContentEntity
import com.example.inhabitnow.data.model.task.content.BaseTaskContentEntity
import com.example.inhabitnow.data.model.task.content.FrequencyContentEntity
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.data.model.task.derived.TaskWithAllContentEntity
import com.example.inhabitnow.data.repository.record.RecordRepository
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.model.statistics.TaskStatisticsModel
import com.example.inhabitnow.domain.model.statistics.TaskStatus
import com.example.inhabitnow.domain.util.DomainUtil.checkIfTaskScheduled
import com.example.inhabitnow.domain.util.DomainUtil.getTaskStatusByEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until

class DefaultCalculateStatisticsUseCase(
    private val taskRepository: TaskRepository,
    private val recordRepository: RecordRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : CalculateStatisticsUseCase {

    override suspend operator fun invoke(taskId: String): TaskStatisticsModel? =
        withContext(defaultDispatcher) {
            val taskWithAllContentDef = async {
                taskRepository.readTaskWithAllTimeContentById(taskId).firstOrNull()
            }
            val allRecordsDef = async {
                recordRepository.readRecordsByTaskId(taskId).firstOrNull()
            }
            taskWithAllContentDef.await()?.let { taskWithAllContentEntity ->
                allRecordsDef.await()?.let { allRecordEntities ->
                    val startDate = taskWithAllContentEntity.taskEntity.startDate
                    val endDate = taskWithAllContentEntity.taskEntity.endDate?.let { endDate ->
                        minOf(endDate, todayDate)
                    } ?: todayDate
                    // [LocalDate, TaskStatus]
                    val statusMap = getStatusMap(
                        taskWithAllContentEntity = taskWithAllContentEntity,
                        allRecordEntities = allRecordEntities,
                        startDate = startDate,
                        endDate = endDate
                    )
                    val habitScoreDef = async {
                        calculateHabitScore(statusMap)
                    }

                    val streakDef = async {
                        calculateStreak(statusMap)
                    }

                    val completionRateDef = async {
                        calculateCompletionRate(statusMap)
                    }

                    val statusCountDef = async {
                        calculateStatusCount(statusMap)
                    }

                    TaskStatisticsModel(
                        habitScore = habitScoreDef.await(),
                        currentStreak = streakDef.await().current,
                        bestStreak = streakDef.await().best,
                        currentWeekCompletionCount = completionRateDef.await().perWeekCount,
                        currentMonthCompletionCount = completionRateDef.await().perMonthCount,
                        currentYearCompletionCount = completionRateDef.await().perYearCount,
                        allTimeCompletionCount = completionRateDef.await().allTimeCount,
                        completedCount = statusCountDef.await().completedCount,
                        pendingCount = statusCountDef.await().pendingCount,
                        skippedCount = statusCountDef.await().skippedCount,
                        failedCount = statusCountDef.await().failedCount,
                        statusMap = statusMap
                    )

                } ?: return@withContext null
            } ?: return@withContext null
        }

    private fun calculateStatusCount(statusMap: Map<LocalDate, TaskStatus>): StatusCountModel {
        statusMap.values.let { allStatuses ->
            return StatusCountModel(
                completedCount = allStatuses.count { it is TaskStatus.Completed },
                pendingCount = allStatuses.count { it is TaskStatus.NotCompleted.Pending },
                skippedCount = allStatuses.count { it is TaskStatus.NotCompleted.Skipped },
                failedCount = allStatuses.count { it is TaskStatus.NotCompleted.Failed }
            )
        }
    }

    private fun calculateCompletionRate(statusMap: Map<LocalDate, TaskStatus>): CompletionRateModel {
        var perWeekCount = 0
        var perMonthCount = 0
        var perYearCount = 0
        var allTimeCount = 0
        todayDate.let { today ->
            val weekRange = today.minus(today.dayOfWeek.ordinal, DateTimeUnit.DAY).rangeTo(today)
            val monthRange = today.minus(today.dayOfMonth, DateTimeUnit.DAY).rangeTo(today)
            val yearRange = today.minus(today.dayOfYear, DateTimeUnit.DAY).rangeTo(today)
            statusMap.forEach { mapEntry ->
                val date = mapEntry.key
                val status = mapEntry.value
                when (status) {
                    is TaskStatus.Completed -> {
                        allTimeCount++
                        if (date in yearRange) {
                            perYearCount++
                        }
                        if (date in monthRange) {
                            perMonthCount++
                        }
                        if (date in weekRange) {
                            perWeekCount++
                        }
                    }

                    else -> Unit
                }
            }
        }
        return CompletionRateModel(
            perWeekCount = perWeekCount,
            perMonthCount = perMonthCount,
            perYearCount = perYearCount,
            allTimeCount = allTimeCount
        )
    }

    private fun calculateStreak(statusMap: Map<LocalDate, TaskStatus>): StreakModel {
        var currentStreakCount = 0
        var bestStreakCount = 0
        statusMap.keys.sorted().let { allDays ->
            allDays.forEach { day ->
                statusMap.getValue(day).let { status ->
                    when (status) {
                        is TaskStatus.Completed -> {
                            currentStreakCount++
                            if (currentStreakCount > bestStreakCount) {
                                bestStreakCount = currentStreakCount
                            }
                        }

                        is TaskStatus.NotCompleted -> {
                            when (status) {
                                is TaskStatus.NotCompleted.Skipped -> Unit
                                is TaskStatus.NotCompleted.Pending, is TaskStatus.NotCompleted.Failed -> {
                                    currentStreakCount = 0
                                }
                            }
                        }
                    }
                }
            }
        }
        return StreakModel(current = currentStreakCount, best = bestStreakCount)
    }

    private fun calculateHabitScore(statusMap: Map<LocalDate, TaskStatus>): Float {
        statusMap.filterValues { it !is TaskStatus.NotCompleted.Skipped }.let { map ->
            if (map.isEmpty()) return 0f
            val allCount = map.size.toFloat()
            val completedCount = map.values.count { it is TaskStatus.Completed }.toFloat()
            return completedCount / allCount
        }
    }

    private suspend fun getStatusMap(
        taskWithAllContentEntity: TaskWithAllContentEntity,
        allRecordEntities: List<RecordEntity>,
        startDate: LocalDate,
        endDate: LocalDate
    ): Map<LocalDate, TaskStatus> = coroutineScope {
        startDate.until(endDate, DateTimeUnit.DAY).let { daysUntilEnd ->
            (0..daysUntilEnd).map { dayOffset ->
                async {
                    startDate.plus(dayOffset, DateTimeUnit.DAY).let { currentDate ->
                        val pcDef = async {
                            taskWithAllContentEntity.allProgressContent.findByDate(
                                currentDate
                            ).content
                        }
                        val fcDef = async {
                            taskWithAllContentEntity.allFrequencyContent.findByDate(
                                currentDate
                            ).content
                        }
                        val acDef = async {
                            taskWithAllContentEntity.allArchiveContent.findByDate(
                                currentDate
                            ).content
                        }
                        val recordEntryDef = async {
                            allRecordEntities.find { it.date == currentDate }?.entry
                        }
                        getTaskStatusByDate(
                            targetDate = currentDate,
                            progressContent = pcDef.await(),
                            frequencyContent = fcDef.await(),
                            archiveContent = acDef.await(),
                            recordEntry = recordEntryDef.await()
                        )
                    }
                }
            }.awaitAll().filterNotNull().toMap()
        }
    }

    private fun getTaskStatusByDate(
        targetDate: LocalDate,
        progressContent: TaskContentEntity.ProgressContent,
        frequencyContent: TaskContentEntity.FrequencyContent,
        archiveContent: TaskContentEntity.ArchiveContent,
        recordEntry: RecordContentEntity.Entry?
    ): Pair<LocalDate, TaskStatus>? {
        return if (frequencyContent.checkIfTaskScheduled(targetDate)) {
            if (!archiveContent.isArchived) {
                progressContent.getTaskStatusByEntry(recordEntry).let { status ->
                    Pair(targetDate, status)
                }
            } else null
        } else null
    }

    private fun <T : BaseTaskContentEntity> List<T>.findByDate(date: LocalDate) =
        this.filter { it.startDate <= date }.maxBy { it.startDate }

    private val todayDate: LocalDate
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    private data class StreakModel(
        val current: Int,
        val best: Int
    )

    private data class CompletionRateModel(
        val perWeekCount: Int,
        val perMonthCount: Int,
        val perYearCount: Int,
        val allTimeCount: Int,
    )

    private data class StatusCountModel(
        val completedCount: Int,
        val pendingCount: Int,
        val skippedCount: Int,
        val failedCount: Int
    )
}