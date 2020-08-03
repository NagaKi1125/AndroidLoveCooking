package com.sict.android.lovecooking.Model;

public class StepImage {

    public String step;
    public String img;

    public StepImage(String step, String img) {
        this.step = step;
        this.img = img;
    }

    public StepImage() {
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
