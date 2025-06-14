package com.example.mygoals;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.example.mygoals.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private EditText etGoalTitle;
    private Button btnAddGoal;
    private Button btnClearCompleted;
    private RecyclerView rvGoals;
    private LinearLayout layoutEmptyState;
    private TextView tvTotalGoals;

    private DatabaseHelper databaseHelper;
    private GoalsAdapter goalsAdapter;
    private List<Goal> goalsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initDatabase();
        setupRecyclerView();
        loadGoals();
        setupClickListeners();
    }

    private void initViews() {
        etGoalTitle = findViewById(R.id.et_goal_title);
        btnAddGoal = findViewById(R.id.btn_add_goal);
        btnClearCompleted = findViewById(R.id.btn_clear_completed);
        rvGoals = findViewById(R.id.rv_goals);
        layoutEmptyState = findViewById(R.id.layout_empty_state);
        tvTotalGoals = findViewById(R.id.tv_total_goals);
    }

    private void initDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }

    private void setupRecyclerView() {
        goalsList = new ArrayList<>();
        goalsAdapter = new GoalsAdapter(goalsList, new GoalsAdapter.OnGoalClickListener() {
            @Override
            public void onGoalStatusChanged(Goal goal, boolean isCompleted) {
                updateGoalStatus(goal, isCompleted);
            }
        });

        rvGoals.setLayoutManager(new LinearLayoutManager(this));
        rvGoals.setAdapter(goalsAdapter);
    }

    private void setupClickListeners() {
        btnAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewGoal();
            }
        });

        btnClearCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCompletedGoals();
            }
        });
    }

    private void addNewGoal() {
        String goalTitle = etGoalTitle.getText().toString().trim();

        if (goalTitle.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_goal_text), Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar nova meta
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Goal newGoal = new Goal(goalTitle, currentDate, false);

        // Inserir no banco de dados
        long result = databaseHelper.insertGoal(newGoal);

        if (result != -1) {
            etGoalTitle.setText("");
            loadGoals();
            Toast.makeText(this, getString(R.string.goal_added_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao adicionar meta!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateGoalStatus(Goal goal, boolean isCompleted) {
        int result = databaseHelper.updateGoalStatus(goal.getId(), isCompleted);

        if (result > 0) {
            goal.setCompleted(isCompleted);
            goalsAdapter.notifyDataSetChanged();
            updateTotalGoalsCount();

            String message = isCompleted ? "Meta concluída! 🎉" : "Meta reativada!";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void clearCompletedGoals() {
        int deletedCount = databaseHelper.deleteCompletedGoals();

        if (deletedCount > 0) {
            loadGoals();
            Toast.makeText(this, deletedCount + " meta(s) concluída(s) removida(s)!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Nenhuma meta concluída para remover!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void loadGoals() {
        goalsList.clear();
        goalsList.addAll(databaseHelper.getAllGoals());
        goalsAdapter.notifyDataSetChanged();

        updateUI();
        updateTotalGoalsCount();
    }

    private void updateUI() {
        if (goalsList.isEmpty()) {
            rvGoals.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvGoals.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);
        }
    }

    private void updateTotalGoalsCount() {
        int totalGoals = goalsList.size();
        tvTotalGoals.setText(getString(R.string.total_goals, totalGoals));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGoals();
    }
}