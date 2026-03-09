package ca.sfu.cmpt276.group15.entity;

import ca.sfu.cmpt276.group15.math.Direction;
import com.almasb.fxgl.entity.component.Component;

import static ca.sfu.cmpt276.group15.math.Position.UNIT_SIZE;

public abstract class FixedFrequencyComponent extends Component {
    public abstract void onUpdate();

    protected void move(Direction direction) {
        this.entity.translate(direction.asVector().multiply(UNIT_SIZE));
    }

    @Override
    public boolean isComponentInjectionRequired() {
        return false;
    }
}
