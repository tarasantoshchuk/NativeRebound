package com.tarasantoshchuk.native_rebound;

import android.util.Log;

class PhysicsUtils {
    static Oscillation getOscillation(double friction, double tension, double amplitudeDecrease) {
        double naturalFrequency = Math.sqrt(tension);
        double dampFactor = friction / 2f;
        double dampingRatio = dampFactor / naturalFrequency;
        double dampedFrequency = naturalFrequency * (Math.sqrt(1 - dampingRatio * dampingRatio));

        final double naturalDuration = getNaturalDuration(dampFactor, dampedFrequency, amplitudeDecrease);

        final Trajectory preciseTrajectory = OscillationType
                .byDampingRatio(dampingRatio)
                .getTrajectory(dampFactor, dampedFrequency);

        final double trajectoryError = preciseTrajectory.getPositionAtTime(naturalDuration) - 1.0f;

        Log.v("BUGFIX", "" + trajectoryError);

        return new Oscillation(
                new Trajectory() {
                    @Override
                    public double getPositionAtTime(double time) {
                        //modify trajectory to be 0 at time == naturalDuration
                        return preciseTrajectory.getPositionAtTime(time) - trajectoryError * time / naturalDuration;
                    }
                },
                naturalDuration
        );
    }

    private static double getNaturalDuration(double mDampFactor, double mDampedFrequency, double amplitudeDecrease) {
        double halfPeriodDuration = Math.PI / mDampedFrequency;

        // A(t)/A(t + T/2)
        double decreaseByHalfPeriod = Math.exp(halfPeriodDuration * mDampFactor / 2f);

        double numOfHalfPeriodsToWait = Math.log(amplitudeDecrease) / Math.log(decreaseByHalfPeriod);
        return numOfHalfPeriodsToWait * halfPeriodDuration + halfPeriodDuration / 2f;
    }
}
