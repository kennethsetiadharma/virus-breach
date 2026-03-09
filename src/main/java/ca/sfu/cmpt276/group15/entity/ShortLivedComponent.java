package ca.sfu.cmpt276.group15.entity;

import com.almasb.fxgl.entity.component.Component;

public class ShortLivedComponent extends Component {
    private double ttl;

    public ShortLivedComponent(int ttl) {
        this.ttl = ttl;
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        this.ttl -= tpf;

        if (this.ttl < 0.0) {
            this.entity.removeFromWorld();
        }
    }
}
