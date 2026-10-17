package com.example.badnewgym.feature.bodyprogress.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.badnewgym.feature.bodyprogress.data.repository.BodyProgressFixtureSource
import com.example.badnewgym.feature.bodyprogress.domain.BodyProgressAggregator
import com.example.badnewgym.feature.bodyprogress.domain.BodyProgressSummary
import com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyCompositionAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyRange
import com.example.badnewgym.feature.bodyprogress.domain.insights.hasEvidence
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyProgressDataset
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup
import com.example.badnewgym.feature.bodyprogress.domain.model.ViewerRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface BodyProgressUiState {
    data object Loading : BodyProgressUiState
    data class Ready(
        val dataset: BodyProgressDataset,
        val summary: BodyProgressSummary,
        val range: BodyRange,
        val viewerRole: ViewerRole
    ) : BodyProgressUiState

    /** Honest state for a member with no usable records at all. */
    data object InsufficientData : BodyProgressUiState
    data class Error(val message: String) : BodyProgressUiState
}

/**
 * Body Progress ViewModel.
 *
 * Aggregation happens here (off the composition path) and is memoised per
 * (range, metric, muscle) so switching a filter reuses the cached result
 * instead of rescanning workout history.
 */
class BodyProgressViewModel(
    private val source: BodyProgressFixtureSource = BodyProgressFixtureSource(),
    private val memberId: String = "BG305",
    private val scenario: Scenario = Scenario.FULL
) : ViewModel() {

    enum class Scenario { FULL, WORKOUT_ONLY, SPARSE }

    private val _state = MutableStateFlow<BodyProgressUiState>(BodyProgressUiState.Loading)
    val state: StateFlow<BodyProgressUiState> = _state.asStateFlow()

    private var dataset: BodyProgressDataset? = null
    private val cache = mutableMapOf<String, BodyProgressSummary>()

    fun load() {
        viewModelScope.launch {
            _state.value = BodyProgressUiState.Loading
            try {
                val loaded = when (scenario) {
                    Scenario.FULL -> source.dataset()
                    Scenario.WORKOUT_ONLY -> source.workoutOnlyDataset()
                    Scenario.SPARSE -> source.sparseDataset()
                }
                dataset = loaded
                cache.clear()
                if (!loaded.hasEvidence()) {
                    _state.value = BodyProgressUiState.InsufficientData
                    return@launch
                }
                // Six months by default: members weigh in roughly fortnightly, so a
                // 30 day window usually held a single reading and every tile fell
                // back to "One reading" with no trend at all.
                _state.value = BodyProgressUiState.Ready(
                    dataset = loaded,
                    summary = summarize(loaded, BodyRange.M6, null, null, ViewerRole.SELF),
                    range = BodyRange.M6,
                    viewerRole = ViewerRole.SELF
                )
            } catch (t: Throwable) {
                _state.value = BodyProgressUiState.Error(t.message ?: "Could not load body progress")
            }
        }
    }

    fun setRange(range: BodyRange) {
        val d = dataset ?: return load()
        _state.value = BodyProgressUiState.Ready(
            dataset = d,
            summary = summarize(d, range, null, null, currentRole()),
            range = range,
            viewerRole = currentRole()
        )
    }

    fun setMetric(metric: BodyMetric?) {
        val d = dataset ?: return load()
        val range = (_state.value as? BodyProgressUiState.Ready)?.range ?: BodyRange.M6
        _state.value = BodyProgressUiState.Ready(
            dataset = d,
            summary = summarize(d, range, metric, null, currentRole()),
            range = range,
            viewerRole = currentRole()
        )
    }

    fun setMuscle(muscle: MuscleGroup?) {
        val d = dataset ?: return load()
        val range = (_state.value as? BodyProgressUiState.Ready)?.range ?: BodyRange.M6
        _state.value = BodyProgressUiState.Ready(
            dataset = d,
            summary = summarize(d, range, null, muscle, currentRole()),
            range = range,
            viewerRole = currentRole()
        )
    }

    /** Privacy is enforced in the domain layer, not by hiding widgets. */
    fun setViewerRole(role: ViewerRole) {
        val d = dataset ?: return load()
        val range = (_state.value as? BodyProgressUiState.Ready)?.range ?: BodyRange.M6
        cache.clear()
        _state.value = BodyProgressUiState.Ready(
            dataset = d,
            summary = summarize(d, range, null, null, role),
            range = range,
            viewerRole = role
        )
    }

    fun toggleBodyCompositionConsent() {
        val d = dataset ?: return
        dataset = d.copy(consent = d.consent.copy(allowsBodyComposition = !d.consent.allowsBodyComposition))
        cache.clear()
        val range = (_state.value as? BodyProgressUiState.Ready)?.range ?: BodyRange.M6
        _state.value = BodyProgressUiState.Ready(
            dataset = dataset!!,
            summary = summarize(dataset!!, range, null, null, currentRole()),
            range = range,
            viewerRole = currentRole()
        )
    }

    private fun currentRole(): ViewerRole =
        (_state.value as? BodyProgressUiState.Ready)?.viewerRole ?: ViewerRole.SELF

    /**
     * Summarise for the analytics screen's filter row. Reuses the cache and
     * falls back to a neutral summary when the dataset has not loaded yet.
     */
    fun summaryFor(
        range: BodyRange,
        metric: BodyMetric?,
        muscle: MuscleGroup?
    ): BodyProgressSummary {
        val d = dataset ?: return BodyProgressAggregator.summarize(
            BodyProgressFixtureSource().sparseDataset(), range
        )
        val ready = _state.value as? BodyProgressUiState.Ready
        val summary = summarize(d, range, metric, muscle, ready?.viewerRole ?: ViewerRole.SELF)
        // Selecting a specific metric re-points the primary chart at it.
        return if (metric != null && summary.primaryMetric != metric) {
            summary.copy(
                primaryMetric = metric,
                primarySeries = summary.compositionSeries(metric) ?: summary.primarySeries
            )
        } else {
            summary
        }
    }

    private fun summarize(
        d: BodyProgressDataset,
        range: BodyRange,
        metric: BodyMetric?,
        muscle: MuscleGroup?,
        role: ViewerRole
    ): BodyProgressSummary {
        val key = "$range|$metric|$muscle|$role"
        cache[key]?.let { return it }

        // Consent is applied before aggregation so a withdrawn permission can
        // never leak a number into the summary.
        val gated = if (permitted(d, role)) d else d.copy(measurements = d.measurements.filter {
            d.consent.permits(role, it.metric)
        })
        val summary = BodyProgressAggregator.summarize(gated, range)
        cache[key] = summary
        return summary
    }

    private fun permitted(d: BodyProgressDataset, role: ViewerRole): Boolean =
        role == ViewerRole.SELF || role in d.consent.visibleTo

    companion object {
        fun factory(scenario: Scenario = Scenario.FULL, memberId: String = "BG305") = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                BodyProgressViewModel(memberId = memberId, scenario = scenario) as T
        }
    }
}

/** Metrics available under the current consent + role. */
fun BodyProgressDataset.visibleMetrics(role: ViewerRole): List<BodyMetric> =
    measurements.map { it.metric }.distinct().filter { consent.permits(role, it) }

/** Re-export so the screen does not need the analytics import. */
fun visibleMetricSeries(dataset: BodyProgressDataset, metric: BodyMetric, range: BodyRange) =
    BodyCompositionAnalytics.series(dataset, metric, range)
