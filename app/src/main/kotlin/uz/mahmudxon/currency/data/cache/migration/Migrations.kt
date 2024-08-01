package uz.mahmudxon.currency.data.cache.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `commercial_price` ADD COLUMN bankAddress TEXT NOT NULL DEFAULT '-'")
        db.execSQL("ALTER TABLE `commercial_price` ADD COLUMN bankPhone TEXT NOT NULL DEFAULT '-'")
        db.execSQL("ALTER TABLE `commercial_price` ADD COLUMN bankLicens TEXT NOT NULL DEFAULT '-'")
        db.execSQL("ALTER TABLE `commercial_price` ADD COLUMN bankInn TEXT NOT NULL DEFAULT '-'")
    }
}