package com.tarasantoshchuk.native_rebound;

enum OscillationType implements TrajectoryProvider {
    UNDERDAMPING {
        @Override
        public Trajectory getTrajectory(double naturalFrequency, final double dampFactor, final double dampedFrequency) {
            final double a = -1.0;
            final double b = -dampFactor / dampedFrequency;

            return new Trajectory() {
                @Override
                public double getPositionAtTime(double time) {
                    return 1.0 + ((a * Math.cos(dampedFrequency * time) + b * Math.sin(dampedFrequency * time)) * Math.exp(-dampFactor * time));
                }
            };
        }

        @Override
        public double getRelaxationCoefficient(double dampFactor, double dampedFrequency) {
            return dampFactor;
        }
    },
    CRITICAL_DAMP {
        @Override
        public Trajectory getTrajectory(final double naturalFrequency, final double dampFactor, final double dampedFrequency) {
            final double a = -1.0;
            final double b = -naturalFrequency;

            return new Trajectory() {
                @Override
                public double getPositionAtTime(double time) {
                    return 1.0 + (a + b * time) * Math.exp(-naturalFrequency * time);
                }
            };
        }

        @Override
        public double getRelaxationCoefficient(double dampFactor, double dampedFrequency) {
            return dampFactor;
        }
    },
    OVERDAMPING {
        @Override
        public Trajectory getTrajectory(final double naturalFrequency, final double dampFactor, final double dampedFrequency) {
            final double root1 = - dampFactor + dampedFrequency;
            final double root2 = - dampFactor - dampedFrequency;

            final double a = -1.0 - 1.0 * (root1 / (root2 - root1));
            final double b = 1.0 * (root1 / (root2 - root1));

            return new Trajectory() {
                @Override
                public double getPositionAtTime(double time) {
                    return 1.0 + a * Math.exp(root1 * time) + b * Math.exp(root2 * time);
                }
            };
        }

        @Override
        public double getRelaxationCoefficient(double dampFactor, double dampedFrequency) {
            return dampFactor - dampedFrequency;
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
