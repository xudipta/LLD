package patterns.facade;

class Amplifier {
    String setVolume(int level) { return "Volume set to " + level; }
    String off() { return "Amplifier off"; }
}
