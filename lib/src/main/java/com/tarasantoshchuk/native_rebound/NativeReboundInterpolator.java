package com.tarasantoshchuk.native_rebound;

import android.animation.TimeInterpolator;

public class NativeReboundInterpolator implements TimeInterpolator {
    private static final int AMPLITUDE_DECREASE = 20;

    private Oscillation mOscillation;

    public NativeReboundInterpolator(float tension, float friction) {
        mOscillation = PhysicsUtils.getOscillation(friction, tension, AMPLITUDE_DECREASE);
    }

    public long getNaturalDuration() {
        return (long) mOscillation.getNaturalDuration() * 1000;
    }

    @Override
    public float getInterpolation(float v) {
        return (float) mOscillation.getPositionAtPercent(v);
    }
}
