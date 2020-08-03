package com.sict.android.lovecooking.ViewHolder;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sict.android.lovecooking.R;

public class StepImgViewHolder extends RecyclerView.ViewHolder{

    public EditText steps;
    public ImageButton btnRemove;
    public ImageView imgSteps;
    public MaterialButton btnStepView;

    public StepImgViewHolder(@NonNull View itemView) {
        super(itemView);
        steps = (EditText)itemView.findViewById(R.id.step);
        btnRemove = (ImageButton)itemView.findViewById(R.id.remove);
        imgSteps = (ImageView)itemView.findViewById(R.id.imgStep);
        btnStepView = (MaterialButton)itemView.findViewById(R.id.stepCount);
    }

}
