package ai;

import java.util.ArrayList;
import java.util.List;

public class PomRecorder {
    private final List<RecordedAction> actions = new ArrayList<>();

    public void record(String page, BrowserAction action) {
        actions.add(new RecordedAction(page, action));
    }

    public List<RecordedAction> actions() {
        return List.copyOf(actions);
    }
}
