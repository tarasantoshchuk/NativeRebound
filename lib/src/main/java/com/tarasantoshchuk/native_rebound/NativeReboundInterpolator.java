package com.tarasantoshchuk.native_rebound;

import android.animation.TimeInterpolator;

public class NativeReboundInterpolator implements TimeInterpolator {
    private static final int AMPLITUDE_DECREASE = 100;

    private double mNaturalFrequency;
    private double mDampedFrequency;

    private double mNaturalDuration;
    private double mDampingRatio;

    private double mDampFactor;

    private double mA;
    private double mB;

    private double mAmplitudeError;

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

        mAmplitudeError = getRawInterpolation(mNaturalDuration) - 1f;
    }

    public long getNaturalDuration() {
        return (long) Math.ceil(mNaturalDuration * 1000f);
    }

    @Override
    public float getInterpolation(float v) {
        if (mDampingRatio < 1f) {
            double t = v * mNaturalDuration;

            return (float) (getRawInterpolation(t) - mAmplitudeError * v);
        } else {
            /** no need to support critical damp and overdamped cases */
            return v;
        }
    }

    private double getRawInterpolation(double t) {
        return 1.0 + ((mA * Math.cos(mDampedFrequency * t) + mB * Math.sin(mDampedFrequency * t)) * Math.exp(- mDampFactor * t));
    }
}
