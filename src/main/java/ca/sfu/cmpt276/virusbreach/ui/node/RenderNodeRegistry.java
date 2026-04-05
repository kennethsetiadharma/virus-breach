package ca.sfu.cmpt276.virusbreach.ui.node;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.entity.*;
import ca.sfu.cmpt276.virusbreach.board.tile.TileType;
import ca.sfu.cmpt276.virusbreach.board.tile.TileTypes;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.scene.Node;
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
    private static final Map<TileType, TileRenderNodeFactory> TILE_REGISTRY = new HashMap<>();

    /**
     * Registers a render node factory for the given class.
     *
     * @param clazz the class to register
     * @param factory the factory to associate
     * @param <T> the type of the class
     */
    public static <T> void register(Class<T> clazz, RenderNodeFactory<T> factory) {
        REGISTRY.put(clazz, factory);
    }

    /**
     * Registers a tile render node factory for the given class.
     *
     * @param tileType the type of tile to register
     * @param factory the factory to associate
     */
    public static void register(TileType tileType, TileRenderNodeFactory factory) {
        TILE_REGISTRY.put(tileType, factory);
    }

    /**
     * Creates a render node for the given object
     *
     * @param object the object to be rendered
     * @param <T> the type of object being rendered
     * @return a new render node
     */
    @SuppressWarnings("unchecked") // #register checks the types
    public static <T> RenderNode<T> createRenderNodeFor(T object) {
        return ((RenderNodeFactory<T>) REGISTRY.get(object.getClass())).createRenderNode(object);
    }

    /**
     * Creates a node for rendering the tile at the given position
     *
     * @param board the board being rendered
     * @param position the location of the tile to render
     * @return a new render node
     */
    public static Node createRenderNodeForTile(Board board, Position position) {
        Node node = TILE_REGISTRY.get(board.getTile(position)).createNode(board, position);
        node.setTranslateX(Position.fromGrid(position.x()));
        node.setTranslateY(Position.fromGrid(position.y()));
        node.setTranslateZ(-1.0);
        return node;
    }

    static {
        register(Antivirus.class, AnimatedNode.factory("antivirus.png", 4, "antivirus_moving1.png", "antivirus_moving2.png"));
        register(Data.class, SimpleRenderNode.factory("data.png", Color.GRAY));
        register(DecryptionKey.class, SimpleRenderNode.factory("decryption_key.png", Color.GOLD));
        register(Firewall.class, SimpleRenderNode.factory("firewall.png", Color.ORANGE));
        register(FreezeToken.class, SimpleRenderNode.factory("freeze_token.png", Color.CORNFLOWERBLUE));
        register(Player.class, AnimatedNode.factory("player.png", 2, "player_moving1.png", "player_moving2.png"));
        register(SourceCode.class, SimpleRenderNode.factory("sourcecode.png", Color.GREEN));

        register(TileTypes.ENTRANCE, RotatedEdgeNode.factory("entrance.png", Color.LIGHTGREEN));
        register(TileTypes.EXIT, MultiSpriteEdgeNode.factory("exit.png", Color.LIGHTGREEN));
        register(TileTypes.FLOOR, TileRenderNodeFactory.simple("floor.png", Color.GRAY));
        register(TileTypes.LOCKED_DOOR, TileRenderNodeFactory.simple("door_locked.png", Color.CRIMSON));
        register(TileTypes.OPEN_DOOR, TileRenderNodeFactory.simple("door_open.png", Color.LIMEGREEN));
        register(TileTypes.WALL, TileRenderNodeFactory.simple("wall.png", Color.DARKSLATEGRAY));
    }

    RenderNodeRegistry() {
        throw new UnsupportedOperationException("This class should not be initialized");
    }

    /**
     * Creates a render node for a specific type of object
     *
     * @param <T> the type
     */
    @FunctionalInterface
    public interface RenderNodeFactory<T> {
        /**
         * Creates a render node for the given object
         *
         * @param object the object to be rendered
         * @return a new render node
         */
        RenderNode<T> createRenderNode(T object);
    }

    /**
     * Creates a render node for a specific tile
     */
    @FunctionalInterface
    public interface TileRenderNodeFactory {
        /**
         * Creates a render node for the given object
         *
         * @param board the board being rendered
         * @param position the location of the tile to be rendered
         * @return a new render node
         */
        Node createNode(Board board, Position position);

        /**
         * Creates a basic render node factory that always generates the same sprite.
         *
         * @param asset the location of the sprite to use
         * @param color the fallback colour to use if the sprite fails to load
         * @return a new render node factory
         */
        static TileRenderNodeFactory simple(String asset, Color color) {
            return (b, p) -> ResourceManager.sprite(asset, color);
        }
    }
}
