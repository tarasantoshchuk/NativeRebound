package com.tarasantoshchuk.native_rebound;

interface Trajectory {
    double getPositionAtTime(double time);
}

interface TrajectoryProvider {
    Trajectory getTrajectory(double dampFactor, double dampedFrequency);
}




