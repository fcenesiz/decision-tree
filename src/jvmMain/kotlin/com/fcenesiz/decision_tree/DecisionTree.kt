package com.fcenesiz.decision_tree

class DecisionTree {

    private lateinit var root: DecisionTreeNode

    fun create(dataset: MutableMap<String, MutableList<String>>, targetKey: String) {
        if (this::root.isInitialized)
            reset(this, "root")
        createNode(null, dataset, targetKey)
    }

    private fun createNode(node: DecisionTreeNode?, dataset: MutableMap<String, MutableList<String>>, targetKey: String) {
        if (this::root.isInitialized && (node == null || node.isLeaf))
            return

        val bestGain = findBestGain(dataset, targetKey)

        if (!this::root.isInitialized) {
            root = DecisionTreeNode(
                name = bestGain,
                condition = "root",
                isLeaf = false
            )
            createNode(root, dataset, targetKey)
        } else {
            node?.let {

                val branches = branches(bestGain, dataset)

                branches.forEach { (condition, map) ->
                    map[targetKey]?.let { list ->
                        val isLeaf = map.keys.size == 1
                        val nextBestGain = findBestGain(map, targetKey)
                        val nextNode = DecisionTreeNode(
                            name = if (isLeaf) list[0] else nextBestGain,
                            condition = condition,
                            isLeaf = isLeaf
                        )
                        it.decisionTreeNodes.add(nextNode)
                        createNode(nextNode, map, targetKey)
                    }
                }
            }
        }
    }

    fun classify(data: MutableMap<String, String>): String? {
        var currentNode = root

        while (!currentNode.isLeaf) {
            val attributeValue = data[currentNode.name] ?: return null
            val nextNode = currentNode.decisionTreeNodes.find { it.condition == attributeValue } ?: return null
            currentNode = nextNode
        }

        return currentNode.name
    }


    fun show(node: DecisionTreeNode = root, indent: String = "") {
        val marker = if (node.isLeaf) "-> " else "-+ "

        println(indent + marker + "" + node.condition + ": " + node.name + "")
        for (childNode in node.decisionTreeNodes) {
            show(childNode, "$indent\t\t")

        }
    }

    fun pruning(node: DecisionTreeNode = root) {
        node.decisionTreeNodes.forEach { pruning(it) }
        if (node.decisionTreeNodes.isNotEmpty() && node.decisionTreeNodes.all { it.isLeaf && it.name == node.decisionTreeNodes[0].name }) {
            node.name = node.decisionTreeNodes[0].name
            node.decisionTreeNodes.clear()
            node.isLeaf = true
        }
    }

    private fun reset(target: Any, fieldName: String) {
        val field = target.javaClass.getDeclaredField(fieldName)

        with (field) {
            isAccessible = true
            set(target, null)
        }
    }

}