package com.fcenesiz.decision_tree
data class DecisionTreeNode(
    var name: String,
    val condition: String,
    var isLeaf: Boolean = false,
    val decisionTreeNodes: MutableList<DecisionTreeNode> = mutableListOf()
){

    override fun toString(): String {
        var string = "name: $name,\n"
        string += "condition: $condition,\n"
        string += "nodes:\n"
        decisionTreeNodes.forEach {
            string += "\t$it\n"
        }
        return string
    }

}