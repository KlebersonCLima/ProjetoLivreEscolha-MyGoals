package com.example.mygoals;
import com.example.mygoals.Goal;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalViewHolder> {

    private List<Goal> goalsList;
    private OnGoalClickListener listener;

    public interface OnGoalClickListener {
        void onGoalStatusChanged(Goal goal, boolean isCompleted);
    }

    public GoalsAdapter(List<Goal> goalsList, OnGoalClickListener listener) {
        this.goalsList = goalsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = goalsList.get(position);
        holder.bind(goal);
    }

    @Override
    public int getItemCount() {
        return goalsList.size();
    }

    class GoalViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cbGoalCompleted;
        private TextView tvGoalTitle;
        private TextView tvGoalDate;
        private TextView tvGoalStatus;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            cbGoalCompleted = itemView.findViewById(R.id.cb_goal_completed);
            tvGoalTitle = itemView.findViewById(R.id.tv_goal_title);
            tvGoalDate = itemView.findViewById(R.id.tv_goal_date);
            tvGoalStatus = itemView.findViewById(R.id.tv_goal_status);
        }

        public void bind(Goal goal) {
            tvGoalTitle.setText(goal.getTitle());
            tvGoalDate.setText("Criada em: " + goal.getDateCreated());
            cbGoalCompleted.setChecked(goal.isCompleted());

            // Configurar status visual
            if (goal.isCompleted()) {
                tvGoalStatus.setText("CONCLUÍDA");
                tvGoalStatus.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.success_color));

                // Riscar o texto quando concluído
                tvGoalTitle.setPaintFlags(tvGoalTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvGoalTitle.setTextColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.text_secondary));
            } else {
                tvGoalStatus.setText("ATIVA");
                tvGoalStatus.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.primary_color));

                // Remover o risco do texto
                tvGoalTitle.setPaintFlags(tvGoalTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                tvGoalTitle.setTextColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.text_color));
            }

            // Listener para mudança de status
            cbGoalCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) {
                    listener.onGoalStatusChanged(goal, isChecked);
                }
            });
        }
    }
}