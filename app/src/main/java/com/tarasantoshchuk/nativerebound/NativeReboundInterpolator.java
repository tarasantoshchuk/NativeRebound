package com.tarasantoshchuk.nativerebound;

import android.animation.TimeInterpolator;

public class NativeReboundInterpolator implements TimeInterpolator {
    private static final int AMPLITUDE_DECREASE = 1000;

    private double mNaturalFrequency;
    private double mDampedFrequency;

    private double mNaturalDuration;
    private double mDampingRatio;

    private double mDampFactor;

    private double mA;
    private double mB;

    public NativeReboundInterpolator(float tension, float friction) {
        mNaturalFrequency = Math.sqrt(tension);
        mDampingRatio = friction / 2f / mNaturalFrequency;
        mDampedFrequency = mNaturalFrequency * (Math.sqrt(1 - mDampingRatio * mDampingRatio));
        mDampFactor = mNaturalFrequency * mDampingRatio;

        double decreaseByHalfPeriod = Math.exp(friction / 4f);
        double numOfHalfPeriodsToWait = Math.log(AMPLITUDE_DECREASE) / Math.log(decreaseByHalfPeriod);
        double halfPeriodDuration = Math.PI / mDampedFrequency;
        mNaturalDuration = numOfHalfPeriodsToWait * halfPeriodDuration + halfPeriodDuration / 2f;

        mA = -1;
        mB = (- mDampFactor) / mDampedFrequency;
    }

    public long getNaturalDuration() {
        return (long) Math.ceil(mNaturalDuration * 1000f);
    }

    @Override
    public float getInterpolation(float v) {
        if (mDampingRatio < 1f) {
            double t = v * mNaturalDuration;

            return 1f + (float) ((mA * Math.cos(mDampedFrequency * t) + mB * Math.sin(mDampedFrequency * t)) * Math.exp(- mDampFactor * t));
        } else {
            /** no need to support critical damp and overdamped cases */
            return v;
        }
    }
}
