package com.example.mygoals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import com.example.mygoals.Goal;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Informações do banco de dados
    private static final String DATABASE_NAME = "goals_database";
    private static final int DATABASE_VERSION = 1;

    // Nome da tabela e colunas
    private static final String TABLE_GOALS = "goals";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DATE_CREATED = "date_created";
    private static final String COLUMN_IS_COMPLETED = "is_completed";

    // SQL para criar a tabela
    private static final String CREATE_TABLE_GOALS =
            "CREATE TABLE " + TABLE_GOALS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_DATE_CREATED + " TEXT NOT NULL, " +
                    COLUMN_IS_COMPLETED + " INTEGER DEFAULT 0" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GOALS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
        onCreate(db);
    }

    // Inserir nova meta
    public long insertGoal(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, goal.getTitle());
        values.put(COLUMN_DATE_CREATED, goal.getDateCreated());
        values.put(COLUMN_IS_COMPLETED, goal.isCompleted() ? 1 : 0);

        long id = db.insert(TABLE_GOALS, null, values);
        db.close();

        return id;
    }

    // Buscar todas as metas
    public List<Goal> getAllGoals() {
        List<Goal> goalList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_GOALS + " ORDER BY " + COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Goal goal = new Goal();
                goal.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                goal.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                goal.setDateCreated(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_CREATED)));
                goal.setCompleted(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_COMPLETED)) == 1);

                goalList.add(goal);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return goalList;
    }

    // Atualizar status da meta
    public int updateGoalStatus(int goalId, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_COMPLETED, isCompleted ? 1 : 0);

        int result = db.update(TABLE_GOALS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(goalId)});
        db.close();

        return result;
    }

    // Deletar metas concluídas
    public int deleteCompletedGoals() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_GOALS, COLUMN_IS_COMPLETED + " = ?", new String[]{"1"});
        db.close();

        return result;
    }

    // Contar total de metas
    public int getGoalsCount() {
        String countQuery = "SELECT * FROM " + TABLE_GOALS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }
}