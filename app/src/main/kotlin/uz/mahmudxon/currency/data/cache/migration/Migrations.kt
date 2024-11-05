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

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Step 1: Create the new table with the unique constraint
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS chart_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                code TEXT NOT NULL, 
                date TEXT NOT NULL, 
                rate REAL NOT NULL
            )
            """.trimIndent()
        )

        // Step 2: Copy the data from the old table to the new table
        database.execSQL(
            """
            INSERT INTO chart_new (id, code, date, rate)
            SELECT id, code, date, rate FROM chart
            """.trimIndent()
        )

        // Step 3: Drop the old table
        database.execSQL("DROP TABLE IF EXISTS chart")

        // Step 4: Rename the new table to the old table's name
        database.execSQL("ALTER TABLE chart_new RENAME TO chart")

        // Step 5: Add the unique index on (code, date)
        database.execSQL("CREATE UNIQUE INDEX index_chart_code_date ON chart (code, date)")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {  // Replace X and Y with the old and new version numbers
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add the unique index on (bankId, currencyCode)
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_commercial_price_bankId_currencyCode ON commercial_price (bankId, currencyCode)")
    }
}

