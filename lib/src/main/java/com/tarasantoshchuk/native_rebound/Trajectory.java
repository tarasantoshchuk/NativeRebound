package com.tarasantoshchuk.native_rebound;

interface Trajectory {
    double getPositionAtTime(double time);
}

interface TrajectoryProvider {
    Trajectory getTrajectory(double naturalFrequency, double dampFactor, double dampedFrequency);
    double getRelaxationCoefficient(double dampFactor, double dampedFrequency);
}




