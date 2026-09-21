package com.example.badnewgym.feature.memberintelligence.data.local

import androidx.room.TypeConverter
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromMemberIdentity(value: MemberIdentity): String = gson.toJson(value)
    
    @TypeConverter
    fun toMemberIdentity(value: String): MemberIdentity = gson.fromJson(value, MemberIdentity::class.java)

    @TypeConverter
    fun fromMembershipStatus(value: MembershipStatus?): String? = value?.let { gson.toJson(it) }
    
    @TypeConverter
    fun toMembershipStatus(value: String?): MembershipStatus? = value?.let { gson.fromJson(it, MembershipStatus::class.java) }

    @TypeConverter
    fun fromAttendanceSummary(value: AttendanceSummary?): String? = value?.let { gson.toJson(it) }
    
    @TypeConverter
    fun toAttendanceSummary(value: String?): AttendanceSummary? = value?.let { gson.fromJson(it, AttendanceSummary::class.java) }

    @TypeConverter
    fun fromPaymentSummary(value: PaymentSummary?): String? = value?.let { gson.toJson(it) }
    
    @TypeConverter
    fun toPaymentSummary(value: String?): PaymentSummary? = value?.let { gson.fromJson(it, PaymentSummary::class.java) }

    @TypeConverter
    fun fromTrainerSummary(value: TrainerSummary?): String? = value?.let { gson.toJson(it) }
    
    @TypeConverter
    fun toTrainerSummary(value: String?): TrainerSummary? = value?.let { gson.fromJson(it, TrainerSummary::class.java) }

    @TypeConverter
    fun fromWorkoutSummary(value: WorkoutSummary?): String? = value?.let { gson.toJson(it) }
    
    @TypeConverter
    fun toWorkoutSummary(value: String?): WorkoutSummary? = value?.let { gson.fromJson(it, WorkoutSummary::class.java) }

    @TypeConverter
    fun fromSupplementSummary(value: SupplementSummary?): String? = value?.let { gson.toJson(it) }
    
    @TypeConverter
    fun toSupplementSummary(value: String?): SupplementSummary? = value?.let { gson.fromJson(it, SupplementSummary::class.java) }

    @TypeConverter
    fun fromNutritionSummary(value: NutritionSummary?): String? = value?.let { gson.toJson(it) }
    
    @TypeConverter
    fun toNutritionSummary(value: String?): NutritionSummary? = value?.let { gson.fromJson(it, NutritionSummary::class.java) }

    @TypeConverter
    fun fromServiceSummaryList(value: List<ServiceSummary>?): String? = value?.let { gson.toJson(it) }
    
    @TypeConverter
    fun toServiceSummaryList(value: String?): List<ServiceSummary>? {
        value ?: return null
        val type = object : TypeToken<List<ServiceSummary>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromMemberEventList(value: List<MemberEvent>): String = gson.toJson(value)
    
    @TypeConverter
    fun toMemberEventList(value: String): List<MemberEvent> {
        val type = object : TypeToken<List<MemberEvent>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromMemberIssueList(value: List<MemberIssue>): String = gson.toJson(value)
    
    @TypeConverter
    fun toMemberIssueList(value: String): List<MemberIssue> {
        val type = object : TypeToken<List<MemberIssue>>() {}.type
        return gson.fromJson(value, type)
    }
}
