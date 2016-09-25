package com.tarasantoshchuk.native_rebound;

class Oscillation implements Trajectory {
    private final Trajectory mTrajectory;
    private final double mNaturalDuration;

    public Oscillation(Trajectory trajectory, double duration) {
        mTrajectory = trajectory;
        mNaturalDuration = duration;
    }

    double getNaturalDuration() {
        return mNaturalDuration;
    }

    double getPositionAtPercent(double percent) {
        return mTrajectory.getPositionAtTime(mNaturalDuration * percent);
    }

    @Override
    public double getPositionAtTime(double time) {
        return mTrajectory.getPositionAtTime(time);
    }
}
