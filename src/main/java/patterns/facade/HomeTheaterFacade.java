package patterns.facade;

import java.util.List;

/** One simple entry point over three subsystems with an order that must be right. */
public class HomeTheaterFacade {
    private final Lights lights = new Lights();
    private final Projector projector = new Projector();
    private final Amplifier amplifier = new Amplifier();

    public List<String> watchMovie() {
        return List.of(lights.dim(), projector.on(), amplifier.setVolume(8));
    }

    public List<String> endMovie() {
        return List.of(amplifier.off(), projector.off(), lights.on());
    }
}
