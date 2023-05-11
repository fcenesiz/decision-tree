package com.fcenesiz.decision_tree

/**
 *
 * A mutable pair consisting of two `Object` elements.
 *
 *
 * Not #ThreadSafe#
 *
 * @param <L> the left element type
 * @param <R> the right element type
 *
 * @since 3.0
</R></L> */

data class MPair<L, R>(
    var left: L,
    var right: R
) {

    constructor(mPair: MPair<L, R>) : this(mPair.left, mPair.right)

    companion object {
        /**
         *
         * Creates a mutable pair of two objects inferring the generic types.
         *
         *
         * This factory allows the pair to be created using inference to
         * obtain the generic types.
         *
         * @param <L> the left element type
         * @param <R> the right element type
         * @param left  the left element, may be null
         * @param right  the right element, may be null
         * @return a pair formed from the two parameters, not null
        </R></L> */
        fun <L, R> of(left: L, right: R): MPair<L, R> {
            return MPair(left, right)
        }

        /**
         *
         * Creates a mutable pair from an existing pair.
         *
         *
         * This factory allows the pair to be created using inference to
         * obtain the generic types.
         *
         * @param <L> the left element type
         * @param <R> the right element type
         * @param pair the existing pair.
         * @return a pair formed from the two parameters, not null
        </R></L> */
        fun <L, R> of(pair: Map.Entry<L, R>?): MPair<L?, R?> {
            val left: L?
            val right: R?
            if (pair != null) {
                left = pair.key
                right = pair.value
            } else {
                left = null
                right = null
            }
            return MPair(left, right)
        }
    }

    override fun toString(): String {
        return "($left, $right)"
    }

}
