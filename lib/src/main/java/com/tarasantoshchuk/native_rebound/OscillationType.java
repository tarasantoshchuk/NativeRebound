package com.tarasantoshchuk.native_rebound;

enum OscillationType implements TrajectoryProvider {
    UNDERDAMPING {
        @Override
        public Trajectory getTrajectory(final double dampFactor, final double dampedFrequency) {
            final double a = -1.0;
            final double b = - dampFactor / dampedFrequency;

            return new Trajectory() {
                @Override
                public double getPositionAtTime(double time) {
                    return 1.0 + ((a * Math.cos(dampedFrequency * time) + b * Math.sin(dampedFrequency * time)) * Math.exp(- dampFactor * time));
                }
            };
        }
    },
    CRITICAL_DAMP {
        @Override
        public Trajectory getTrajectory(double dampFactor, double dampedFrequency) {
            return new Trajectory() {
                @Override
                public double getPositionAtTime(double time) {
                    return time;
                }
            };
        }
    },
    OVERDAMPING {
        @Override
        public Trajectory getTrajectory(double dampFactor, double dampedFrequency) {
            return new Trajectory() {
                @Override
                public double getPositionAtTime(double time) {
                    return time * time * time;
                }
            };
        }
    };

    public static OscillationType byDampingRatio(double dampingRatio) {
        if (dampingRatio > 1f) {
            return OVERDAMPING;
        } else if (dampingRatio < 1f) {
            return UNDERDAMPING;
        } else {
            return CRITICAL_DAMP;
        }
    }
}
