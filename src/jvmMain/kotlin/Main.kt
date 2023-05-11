import com.fcenesiz.decision_tree.DecisionTree

fun main() {

    val targetKey = "buys_computer"
    val decisionTree = DecisionTree()

    // create the tree
    decisionTree.create(dataset, targetKey)
    // show created tree
    decisionTree.show()

    // prune tree
    decisionTree.pruning()
    // show pruned tree
    decisionTree.show()

    // classify
    val result = decisionTree.classify(mutableMapOf(
        "age" to "<=30",
        "income" to "high",
        "student" to "no",
        "credit_rating" to "fair")
    )
    println("result: $result")
}

// dataset-------------------------------------------------

private val age = mutableListOf("<=30", "<=30", "31..40", ">40", ">40", ">40", "31..40", "<=30", "<=30", ">40", "<=30", "31..40", "31..40", ">40")
private val income = mutableListOf("high", "high", "high", "medium", "low", "low", "low", "medium", "low", "medium", "medium", "medium", "high", "medium")
private val student = mutableListOf("no", "no", "no", "no", "yes", "yes", "yes", "no", "yes", "yes", "yes", "no", "yes", "no")
private val credit_rating = mutableListOf("fair", "excellent", "fair", "fair", "fair", "excellent", "excellent", "fair", "fair", "fair", "excellent", "excellent", "fair", "excellent")
private val buys_computer = mutableListOf("no", "no", "yes", "yes", "yes", "no", "yes", "no", "yes", "yes", "yes", "yes", "yes", "no")

private val dataset : MutableMap<String, MutableList<String>> = mutableMapOf(
    "age" to age,
    "income" to income,
    "student" to student,
    "credit_rating" to credit_rating,
    "buys_computer" to buys_computer
)