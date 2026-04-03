package ca.sfu.cmpt276.virusbreach.ui;

import ca.sfu.cmpt276.virusbreach.board.entity.*;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for render nodes.
 */
public class RenderNodeRegistry {
    /**
     * Internal registry mappings.
     * Class type should match the factory type.
     */
    private static final Map<Class<?>, RenderNodeFactory<?>> REGISTRY = new HashMap<>();

    /**
     * Registers a render node factory for the given class.
     *
     * @param clazz the class to register
     * @param node the factory to register
     * @param <T> the type of the class
     */
    public static <T> void register(Class<T> clazz, RenderNodeFactory<T> node) {
        REGISTRY.put(clazz, node);
    }

    /**
     * Creates a render node for the given object
     * @param object the object to be rendered
     * @param <T> the type of object being rendered
     * @return a new render node
     */
    @SuppressWarnings("unchecked") // #register checks the types
    public static <T> RenderNode<T> createRenderNodeFor(T object) {
        return ((RenderNodeFactory<T>) REGISTRY.get(object.getClass())).createRenderNode(object);
    }

    static {
        register(Antivirus.class, e -> new AnimatedNode<>(e, "antivirus.png", 4, "antivirus_moving1.png", "antivirus_moving2.png"));
        register(Data.class, e -> new SimpleRenderNode<>(e, ResourceManager.sprite("data.png", Color.GRAY)));
        register(DecryptionKey.class, e -> new SimpleRenderNode<>(e, ResourceManager.sprite("decryption_key.png", Color.GOLD)));
        register(Firewall.class, e -> new SimpleRenderNode<>(e, ResourceManager.sprite("firewall.png", Color.ORANGE)));
        register(FreezeToken.class, e -> new SimpleRenderNode<>(e, ResourceManager.sprite("freeze_token.png", Color.CORNFLOWERBLUE)));
        register(Player.class, e -> new AnimatedNode<>(e, "player.png", 2, "player_moving1.png", "player_moving2.png"));
        register(SourceCode.class, e -> new SimpleRenderNode<>(e, ResourceManager.sprite("sourcecode.png", Color.GREEN)));
    }

    RenderNodeRegistry() {
        throw new UnsupportedOperationException("This class should not be initialized");
    }

    /**
     * Creates a render node for a specific type of object
     * @param <T> the type
     */
    @FunctionalInterface
    public interface RenderNodeFactory<T> {
        /**
         * Creates a render node for the given object
         * @param object the object to be rendered
         * @return a new render node
         */
        RenderNode<T> createRenderNode(T object);
    }
}
