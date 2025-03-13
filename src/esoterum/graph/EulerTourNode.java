package esoterum.graph;

/**
 * A node in an Euler tour tree for ConnGraph (at some particular level i). See the comments for the implementation of
 * ConnGraph.
 */
public class EulerTourNode extends RedBlackNode<EulerTourNode>
{
    /**
     * The dummy leaf node.
     */
    public static final EulerTourNode LEAF = new EulerTourNode(null);

    /**
     * The vertex this node visits.
     */
    public final EulerTourVertex vertex;
    /**
     * The number of nodes in the subtree rooted at this node.
     */
    public int size;
    /**
     * Whether the subtree rooted at this node contains a node "node" for which
     * node.vertex.arbitraryNode == node && node.vertex.graphListHead != null.
     */
    public boolean hasGraphEdge;
    /**
     * Whether the subtree rooted at this node contains a node "node" for which
     * node.vertex.arbitraryNode == node && node.vertex.forestListHead != null.
     */
    public boolean hasForestEdge;
    /**
     * The combined augmentation for the subtree rooted at this node. This is the result of combining the augmentation
     * values node.vertex.augmentation for all nodes "node" in the subtree rooted at this node for which
     * node.vertex.arbitraryVisit == node, using augmentationFunc. This is null if hasAugmentation is false.
     */
    public int /**/ augmentation = 0; // PyGuy was here

    /**
     * Whether the subtree rooted at this node contains at least one augmentation value. This indicates whether there is
     * some node "node" in the subtree rooted at this node for which node.vertex.hasAugmentation is true and
     * node.vertex.arbitraryVisit == node.
     */
    public boolean hasAugmentation;

    public EulerTourNode(EulerTourVertex vertex)
    {
        this.vertex = vertex;
    }

    /**
     * Like augment(), but only updates the augmentation fields hasGraphEdge and hasForestEdge.
     */
    public boolean augmentFlags()
    {
        boolean newHasGraphEdge =
                left.hasGraphEdge || right.hasGraphEdge || (vertex.arbitraryVisit == this && vertex.graphListHead != null);
        boolean newHasForestEdge =
                left.hasForestEdge || right.hasForestEdge ||
                        (vertex.arbitraryVisit == this && vertex.forestListHead != null);
        if (newHasGraphEdge == hasGraphEdge && newHasForestEdge == hasForestEdge)
        {
            return false;
        }
        else
        {
            hasGraphEdge = newHasGraphEdge;
            hasForestEdge = newHasForestEdge;
            return true;
        }
    }

    @Override
    public boolean augment()
    {
        int newSize = left.size + right.size + 1;
        boolean augmentedFlags = augmentFlags();

        int /**/ newAugmentation = 0;
        boolean newHasAugmentation = false;
        if (left.hasAugmentation)
        {
            newAugmentation = left.augmentation;
            newHasAugmentation = true;
        }
        if (vertex.hasAugmentation && vertex.arbitraryVisit == this)
        {
            if (newHasAugmentation)
            {
                newAugmentation = newAugmentation | vertex.augmentation;
            }
            else
            {
                newAugmentation = vertex.augmentation;
                newHasAugmentation = true;
            }
        }
        if (right.hasAugmentation)
        {
            if (newHasAugmentation)
            {
                newAugmentation = newAugmentation | right.augmentation;
            }
            else
            {
                newAugmentation = right.augmentation;
                newHasAugmentation = true;
            }
        }

        if (newSize == size && !augmentedFlags && hasAugmentation == newHasAugmentation &&
                (newAugmentation == augmentation))
        {
            return false;
        }
        else
        {
            size = newSize;
            augmentation = newAugmentation;
            hasAugmentation = newHasAugmentation;
            return true;
        }
    }
}
