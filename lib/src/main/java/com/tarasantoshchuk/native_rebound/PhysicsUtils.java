package com.tarasantoshchuk.native_rebound;

class PhysicsUtils {
    static Oscillation getOscillation(double friction, double tension, final double amplitudeDecrease) {
        double naturalFrequency = Math.sqrt(tension);
        double dampFactor = friction / 2f;
        double dampingRatio = dampFactor / naturalFrequency;
        double dampedFrequency = naturalFrequency * (Math.sqrt(Math.abs(1 - dampingRatio * dampingRatio)));


        OscillationType oscillationType = OscillationType
                .byDampingRatio(dampingRatio);

        final double naturalDuration = getNaturalDuration(
                oscillationType.getRelaxationCoefficient(dampFactor, dampedFrequency),
                amplitudeDecrease
        );

        final Trajectory preciseTrajectory = oscillationType
                .getTrajectory(naturalFrequency, dampFactor, dampedFrequency);

        final double trajectoryError = preciseTrajectory.getPositionAtTime(naturalDuration) - 1.0f;

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

    private static double getNaturalDuration(double relaxationCoeficient, double amplitudeDecrease) {
        return Math.log(amplitudeDecrease) / relaxationCoeficient;
    }
}
